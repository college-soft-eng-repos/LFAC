package main.java.com.projeto.core.pipeline;

import java.io.IOException;

import main.java.com.projeto.core.service.GerenciadorArquivos;

/**
 * Responsável por preprocessar o arquivo de entrada .ent
 * Remove comentários, formata conteúdo, gera o arquivo pré-processado .ppr e
 * conta o total de linhas
 */
public class PreProcessador {

    private GerenciadorArquivos gerenciadorArquivos;

    public PreProcessador() {
        this.gerenciadorArquivos = new GerenciadorArquivos();
    }

    public PreProcessador(GerenciadorArquivos gerenciadorArquivos) {
        this.gerenciadorArquivos = gerenciadorArquivos;
    }

    /**
     * Remove comentários do conteudo
     * 
     * @param conteudo conteudo original com comentários
     * @return conteudo sem comentários
     */
    private String removerComentarios(String conteudo) {
        // Remove comentários de bloco (/* */) inclusive com múltiplas linhas
        conteudo = conteudo.replaceAll("(?s)/\\*.*?\\*/", "");
        // Remove comentários (//) até o final da linha
        conteudo = conteudo.replaceAll("//.*", "");
        return conteudo;
    }

    /**
     * Remove espaços excessivos e colapsa linhas vazias
     * 
     * @param conteudo conteudo original
     * @return conteudo formatado
     */
    private String formatarConteudo(String conteudo) {
        // Normaliza line endings (Windows \r\n -> \n) antes de qualquer processamento
        conteudo = conteudo.replace("\r\n", "\n");
        // Colapsa múltiplas linhas vazias e deixa no máximo uma
        conteudo = conteudo.replaceAll("\n\\s*\n+", "\n\n");
        // Remove espaços/tabs excessivos e os substitui por um único espaço
        conteudo = conteudo.replaceAll("[ \t]+", " ");
        // Remove espaços no início e fim
        return conteudo.trim();
    }

    /**
     * Processa o arquivo completo: lê, remove comentários, formata e escreve .ppr
     * 
     * @param caminho caminho do arquivo .ent de entrada
     * @throws IOException se houver erro na leitura/escrita
     */
    public void processarArquivo(String caminho) throws IOException {
        // Gerar nome do arquivo de saída (.ppr)
        String caminhoArquivoSaida = caminho.replace(".ent", ".ppr");

        // Ler arquivo
        String conteudo = gerenciadorArquivos.lerArquivo(caminho);

        // Remover comentários
        conteudo = removerComentarios(conteudo);

        // Formatar (remover espaços excessivos e linhas vazias)
        conteudo = formatarConteudo(conteudo);

        // Escrever arquivo .ppr
        gerenciadorArquivos.escreverArquivo(caminhoArquivoSaida, conteudo);

        System.out.println("Arquivo processado com sucesso: " + caminhoArquivoSaida);
    }

    /**
     * Conta o total de linhas em um conteúdo
     * 
     * @param conteudo conteúdo a contar
     * @return número de linhas
     */
    public int contarLinhas(String conteudo) {
        if (conteudo == null || conteudo.isEmpty()) {
            return 0;
        }
        return conteudo.split("\n", -1).length;
    }
}
