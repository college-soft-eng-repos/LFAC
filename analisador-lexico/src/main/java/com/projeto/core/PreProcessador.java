package main.java.com.projeto.core;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

public class PreProcessador {

    /**
     * Lê o arquivo .fat e retorna seu conteúdo como String
     * @param caminhoArquivo caminho do arquivo a ser lido
     * @return conteúdo do arquivo
     * @throws IOException se houver erro na leitura
     */
    public static String lerArquivo(String caminhoArquivo) throws IOException {
        return Files.readString(Paths.get(caminhoArquivo));
    }

    /**
     * Remove comentários do conteudo
     * @param conteudo conteudo original com comentários
     * @return conteudo sem comentários
     */
    public static String removerComentarios(String conteudo){
      // Remove comentários de bloco (/* */) inclusive com múltiplas linhas
      conteudo = conteudo.replaceAll("(?s)/\\*.*?\\*/", "");
      // Remove comentários (//) até o final da linha
      conteudo = conteudo.replaceAll("//.*", "");
      return conteudo;
    }

    /**
     * Remove espaços excessivos e colapsa linhas vazias
     * @param conteudo conteudo original
     * @return conteudo formatado
     */
    public static String formatarConteudo(String conteudo){
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
     * Escreve o conteúdo processado em um novo arquivo
     * @param conteudoProcessado conteúdo a ser escrito
     * @param caminhoArquivo caminho do arquivo de saída
     * @throws IOException se houver erro na escrita
     */
    public static void escreverArquivo(String conteudoProcessado, String caminhoArquivo) throws IOException {
        Files.writeString(Paths.get(caminhoArquivo), conteudoProcessado);
    }

    /**
     * Processa o arquivo completo: lê, remove comentários, formata e escreve .esg
     * @param caminhoArquivoEntrada caminho do arquivo .fat de entrada
     * @throws IOException se houver erro na leitura/escrita
     */
    public static void processarArquivo(String caminhoArquivoEntrada) throws IOException {
        // Gerar nome do arquivo de saída (.esg)
        String caminhoArquivoSaida = caminhoArquivoEntrada.replace(".fat", ".esg");

        // Ler arquivo
        String conteudo = lerArquivo(caminhoArquivoEntrada);

        // Remover comentários
        conteudo = removerComentarios(conteudo);

        // Formatar (remover espaços excessivos e linhas vazias)
        conteudo = formatarConteudo(conteudo);

        // Escrever arquivo .esg
        escreverArquivo(conteudo, caminhoArquivoSaida);

        System.out.println("Arquivo processado com sucesso: " + caminhoArquivoSaida);
    }
}
