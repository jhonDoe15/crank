package org.example.genericcalculator.operators.types;

import org.example.genericcalculator.operators.Operator;

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
    public double apply(double operand1, double operand2) {
        return operand1 * operand2;
    }
}
