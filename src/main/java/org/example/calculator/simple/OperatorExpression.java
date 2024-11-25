package org.example.calculator.simple;

import org.example.calculator.Expression;

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
            case '/' -> {
                if (rightValue == 0) {
                    throw new ArithmeticException("Division by zero in compound assignment.");
                }

                yield leftValue / rightValue;
            }
            default -> throw new UnsupportedOperationException("Unsupported operator: " + operator);
        };
    }
}
