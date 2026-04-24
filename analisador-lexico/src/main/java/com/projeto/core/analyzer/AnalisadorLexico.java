package main.java.com.projeto.core.analyzer;

import java.io.IOException;
import java.nio.file.Paths;

import main.java.com.projeto.core.model.ResultadoAnalise;
import main.java.com.projeto.core.model.Token;
import main.java.com.projeto.core.processor.GerenciadorArquivos;
import main.java.com.projeto.core.processor.PreProcessador;

/**
 * Orquestrador do pipeline de análise léxica
 * Coordena: preprocessamento - tokenização - retorno de resultados
 */
public class AnalisadorLexico {
    private GerenciadorArquivos gerenciadorArquivos;

    public AnalisadorLexico() {
        this.gerenciadorArquivos = new GerenciadorArquivos();
    }

    public AnalisadorLexico(String resourcesPath) {
        this.gerenciadorArquivos = new GerenciadorArquivos(resourcesPath);
    }

    /**
     * Executa a análise léxica completa
     * Pipeline: .fat -> preprocessar -> .esg -> tokenizar -> ResultadoAnalise
     * 
     * @return resultado da análise com tokens e erros
     * @throws IOException se houver erro na leitura/escrita de arquivos
     */
    public ResultadoAnalise analisar() throws IOException {
        // Preprocessar
        String caminhoFat = gerenciadorArquivos.getCaminhoFat();
        String caminhoEsg = gerenciadorArquivos.getCaminhoEsg();
        
        preprocessar(caminhoFat, caminhoEsg);
        
        // Ler arquivo preprocessado
        String conteudo = gerenciadorArquivos.lerArquivo(caminhoEsg);
        int totalLinhas = contarLinhas(conteudo);
        
        // Tokenizar
        var tokens = Tokenizacao.tokenizar(conteudo);
        var gerenciadorErros = Tokenizacao.obterGerenciadorErros();
        
        // Criar resultado consolidado
        String nomeArquivoFonte = Paths.get(caminhoFat).getFileName().toString();
        ResultadoAnalise resultado = new ResultadoAnalise(
            tokens, 
            gerenciadorErros, 
            totalLinhas,
            nomeArquivoFonte
        );
        
        return resultado;
    }

    /**
     * Executa a análise léxica e salva os resultados em arquivos
     * 
     * @return resultado da análise
     * @throws IOException se houver erro na leitura/escrita de arquivos
     */
    public ResultadoAnalise analisarESalvar() throws IOException {
        ResultadoAnalise resultado = analisar();
        
        // Salvar tokens
        String saidaTokens = resultado.formatarTokens();
        gerenciadorArquivos.escreverArquivo(
            gerenciadorArquivos.getCaminhoTks(), 
            saidaTokens
        );
        
        // Salvar erros se houver
        if (resultado.temErros()) {
            resultado.getGerenciadorErros().salvarEmArquivo(
                gerenciadorArquivos.getCaminhoErr()
            );
        }
        
        return resultado;
    }

    /**
     * Preprocessa o arquivo (remove comentários, normaliza espaços)
     * 
     * @param caminhoEntrada arquivo de entrada (.fat)
     * @param caminhoSaida   arquivo de saída (.esg)
     * @throws IOException se houver erro
     */
    private void preprocessar(String caminhoEntrada, String caminhoSaida) throws IOException {
        PreProcessador.processarArquivo(caminhoEntrada);
    }

    /**
     * Conta o total de linhas em um conteúdo
     * 
     * @param conteudo conteúdo a contar
     * @return número de linhas
     */
    private int contarLinhas(String conteudo) {
        if (conteudo == null || conteudo.isEmpty()) {
            return 0;
        }
        return conteudo.split("\n", -1).length;
    }

    /**
     * Retorna o gerenciador de arquivos
     */
    public GerenciadorArquivos getGerenciadorArquivos() {
        return gerenciadorArquivos;
    }
}
