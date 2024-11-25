package org.example.calculator.UnaryOperatorsCalculator.simple;

import org.example.calculator.Expression;

import java.util.Map;

// VariableExpression for handling variable assignment
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
}
