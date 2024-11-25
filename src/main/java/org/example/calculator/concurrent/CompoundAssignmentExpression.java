package org.example.calculator.concurrent;


import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class CompoundAssignmentExpression extends Expression {
    private final String variableName;
    private final Expression rightExpression;
    private final String operator;
    private final Map<String, Long> variables;

    public CompoundAssignmentExpression(String variableName, Expression rightExpression, String operator, Map<String, Long> variables) {
        this.variableName = variableName;
        this.rightExpression = rightExpression;
        this.operator = operator;
        this.variables = variables;
    }

    @Override
    public Long evaluate() {
        Long currentValue = variables.getOrDefault(variableName, 0L);
        Long rightValue = rightExpression.evaluate();
        long newValue;

        switch (operator) {
            case "+=" -> newValue = currentValue + rightValue;
            case "-=" -> newValue = currentValue - rightValue;
            case "*=" -> newValue = currentValue * rightValue;
            case "/=" -> {
                if (rightValue == 0) {
                    throw new ArithmeticException("Division by zero in compound assignment.");
                }
                newValue = currentValue / rightValue;
            }
            default -> throw new UnsupportedOperationException("Unsupported compound operator: " + operator);
        }

        variables.put(variableName, newValue);
        return newValue;
    }

    @Override
    public Set<String> getDependencies() {
        Set<String> dependencies = new HashSet<>(rightExpression.getDependencies());
        dependencies.add(variableName); // Depends on the variable being updated and right expression
        return dependencies;
    }
}
