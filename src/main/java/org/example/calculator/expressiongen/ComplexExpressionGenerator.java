package org.example.calculator.expressiongen;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;

public class ComplexExpressionGenerator {
    private static final String FILE_NAME = "test_inputs_cmplx2.txt";

    private static final Random random = new Random();
    private static final String[] operators = {"+", "-", "*"};
    private static final String[] compoundOperators = {"+=", "-=", "*="};

    public static void main(String[] args) {
        // Generate and print a list of complex expressions
        String[] complexExpressions = generateComplexExpressions(20_000_000);

        writeToFile(complexExpressions);
    }

    private static void writeToFile(String[] inputs) {
        Path path = Paths.get(FILE_NAME);
        try {
            Files.write(path, List.of(inputs));
            System.out.println("Test inputs written to " + FILE_NAME);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    public static String[] generateComplexExpressions(int numExpressions) {
        String[] expressions = new String[numExpressions];
        for (int i = 0; i < numExpressions; i++) {
            expressions[i] = generateExpression(i);
        }
        return expressions;
    }

    public static String generateExpression(int index) {
        String varName = "var" + index;
        String expression = varName + " = ";

        // Randomly decide the type of expression (simple, compound, or complex)
        int type = random.nextInt(3); // 0 = simple, 1 = compound, 2 = complex

        switch (type) {
            case 0: // Simple expression (e.g., var = 5 + 10)
                expression += generateSimpleExpression();
                break;
            case 1: // Compound assignment (e.g., var += var3 + 10)
                expression += generateCompoundAssignment(varName);
                break;
            case 2: // Complex expression with parentheses (e.g., var = (5 + 3) * (2 + var5))
                expression += generateComplexExpression();
                break;
        }
        return expression;
    }

    public static String generateSimpleExpression() {
        int leftOperand = random.nextInt(10) + 1;
        int rightOperand = random.nextInt(10) + 1;
        String operator = operators[random.nextInt(operators.length)];

        return leftOperand + " " + operator + " " + rightOperand;
    }

    public static String generateCompoundAssignment(String leftVar) {
        String rightVar = "var" + random.nextInt(100); // Random variable name
        String operator = compoundOperators[random.nextInt(compoundOperators.length)];
        return leftVar + " " + operator + " " + rightVar;
    }

    public static String generateComplexExpression() {
        // Generate two simple sub-expressions to combine
        String leftExpr = "(" + generateSimpleExpression() + ")";
        String rightExpr = "(" + generateSimpleExpression() + ")";
        String operator = operators[random.nextInt(operators.length)];

        return leftExpr + " " + operator + " " + rightExpr;
    }
}

