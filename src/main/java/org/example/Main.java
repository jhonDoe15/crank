package org.example;

import org.example.calculator.CalculatorApp;


public class Main {
    private static final String FILE_NAME = "input-expressions.txt";

    public static void main(String[] args) {
        CalculatorApp calculator = new CalculatorApp();
        calculator.solve(false, FILE_NAME);
    }
}