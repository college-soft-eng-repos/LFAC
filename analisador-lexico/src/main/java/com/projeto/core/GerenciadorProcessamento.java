package main.java.com.projeto.core;

import java.io.IOException;
import java.nio.file.Paths;

import main.java.com.projeto.core.service.GerenciadorArquivos;
import main.java.com.projeto.core.dto.ResultadoAnalise;
import main.java.com.projeto.core.pipeline.PreProcessador;
import main.java.com.projeto.core.pipeline.Tokenizador;

/**
 * Orquestrador do pipeline de análise léxica
 * Gerencia leitura, preprocessamento, tokenização e salvamento
 */
public class GerenciadorProcessamento {
    private GerenciadorArquivos gerenciadorArquivos;
    private PreProcessador preProcessador;
    private Tokenizador tokenizador;

    public GerenciadorProcessamento() {
        this.gerenciadorArquivos = new GerenciadorArquivos();
        this.preProcessador = new PreProcessador(this.gerenciadorArquivos);
        this.tokenizador = new Tokenizador();
    }

    public GerenciadorProcessamento(String resourcesPath) {
        this.gerenciadorArquivos = new GerenciadorArquivos(resourcesPath);
        this.preProcessador = new PreProcessador(this.gerenciadorArquivos);
        this.tokenizador = new Tokenizador();
    }

    /**
     * Executa a análise léxica completa
     * Pipeline: .ent -> preprocessar -> .ppr -> tokenizar -> ResultadoAnalise
     * 
     * @return resultado da análise com tokens e erros
     * @throws IOException se houver erro na leitura/escrita de arquivos
     */
    public ResultadoAnalise analisar() throws IOException {
        // Preprocessar
        String caminhoEnt = gerenciadorArquivos.getCaminhoEnt();
        String caminhoPpr = gerenciadorArquivos.getCaminhoPpr();

        preProcessador.processarArquivo(caminhoEnt);

        // Ler arquivo preprocessado
        String conteudo = gerenciadorArquivos.lerArquivo(caminhoPpr);
        int totalLinhas = preProcessador.contarLinhas(conteudo);

        // Tokenizar
        var tokens = tokenizador.tokenizar(conteudo);
        var gerenciadorErros = tokenizador.obterGerenciadorErros();

        // Criar resultado consolidado
        String nomeArquivoFonte = Paths.get(caminhoEnt).getFileName().toString();
        ResultadoAnalise resultado = new ResultadoAnalise(
                tokens,
                gerenciadorErros,
                totalLinhas,
                nomeArquivoFonte);

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
                saidaTokens);

        // Salvar erros se houver
        if (resultado.temErros()) {
            resultado.getGerenciadorErros().salvarEmArquivo(
                    gerenciadorArquivos.getCaminhoErr());
        }

        return resultado;
    }
}
