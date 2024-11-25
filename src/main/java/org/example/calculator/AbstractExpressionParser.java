package org.example.calculator;

public abstract class AbstractExpressionParser {


    protected ParsedExpression getParsedExpression(String input) {
        String[] parts;
        String operator = null;

        // Check for compound assignment operators
        if (input.contains("+=")) {
            parts = input.split("\\+=", 2);
            operator = "+=";
        } else if (input.contains("-=")) {
            parts = input.split("-=", 2);
            operator = "-=";
        } else if (input.contains("*=")) {
            parts = input.split("\\*=", 2);
            operator = "*=";
        } else if (input.contains("/=")) {
            parts = input.split("/=", 2);
            operator = "/=";
        } else {
            // Default to simple assignment "="
            parts = input.split("=", 2);
        }

        String varName = parts[0].trim();
        String expr = parts[1].trim();
        Expression rightExpression = parse(expr);
        return new ParsedExpression(operator, varName, rightExpression);
    }

    protected abstract Expression parse(String expr);


    // Helper method to find operator index considering operator precedence
    public static int findOperatorIndex(String expr, String... operators) {
        int balance = 0; // Track parentheses balance
        for (int i = expr.length() - 1; i >= 0; i--) {
            char c = expr.charAt(i);

            if (c == ')') {
                balance++;
            } else if (c == '(') {
                balance--;
            } else if (balance == 0) {
                // Check if the character is one of the target operators
                for (String op : operators) {
                    if (String.valueOf(c).equals(op)) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }


    public static int findClosingParenthesesIndex(String expr, int openParenthesesIndex) {
        int balance = 1; // We start right after the initial '('
        for (int i = openParenthesesIndex + 1; i < expr.length(); i++) {
            char c = expr.charAt(i);
            if (c == '(') {
                balance++;
            } else if (c == ')') {
                balance--;
                if (balance == 0) {
                    return i;
                }
            }
        }
        throw new IllegalArgumentException("Unbalanced parentheses in expression: " + expr);
    }

    public static String expressionInitialHandling(String expr) {
        expr = expr.trim();

        // Check for empty expression after trimming
        if (expr.isEmpty()) {
            throw new IllegalArgumentException("Expression cannot be empty.");
        }
        return expr;
    }
}
