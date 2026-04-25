package main.java.com.projeto.core.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Gerenciador centralizado de erros durante a tokenização
 * Coleta e armazena todos os erros para posterior análise e geração de
 * relatório
 */
public class GerenciadorErros {
  private List<ErroToken> erros;

  public GerenciadorErros() {
    this.erros = new ArrayList<>();
  }

  /**
   * Adiciona um erro à lista
   * 
   * @param lexema o lexema não reconhecido
   * @param linha  número da linha
   * @param coluna número da coluna
   */
  public void adicionarErro(String lexema, int linha, int coluna) {
    erros.add(new ErroToken(lexema, linha, coluna));
  }

  /**
   * Retorna todos os erros coletados
   * 
   * @return lista de erros
   */
  public List<ErroToken> obterTodos() {
    return new ArrayList<>(erros);
  }

  /**
   * Retorna a quantidade total de erros
   * 
   * @return número de erros
   */
  public int getTotalErros() {
    return erros.size();
  }

  /**
   * Verifica se houve erros
   * 
   * @return true se houver erros, false caso contrário
   */
  public boolean temErros() {
    return !erros.isEmpty();
  }

  /**
   * Salva todos os erros em um arquivo
   * 
   * @param caminho caminho do arquivo de saída
   * @throws IOException se houver erro na escrita
   */
  public void salvarEmArquivo(String caminho) throws IOException {
    if (erros.isEmpty()) {
      return;
    }

    StringBuilder sb = new StringBuilder();
    sb.append("RELATÓRIO DE ERROS - ANÁLISE LÉXICA\n");
    sb.append("===================================\n");
    sb.append("Total de erros: ").append(erros.size()).append("\n\n");

    for (ErroToken erro : erros) {
      sb.append(erro.toString()).append("\n");
    }

    Files.writeString(Paths.get(caminho), sb.toString());
  }

  /**
   * Exibe todos os erros no console (para debug)
   */
  public void exibirNoConsole() {
    if (erros.isEmpty()) {
      System.out.println("Nenhum erro encontrado durante a tokenização.");
      return;
    }

    System.out.println("\n=== ERROS ENCONTRADOS ===");
    for (ErroToken erro : erros) {
      System.err.println(erro.toString());
    }
    System.out.println("Total: " + erros.size() + " erro(s)\n");
  }

  /**
   * Classe interna para representar um erro de tokenização
   */
  public static class ErroToken {
    private String lexema;
    private int linha;
    private int coluna;

    public ErroToken(String lexema, int linha, int coluna) {
      this.lexema = lexema;
      this.linha = linha;
      this.coluna = coluna;
    }

    public String getLexema() {
      return lexema;
    }

    public int getLinha() {
      return linha;
    }

    public int getColuna() {
      return coluna;
    }

    @Override
    public String toString() {
      return String.format("[ERRO] Lexema não reconhecido: '%s' em linha %d, coluna %d",
          lexema, linha, coluna);
    }
  }
}
