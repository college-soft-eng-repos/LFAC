package main.java.com.projeto.core.model;

/**
 * Enum que representa os tipos de tokens reconhecidos pelo analisador léxico
 */
public enum TipoToken {
    ID("Id"),           // (letra)+
    NUMBER("Nu"),       // (digito)+
    STRING("Fr"),       // "caracteres"
    OPERATOR("Or"),     // < > =
    ARITHMETIC("Om"),   // + * / -
    LOGICAL("Ol"),      // AND OR NOT
    RESERVED("");       // palavras reservadas

    private String sigla;

    TipoToken(String sigla) {
        this.sigla = sigla;
    }

    public String getSigla() {
        return sigla;
    }
}
