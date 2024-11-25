package org.example.calculator.concurrent;

// LiteralExpression for handling literal values
class LiteralExpression extends Expression {
    private final Long value;

    public LiteralExpression(Long value) {
        this.value = value;
    }

    @Override
    public Long evaluate() {
        return value;
    }
}
