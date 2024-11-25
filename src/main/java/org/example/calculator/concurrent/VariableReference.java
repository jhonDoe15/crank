package org.example.calculator.concurrent;


import java.util.Collections;
import java.util.Map;
import java.util.Set;

// VariableReference for handling references to existing variables
class VariableReference extends Expression {
    private final String variableName;
    private final Map<String, Long> variables;

    public VariableReference(String variableName, Map<String, Long> variables) {
        this.variableName = variableName;
        this.variables = variables;
    }

    @Override
    public Long evaluate() {
        return variables.getOrDefault(variableName, 0L);
    }

    @Override
    public Set<String> getDependencies() {
        return Collections.singleton(variableName);
    }
}
