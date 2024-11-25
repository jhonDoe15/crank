package org.example.openclosecalculator;

import org.example.openclosecalculator.operators.*;
import org.example.openclosecalculator.operators.types.AdditionOperator;
import org.example.openclosecalculator.operators.types.DivisionOperator;
import org.example.openclosecalculator.operators.types.MultiplicationOperator;
import org.example.openclosecalculator.operators.types.SubtractionOperator;

public class Calculator {
    public static void main(String[] args) {
        // Initialize the registry
        OperatorRegistry registry = new OperatorRegistry();
        registry.registerOperator(new AdditionOperator());
        registry.registerOperator(new SubtractionOperator());
        registry.registerOperator(new MultiplicationOperator());
        registry.registerOperator(new DivisionOperator());

        // Initialize the parser
        ExpressionParser parser = new ExpressionParser(registry);

        // Evaluate expressions
        parser.evaluateExpressionLine("i = 10 + 5 * 2 - 3");
        parser.evaluateExpressionLine("j = i + 20 / 5 + 3");
        parser.printVariables();
    }
}

