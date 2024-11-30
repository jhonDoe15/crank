package org.example.genericcalculator;

public class MalformedExpression extends RuntimeException {
    public MalformedExpression(String message) {
        super(message);
    }
}
