package projetoCompilador;

public class Token {
    public String lexema;
    public String tipo;
    public int posicao;

    public Token(String lexema, String tipo, int posicao) {
        this.lexema = lexema;
        this.tipo = tipo;
        this.posicao = posicao;
    }

    @Override
    public String toString() {
        return tipo + " (" + lexema + ") - pos: " + posicao;
    }
}