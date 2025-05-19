package projetoCompilador;

import java.util.*;
import java.text.Normalizer;
import java.util.regex.*;

public class AnalisadorLexico {
    private static final Map<String, String> palavrasReservadas = new HashMap<>();
  
    private static final Pattern padrao = Pattern.compile(
    	    "\"[^\"]*\"|<=|>=|<>|<-|[\\p{L}_][\\p{L}\\p{N}_]*|[0-9]+|[+\\-*/=><():;]"
    	);

    

    
    static {
        String[][] reservas = {
            {"inteiro", "TIPO"}, {"leia", "LEIA"}, {"escreva", "ESCREVA"},
            {"se", "SE"}, {"então", "ENTAO"}, {"senão", "SENAO"}, {"fim_se", "FIMSE"},
            {"para", "PARA"}, {"até", "ATE"}, {"passo", "PASSO"}, {"fim_para", "FIMPARA"},
            {"e", "E"}, {"ou", "OU"}, {"nao", "NAO"}
        };
        for (String[] r : reservas) palavrasReservadas.put(r[0], r[1]);
    }

 
    
    public static List<Token> analisar(String codigo, TabelaSimbolos tabela) {
        List<Token> tokens = new ArrayList<>();
        Matcher matcher = padrao.matcher(codigo);

        while (matcher.find()) {
            String lexema = matcher.group();
            String tipo = determinarTipo(lexema);
            int pos = tabela.adicionar(lexema);
            tokens.add(new Token(lexema, tipo, pos));
        }

        tokens.add(new Token("EOF", "EOF", -1));
        return tokens;
    }

    private static String determinarTipo(String lexema) {
    	
        if (palavrasReservadas.containsKey(lexema)) return palavrasReservadas.get(lexema);
        if (lexema.matches("[0-9]+")) return "NUMINT";
        if (lexema.matches("\"[^\"]*\"")) return "STRING";
        switch (lexema) {
            case "<-": return "ATR";
            case "+": return "OPMAIS";
            case "-": return "OPMENOS";
            case "*": return "OPMULTI";
            case "/": return "OPDIVI";
            case "=": return "LOGIGUAL";
            case "<>": return "LOGDIFF";
            case ">=": return "LOGMAIORIGUAL";
            case "<=": return "LOGMENORIGUAL";
            case ">": return "LOGMAIOR";
            case "<": return "LOGMENOR";
            case "(": return "PARAB";
            case ")": return "PARFE";
            case ":": return ":";
            case ";": return ";";
            default: return "ID";
        }
    }
}
