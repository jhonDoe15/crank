package org.example.genericcalculator.operators;

public interface Operator {
    String getSymbol(); // e.g., "+", "-", etc.
    int getPrecedence(); // Operator precedence
    double apply(double operand1, double operand2); // Perform the operation
}

