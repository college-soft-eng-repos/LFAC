package main.java.com.projeto.core;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Classe responsável por tokenizar o conteúdo do arquivo preprocessado
 * Reconhece: palavras reservadas, identificadores, números, strings, operadores, etc
 */
public class Tokenizacao {

    /**
     * Tokeniza o conteúdo completo do arquivo
     * @param conteudo conteúdo do arquivo preprocessado
     * @return lista de tokens identificados
     */
    public static List<Token> tokenizar(String conteudo) {
        List<Token> tokens = new ArrayList<>();
        
        // Split por espaços, quebras de linha e caracteres especiais
        // Mantém os delimitadores também
        Pattern pattern = Pattern.compile("[\\s]+|[^\\s]+");
        Matcher matcher = pattern.matcher(conteudo);
        
        int linha = 1;
        int coluna = 1;
        
        while (matcher.find()) {
            String lexema = matcher.group();
            
            // Ignorar espaços em branco
            if (lexema.trim().isEmpty()) {
                // Contar quebras de linha
                if (lexema.contains("\n")) {
                    linha++;
                    coluna = 1;
                } else {
                    coluna += lexema.length();
                }
                continue;
            }
            
            Token token = identificarToken(lexema, linha, coluna);
            if (token != null) {
                tokens.add(token);
            }
            
            coluna += lexema.length();
        }
        
        return tokens;
    }

    /**
     * Identifica o tipo de token baseado no lexema
     * Ordem: Palavra Reservada → String → Número → Identificador → Operador → Lógico → Aritmético
     * @param lexema o lexema a ser identificado
     * @param linha número da linha
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
            return new Token(TipoToken.STRING, lexema, linha, coluna);
        }

        // Verificar se é número (apenas dígitos)
        if (lexema.matches("[0-9]+")) {
            return new Token(TipoToken.NUMBER, "[Nu]", linha, coluna);
        }

        // Verificar se é identificador (começa com letra, contém letras/números/_)
        if (lexema.matches("[a-zA-Z][a-zA-Z0-9_]*")) {
            return new Token(TipoToken.ID, "[Id]", linha, coluna);
        }

        // Verificar se é operador de comparação (< > =)
        if (lexema.matches("[<>=]+")) {
            return new Token(TipoToken.OPERATOR, "[Or]", linha, coluna);
        }

        // Verificar se é operador lógico (AND OR NOT)
        if (lexema.equalsIgnoreCase("AND") || lexema.equalsIgnoreCase("OR") || 
            lexema.equalsIgnoreCase("NOT")) {
            return new Token(TipoToken.LOGICAL, "[Ol]", linha, coluna);
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
     * Formata a lista de tokens para saída
     * @param tokens lista de tokens
     * @return string formatada
     */
    public static String formatarSaida(List<Token> tokens) {
        StringBuilder sb = new StringBuilder();
        for (Token token : tokens) {
            sb.append(token.getValor()).append(" ");
        }
        return sb.toString().trim();
    }
}
