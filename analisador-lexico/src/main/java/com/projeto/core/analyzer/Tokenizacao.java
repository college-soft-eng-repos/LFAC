package main.java.com.projeto.core.analyzer;

import java.util.ArrayList;
import java.util.List;

import main.java.com.projeto.core.lexema.PalavraReservada;
import main.java.com.projeto.core.model.TipoToken;
import main.java.com.projeto.core.model.Token;

/**
 * Classe responsável por tokenizar o conteúdo do arquivo preprocessado
 * Reconhece: palavras reservadas, identificadores, números, strings,
 * operadores, etc
 */
public class Tokenizacao {

    /**
     * Tokeniza o conteúdo completo do arquivo
     * Processa caractere por caractere para evitar problemas com strings
     * multilinhas
     * 
     * @param conteudo conteúdo do arquivo preprocessado
     * @return lista de tokens identificados
     */
    public static List<Token> tokenizar(String conteudo) {
        List<Token> tokens = new ArrayList<>();
        int[] pos = { 0, 1, 1 }; // i, linha, coluna

        while (pos[0] < conteudo.length()) {
            char c = conteudo.charAt(pos[0]);

            if (Character.isWhitespace(c)) {
                processarEspaco(c, pos);
            } else if (c == '"') {
                Token token = capturarString(conteudo, pos);
                if (token != null) {
                    tokens.add(token);
                }
            } else if (Character.isLetterOrDigit(c) || c == '_') {
                Token token = capturarIdentificador(conteudo, pos);
                if (token != null) {
                    tokens.add(token);
                }
            } else {
                Token token = capturarOperador(conteudo, pos);
                if (token != null) {
                    tokens.add(token);
                }
            }
        }

        return tokens;
    }

    /**
     * Processa espaços em branco e quebras de linha
     * Atualiza linha e coluna conforme necessário
     * 
     * @param c   caractere atual
     * @param pos array com [índice, linha, coluna]
     */
    private static void processarEspaco(char c, int[] pos) {
        if (c == '\n') {
            pos[1]++; // próxima linha
            pos[2] = 1; // coluna volta a 1
        } else {
            pos[2]++;
        }
        pos[0]++;
    }

    /**
     * Captura uma string completa (entre aspas duplas)
     * Suporta quebras de linha dentro da string
     * 
     * @param conteudo conteúdo total
     * @param pos      array com [índice, linha, coluna]
     * @return Token da string ou null
     */
    private static Token capturarString(String conteudo, int[] pos) {
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

        return identificarToken(sb.toString(), inicioLinha, inicioColuna);
    }

    /**
     * Captura um identificador ou número
     * Consiste em letras, dígitos e underscore
     * 
     * @param conteudo conteúdo total
     * @param pos      array com [índice, linha, coluna]
     * @return Token do identificador/número ou null
     */
    private static Token capturarIdentificador(String conteudo, int[] pos) {
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

        return identificarToken(sb.toString(), inicioLinha, inicioColuna);
    }

    /**
     * Captura um operador ou símbolo de pontuação
     * 
     * @param conteudo conteúdo total
     * @param pos      array com [índice, linha, coluna]
     * @return Token do operador ou null
     */
    private static Token capturarOperador(String conteudo, int[] pos) {
        int linha = pos[1];
        int coluna = pos[2];
        Token token = identificarToken(String.valueOf(conteudo.charAt(pos[0])), linha, coluna);
        pos[2]++;
        pos[0]++;
        return token;
    }

    /**
     * Identifica o tipo de token baseado no lexema
     * Ordem: Palavra Reservada - String - Número - Operador Lógico Identificador -
     * Operador de Comparação - Operador Aritmético
     * 
     * @param lexema o lexema a ser identificado
     * @param linha  número da linha
     * @param coluna número da coluna
     * @return Token identificado
     */
    private static Token identificarToken(String lexema, int linha, int coluna) {
        // Verificar se é palavra reservada
        String tokenReservada = PalavraReservada.buscar(lexema);
        if (tokenReservada != null) {
            return new Token(TipoToken.RESERVED, tokenReservada, linha, coluna);
        }

        // Verificar se é string ("...")
        if (lexema.startsWith("\"") && lexema.endsWith("\"")) {
            return new Token(TipoToken.STRING, "[Fr]", linha, coluna);
        }

        // Verificar se é número (apenas dígitos)
        if (lexema.matches("[0-9]+")) {
            return new Token(TipoToken.NUMBER, "[Nu]", linha, coluna);
        }

        // Verificar se é operador lógico (AND OR NOT)
        if (lexema.equalsIgnoreCase("AND") || lexema.equalsIgnoreCase("OR") ||
                lexema.equalsIgnoreCase("NOT")) {
            return new Token(TipoToken.LOGICAL, "[Ol]", linha, coluna);
        }

        // Verificar se é identificador (começa com letra, contém letras/números/_)
        if (lexema.matches("[a-zA-Z][a-zA-Z0-9_]*")) {
            return new Token(TipoToken.ID, "[Id]", linha, coluna);
        }

        // Verificar se é operador de comparação (< > =)
        if (lexema.matches("[<>=]+")) {
            return new Token(TipoToken.OPERATOR, "[Or]", linha, coluna);
        }

        // Verificar se é operador aritmético (+ * / -)
        if (lexema.matches("[+\\-*/]")) {
            return new Token(TipoToken.ARITHMETIC, "[Om]", linha, coluna);
        }

        // Token não reconhecido
        System.err.println("Aviso: Lexema não reconhecido: '" + lexema + "' em linha " + linha);
        return null;
    }

    /**
     * Formata a lista de tokens para saída, mantendo as linhas do arquivo original
     * Preserva linhas vazias também
     * 
     * @param tokens lista de tokens
     * @return string formatada com quebra de linhas
     */
    public static String formatarSaida(List<Token> tokens) {
        if (tokens.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        int linhaAtual = tokens.get(0).getLinha();

        for (Token token : tokens) {
            // Se mudou de linha, adiciona quebras para cada linha vazia
            if (token.getLinha() != linhaAtual) {
                // Adiciona quebras para linhas que não têm tokens
                for (int i = linhaAtual; i < token.getLinha(); i++) {
                    sb.append("\n");
                }
                linhaAtual = token.getLinha();
            }
            sb.append(token.getValor()).append(" ");
        }
        return sb.toString().trim();
    }
}
