package org.example.genericcalculator.operators.types;

import org.example.genericcalculator.operators.Operator;

public class PowerOperator implements Operator {
    @Override
    public String getSymbol() {
        return "^";
    }

    @Override
    public int getPrecedence() {
        return 2;
    }

    @Override
    public double apply(double operand1, double operand2) {
        return Math.pow(operand1, operand2);
    }
}
