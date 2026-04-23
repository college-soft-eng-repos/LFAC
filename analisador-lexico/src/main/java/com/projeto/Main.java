package main.java.com.projeto;

import main.java.com.projeto.core.PreProcessador;
import main.java.com.projeto.core.Tokenizacao;
import main.java.com.projeto.core.Token;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {

  public static void main(String[] args) {
    try {
      // Caminhos dos arquivos - usando variável de ambiente LFAC_RESOURCES_PATH
      String resourcesPath = System.getenv("LFAC_RESOURCES_PATH");
      if (resourcesPath == null || resourcesPath.trim().isEmpty()) {
        resourcesPath = "./resources";
      }
      String caminhoArquivoFat = resourcesPath + "/arquivo.fat";
      String caminhoArquivoEsg = caminhoArquivoFat.replace(".fat", ".esg");
      String caminhoArquivoTks = caminhoArquivoFat.replace(".fat", ".tks");


      // Pré-processar arquivo .fat e gerar arquivo .esg
      System.out.println("Pré-processando arquivo...");
      PreProcessador.processarArquivo(caminhoArquivoFat);
      System.out.println("Arquivo .esg gerado: " + caminhoArquivoEsg + "\n");

      // Ler arquivo .esg
      System.out.println("Lendo arquivo .esg...");
      String conteudo = PreProcessador.lerArquivo(caminhoArquivoEsg);
      System.out.println("Arquivo lido com sucesso\n");

      // Tokenizar conteúdo
      System.out.println("Tokenizando...");
      List<Token> tokens = Tokenizacao.tokenizar(conteudo);
      System.out.println(tokens.size() + " tokens identificados\n");

      // Exibir tokens
      System.out.println("TOKENS");
      String saidaTokens = Tokenizacao.formatarSaida(tokens);
      System.out.println(saidaTokens);

      // 5. Salvar tokens em arquivo .tks
      System.out.println("\nSALVANDO");
      salvarTokensEmArquivo(saidaTokens, caminhoArquivoTks);
      System.out.println("Tokens salvos em: " + caminhoArquivoTks);

    } catch (IOException e) {
      System.err.println("Erro ao processar arquivo: " + e.getMessage());
      e.printStackTrace();
    }
  }

  /**
   * Salva a saída de tokens em um arquivo .tks
   * @param conteudo conteúdo formatado dos tokens
   * @param caminho caminho do arquivo de saída
   * @throws IOException se houver erro na escrita
   */
  private static void salvarTokensEmArquivo(String conteudo, String caminho) throws IOException {
    Files.writeString(Paths.get(caminho), conteudo);
  }
}
