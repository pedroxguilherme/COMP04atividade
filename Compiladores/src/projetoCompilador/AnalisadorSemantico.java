package projetoCompilador;

import java.util.*;

public class AnalisadorSemantico {
    private List<Token> tokens;
    private Set<String> variaveisDeclaradas = new HashSet<>();
    private int pos = 0;
    private Token atual;

    public AnalisadorSemantico(List<Token> tokens) {
        this.tokens = tokens;
        avancar();
    }

    private void avancar() {
        if (pos < tokens.size()) {
            atual = tokens.get(pos++);
        }
    }

    private Token lookahead(int offset) {
        if (pos + offset - 1 < tokens.size()) {
            return tokens.get(pos + offset - 1);
        }
        return new Token("EOF", "EOF", -1);
    }

    private void erro(String mensagem) {
        throw new RuntimeException("Erro semântico na posição " + atual.posicao + ": " + mensagem);
    }

    public void analisar() {
        while (!atual.tipo.equals("EOF")) {
            if (atual.tipo.equals("TIPO")) {
                verificarDeclaracao();
            } else if (atual.tipo.equals("ID")) {
                verificarUso();
            }
            avancar();
        }
    }

    private void verificarDeclaracao() {
        avancar(); // TIPO
        if (!atual.tipo.equals(":")) erro("Esperado ':' após tipo.");
        avancar(); // :

        if (!atual.tipo.equals("ID")) erro("Esperado identificador após ':'.");
        String nome = atual.lexema;
        if (variaveisDeclaradas.contains(nome)) {
            erro("Variável '" + nome + "' já declarada.");
        } else {
            variaveisDeclaradas.add(nome);
        }
    }

    private void verificarUso() {
        String nome = atual.lexema;
        if (!variaveisDeclaradas.contains(nome)) {
            erro("Variável '" + nome + "' usada sem declaração.");
        }
    }
}
