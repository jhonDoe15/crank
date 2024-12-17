package org.example.genericcalculator;

import org.example.genericcalculator.operators.OperatorRegistry;

public class ExpressionParser {
    private final OperatorRegistry operatorRegistry;
    private final String expression;
    private int currentPosition = -1;
    private char currentCharacter;

    public ExpressionParser(OperatorRegistry operatorRegistry, String expression) {
        this.operatorRegistry = operatorRegistry;
        this.expression = expression;
    }

    private void advanceToNextCharacter() {
        currentPosition++;
        currentCharacter = (currentPosition < expression.length())
                ? expression.charAt(currentPosition)
                : (char) -1;
    }

    // Skips whitespace and checks if the current character matches the expected one
    private boolean matchAndConsumeCharacter(char expectedCharacter) {
        // Skip whitespace
        while (Character.isWhitespace(currentCharacter)) {
            advanceToNextCharacter();
        }

        if (currentCharacter == expectedCharacter) {
            advanceToNextCharacter();
            return true;
        }
        return false;
    }

    // Parses the entire expression
    public double parse() {
        advanceToNextCharacter();
        double result = parseAddSubtractExpression();

        // Ensure entire expression is consumed
        if (currentPosition < expression.length()) {
            throw new MalformedExpression("Unexpected character: " + currentCharacter);
        }
        return result;
    }

    private double parseAddSubtractExpression() {
        double value = parseMultiplyDivideExpression();

        while (true) {
            if (matchAndConsumeCharacter('+')) {
                value += parseMultiplyDivideExpression();
            } else if (matchAndConsumeCharacter('-')) {
                value -= parseMultiplyDivideExpression();
            } else {
                return value;
            }
        }
    }

    private double parseMultiplyDivideExpression() {
        double value = parseNumberOrParenthesis();

        while (true) {
            boolean operatorFound = false;

            for (char operator : operatorRegistry.getSpecialOperatorsSymbolsAsString().toCharArray()) {
                if (matchAndConsumeCharacter(operator)) {
                    value = operatorRegistry.getOperator(operator).apply(value, parseNumberOrParenthesis());
                    operatorFound = true;
                    break;
                }
            }

            if (!operatorFound) {
                return value;
            }
        }
    }

    private double parseNumberOrParenthesis() {
        if (matchAndConsumeCharacter('+')) {
            return parseNumberOrParenthesis();
        }
        if (matchAndConsumeCharacter('-')) {
            return -parseNumberOrParenthesis();
        }

        if (matchAndConsumeCharacter('(')) {
            double value = parseAddSubtractExpression();
            matchAndConsumeCharacter(')'); // Consume closing parenthesis
            return value;
        }

        return parseNumericLiteral();
    }

    private double parseNumericLiteral() {
        int startPosition = currentPosition;

        // Consume all characters that make up a number
        while (isPartOfNumber(currentCharacter)) {
            advanceToNextCharacter();
        }

        String numericSubstring = expression.substring(startPosition, currentPosition);

        try {
            return Double.parseDouble(numericSubstring);
        } catch (NumberFormatException e) {
            throw new MalformedExpression("Invalid number format: " + numericSubstring);
        }
    }

    private boolean isPartOfNumber(char c) {
        return Character.isDigit(c) || c == '.';
    }
}
