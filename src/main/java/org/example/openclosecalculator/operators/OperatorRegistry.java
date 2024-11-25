package org.example.openclosecalculator.operators;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class OperatorRegistry {
    private final Map<String, Operator> operators = new HashMap<>();

    public void registerOperator(Operator operator) {
        operators.put(operator.getSymbol(), operator);
    }

    public Operator getOperator(String symbol) {
        return operators.get(symbol);
    }

    public boolean isOperator(String symbol) {
        return operators.containsKey(symbol);
    }

    public Set<String> getOperatorsSymbols(){
        return operators.keySet();
    }
}

