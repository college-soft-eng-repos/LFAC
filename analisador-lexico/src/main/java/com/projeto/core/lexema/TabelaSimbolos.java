package main.java.com.projeto.core.lexema;

import java.util.HashMap;
import java.util.Map;

/**
 * Tabela de símbolos para armazenar identificadores
 * Cada ID novo recebe um índice sequencial
 */
public class TabelaSimbolos {
    private Map<String, Integer> simbolos;
    private int proximoIndice;

    public TabelaSimbolos() {
        this.simbolos = new HashMap<>();
        this.proximoIndice = 0;
    }

    /**
     * Busca ou registra um símbolo na tabela
     * Se já existe, retorna o índice existente
     * Se não existe, cria um novo índice
     * 
     * @param simbolo o identificador a registrar
     * @return o índice do símbolo na tabela
     */
    public int obterOuRegistrar(String simbolo) {
        if (simbolos.containsKey(simbolo)) {
            return simbolos.get(simbolo);
        } else {
            int indice = proximoIndice;
            simbolos.put(simbolo, indice);
            proximoIndice++;
            return indice;
        }
    }

    /**
     * Busca o índice de um símbolo já registrado
     * 
     * @param simbolo o identificador a buscar
     * @return o índice ou -1 se não encontrado
     */
    public int buscar(String simbolo) {
        return simbolos.getOrDefault(simbolo, -1);
    }

    /**
     * Retorna todos os símbolos registrados
     * 
     * @return mapa de símbolos
     */
    public Map<String, Integer> obterTodos() {
        return new HashMap<>(simbolos);
    }

    /**
     * Retorna o número de símbolos registrados
     * 
     * @return quantidade de símbolos
     */
    public int getTamanho() {
        return simbolos.size();
    }
}
