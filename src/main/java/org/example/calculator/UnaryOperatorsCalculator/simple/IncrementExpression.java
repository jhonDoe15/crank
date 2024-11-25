package org.example.calculator.UnaryOperatorsCalculator.simple;

import org.example.calculator.Expression;

import java.util.Map;

class IncrementExpression extends Expression {
    private final String variableName;
    private final boolean isPrefix;
    private final Map<String, Long> variables;

    public IncrementExpression(String variableName, boolean isPrefix, Map<String, Long> variables) {
        this.variableName = variableName;
        this.isPrefix = isPrefix;
        this.variables = variables;
    }

    @Override
    public Long evaluate() {
        Long value = variables.getOrDefault(variableName, 0L);
        if (isPrefix) {
            value += 1;
            variables.put(variableName, value);
        } else {
            variables.put(variableName, value + 1);
        }
        return value;
    }
}
