package org.example.negative_expression_nested_parenthesis_calculator;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Math.round;

public class ExpressionEvaluator {
    private final Map<String, Double> variables = new HashMap<>();

    public double evaluate(String expression) {
        // Remove whitespace
        expression = expression.replaceAll("\\s+", "");

        // Check if it's a variable assignment
        if (expression.contains("=")) {
            return handleAssignment(expression);
        }

        throw new IllegalArgumentException("Invalid expression format");
    }

    private double handleAssignment(String expression) {
        // Split expression into variable and value parts
        String[] parts = expression.split("=", 2);
        String variable = parts[0].trim();
        String valueExpression = parts[1].trim();

        // Check for compound assignment
        Matcher compoundMatcher = Pattern.compile("(\\w+)([+\\-*/])=").matcher(expression);
        if (compoundMatcher.find()) {
            String varName = compoundMatcher.group(1);
            String operatorSymbol = compoundMatcher.group(2);

            // Get current value of the variable
            double currentValue = variables.getOrDefault(varName, 0.0);

            // Parse and calculate the right-hand side
            double rightValue = calculateExpression(valueExpression);

            // Perform the compound operation
            double newValue = performOperation(currentValue, rightValue, operatorSymbol);
            variables.put(varName, newValue);
            return newValue;
        }

        // Simple assignment
        double value = calculateExpression(valueExpression);
        variables.put(variable, value);
        return value;
    }

    public double calculateExpression(String expression) {
        // Replace variables with their values
        for (Map.Entry<String, Double> entry : variables.entrySet()) {
            expression = expression.replace(entry.getKey(), String.valueOf(entry.getValue()));
        }

        double evaluatedExpression = evaluateExpression(expression);
        return Math.round(evaluatedExpression * 100.0) / 100.0;
    }

    private double evaluateExpression(String expression) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < expression.length()) ? expression.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < expression.length()) throw new RuntimeException("Unexpected: " + (char)ch);
                return x;
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | number | `(` expression `)`

            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if (eat('+')) x += parseTerm(); // addition
                    else if (eat('-')) x -= parseTerm(); // subtraction
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if (eat('*')) x *= parseFactor(); // multiplication
                    else if (eat('/')) x /= parseFactor(); // division
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus

                double x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(expression.substring(startPos, this.pos));
                } else {
                    throw new RuntimeException("Unexpected: " + (char)ch);
                }

                return x;
            }
        }.parse();
    }

    private double performOperation(double left, double right, String operator) {
        return switch (operator) {
            case "+" -> left + right;
            case "-" -> left - right;
            case "*" -> left * right;
            case "/" -> {
                if (right == 0) throw new ArithmeticException("Division by zero");
                yield left / right;
            }
            default -> throw new IllegalArgumentException("Unknown operator: " + operator);
        };
    }

    public double getVariable(String varName) {
        return variables.getOrDefault(varName, 0.0);
    }

    public void printVariables() {
        StringBuilder output = new StringBuilder("(");
        this.variables.forEach((var, value) -> output.append(var).append("=").append(round(value)).append(","));
        if (output.length() > 1) output.setLength(output.length() - 1); // Remove last comma
        output.append(")");
        System.out.println(output);
    }
}
