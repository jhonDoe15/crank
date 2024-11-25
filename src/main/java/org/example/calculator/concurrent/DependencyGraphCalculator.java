package org.example.calculator.concurrent;


import java.util.*;
import java.util.concurrent.*;

public class DependencyGraphCalculator {
    private final Map<String, Long> variables = new ConcurrentHashMap<>();
    private final ExpressionParser parser = new ExpressionParser(variables);
    private final ExecutorService executor = Executors.newCachedThreadPool();

    public void evaluateExpressions(String[] expressions) throws ExecutionException, InterruptedException {
        Map<String, CompletableFuture<Long>> futures = new HashMap<>();

        for (String expression : expressions) {
            Expression expr = parser.parseExpression(expression);
            String variable = expr.getVariable();

            // Collect dependencies for the current expression
            Set<String> dependencies = expr.getDependencies();
            List<CompletableFuture<Long>> dependencyFutures = new ArrayList<>();
            for (String dependency : dependencies) {
                dependencyFutures.add(futures.getOrDefault(dependency, CompletableFuture.completedFuture(variables.getOrDefault(dependency, 0L))));
            }

            // Evaluate after dependencies are complete
            CompletableFuture<Long> future = CompletableFuture.allOf(dependencyFutures.toArray(new CompletableFuture[0]))
                    .thenApplyAsync(v -> {
                        try {
                            // Wait for dependencies to complete
                            for (CompletableFuture<Long> depFuture : dependencyFutures) depFuture.join();
                            return expr.evaluate();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }, executor);

            futures.put(variable, future);
            // Store variable result
            future.thenAccept(result -> variables.put(variable, result));
        }

        // Wait for all futures to complete
        for (CompletableFuture<Long> future : futures.values()) {
            future.get();
        }

        executor.shutdown();
    }

    public void printVariables() {
        StringBuilder output = new StringBuilder("(");
        this.variables.forEach((var, value) -> output.append(var).append("=").append(value).append(","));
        if (output.length() > 1) output.setLength(output.length() - 1); // Remove last comma
        output.append(")");
        System.out.println(output);
    }
}
