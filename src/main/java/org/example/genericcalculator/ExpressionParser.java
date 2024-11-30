package org.example.genericcalculator;

import org.example.genericcalculator.operators.OperatorRegistry;

public class ExpressionParser {
    private final OperatorRegistry operatorRegistry;
    int expressionPosition = -1, ch;
    String expression;

    public ExpressionParser(OperatorRegistry operatorRegistry, String expression) {
        this.operatorRegistry = operatorRegistry;
        this.expression = expression;
    }

    void nextChar() {
        expressionPosition++;
        if (expressionPosition >= expression.length()) {
            ch = -1;
            return;
        }
        ch = expression.charAt(expressionPosition);
    }

    boolean processChar(int charToProcess) {
        while (ch == ' ') nextChar();
        if (ch == charToProcess) {
            nextChar();
            return true;
        }
        return false;
    }

    double parse() {
        nextChar();
        double x = parseExpression();
        if (expressionPosition < expression.length()) throw new RuntimeException("Unexpected: " + (char)ch);
        return x;
    }

    // Grammar:
    // expression = term | expression `+` term | expression `-` term
    // term = factor | term `*` factor | term `/` factor
    // factor = `+` factor | `-` factor | number | `(` expression `)`

    double parseExpression() {
        double x = parseTerm();
        for (;;) {
            if (processChar('+')) x += parseTerm(); // addition
            else if (processChar('-')) x -= parseTerm(); // subtraction
            else return x;
        }
    }

    double parseTerm() {
        double x = parseNumber();
        for (;;) {
            if (processChar('*')) x *= parseNumber(); // multiplication
            else if (processChar('/')) x /= parseNumber(); // division
            else return x;
        }
    }

    double parseNumber() {
        if (processChar('+')) return parseNumber(); // unary plus
        if (processChar('-')) return -parseNumber(); // unary minus

        double parsedNumber;
        int startPos = this.expressionPosition;
        if (processChar('(')) { // parentheses
            parsedNumber = parseExpression();
            processChar(')');
        } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
            while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
            parsedNumber = Double.parseDouble(expression.substring(startPos, this.expressionPosition));
        } else {
            throw new RuntimeException("Unexpected: " + (char)ch);
        }

        return parsedNumber;
    }
}
