package org.example.calculator;


public record ParsedExpression(String operator, String varName, Expression rightExpression) {
}
