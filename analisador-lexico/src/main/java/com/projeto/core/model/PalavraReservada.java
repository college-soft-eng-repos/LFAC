package main.java.com.projeto.core.model;

import java.util.HashMap;
import java.util.Map;

public class PalavraReservada {
    private static final Map<String, String> RESERVADAS = new HashMap<>();
    
    static {
        RESERVADAS.put("start", "[start]");
        RESERVADAS.put("end", "[end]");
        RESERVADAS.put("var", "[var]");
        RESERVADAS.put("read", "[read]");
        RESERVADAS.put("write", "[write]");
        RESERVADAS.put("if", "[if]");
        RESERVADAS.put("then", "[then]");
        RESERVADAS.put("(", "[(]");
        RESERVADAS.put(")", "[)]");
        RESERVADAS.put(":", "[:]");
        RESERVADAS.put(";", "[;]");
    }
    
    public static String buscar(String lexema) {
        return RESERVADAS.get(lexema.toLowerCase());
    }
}
