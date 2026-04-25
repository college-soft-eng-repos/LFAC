package main.java.com.projeto.core.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Tabela para armazenar identificadores
 * Cada ID novo recebe um índice sequencial
 */
public class Identificador {
    private Map<String, Integer> identificadores;
    private int proximoIndice;

    public Identificador() {
        this.identificadores = new HashMap<>();
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
    public int obterOuRegistrar(String identificador) {
        if (identificadores.containsKey(identificador)) {
            return identificadores.get(identificador);
        } else {
            int indice = proximoIndice;
            identificadores.put(identificador, indice);
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
    public int buscar(String identificador) {
        return identificadores.getOrDefault(identificador, -1);
    }

    /**
     * Retorna todos os símbolos registrados
     * 
     * @return mapa de símbolos
     */
    public Map<String, Integer> obterTodos() {
        return new HashMap<>(identificadores);
    }

    /**
     * Retorna o número de símbolos registrados
     * 
     * @return quantidade de símbolos
     */
    public int getTamanho() {
        return identificadores.size();
    }
}
