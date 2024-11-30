package org.example.genericcalculator;


import org.example.genericcalculator.operators.OperatorRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Math.round;

public class ExpressionEvaluator {
    Pattern variablePattern = Pattern.compile("^\\w+$");
    Matcher matcher = variablePattern.matcher("");
    private final OperatorRegistry operatorRegistry;
    private final Map<String, Double> variables;
    private ExpressionParser expressionParser;

    public ExpressionEvaluator(OperatorRegistry operatorRegistry) {
        this.operatorRegistry = operatorRegistry;
        this.variables = new HashMap<>();
    }

    public double evaluate(String expression) {
        // Remove whitespace
        expression = expression.replaceAll("\\s+", "");

        // Check if it's a variable assignment
        if (expression.contains("=")) {
            return handleAssignment(expression);
        }

        throw new MalformedExpression("Invalid expression format");
    }

    private double handleAssignment(String expression) {
        // Split expression into variable and value parts
        String[] parts = expression.split("=", 2);
        String variable = parts[0].trim();
        if (operatorRegistry.getOperators().stream().anyMatch(variable::contains)){
            variable = variable.substring(0, variable.length() - 1); // assuming operator length is 1 but the entire system relies on that assumption
        }


        if (!matcher.reset(variable).matches()) {
            throw new MalformedExpression("Invalid variable name");
        }
        String valueExpression = parts[1].trim();

        // Check for compound assignment
        Matcher compoundMatcher = Pattern.compile("(\\w+)([" + operatorRegistry.getOperatorsSymbolsForRegex() + "])=").matcher(expression);
        if (compoundMatcher.find()) {
            String varName = compoundMatcher.group(1);
            String operatorSymbol = compoundMatcher.group(2);

            // Get current value of the variable
            double currentValue = variables.getOrDefault(varName, 0.0);

            // Parse and calculate the right-hand side
            double rightValue = calculateExpression(valueExpression);

            // Perform the compound operation
            double newValue = operatorRegistry.getOperator(operatorSymbol).apply(currentValue, rightValue);
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
            expression = expression.replace(entry.getKey(), entry.getValue().toString());
        }

        double evaluatedExpression = evaluateExpression(expression);
        return round(evaluatedExpression * 100.0) / 100.0;
    }

    private double evaluateExpression(String expression) {
        expressionParser = new ExpressionParser(operatorRegistry, expression);
        return expressionParser.parse();
    }

    public void printVariables() {
        StringBuilder output = new StringBuilder("(");
        this.variables.forEach((var, value) -> output.append(var).append("=").append(round(value)).append(","));
        if (output.length() > 1) output.setLength(output.length() - 1); // Remove last comma
        output.append(")");
        System.out.println(output);
    }
}
