package org.example.openclosecalculator.operators.types;

import org.example.openclosecalculator.operators.Operator;

public class MultiplicationOperator implements Operator {
    @Override
    public String getSymbol() {
        return "*";
    }

    @Override
    public int getPrecedence() {
        return 2;
    }

    @Override
    public long apply(long operand1, long operand2) {
        return operand1 * operand2;
    }
}
