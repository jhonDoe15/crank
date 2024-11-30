package org.example.genericcalculator;

import org.example.genericcalculator.operators.OperatorRegistry;
import org.example.genericcalculator.operators.types.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ExpressionEvaluatorTest {

    static ExpressionEvaluator expressionEvaluator;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;


    @BeforeEach
    public void setUpStreams() {
        // Initialize the registry
        OperatorRegistry registry = new OperatorRegistry();
        registry.registerOperator(new AdditionOperator());
        registry.registerOperator(new SubtractionOperator());
        registry.registerOperator(new MultiplicationOperator());
        registry.registerOperator(new DivisionOperator());
        expressionEvaluator = new ExpressionEvaluator(registry);
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    public static Stream<Arguments> validCalculatorExpressionsWithResults() {
        return Stream.of(
                Arguments.of("10 + 5 * 2 - 3", 17.0),
                Arguments.of("20 / 5 + 3", 7.0),
                Arguments.of("5 * (2 + 3)", 25.0),
                Arguments.of("(5 + 3) * 2", 16.0),
                Arguments.of("5 + 3 * (2 + 3)", 20.0),
                Arguments.of("(5 + 3) * (2 + 3)", 40.0),
                Arguments.of("(5 + (3 + 2)) * (2 + 3)", 50.0),
                Arguments.of("(-5 + 3) * (2 + 3)", -10.0),
                Arguments.of("5 + 3 * (2 + -3)", 2.0),
                Arguments.of("(5 + 3) * (2 + -3)", -8.0),
                Arguments.of("-5 + 3 * (2 + -3)", -8.0),
                Arguments.of("(-5 + 3) * (2 + -3)", 2.0),
                Arguments.of("5 + 3 * (-2 + 3)", 8.0),
                Arguments.of("(5 + 3) * (-2 + 3)", 8.0),
                Arguments.of("-5 + 3 * (-2 + 3)", -2.0),
                Arguments.of("(-5 + 3) * (-2 + 3)", -2.0),
                Arguments.of("-5 + 3 * (-2 + -3)", -20.0),
                Arguments.of("(-5 + 3) * (-2 + -3)", 10.0),
                Arguments.of("5 + 3 * (-2 + -3)", -10.0),
                Arguments.of("-5 + 3 * (2 + 3)", 10.0),

                // Edge case: complex expressions with multiple parentheses
                Arguments.of("5 + (3 * (2 + 3))", 20.0),
                Arguments.of("((5 + 3) * (2 + 3))", 40.0),
                Arguments.of("5 + ((3 + 2) * (2 + 3))", 30.0),
                Arguments.of("5 * (3 + (2 + 3))", 40.0),
                Arguments.of("((5 + 3) + (2 + 3))", 13.0),

                Arguments.of("0 / 5", 0.0),


                // Large numbers
                Arguments.of("1000000000 / 100000", 10000.0),

                // Mixed operations with large numbers
                Arguments.of("1000000000 + 5 * (100000 - 500)", 1_000_497_500.0),
                Arguments.of("1000000000 * 5 + (100000 - 500)", 5_000_099_500.0),

                // Expressions that mix negative numbers and large numbers
                Arguments.of("-1000000000 + 5 * (100000 - 500)", -999_502_500.0),
                Arguments.of("-1000000000 * 5 + (100000 - 500)", -4_999_900_500.0),

                // Expressions with only negative results
                Arguments.of("5 + -3", 2.0),
                Arguments.of("(-5 + 3) * (-2 + 3)", -2.0),
                Arguments.of("(-5 + 3) * -2", 4.0),

                // Expressions with floating point results (optional based on support)
                Arguments.of("10 / 3", 3.33),  // If your parser supports floats, you can test for rounding
                Arguments.of("5", 5.0),  // literal numbers
                Arguments.of("1 + -(-(6 * -2))", -11),


                // Testing operator precedence
                Arguments.of("2 + 3 * 4 - 5", 9.0),    // Multiplication before addition and subtraction
                Arguments.of("(2 + 3) * (4 - 5)", -5.0),  // Parentheses precedence
                Arguments.of("2 * 3 + 4 * 5", 26.0),  // Left-to-right evaluation for operators with the same precedence
                Arguments.of("10 / 2 + 3", 8.0),      // Division before addition
                Arguments.of("10 + 2 / 2", 11.0),      // Division before addition

                // Literal numbers
                Arguments.of("42", 42.0),
                Arguments.of("0", 0.0),
                Arguments.of("-1", -1.0),


                // Edge case with deeply nested operations
                Arguments.of("1 + (2 * (3 + (4 * (5 + (-6 * (7 + 8))))))", -673.0),
                Arguments.of("(1 + 2) * (3 + (4 * (5 + (6 - 7))))", 57.0),


                // Parentheses with unary operators and subtraction
                Arguments.of("5 - (-3 + (2 - 4))", 10.0),
                Arguments.of("(-5 + (3 - (2 - 1))) * (2 + 3)", -15.0),
                Arguments.of("(5 + (-3 - (2 + 1))) * (-2)", 2.0),

// Combining multiplication, division, and subtraction
                Arguments.of("5 * (10 - 3) / 7", 5.0),
                Arguments.of("(5 + 10) * (20 / 4) - 15", 60.0),
                Arguments.of("100 / (5 * (2 - 1))", 20.0),

                // Complex nested cases
                Arguments.of("1 + (2 * (3 + (4 * (5 + 6))))", 95.0),
                Arguments.of("((1 + 2) * (3 + 4)) - (5 * (6 + 7))", -44.0),
                Arguments.of("10 + (5 * (2 - (3 + 4)))", -15.0),
                Arguments.of("(5 + (3 * (2 - (1 + 1)))) * (2 + 3)", 25.0),
                Arguments.of("((5 + 3) * (2 - (3 - 1))) + (6 / (1 + 1))", 3.0),

// Unary minus with nesting
                Arguments.of("-((5 + 3) * (2 + 3))", -40.0),
                Arguments.of("-5 + (3 * (-2 + -3))", -20.0),
                Arguments.of("-(5 + (-3 * (2 + -3)))", -8.0),
                Arguments.of("-(-5 + (3 * (-2 + 3)))", 2.0)
        );
    }

    @ParameterizedTest
    @MethodSource("validCalculatorExpressionsWithResults")
    void evaluate_RightSide_valid_cases(String expression, double outcome) {
        assertEquals(outcome, expressionEvaluator.calculateExpression(expression));
    }



    @Test
    void evaluateExpressions_originalExerciseExample() {
        String[] inputs = {
                "i = 0",
                "j = i",
                "x = i + 5",
                "y = (5 + 3) * 10",
                "i += y"
        };

        for (String input : inputs) {
            expressionEvaluator.evaluate(input);
        }
        expressionEvaluator.printVariables();
        String evaluationOutput = outContent.toString();

        assertTrue(evaluationOutput.contains("i=80"));
        assertTrue(evaluationOutput.contains("j=0"));
        assertTrue(evaluationOutput.contains("x=5"));
        assertTrue(evaluationOutput.contains("y=80"));
    }

    @Test
    void evaluateExpressions_originalExerciseExample_differentSpacing() {
        String[] inputs = {
                "i = 0",
                "j = i ",
                "x = i+ 5",
                "y =( 5 + 3) * 10",
                "i += y "
        };

        for (String input : inputs) {
            expressionEvaluator.evaluate(input);
        }
        expressionEvaluator.printVariables();
        String evaluationOutput = outContent.toString();

        assertTrue(evaluationOutput.contains("i=80"));
        assertTrue(evaluationOutput.contains("j=0"));
        assertTrue(evaluationOutput.contains("x=5"));
        assertTrue(evaluationOutput.contains("y=80"));
    }

    @Test
    void evaluateExpressions_originalExerciseExample_noSpacing() {
        String[] inputs = {
                "i=0",
                "j=i ",
                "x=i+5",
                "y=(5+3)*10",
                "i+=y"
        };

        for (String input : inputs) {
            expressionEvaluator.evaluate(input);
        }
        expressionEvaluator.printVariables();
        String evaluationOutput = outContent.toString();

        assertTrue(evaluationOutput.contains("i=80"));
        assertTrue(evaluationOutput.contains("j=0"));
        assertTrue(evaluationOutput.contains("x=5"));
        assertTrue(evaluationOutput.contains("y=80"));
    }

    @Test
    void evaluateExpressions_longFormExample() {
        String[] inputs = {
                "a = 5",
                "b = a + 10",
                "c = b * 2",
                "d = c / 5 + a",
                "e = d - 3 * (a + 2)",
                "f = e / 2 + 15",
                "g = f + 3 * b - 4",
                "h = (g * 2) + (c / 3) - b",
                "i = h + 7 / 3 + (a * b)",
                "j = i",
                "k = j + a",
                "l = k - 2",
                "m = l + 4 * (a + 3)",
                "n = m + (j + 2) * b",
                "o += n",
                "p *= (o / 3) + 4",
                "q = p + a - (b * 2)",
                "r = q / 2 + (c / 3)",
                "s = r + (a + b) * 2",
                "t = s - 3 + f / (d + 1)",
                "u = t + k - (l * 2)",
                "v = (u * 3) / (b + a) + j",
                "w = 100 / v / 5 - (o * 2) + (p / q)",
                "x = (w + 4) * (r - 2) + s / t",
                "y = x / 3 + (v * 2) - p",
                "z = a * b + c - d / (e + 1)"
        };

        for (String input : inputs) {
            expressionEvaluator.evaluate(input);
        }
        expressionEvaluator.printVariables();
        String evaluationOutput = outContent.toString();

        assertEquals("(a=5,b=15,c=30,d=11,e=-10,f=10,g=51,h=97,i=174,j=174,k=179,l=177,m=209,n=2854,o=2854,p=0,q=-25,r=-2,s=38,t=35,u=-140,v=153,w=-5708,x=25671,y=8864,z=106)\r\n", evaluationOutput);
    }

    @Test
    void evaluate_multipleOperationsInParenthesis() {
        String[] inputs = {
                "a = 10",
                "b = a * 2 + 5",
                "c = b - 4 / 2 + a",
                "d = (c + a) * b",
                "e = d / (b - a) + c",
                "l = a * (b - c + 20)"
        };

        for (String input : inputs) {
            expressionEvaluator.evaluate(input);
        }
        expressionEvaluator.printVariables();
        String evaluationOutput = outContent.toString();

        assertEquals("(a=10,b=25,c=33,d=1075,e=105,l=120)\r\n", evaluationOutput);
    }

    @Test
    void evaluateExpressions_variableReassignment() {
        String[] inputs = {
                "a = 5",
                "b = a + 10",
                "a = b + 15", // reassigning 'a' based on updated 'b'
                "c = a * 2",
        };

        for (String input : inputs) {
            expressionEvaluator.evaluate(input);
        }
        expressionEvaluator.printVariables();
        String evaluationOutput = outContent.toString();

        assertTrue(evaluationOutput.contains("a=30"));
        assertTrue(evaluationOutput.contains("b=15"));
        assertTrue(evaluationOutput.contains("c=60"));
    }

    @Test
    void evaluateExpressions_largeNumbers() {
        String[] inputs = {
                "a = 1000",
                "b = a * a",          // large multiplication
                "c = b + a * 2",       // large addition
                "d = c / (a / 10)",    // division with large number
        };

        for (String input : inputs) {
            expressionEvaluator.evaluate(input);
        }
        expressionEvaluator.printVariables();
        String evaluationOutput = outContent.toString();

        assertTrue(evaluationOutput.contains("a=1000"));
        assertTrue(evaluationOutput.contains("b=1000000"));
        assertTrue(evaluationOutput.contains("c=1002000"));
        assertTrue(evaluationOutput.contains("d=10020"));
    }

    @Test
    void evaluateExpressions_onlySingleExpression() {
        String[] inputs = {
                "a = 42"
        };

        for (String input : inputs) {
            expressionEvaluator.evaluate(input);
        }
        expressionEvaluator.printVariables();
        String evaluationOutput = outContent.toString();

        assertTrue(evaluationOutput.contains("a=42"));
    }

    @Test
    void evaluateExpressions_mixedOperations() {
        String[] inputs = {
                "a = 10",
                "b = a * 2 + 5",
                "c = b - 4 / 2 + a",
                "d = (c + a) * b",
                "e = d / (b - a) + c"
        };

        for (String input : inputs) {
            expressionEvaluator.evaluate(input);
        }
        expressionEvaluator.printVariables();
        String evaluationOutput = outContent.toString();

        assertEquals("(a=10,b=25,c=33,d=1075,e=105)\r\n", evaluationOutput);
    }

    // "l = a * (b - c + d)",
    @Test
    void evaluateExpressions_multipleOperationsInParenthesis() {
        String[] inputs = {
                "a = 10",
                "b = a * 2 + 5",
                "c = b - 4 / 2 + a",
                "d = (c + a) * b",
                "e = d / (b - a) + c",
                "l = a * (b - c + 20)"
        };

        for (String input : inputs) {
            expressionEvaluator.evaluate(input);
        }
        expressionEvaluator.printVariables();
        String evaluationOutput = outContent.toString();

        assertEquals("(a=10,b=25,c=33,d=1075,e=105,l=120)\r\n", evaluationOutput);
    }

    @Test
    void evaluateExpressions_newOperators() {

        // reinitialize the registry
        OperatorRegistry registry = new OperatorRegistry();
        registry.registerOperator(new AdditionOperator());
        registry.registerOperator(new SubtractionOperator());
        registry.registerOperator(new MultiplicationOperator());
        registry.registerOperator(new DivisionOperator());
        registry.registerOperator(new PowerOperator());
        expressionEvaluator = new ExpressionEvaluator(registry);
        String[] inputs = {
                "a = 5",
                "b = a ^ 2",
                "a = b + 15", // reassigning 'a' based on updated 'b'
                "c = a * 2",
                "c = c ^ 2", // 6400
                "c = c / 2", // 3200
                "c = c + a", // 3240
                "c = c - b", // 3215
                "c = c * 2", // 6430
                "c = c / 3", // 2143.33 = 2143
                "c = c + a", // 2183
                "c = c - b", // 2158
        };

        for (String input : inputs) {
            expressionEvaluator.evaluate(input);
        }
        expressionEvaluator.printVariables();
        String evaluationOutput = outContent.toString();

        assertTrue(evaluationOutput.contains("a=40"));
        assertTrue(evaluationOutput.contains("b=25"));
        assertTrue(evaluationOutput.contains("c=2158"));
    }
}