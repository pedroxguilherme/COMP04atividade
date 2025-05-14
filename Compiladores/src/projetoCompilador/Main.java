package projetoCompilador;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        String conteudo = Files.readString(Path.of("entrada.POR"));
        TabelaSimbolos tabela = new TabelaSimbolos();
        List<Token> tokens = AnalisadorLexico.analisar(conteudo, tabela);

        try (PrintWriter writer = new PrintWriter("saida.TEM")) {
            for (Token token : tokens) {
                writer.println(token.tipo);
            }
        }
        
        System.out.println(tokens);

        System.out.println("Tokens reconhecidos:");
        for (Token t : tokens) {
            System.out.println(t.lexema + t.tipo);
        }
        
        
        AnalisadorSintatico sintatico = new AnalisadorSintatico(tokens);
        try {
            sintatico.analisar();
            System.out.println("Análise sintática concluída com sucesso.");
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
        }
        
        

     
      
    }
}
