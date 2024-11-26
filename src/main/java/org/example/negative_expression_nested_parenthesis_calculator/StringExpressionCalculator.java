package org.example.negative_expression_nested_parenthesis_calculator;


public class StringExpressionCalculator {


    public static void main(String[] args) {
        ExpressionEvaluator calculator = new ExpressionEvaluator();

        // Demonstrate various expressions
        System.out.println("x = 1: " + calculator.evaluate("x = 1"));
        System.out.println("x += 3 * 5: " + calculator.evaluate("x += 3 * 5"));
        System.out.println("x value: " + calculator.getVariable("x"));
        System.out.println("x -= 7 - (6 - 50): " + calculator.evaluate("x -= 7 - (6 - 50)"));
        System.out.println("x value: " + calculator.getVariable("x"));

        // Demonstrate variable assignment
        System.out.println("y = 10: " + calculator.evaluate("y = 10"));
        System.out.println("x = 10 + 10: " + calculator.evaluate("x = 10 + 10"));
        System.out.println("Final x value: " + calculator.getVariable("x"));
        calculator.printVariables();
    }
}

