package main.java.com.projeto.core.model;

/**
 * Representa um token identificado pelo analisador léxico
 */
public class Token {
    private String classe;      // Tipo do token (var, id, fr, etc)
    private String valor;       // Valor específico (índice, conteúdo, etc)
    private int linha;
    private int coluna;

    /**
     * Construtor com classe e valor
     */
    public Token(String classe, String valor) {
        this(classe, valor, 0, 0);
    }

    /**
     * Construtor completo
     * 
     * @param classe o tipo do token
     * @param valor o valor específico do token
     * @param linha número da linha
     * @param coluna número da coluna
     */
    public Token(String classe, String valor, int linha, int coluna) {
        this.classe = classe;
        this.valor = valor;
        this.linha = linha;
        this.coluna = coluna;
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
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

    /**
     * Retorna a representação do token no formato [classe, valor]
     * Se valor estiver vazio, retorna apenas [classe]
     * 
     * @return representação formatada do token
     */
    @Override
    public String toString() {
        if (valor == null || valor.isEmpty()) {
            return "[" + classe + "]";
        } else {
            return "[" + classe + ", " + valor + "]";
        }
    }
}
