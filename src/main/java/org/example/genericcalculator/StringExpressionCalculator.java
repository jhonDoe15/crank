package org.example.genericcalculator;

import org.example.genericcalculator.operators.OperatorRegistry;
import org.example.genericcalculator.operators.types.AdditionOperator;
import org.example.genericcalculator.operators.types.DivisionOperator;
import org.example.genericcalculator.operators.types.MultiplicationOperator;
import org.example.genericcalculator.operators.types.SubtractionOperator;

public class StringExpressionCalculator {


    public static void main(String[] args) {


        // Initialize the registry
        OperatorRegistry registry = new OperatorRegistry();
        registry.registerOperator(new AdditionOperator());
        registry.registerOperator(new SubtractionOperator());
        registry.registerOperator(new MultiplicationOperator());
        registry.registerOperator(new DivisionOperator());
        ExpressionEvaluator calculator = new ExpressionEvaluator(registry);

        // Demonstrate various expressions
        System.out.println("x = 1: " + calculator.evaluate("x = 1"));
        System.out.println("x += 3 * 5: " + calculator.evaluate("x += 3 * 5"));
        System.out.println("x -= 7 - (6 - 50): " + calculator.evaluate("x -= 7 - (6 - 50)"));

        // Demonstrate variable assignment
        System.out.println("y = 10: " + calculator.evaluate("y = 10"));
        System.out.println("x = 10 + 10: " + calculator.evaluate("x = 10 + 10"));
        calculator.printVariables();
    }
}

