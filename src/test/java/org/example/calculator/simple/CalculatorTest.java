package org.example.calculator.simple;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    final Calculator calculator = new Calculator();
    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
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

        calculator.evaluateExpressions(inputs);
        calculator.printVariables();
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

        calculator.evaluateExpressions(inputs);
        calculator.printVariables();
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

        calculator.evaluateExpressions(inputs);
        calculator.printVariables();
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

        calculator.evaluateExpressions(inputs);
        calculator.printVariables();
        String evaluationOutput = outContent.toString();

        assertEquals("(a=5,b=15,c=30,d=11,e=-10,f=10,g=51,h=97,i=174,j=174,k=179,l=177,m=209,n=2849,o=2849,p=0,q=-25,r=-2,s=38,t=35,u=-140,v=153,w=-5698,x=22777,y=7898,z=106)\r\n", evaluationOutput);
    }

    @Test
    void evaluateExpressions_divisionByZero() {
        String[] inputs = {
                "i = 0",
                "j = i",
                "x = 5 / i",
                "y = (5 + 3) * 10",
                "i += y"
        };

        assertThrows(ArithmeticException.class,
                () -> calculator.evaluateExpressions(inputs),
                "Division by zero in compound assignment."
        );
    }

    @Test
    void evaluateExpressions_divisionByZero_withCompoundOperator() {
        String[] inputs = {
                "i = 0",
                "j = i",
                "x = 5 / (i / 2)",
                "y = (5 + 3) * 10",
                "i += y"
        };

        assertThrows(ArithmeticException.class,
                () -> calculator.evaluateExpressions(inputs),
                "Division by zero in compound assignment."
        );
    }


    @Test
    void evaluateExpressions_variableReassignment() {
        String[] inputs = {
                "a = 5",
                "b = a + 10",
                "a = b + 15", // reassigning 'a' based on updated 'b'
                "c = a * 2",
        };

        calculator.evaluateExpressions(inputs);
        calculator.printVariables();
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

        calculator.evaluateExpressions(inputs);
        calculator.printVariables();
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

        calculator.evaluateExpressions(inputs);
        calculator.printVariables();
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

        calculator.evaluateExpressions(inputs);
        calculator.printVariables();
        String evaluationOutput = outContent.toString();

        assertEquals("(a=10,b=25,c=33,d=1075,e=104)\r\n", evaluationOutput);
    }

    // "l = a * (b - c + d)",
    @Test
    void evaluateExpressions_multipleOperationsInParenthesis () {
        String[] inputs = {
                "a = 10",
                "b = a * 2 + 5",
                "c = b - 4 / 2 + a",
                "d = (c + a) * b",
                "e = d / (b - a) + c",
                "l = a * (b - c + 20)"
        };

        calculator.evaluateExpressions(inputs);
        calculator.printVariables();
        String evaluationOutput = outContent.toString();

        assertEquals("(a=10,b=25,c=33,d=1075,e=104,l=120)\r\n", evaluationOutput);
    }

    @Test
    void evaluateExpressions_invalidSyntax() {
        String[] inputs = {
                "a = 5",
                "b = a + + 10", // invalid syntax
                "c = b * 2"
        };

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> calculator.evaluateExpressions(inputs),
                "Expected invalid syntax exception."
        );

        assertEquals("Expression cannot be empty. expression wasn't phrased correctly", exception.getMessage());
    }

    @Test
    void evaluateExpressions_noOperators() {
        String[] inputs = {
                "a = 5",
                "b = a",
                "c = b"  // no operators, just variable references
        };

        calculator.evaluateExpressions(inputs);
        calculator.printVariables();
        String evaluationOutput = outContent.toString();

        assertTrue(evaluationOutput.contains("a=5"));
        assertTrue(evaluationOutput.contains("b=5"));
        assertTrue(evaluationOutput.contains("c=5"));
    }
}