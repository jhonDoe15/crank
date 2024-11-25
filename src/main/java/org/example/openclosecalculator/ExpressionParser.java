package org.example.openclosecalculator;

import org.example.openclosecalculator.operators.Operator;
import org.example.openclosecalculator.operators.OperatorRegistry;

import java.util.*;

import static java.lang.String.format;


public class ExpressionParser {
    private final OperatorRegistry operatorRegistry;
    private final Map<String, Long> variableStorage;

    public ExpressionParser(OperatorRegistry operatorRegistry) {
        this.operatorRegistry = operatorRegistry;
        this.variableStorage = new HashMap<>();
    }


    public void evaluateExpressionLine(String expression) {
        String[] parts;
        Optional<String> assignmentOperator = operatorRegistry.getOperatorsSymbols()
                .stream().filter(opt -> expression.contains(format("%s=", opt)))
                .findFirst();
        parts = expression.split("=");
        if (assignmentOperator.isPresent()) {
            parts[0] = parts[0].substring(0, parts[0].length()-1); // to remove operator char
        }

        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid assignment expression");
        }

        String variable = parts[0].trim();
        String valueExpression = parts[1].trim();

        // Evaluate the right-hand side expression
        long value = evaluateRightSide(valueExpression);
        if(assignmentOperator.isPresent()){
            Operator operator = operatorRegistry.getOperator(assignmentOperator.get());
            Long varValue = variableStorage.getOrDefault(variable, 0L);
            value = operator.apply(varValue, value);
        }

        // Store the variable
        variableStorage.put(variable, value);
    }

    public long evaluateRightSide(String rightSideExpression) {
        rightSideExpression = preprocessExpression(rightSideExpression);
        String[] tokens = tokenize(rightSideExpression);
        Stack<Long> values = new Stack<>();
        Stack<Operator> operators = new Stack<>();

        for (String token : tokens) {
            if (isNumeric(token)) {
                values.push(Long.parseLong(token));
            } else if (isVariable(token)) {
                Long variableValue = variableStorage.get(token);
                if (variableValue == null) {
                    throw new IllegalArgumentException(format("Invalid expression: Variable %s is not defined", token));
                }
                values.push(variableValue);
            } else if (token.equals("~")) { // Negation operator
                if (values.isEmpty()) {
                    throw new IllegalArgumentException("Invalid expression: Missing value for negation");
                }
                values.push(-values.pop());
            } else if (operatorRegistry.isOperator(token)) {
                Operator operator = operatorRegistry.getOperator(token);
                while (!operators.isEmpty() &&
                        operators.peek() != null &&
                        operators.peek().getPrecedence() >= operator.getPrecedence()) {
                    evaluateTop(values, operators.pop());
                }
                operators.push(operator);
            } else if (token.equals("(")) {
                operators.push(null); // Marker for open parentheses
            } else if (token.equals(")")) {
                while (!operators.isEmpty() && operators.peek() != null) {
                    evaluateTop(values, operators.pop());
                }
                if (operators.isEmpty() || operators.pop() != null) {
                    throw new IllegalArgumentException("Invalid expression: Mismatched parentheses");
                }
            } else {
                throw new IllegalArgumentException("Invalid token: " + token);
            }
        }

        while (!operators.isEmpty()) {
            evaluateTop(values, operators.pop());
        }

        if (values.size() != 1) {
            throw new IllegalArgumentException("Invalid expression: Excess values in stack");
        }

        return values.pop();
    }



    private void evaluateTop(Stack<Long> values, Operator operator) {
        try {
            long operand2 = values.pop();
            long operand1 = values.pop();
            values.push(operator.apply(operand1, operand2));
        }catch(RuntimeException e){
            throw new IllegalArgumentException("Expression syntax error");
        }
    }

    // Check if a token is a variable (a string that starts with a letter)
    private boolean isVariable(String token) {
        return token.matches("[a-zA-Z]");
    }

    private boolean isNumeric(String str) {
        try {
            Long.parseLong(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private String preprocessExpression(String expression) {
        // Match negative numbers
        // Replace them with (0 - number)
        expression = expression.replaceAll("(?<!\\))\\s*-([0-9]+)", "(0-$1)");

        return expression;
    }



        private String[] tokenize(String expression) {
        // Trim and replace multiple spaces with a single space for clean tokenization
        expression = expression.trim().replaceAll("\\s+", "");

        // Tokenize based on operators and parentheses, while keeping them as separate tokens
        return expression.split("(?<=[-+*/()])|(?=[-+*/()])");
    }

    public void printVariables() {
        StringBuilder output = new StringBuilder("(");
        this.variableStorage.forEach((var, value) -> output.append(var).append("=").append(value).append(","));
        if (output.length() > 1) output.setLength(output.length() - 1); // Remove last comma
        output.append(")");
        System.out.println(output);
    }
}



































