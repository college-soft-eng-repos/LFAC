package main.java.com.projeto.core.analyzer;

import java.util.ArrayList;
import java.util.List;

import main.java.com.projeto.core.lexema.PalavraReservada;
import main.java.com.projeto.core.lexema.TabelaSimbolos;
import main.java.com.projeto.core.model.Token;

/**
 * Classe responsável por tokenizar o conteúdo do arquivo preprocessado
 * Reconhece: palavras reservadas, identificadores, números, strings,
 * operadores, etc
 * 
 * Formato de saída: [classe, valor] onde:
 * - classe: tipo do token (var, id, fr, etc)
 * - valor: índice na tabela (para id), conteúdo (para fr), valor (para nu), etc
 */
public class Tokenizacao {
    private static GerenciadorErros gerenciadorErros;

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
        gerenciadorErros = new GerenciadorErros();
        TabelaSimbolos tabela = new TabelaSimbolos();
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
                Token token = capturarIdentificador(conteudo, pos, tabela);
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
     * Retorna o gerenciador de erros da última tokenização
     * 
     * @return gerenciador de erros
     */
    public static GerenciadorErros obterGerenciadorErros() {
        if (gerenciadorErros == null) {
            gerenciadorErros = new GerenciadorErros();
        }
        return gerenciadorErros;
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

        return identificarToken(sb.toString(), inicioLinha, inicioColuna, null);
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
    private static Token capturarIdentificador(String conteudo, int[] pos, TabelaSimbolos tabela) {
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

        return identificarToken(sb.toString(), inicioLinha, inicioColuna, tabela);
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
        Token token = identificarToken(String.valueOf(conteudo.charAt(pos[0])), linha, coluna, null);
        pos[2]++;
        pos[0]++;
        return token;
    }

    /**
     * Identifica o tipo de token baseado no lexema
     * Formato: [classe, valor] onde valor pode ser:
     * - Para ID: índice na tabela de símbolos
     * - Para FR: conteúdo da string
     * - Para NU: valor numérico
     * - Para palavra reservada: vazio (apenas [classe])
     * 
     * @param lexema o lexema a ser identificado
     * @param linha  número da linha
     * @param coluna número da coluna
     * @param tabela tabela de símbolos (null para operadores e palavras reservadas)
     * @return Token identificado
     */
    private static Token identificarToken(String lexema, int linha, int coluna, TabelaSimbolos tabela) {
        // Verificar se é palavra reservada
        String tokenReservada = PalavraReservada.buscar(lexema);
        if (tokenReservada != null) {
            // Palavras reservadas: [classe] sem valor
            return new Token(tokenReservada.replaceAll("[\\[\\]]", ""), "", linha, coluna);
        }

        // Verificar se é string ("...")
        if (lexema.startsWith("\"") && lexema.endsWith("\"")) {
            // String: [fr, conteúdo]
            String conteudo = lexema.substring(1, lexema.length() - 1);
            return new Token("fr", conteudo, linha, coluna);
        }

        // Verificar se é número (apenas dígitos)
        if (lexema.matches("[0-9]+")) {
            // Número: [nu, valor]
            return new Token("nu", lexema, linha, coluna);
        }

        // Verificar se é operador lógico (AND OR NOT)
        if (lexema.equalsIgnoreCase("AND") || lexema.equalsIgnoreCase("OR") ||
                lexema.equalsIgnoreCase("NOT")) {
            // Operador lógico: [ol, símbolo]
            return new Token("ol", lexema.toLowerCase(), linha, coluna);
        }

        // Verificar se é identificador (começa com letra, contém letras/números/_)
        if (lexema.matches("[a-zA-Z][a-zA-Z0-9_]*")) {
            // Identificador: [id, índice_tabela]
            if (tabela != null) {
                int indice = tabela.obterOuRegistrar(lexema);
                return new Token("id", String.valueOf(indice), linha, coluna);
            }
            return new Token("id", "0", linha, coluna);
        }

        // Verificar se é operador de comparação (< > =)
        if (lexema.matches("[<>=]+")) {
            // Operador de comparação: [or, símbolo]
            return new Token("or", lexema, linha, coluna);
        }

        // Verificar se é operador aritmético (+ * / -)
        if (lexema.matches("[+\\-*/]")) {
            // Operador aritmético: [om, símbolo]
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
