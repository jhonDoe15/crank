package org.example.openclosecalculator.operators.types;

import org.example.openclosecalculator.operators.Operator;

public class SubtractionOperator implements Operator {
    @Override
    public String getSymbol() {
        return "-";
    }

    @Override
    public int getPrecedence() {
        return 1;
    }

    @Override
    public long apply(long operand1, long operand2) {
        return operand1 - operand2;
    }
}
