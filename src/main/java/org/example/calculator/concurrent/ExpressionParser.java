package org.example.calculator.concurrent;


import org.example.calculator.AbstractExpressionParser;
import org.example.calculator.ParsedExpression;

import java.util.Map;


// ExpressionParser updates to return parsed expressions with dependencies
class ExpressionParser extends AbstractExpressionParser {
    private final Map<String, Long> variables;

    public ExpressionParser(Map<String, Long> variables) {
        this.variables = variables;
    }

    public Expression parseExpression(String input) {
        ParsedExpression result = getParsedExpression(input);

        if (result.operator() == null) {
            // Simple assignment
            return new VariableExpression(result.varName(), (Expression) result.rightExpression(), variables);
        } else {
            // Compound assignment
            return new CompoundAssignmentExpression(result.varName(), (Expression) result.rightExpression(), result.operator(), variables);
        }
    }


    @Override
    protected Expression parse(String expr) {
        expr = expressionInitialHandling(expr);

        // Handle prefix increment/decrement (e.g., ++i, --i)
        if (expr.startsWith("++") || expr.startsWith("--")) {
            String variable = expr.substring(2).trim(); // Skip "++" or "--"
            boolean isIncrement = expr.startsWith("++");
            return isIncrement ? new IncrementExpression(variable, true, variables)
                    : new DecrementExpression(variable, true, variables);
        }

        // Handle postfix increment/decrement (e.g., i++, i--)
        if (expr.endsWith("++") || expr.endsWith("--")) {
            String variable = expr.substring(0, expr.length() - 2).trim(); // Remove "++" or "--"
            boolean isIncrement = expr.endsWith("++");
            return isIncrement ? new IncrementExpression(variable, false, variables)
                    : new DecrementExpression(variable, false, variables);
        }


//        // Handle pre-increment and pre-decrement (e.g., ++i, --i)
//        if (expr.startsWith("++")) {
//            String variable = expr.substring(2); // Skip "++"
//            return new IncrementExpression(variable, true, variables);
//        } else if (expr.startsWith("--")) {
//            String variable = expr.substring(2); // Skip "--"
//            return new DecrementExpression(variable, true, variables);
//        }
//
//        // Handle post-increment and post-decrement (e.g., i++, i--)
//        if (expr.endsWith("++")) {
//            String variable = expr.substring(0, expr.length() - 2); // Remove "++"
//            return new IncrementExpression(variable, false, variables);
//        } else if (expr.endsWith("--")) {
//            String variable = expr.substring(0, expr.length() - 2); // Remove "--"
//            return new DecrementExpression(variable, false, variables);
//        }


        // Handle expressions wrapped in parentheses
        if (expr.startsWith("(") && expr.endsWith(")")) {
            int closingParenIndex = findClosingParenthesesIndex(expr, 0);
            if (closingParenIndex == expr.length() - 1) {
                // Strip outer parentheses and parse the inner expression
                return parse(expr.substring(1, expr.length() - 1).trim());
            }
        }

        // Handle operators with precedence from lowest to highest (+, -, *, /)

        int index = findOperatorIndex(expr, "*", "/");
        if (index != -1) {
            char operator = expr.charAt(index);
            return new OperatorExpression(parse(expr.substring(0, index).trim()), parse(expr.substring(index + 1).trim()), operator);
        }

        index = findOperatorIndex(expr, "+", "-");
        if (index != -1) {
            char operator = expr.charAt(index);
            return new OperatorExpression(parse(expr.substring(0, index).trim()), parse(expr.substring(index + 1).trim()), operator);
        }


        // Handle literal numbers
        if (Character.isDigit(expr.charAt(0))) {
            return new LiteralExpression(Long.parseLong(expr));
        }

        // Handle variables
        return new VariableReference(expr, variables);
    }
}
