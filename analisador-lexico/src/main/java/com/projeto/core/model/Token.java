package main.java.com.projeto.core.model;

/**
 * Representa um token identificado pelo analisador léxico
 */
public class Token {
    private TipoToken tipo;
    private String valor;
    private int linha;
    private int coluna;

    public Token(TipoToken tipo, String valor) {
        this(tipo, valor, 0, 0);
    }

    public Token(TipoToken tipo, String valor, int linha, int coluna) {
        this.tipo = tipo;
        this.valor = valor;
        this.linha = linha;
        this.coluna = coluna;
    }

    public TipoToken getTipo() {
        return tipo;
    }

    public void setTipo(TipoToken tipo) {
        this.tipo = tipo;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public int getLinha() {
        return linha;
    }

    public void setLinha(int linha) {
        this.linha = linha;
    }

    public int getColuna() {
        return coluna;
    }

    public void setColuna(int coluna) {
        this.coluna = coluna;
    }

    @Override
    public String toString() {
        return valor;
    }
}
