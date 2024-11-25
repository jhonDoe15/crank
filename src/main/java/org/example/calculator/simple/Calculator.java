package org.example.calculator.simple;

import org.example.calculator.Expression;

import java.util.HashMap;
import java.util.Map;

public class Calculator {

    private final Map<String, Long> variables = new HashMap<>();
    private final ExpressionParser parser = new ExpressionParser(variables);

    public void evaluateExpressions(String[] expressions) {
        for (String expression : expressions) {
            Expression expr = parser.parseExpression(expression);
            expr.evaluate();
        }
    }

    public void printVariables() {
        StringBuilder output = new StringBuilder("(");
        this.variables.forEach((var, value) -> output.append(var).append("=").append(value).append(","));
        if (output.length() > 1) output.setLength(output.length() - 1); // Remove last comma
        output.append(")");
        System.out.println(output);
    }
}
