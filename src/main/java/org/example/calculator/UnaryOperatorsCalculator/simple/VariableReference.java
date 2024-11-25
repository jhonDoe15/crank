package org.example.calculator.UnaryOperatorsCalculator.simple;

import org.example.calculator.Expression;

import java.util.Map;

// VariableReference for handling references to existing variables
class VariableReference extends Expression {
    private final String variableName;
    private final Map<String, Long> variables;

    public VariableReference(String variableName, Map<String, Long> variables) {
        this.variableName = variableName;
        this.variables = variables;
    }

    @Override
    public Long evaluate() {
        return variables.getOrDefault(variableName, 0L);
    }
}
