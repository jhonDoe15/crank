package org.example.calculator.concurrent;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

class IncrementExpression extends Expression {
    private final String variableName;
    private final boolean isPrefix;
    private final Map<String, Long> variables;

    public IncrementExpression(String variableName, boolean isPrefix, Map<String, Long> variables) {
        this.variableName = variableName;
        this.isPrefix = isPrefix;
        this.variables = variables;
    }

    @Override
    public Long evaluate() {
        Long value = variables.getOrDefault(variableName, 0L);
        if (isPrefix) {
            value += 1;
            variables.put(variableName, value);
        } else {
            variables.put(variableName, value + 1);
        }
        return value;
    }

    @Override
    public Set<String> getDependencies() {
        return Collections.singleton(variableName); // Depends on the incremented variable
    }
}
