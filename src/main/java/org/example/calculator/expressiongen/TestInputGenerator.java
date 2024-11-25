package org.example.calculator.expressiongen;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.List;

public class TestInputGenerator {
    private static final String FILE_NAME = "input-expressions.txt";

    public static void main(String[] args) {
        int numberOfInputs = 1_000_000; // Number of test inputs to generate
        String[] inputs = generateTestInputs(numberOfInputs);

        // Write generated inputs to a file
        writeToFile(inputs);

        // Read and display the content from the file
//        readFromFile();
    }

    private static String[] generateTestInputs(int numberOfInputs) {
        String[] variables = {"i", "j", "k", "x", "y", "a", "b", "c", "d", "e"};
        String[] expressions = new String[numberOfInputs];

        for (int i = 0; i < numberOfInputs; i++) {
            String varName = variables[i % variables.length];
            int randomValue = (int) (Math.random() * 100);
            String operation = getRandomOperation();
            String secondVarName = variables[(i + 1) % variables.length];

            // Create expressions
            if (Math.random() > 0.5) {
                // Simple assignment
                expressions[i] = varName + " = " + randomValue;
            } else if (Math.random() > 0.5) {
                // Basic operation
                expressions[i] = varName + " = " + secondVarName + " " + operation + " " + randomValue;
            } else {
                // Compound assignment
                expressions[i] = varName + " " + getRandomCompoundOperator() + " " + randomValue;
            }
        }
        return expressions;
    }

    private static String getRandomOperation() {
        String[] operations = {"+", "-", "*"};
        return operations[(int) (Math.random() * operations.length)];
    }

    private static String getRandomCompoundOperator() {
        String[] compoundOperators = {"+=", "-=", "*="};
        return compoundOperators[(int) (Math.random() * compoundOperators.length)];
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
}
