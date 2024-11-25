package org.example.calculator.UnaryOperatorsCalculator.simple;

import org.example.calculator.AbstractExpressionParser;
import org.example.calculator.Expression;
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
            return new VariableExpression(result.varName(), result.rightExpression(), variables);
        } else {
            // Compound assignment
            return new CompoundAssignmentExpression(result.varName(), result.rightExpression(), result.operator(), variables);
        }
    }

    protected Expression parse(String expr) {
        expr = expr.trim();

        // Check for empty expression after trimming
        if (expr.isEmpty()) {
            throw new IllegalArgumentException("Expression cannot be empty. expression wasn't phrased correctly");
        }

        if (expr.startsWith("++") || expr.startsWith("--")) {
            String variable = expr.substring(2).trim();
            boolean isIncrement = expr.startsWith("++");
            return isIncrement ? new IncrementExpression(variable, true, variables)
                    : new DecrementExpression(variable, true, variables);
        }

        if (expr.endsWith("++") || expr.endsWith("--")) {
            String variable = expr.substring(0, expr.length() - 2).trim();
            boolean isIncrement = expr.endsWith("++");
            return isIncrement ? new IncrementExpression(variable, false, variables)
                    : new DecrementExpression(variable, false, variables);
        }

        if (expr.startsWith("(") && expr.endsWith(")")) {
            int closingParenIndex = findClosingParenthesesIndex(expr, 0);
            if (closingParenIndex == expr.length() - 1) {
                // Strip outer parentheses and parse the inner expression
                return parse(expr.substring(1, expr.length() - 1).trim());
            }
        }

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


        if (Character.isDigit(expr.charAt(0))) {
            return new LiteralExpression(Long.parseLong(expr));
        }

        return new VariableReference(expr, variables);
    }
}
