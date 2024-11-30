package org.example.genericcalculator.operators.types;

import org.example.genericcalculator.operators.Operator;

public class DivisionOperator implements Operator {
    @Override
    public String getSymbol() {
        return "/";
    }

    @Override
    public int getPrecedence() {
        return 2;
    }

    @Override
    public double apply(double operand1, double operand2) {
        if (operand2 == 0) throw new ArithmeticException("Division by zero");
        return operand1 / operand2;
    }
}
