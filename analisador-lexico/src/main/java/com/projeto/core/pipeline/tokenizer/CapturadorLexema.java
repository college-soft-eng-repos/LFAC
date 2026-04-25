package main.java.com.projeto.core.pipeline.tokenizer;

import main.java.com.projeto.core.model.Identificador;
import main.java.com.projeto.core.model.Token;
import main.java.com.projeto.core.service.GerenciadorErros;

/**
 * Responsável por capturar lexemas do conteúdo
 * Extrai strings, identificadores e operadores caractere por caractere
 */
public class CapturadorLexema {
  private GerenciadorErros gerenciadorErros;

  public CapturadorLexema(GerenciadorErros gerenciadorErros) {
    this.gerenciadorErros = gerenciadorErros;
  }

  /**
   * Captura uma string completa (entre aspas duplas)
   * Suporta quebras de linha dentro da string
   * 
   * @param conteudo conteúdo total
   * @param pos      array com [índice, linha, coluna]
   * @return Token da string ou null
   */
  public Token capturarString(String conteudo, int[] pos) {
    int inicioLinha = pos[1];
    int inicioColuna = pos[2];
    StringBuilder sb = new StringBuilder("\"");
    pos[0]++;
    pos[2]++;

    while (pos[0] < conteudo.length() && conteudo.charAt(pos[0]) != '"') {
      char ch = conteudo.charAt(pos[0]);
      sb.append(ch);
      if (ch == '\n') {
        pos[1]++;
        pos[2] = 1;
      } else {
        pos[2]++;
      }
      pos[0]++;
    }

    if (pos[0] < conteudo.length()) {
      sb.append('"');
      pos[0]++;
      pos[2]++;
    }

    return IdentificadorToken.identificar(sb.toString(), inicioLinha, inicioColuna, null, gerenciadorErros);
  }

  /**
   * Captura um identificador ou número
   * Consiste em letras, dígitos e underscore
   * 
   * @param conteudo conteúdo total
   * @param pos      array com [índice, linha, coluna]
   * @param tabela   tabela de símbolos para IDs
   * @return Token do identificador/número ou null
   */
  public Token capturarIdentificador(String conteudo, int[] pos, Identificador tabela) {
    int inicioLinha = pos[1];
    int inicioColuna = pos[2];
    StringBuilder sb = new StringBuilder();

    while (pos[0] < conteudo.length() &&
        (Character.isLetterOrDigit(conteudo.charAt(pos[0])) ||
            conteudo.charAt(pos[0]) == '_')) {
      sb.append(conteudo.charAt(pos[0]));
      pos[2]++;
      pos[0]++;
    }

    return IdentificadorToken.identificar(sb.toString(), inicioLinha, inicioColuna, tabela, gerenciadorErros);
  }

  /**
   * Captura um operador ou símbolo de pontuação
   * 
   * @param conteudo conteúdo total
   * @param pos      array com [índice, linha, coluna]
   * @return Token do operador ou null
   */
  public Token capturarOperador(String conteudo, int[] pos) {
    int linha = pos[1];
    int coluna = pos[2];
    Token token = IdentificadorToken.identificar(String.valueOf(conteudo.charAt(pos[0])), linha, coluna, null,
        gerenciadorErros);
    pos[2]++;
    pos[0]++;
    return token;
  }
}