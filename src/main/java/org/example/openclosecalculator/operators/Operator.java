package org.example.openclosecalculator.operators;

public interface Operator {
    String getSymbol(); // e.g., "+", "-", etc.
    int getPrecedence(); // Operator precedence
    long apply(long operand1, long operand2); // Perform the operation
}

