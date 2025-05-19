package projetoCompilador;

import java.util.*;

public class AnalisadorSintatico {
    private List<Token> tokens;
    private int pos = 0;
    private Token atual;

    public AnalisadorSintatico(List<Token> tokens) {
        this.tokens = tokens;
        this.avancar();
    }

    private void avancar() {
        if (pos < tokens.size()) {
            atual = tokens.get(pos++);
        }
    }

    private boolean aceitar(String tipo) {
        if (atual.tipo.equals(tipo)) {
            avancar();
            return true;
        }
        return false;
    }

    private void esperar(String tipo) {
        if (!aceitar(tipo)) {
            erro("Esperado token: " + tipo + " mas encontrado: " + atual.tipo);
        }
    }

    private void erro(String mensagem) {
        throw new RuntimeException("Erro sintático na posição " + atual.posicao + ": " + mensagem);
    }

    public void analisar() {
        programa();
        if (!atual.tipo.equals("EOF")) {
            erro("Código após o fim do programa");
        }
    }

    private void programa() {
        listaComandos();
    }

    private void listaComandos() {
        while (atual.tipo.equals("TIPO") || atual.tipo.equals("ID") || atual.tipo.equals("LEIA")
                || atual.tipo.equals("ESCREVA") || atual.tipo.equals("SE") || atual.tipo.equals("PARA")) {
            comando();
        }
    }

    private void comando() {
        switch (atual.tipo) {
            case "TIPO": declaracao(); break;
            case "ID": atribuicao(); break;
            case "LEIA": comandoLeitura(); break;
            case "ESCREVA": comandoEscrita(); break;
            case "SE": comandoSe(); break;
            case "PARA": comandoPara(); break;
            default: erro("Comando inválido: " + atual.tipo);
        }
    }

    private void declaracao() {
        esperar("TIPO");
        esperar(":");
        esperar("ID");
        esperar(";");
    }

    private void atribuicao() {
        esperar("ID");
        esperar("ATR");
        expressao();
        esperar(";");
    }
    private void atribuicaoInline() {
        esperar("ID");
        esperar("ATR");
        expressao();
    }

    private void comandoLeitura() {
        esperar("LEIA");
        esperar("PARAB");
        esperar("ID");
        esperar("PARFE");
        esperar(";");
    }

    private void comandoEscrita() {
        esperar("ESCREVA");
        esperar("PARAB");
        if (atual.tipo.equals("ID") || atual.tipo.equals("NUMINT")) {
            expressao();
        } else if (atual.tipo.equals("STRING")) {
            avancar();
        } else {
            erro("Esperado ID, NUMINT ou STRING em escreva()");
        }
        esperar("PARFE");
        esperar(";");
    }


    private void comandoSe() {
        esperar("SE");
        expressaoLogica();
        esperar("ENTAO");
        listaComandos();
        if (aceitar("SENAO")) {
            listaComandos();
        }
        esperar("FIMSE");
    }

    private void comandoPara() {
        esperar("PARA");
        atribuicaoInline();
        esperar("ATE");
        esperar("NUMINT");
        if (aceitar("PASSO")) {
            esperar("NUMINT");
        }
        listaComandos();
        esperar("FIMPARA");
    }

    private void expressao() {
        if (aceitar("NUMINT") || aceitar("ID")) {
            while (atual.tipo.matches("OPMAIS|OPMENOS|OPMULTI|OPDIVI")) {
                avancar();
                expressao();
            }
        } else if (aceitar("PARAB")) {
            expressao();
            esperar("PARFE");
        } else {
            erro("Expressão inválida");
        }
    }

    private void expressaoLogica() {
        expressao();
        operadorRelacional();
        expressao();
    }

    private void operadorRelacional() {
        if (!(aceitar("LOGIGUAL") || aceitar("LOGDIFF") || aceitar("LOGMAIOR") || aceitar("LOGMENOR")
                || aceitar("LOGMAIORIGUAL") || aceitar("LOGMENORIGUAL"))) {
            erro("Operador relacional esperado");
        }
    }
}
