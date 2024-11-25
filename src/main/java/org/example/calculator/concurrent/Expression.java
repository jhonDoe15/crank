package org.example.calculator.concurrent;

import java.util.Collections;
import java.util.Set;

// Abstract Expression class
public abstract class Expression extends org.example.calculator.Expression {
    public abstract Long evaluate();

    // Default method returning null, to be overridden if needed
    public String getVariable() {
        return null;
    }

    // Default method returning empty list, to be overridden if needed
    public Set<String> getDependencies() {
        return Collections.emptySet();
    }
}
