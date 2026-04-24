package main.java.com.projeto;

import main.java.com.projeto.core.analyzer.AnalisadorLexico;
import main.java.com.projeto.core.model.ResultadoAnalise;

import java.io.IOException;

public class Main {

  public static void main(String[] args) {
    try {
      // Criar analisador e executar análise completa
      AnalisadorLexico analisador = new AnalisadorLexico();
      
      System.out.println("=== ANÁLISE LÉXICA ===\n");
      System.out.println("Pré-processando arquivo...");
      System.out.println("Tokenizando...");
      
      // Executar análise e salvar resultados
      ResultadoAnalise resultado = analisador.analisarESalvar();
      
      // Exibir resumo
      exibirResumo(resultado);
      
    } catch (IOException e) {
      System.err.println("Erro ao processar arquivo: " + e.getMessage());
      e.printStackTrace();
    }
  }

  /**
   * Exibe um resumo dos resultados da análise
   * 
   * @param resultado resultado da análise
   */
  private static void exibirResumo(ResultadoAnalise resultado) {
    System.out.println("\n=== RESULTADO ===");
    System.out.println("Arquivo: " + resultado.getNomeArquivoFonte());
    System.out.println("Linhas: " + resultado.getTotalLinhas());
    System.out.println("Tokens: " + resultado.getTotalTokens());
    System.out.println("Erros: " + resultado.getTotalErros());
    System.out.println("================\n");
    
    if (resultado.temErros()) {
      resultado.getGerenciadorErros().exibirNoConsole();
      String caminhoErr = resultado.getGerenciadorErros().toString();
      System.out.println("Relatório de erros salvo em: arquivo.err");
    } else {
      System.out.println("✓ Nenhum erro encontrado durante a tokenização!");
    }
    
    String caminhoTks = "arquivo.tks";
    System.out.println("✓ Tokens salvos em: " + caminhoTks);
  }
}
