package main.java.com.projeto.core.pipeline;

import java.util.ArrayList;
import java.util.List;

import main.java.com.projeto.core.model.Identificador;
import main.java.com.projeto.core.model.Token;
import main.java.com.projeto.core.pipeline.tokenizer.CapturadorLexema;
import main.java.com.projeto.core.service.GerenciadorErros;

/**
 * Orquestrador principal da tokenização
 * Coordena captura de lexemas e classificação em tokens
 */
public class Tokenizador {
    private GerenciadorErros gerenciadorErros;
    private CapturadorLexema capturador;

    /**
     * Tokeniza o conteúdo completo do arquivo
     * Processa caractere por caractere para evitar problemas com strings
     * multilinhas
     * 
     * @param conteudo conteúdo do arquivo preprocessado
     * @return lista de tokens identificados
     */
    public List<Token> tokenizar(String conteudo) {
        List<Token> tokens = new ArrayList<>();
        gerenciadorErros = new GerenciadorErros();
        capturador = new CapturadorLexema(gerenciadorErros);
        Identificador tabela = new Identificador();
        int[] pos = { 0, 1, 1 }; // i, linha, coluna

        while (pos[0] < conteudo.length()) {
            char c = conteudo.charAt(pos[0]);

            if (Character.isWhitespace(c)) {
                processarEspaco(c, pos);
            } else if (c == '"') {
                Token token = capturador.capturarString(conteudo, pos);
                if (token != null) {
                    tokens.add(token);
                }
            } else if (Character.isLetterOrDigit(c) || c == '_') {
                Token token = capturador.capturarIdentificador(conteudo, pos, tabela);
                if (token != null) {
                    tokens.add(token);
                }
            } else {
                Token token = capturador.capturarOperador(conteudo, pos);
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
     * Retorna o gerenciador de erros da última tokenização
     * 
     * @return gerenciador de erros
     */
    public GerenciadorErros obterGerenciadorErros() {
        if (gerenciadorErros == null) {
            gerenciadorErros = new GerenciadorErros();
        }
        return gerenciadorErros;
    }
}