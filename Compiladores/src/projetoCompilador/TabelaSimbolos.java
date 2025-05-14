package projetoCompilador;

import java.util.*;

public class TabelaSimbolos {
    private final Map<String, Integer> simbolos = new LinkedHashMap<>();
    private int proximo = 0;

    public int adicionar(String lexema) {
        if (!simbolos.containsKey(lexema)) {
            simbolos.put(lexema, proximo++);
        }
        return simbolos.get(lexema);
    }

    public void imprimir() {
        System.out.println("Tabela de Símbolos:");
        simbolos.forEach((k, v) -> System.out.println(v + ": " + k));
    }

    public Map<String, Integer> getSimbolos() {
        return simbolos;
    }
}

