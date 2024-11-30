package org.example.genericcalculator.operators;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class OperatorRegistry {
    private final Map<String, Operator> operators = new HashMap<>();

    public void registerOperator(Operator operator) {
        operators.put(operator.getSymbol(), operator);
    }

    public Operator getOperator(String symbol) {
        if (!isOperator(symbol)) throw new IllegalArgumentException("Unknown operator: " + symbol);
        return operators.get(symbol);
    }

    public Operator getOperator(char symbol) {
        return getOperator(String.valueOf(symbol));
    }

    public boolean isOperator(String symbol) {
        return operators.containsKey(symbol);
    }

    public Set<String> getOperatorsSymbols(){
        return operators.keySet();
    }

    public String getOperatorsSymbolsAsString(){
        return String.join("", operators.keySet());

    }

    public String getOperatorsSymbolsForRegex(){
        return getOperatorsSymbolsAsString().replace("-", "\\-"); // known characters to escape in regex
    }
}

