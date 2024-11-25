package org.example.calculator.concurrent;

import java.util.HashSet;
import java.util.Set;

import static java.lang.String.format;

class OperatorExpression extends Expression {
    private final Expression left;
    private final Expression right;
    private final char operator;

    public OperatorExpression(Expression left, Expression right, char operator) {
        this.left = left;
        this.right = right;
        this.operator = operator;
    }

    @Override
    public Long evaluate() {
        Long leftValue = left.evaluate();
        Long rightValue = right.evaluate();
        return switch (operator) {
            case '+' -> leftValue + rightValue;
            case '-' -> leftValue - rightValue;
            case '*' -> leftValue * rightValue;
            case '/' -> leftValue / (rightValue == 0 ? 1 : rightValue);
            default -> throw new UnsupportedOperationException(format("Unsupported operator: %s", operator));
        };
    }

    @Override
    public Set<String> getDependencies() {
        Set<String> deps = new HashSet<>(left.getDependencies());
        deps.addAll(right.getDependencies());
        return deps;
    }
}
