package org.example.openclosecalculator;

import org.example.openclosecalculator.operators.*;
import org.example.openclosecalculator.operators.types.AdditionOperator;
import org.example.openclosecalculator.operators.types.DivisionOperator;
import org.example.openclosecalculator.operators.types.MultiplicationOperator;
import org.example.openclosecalculator.operators.types.SubtractionOperator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("LongLiteralEndingWithLowercaseL")
class ExpressionParserTest {
    static ExpressionParser expressionParser;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;


    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @BeforeEach
    void setupParser() {
        // Initialize the registry
        OperatorRegistry registry = new OperatorRegistry();
        registry.registerOperator(new AdditionOperator());
        registry.registerOperator(new SubtractionOperator());
        registry.registerOperator(new MultiplicationOperator());
        registry.registerOperator(new DivisionOperator());

        // Initialize the parser
        expressionParser = new ExpressionParser(registry);
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
            expressionParser.evaluateExpressionLine(input);
        }
        expressionParser.printVariables();
        String evaluationOutput = outContent.toString();

        assertTrue(evaluationOutput.contains("i=80"));
        assertTrue(evaluationOutput.contains("j=0"));
        assertTrue(evaluationOutput.contains("x=5"));
        assertTrue(evaluationOutput.contains("y=80"));
    }

    @Test
    void evaluateExpressions_originalExerciseExample_differentSpacing() {
        String[] inputs = {
                "i =0",
                "j = i ",
                "x = i+ 5",
                "y =( 5 + 3) * 10",
                "i += y "
        };

        for (String input : inputs) {
            expressionParser.evaluateExpressionLine(input);
        }
        expressionParser.printVariables();
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
            expressionParser.evaluateExpressionLine(input);
        }
        expressionParser.printVariables();
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
            expressionParser.evaluateExpressionLine(input);
        }
        expressionParser.printVariables();
        String evaluationOutput = outContent.toString();

        assertEquals("(a=5,b=15,c=30,d=11,e=-10,f=10,g=51,h=97,i=174,j=174,k=179,l=177,m=209,n=2849,o=2849,p=0,q=-25,r=-2,s=38,t=35,u=-140,v=153,w=-5698,x=22777,y=7898,z=106)\r\n", evaluationOutput);
    }

    @Test
    void evaluateExpressionLine_multipleOperationsInParenthesis() {
        String[] inputs = {
                "a = 10",
                "b = a * 2 + 5",
                "c = b - 4 / 2 + a",
                "d = (c + a) * b",
                "e = d / (b - a) + c",
                "l = a * (b - c + 20)"
        };

        for (String input : inputs) {
            expressionParser.evaluateExpressionLine(input);
        }
        expressionParser.printVariables();
        String evaluationOutput = outContent.toString();

        assertEquals("(a=10,b=25,c=33,d=1075,e=104,l=120)\r\n", evaluationOutput);
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
            expressionParser.evaluateExpressionLine(input);
        }
        expressionParser.printVariables();
        String evaluationOutput = outContent.toString();

        assertTrue(evaluationOutput.contains("a=30"));
        assertTrue(evaluationOutput.contains("b=15"));
        assertTrue(evaluationOutput.contains("c=60"));
    }

    @Test
    void evaluateExpressions_largeNumbers() {
        String[] inputs = {
                "a = 1000000",
                "b = a * a",          // large multiplication
                "c = b + a * 2",       // large addition
                "d = c / (a / 10)",    // division with large number
        };

        for (String input : inputs) {
            expressionParser.evaluateExpressionLine(input);
        }
        expressionParser.printVariables();
        String evaluationOutput = outContent.toString();

        assertTrue(evaluationOutput.contains("a=1000000"));
        assertTrue(evaluationOutput.contains("b=1000000000000"));
        assertTrue(evaluationOutput.contains("c=1000002000000"));
        assertTrue(evaluationOutput.contains("d=10000020"));
    }

    @Test
    void evaluateExpressions_onlySingleExpression() {
        String[] inputs = {
                "a = 42"
        };

        for (String input : inputs) {
            expressionParser.evaluateExpressionLine(input);
        }
        expressionParser.printVariables();
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
            expressionParser.evaluateExpressionLine(input);
        }
        expressionParser.printVariables();
        String evaluationOutput = outContent.toString();

        assertEquals("(a=10,b=25,c=33,d=1075,e=104)\r\n", evaluationOutput);
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
            expressionParser.evaluateExpressionLine(input);
        }
        expressionParser.printVariables();
        String evaluationOutput = outContent.toString();

        assertEquals("(a=10,b=25,c=33,d=1075,e=104,l=120)\r\n", evaluationOutput);
    }


    public static Stream<Arguments> validCalculatorExpressionsWithResults() {
        return Stream.of(
                Arguments.of("10 + 5 * 2 - 3", 17l),
                Arguments.of("20 / 5 + 3", 7l),
                Arguments.of("5 * (2 + 3)", 25l),
                Arguments.of("(5 + 3) * 2", 16l),
                Arguments.of("5 + 3 * (2 + 3)", 20l),
                Arguments.of("(5 + 3) * (2 + 3)", 40l),
                Arguments.of("(5 + (3 + 2)) * (2 + 3)", 50l),
                Arguments.of("(-5 + 3) * (2 + 3)", -10l),
                Arguments.of("5 + 3 * (2 + -3)", 2l),
                Arguments.of("(5 + 3) * (2 + -3)", -8l),
                Arguments.of("-5 + 3 * (2 + -3)", -8l),
                Arguments.of("(-5 + 3) * (2 + -3)", 2l),
                Arguments.of("5 + 3 * (-2 + 3)", 8l),
                Arguments.of("(5 + 3) * (-2 + 3)", 8l),
                Arguments.of("-5 + 3 * (-2 + 3)", -2l),
                Arguments.of("(-5 + 3) * (-2 + 3)", -2l),
                Arguments.of("-5 + 3 * (-2 + -3)", -20l),
                Arguments.of("(-5 + 3) * (-2 + -3)", 10l),
                Arguments.of("5 + 3 * (-2 + -3)", -10l),
                Arguments.of("-5 + 3 * (2 + 3)", 10l),

                // Edge case: complex expressions with multiple parentheses
                Arguments.of("5 + (3 * (2 + 3))", 20l),
                Arguments.of("((5 + 3) * (2 + 3))", 40l),
                Arguments.of("5 + ((3 + 2) * (2 + 3))", 30l),
                Arguments.of("5 * (3 + (2 + 3))", 40l),
                Arguments.of("((5 + 3) + (2 + 3))", 13l),

                Arguments.of("0 / 5", 0l),


                // Large numbers
                Arguments.of("1000000000 * 1000000000", 1000000000000000000l),
                Arguments.of("1000000000 / 100000", 10000l),

                // Mixed operations with large numbers
                Arguments.of("1000000000 + 5 * (100000 - 500)", 1_000_497_500l),
                Arguments.of("1000000000 * 5 + (100000 - 500)", 5_000_099_500l),

                // Expressions that mix negative numbers and large numbers
                Arguments.of("-1000000000 + 5 * (100000 - 500)", -999_502_500l),
                Arguments.of("-1000000000 * 5 + (100000 - 500)", -4_999_900_500l),

                // Expressions with only negative results
                Arguments.of("5 + -3", 2l),
                Arguments.of("(-5 + 3) * (-2 + 3)", -2l),
                Arguments.of("(-5 + 3) * -2", 4l),

                // Expressions with floating point results (optional based on support)
                Arguments.of("10 / 3", 3l),  // If your parser supports floats, you can test for rounding
                Arguments.of("5", 5l)  // literal numbers
        );
    }

    @ParameterizedTest
    @MethodSource("validCalculatorExpressionsWithResults")
    void evaluate_RightSide_valid_cases(String expression, long outcome) {
        assertEquals(outcome, expressionParser.evaluateRightSide(expression));
    }

    @Test
    void evaluate_RightSide_divideByZero() {
        assertThrows(IllegalArgumentException.class, () -> expressionParser.evaluateRightSide("1 / 0"));
    }


    @ParameterizedTest
    @ValueSource(strings = {
            "1 + 2 *+ 3",
            "1 + 2 * ((3",
            "1 + 2 * (3",

    })
    void evaluate_RightSide_malformedExpression(String expression) {
        assertThrows(IllegalArgumentException.class, () -> expressionParser.evaluateRightSide(expression));
    }

}