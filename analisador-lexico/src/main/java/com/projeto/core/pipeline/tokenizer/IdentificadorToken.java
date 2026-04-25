package main.java.com.projeto.core.pipeline.tokenizer;

import main.java.com.projeto.core.model.PalavraReservada;
import main.java.com.projeto.core.model.Identificador;
import main.java.com.projeto.core.model.Token;
import main.java.com.projeto.core.service.GerenciadorErros;

/**
 * Responsável por classificar e identificar o tipo de token
 * Baseado no lexema capturado, determina qual é sua classe
 */
public class IdentificadorToken {

  /**
   * Identifica o tipo de token baseado no lexema
   * 
   * @param lexema           o lexema a ser identificado
   * @param linha            número da linha
   * @param coluna           número da coluna
   * @param tabela           tabela de símbolos (null para operadores e palavras
   *                         reservadas)
   * @param gerenciadorErros gerenciador de erros
   * @return Token identificado
   */
  public static Token identificar(String lexema, int linha, int coluna, Identificador tabela,
      GerenciadorErros gerenciadorErros) {
    // Verificar se é palavra reservada
    String tokenReservada = PalavraReservada.buscar(lexema);
    if (tokenReservada != null) {
      return new Token(tokenReservada.replaceAll("[\\[\\]]", ""), "", linha, coluna);
    }

    // Verifica se é string ("...")
    if (lexema.startsWith("\"") && lexema.endsWith("\"")) {
      String conteudo = lexema.substring(1, lexema.length() - 1);
      return new Token("fr", conteudo, linha, coluna);
    }

    // Verifica se é número (apenas dígitos)
    if (lexema.matches("[0-9]+")) {
      return new Token("nu", lexema, linha, coluna);
    }

    // Verifica se é operador lógico (AND OR NOT)
    if (lexema.equalsIgnoreCase("AND") || lexema.equalsIgnoreCase("OR") ||
        lexema.equalsIgnoreCase("NOT")) {
      return new Token("ol", lexema.toLowerCase(), linha, coluna);
    }

    // Verifica se é identificador (começa com letra, contém letras/números/_)
    if (lexema.matches("[a-zA-Z][a-zA-Z0-9_]*")) {
      if (tabela != null) {
        int indice = tabela.obterOuRegistrar(lexema);
        return new Token("id", String.valueOf(indice), linha, coluna);
      }
      return new Token("id", "0", linha, coluna);
    }

    // Verificar se é operador de comparação (< > =)
    if (lexema.matches("[<>=]+")) {
      return new Token("or", lexema, linha, coluna);
    }

    // Verificar se é operador aritmético (+ * / -)
    if (lexema.matches("[+\\-*/]")) {
      return new Token("om", lexema, linha, coluna);
    }

    // Outros símbolos: [;], [:], etc - sem valor
    if (lexema.matches("[;:()\\[\\]]")) {
      return new Token(lexema, "", linha, coluna);
    }

    // Token não reconhecido
    gerenciadorErros.adicionarErro(lexema, linha, coluna);
    return null;
  }
}