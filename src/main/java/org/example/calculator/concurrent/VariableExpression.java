package org.example.calculator.concurrent;


import java.util.Map;
import java.util.Set;

class VariableExpression extends Expression {
    private final String variableName;
    private final Expression rightExpression;
    private final Map<String, Long> variables;

    public VariableExpression(String variableName, Expression rightExpression, Map<String, Long> variables) {
        this.variableName = variableName;
        this.rightExpression = rightExpression;
        this.variables = variables;
    }

    @Override
    public Long evaluate() {
        Long value = rightExpression.evaluate();
        variables.put(variableName, value);
        return value;
    }

    @Override
    public String getVariable() {
        return variableName;
    }

    @Override
    public Set<String> getDependencies() {
        return rightExpression.getDependencies();
    }
}
