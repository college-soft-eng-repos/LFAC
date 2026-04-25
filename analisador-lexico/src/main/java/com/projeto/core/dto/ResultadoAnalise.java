package main.java.com.projeto.core.dto;

import java.util.List;

import main.java.com.projeto.core.model.Token;
import main.java.com.projeto.core.service.GerenciadorErros;

/**
 * Encapsula o resultado completo da análise léxica
 * Agrupa tokens, erros e metadados em um único objeto
 */
public class ResultadoAnalise {
  private List<Token> tokens;
  private GerenciadorErros gerenciadorErros;
  private int totalLinhas;
  private String nomeArquivoFonte;

  public ResultadoAnalise(List<Token> tokens, GerenciadorErros gerenciadorErros,
      int totalLinhas, String nomeArquivoFonte) {
    this.tokens = tokens;
    this.gerenciadorErros = gerenciadorErros;
    this.totalLinhas = totalLinhas;
    this.nomeArquivoFonte = nomeArquivoFonte;
  }

  /**
   * Retorna a lista de tokens identificados
   */
  public List<Token> getTokens() {
    return tokens;
  }

  /**
   * Retorna o gerenciador de erros
   */
  public GerenciadorErros getGerenciadorErros() {
    return gerenciadorErros;
  }

  /**
   * Retorna a quantidade de tokens
   */
  public int getTotalTokens() {
    return tokens.size();
  }

  /**
   * Retorna a quantidade de erros
   */
  public int getTotalErros() {
    return gerenciadorErros.getTotalErros();
  }

  /**
   * Verifica se houve erros
   */
  public boolean temErros() {
    return gerenciadorErros.temErros();
  }

  /**
   * Retorna o total de linhas analisadas
   */
  public int getTotalLinhas() {
    return totalLinhas;
  }

  /**
   * Retorna o nome do arquivo fonte
   */
  public String getNomeArquivoFonte() {
    return nomeArquivoFonte;
  }

  /**
   * Formata a saída dos tokens
   */
  public String formatarTokens() {
    return formatarTokens(false);
  }

  /**
   * Formata a saída dos tokens, opcionalmente com resumo
   */
  public String formatarTokens(boolean comResumo) {
    StringBuilder sb = new StringBuilder();

    if (comResumo) {
      sb.append("=== RESUMO DA ANÁLISE ===\n");
      sb.append("Arquivo: ").append(nomeArquivoFonte).append("\n");
      sb.append("Linhas: ").append(totalLinhas).append("\n");
      sb.append("Tokens: ").append(getTotalTokens()).append("\n");
      sb.append("Erros: ").append(getTotalErros()).append("\n");
      sb.append("========================\n\n");
    }

    sb.append(formatarTokensSaida());
    return sb.toString();
  }

  /**
   * Formata apenas os tokens
   */
  private String formatarTokensSaida() {
    if (tokens.isEmpty()) {
      return "";
    }

    StringBuilder sb = new StringBuilder();
    int linhaAtual = tokens.get(0).getLinha();

    for (Token token : tokens) {
      if (token.getLinha() != linhaAtual) {
        for (int i = linhaAtual; i < token.getLinha(); i++) {
          sb.append("\n");
        }
        linhaAtual = token.getLinha();
      }
      sb.append(token.toString()).append(" ");
    }
    return sb.toString().trim();
  }
}
