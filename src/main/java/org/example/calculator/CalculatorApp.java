package org.example.calculator;

import org.example.calculator.simple.Calculator;
import org.example.calculator.validation.ExpressionValidator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Objects;
import java.util.logging.Logger;

import static java.lang.String.format;

public class CalculatorApp {
    private static final Logger log = Logger.getLogger(CalculatorApp.class.getName());

    private final Calculator calculator = new Calculator();
    private final ExpressionValidator validator = new ExpressionValidator();

    public void solve(String[] expressions, boolean verbose) {
        long startTime = System.nanoTime();
        if (!validator.valid(expressions)) {
            if (verbose) log.severe("Invalid expressions");
            return;
        }
        else {
            if (verbose) log.fine("Valid expressions");
            calculator.evaluateExpressions(expressions);
            if (verbose) log.fine("Evaluated expressions");
        }
        calculator.printVariables();
        if (verbose) printRuntime(startTime, "validation parsing and evaluation");
    }

    public void solve(boolean verbose, String fileName) {
        long startTime = System.nanoTime();
        String[] expressions = Objects.requireNonNull(readFromFile(fileName, verbose));
        if (verbose) printRuntime(startTime, "file reading");
        solve(expressions, verbose);
    }

    private static String[] readFromFile(String fileName, boolean verbose) {
        Path path = Paths.get(fileName);
        try {
            String[] lines = Files.readAllLines(path).toArray(new String[0]);
            if(verbose) log.info(format("Read file %s", fileName));
            return lines;
        } catch (IOException e) {
            log.severe(format("Error reading from file: %s, cause: %s", fileName, e.getMessage()));
        }
        return null;
    }

    private static void printRuntime(long startTime, String operation) {
        long endTime   = System.nanoTime();
        long totalTime = endTime - startTime;
        NumberFormat formatter = new DecimalFormat("#0.00000");

        String secondsFormattedRuntime = formatter.format(totalTime / 1_000_000_000d);
        log.info(format("Execution time was %s seconds for %s", secondsFormattedRuntime, operation));
    }
}
