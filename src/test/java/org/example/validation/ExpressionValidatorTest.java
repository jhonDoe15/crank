package org.example.validation;

import org.example.calculator.validation.ExpressionValidator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class ExpressionValidatorTest {

    final ExpressionValidator validator = new ExpressionValidator();

    @ParameterizedTest
    @ValueSource(strings = {
            "a %= 5",              // Modulo assignment operator should not be allowed
            "b = a % 10",          // Modulo operation is invalid in this context
            "c = b ** 2",          // Exponentiation operator is not supported
            "d = c // 5 + a",      // Floor division is not supported
            "a = (5)",             // Parentheses enclosing a single literal
            "a = (a=1)",           // Nested assignment within parentheses
            "a = 5 += 8",          // Invalid compound assignment structure
            "a == 5",              // Invalid equality comparison
            "b = (a + 10",         // Missing closing parenthesis
            "c = a +)",            // Extra closing parenthesis
            "a = (b + (c * d)",    // Missing closing parenthesis for nested expression
            "a = (b + c))",        // Extra closing parenthesis after a valid expression
            "a = 5 + (b * c",      // Missing closing parenthesis
            "a = b + (c * d))",    // Extra closing parenthesis at the end
            "a = 5 5",             // Two literals without operator
            "a = +5",              // Unary plus operator without a variable or context
            "a = -5",              // Unary minus operator on its own
            "b = a + ",            // Trailing operator without operand
            "c = (a + b",          // Missing closing parenthesis
            "d = )a + b(",         // Incorrect ordering of parentheses
            "e = (a + * b)",       // Invalid operator placement
            "f = a + + b",         // Consecutive operators without operand
            "g = a + / b",         // Invalid operator sequence
            "h = *a + b",          // Leading operator without left operand
            "i = a+",              // Operator at the end of expression
            "j = (a + (b))",       // Extra nested parentheses without a need
            "k = (5 + a) * (b + c", // Missing closing parenthesis for multiple subexpressions
            "l = m = 5",           // Multiple assignments in one expression
            "n = 5 + a b",         // Missing operator between variables or literals
            "o = + + a",           // Leading consecutive operators
            "p = - + b",           // Invalid combination of operators
            "q = 5 (a + b)",       // Literal directly followed by parentheses without operator
            "r = (a + (5 * 10)",   // Imbalanced parentheses with complex expression
            "s = a 5 + b",         // Missing operator between variables
            "t = b * (a + ) c",    // Invalid use of parentheses and missing operator
            "u = a * (b + 5))",    // Extra closing parenthesis at the end
            "v = (a + b) (c + d)", // Missing operator between two parenthesized expressions
            "w = (a b) + c",       // Missing operator inside parentheses
            "x = a **",            // Invalid trailing operator
            "y = (a + b) +",       // Trailing operator after a valid subexpression
            "z = a /",             // Division operator without right operand
            "a = / b",             // Division operator without left operand
            "b = 5 + * a",          // Invalid operator sequence
            "a == b",                  // Equality comparison is not allowed
            "a = b =",                 // Invalid compound assignment without right operand
            "a + b =",                 // Missing left-side variable for assignment
            "= b + c",                 // Missing left-side variable
            "a = (b + c) +",           // Trailing operator after parentheses
            "b = (c + d)) + e",        // Extra closing parenthesis in the middle
            "c = a += b",              // Assignment to expression instead of variable
            "d = a + (b * c + )",      // Extra closing parenthesis after valid expression
            "e = (a + b) (c - d)",     // Missing operator between two grouped expressions
            "f = a + 5 5",             // Two literals without an operator between them
            "g = a b + 5",             // Two variables without an operator between them
            "h = (a + b))",            // Extra closing parenthesis at the end
            "i = ((a + b)",            // Missing closing parenthesis for the first expression
            "j = a + (b + (c * d)",    // Deeply nested but incomplete parentheses
            "k = a (b + c)",           // Missing operator before opening parenthesis
            "l = (a b) + c",           // Missing operator inside parentheses
            "m = a + ()",              // Empty parentheses
            "n = a + (b +)",           // Operator without right operand in parentheses
            "o = (a + 5))",            // Extra closing parenthesis at the end
            "p = () + a",              // Empty parentheses as left operand
            "q = a + (*b)",            // Operator before variable in parentheses
            "r = (a + ) b",            // Operator with missing right operand
            "s = (a) b",               // Missing operator between grouped variable and other variable
            "t = a ++",                // Unsupported post-increment
            "u = a --",                // Unsupported post-decrement
            "v = a @ b",               // Unsupported symbol for operation
            "w = a * + b",             // Consecutive operators in middle of expression
            "x = (a + 5) 10",          // Missing operator between parentheses and number
            "y = 10 a + b",            // Literal followed by variable without operator
            "z = a && b",              // Logical AND not supported
            "a = a || b",              // Logical OR not supported
            "b = !a",                  // Logical NOT not supported
            "c = a >> 2",              // Right shift is unsupported
            "d = a << 1",              // Left shift is unsupported
            "e = a >>> 1",             // Unsigned right shift is unsupported
            "f = (a",                  // Unclosed parenthesis with only left side operand
            "g = a + 2 *",             // Operator at the end of expression
            "h = * a + 2",             // Leading operator without left operand
            "i = (a / b",              // Missing closing parenthesis for division
            "j = (5 * 4) + )",         // Closing parenthesis without corresponding opening
            "k = ((a + b) * c))",      // Extra closing parenthesis for nested expression
            "l = 5 + (a - b))",        // Extra closing parenthesis at end of valid expression
            "m = a + 5 * / b",         // Consecutive operators in the middle of expression
            "n = a = b = c",           // Multiple assignments are not supported
            "o = (5 + (3 * 2)) /",     // Trailing operator after nested valid expression
            "p = + 10",                // Unary plus without variable
            "q = a * (5 + b)) + c",    // Extra closing parenthesis after valid group
            "r = (a + (b + (c)",       // Nested parentheses without closure
            "s = (a + b) + (c",        // Unclosed parenthesis with no operator following
            "t = a * (b + c * )",      // Incomplete nested expression with operator at the end
            "u = 5 + (4 * a + 3",      // Missing closing parenthesis for entire expression
            "v = (5 + (4 * 3)) a",     // Missing operator between expressions
            "w = (5 + (4) *",          // Incomplete expression with unmatched parenthesis
            "x = a = (b + c) * 2",     // Assignment to expression instead of variable
            "y = ((a) + b",            // Extra parenthesis around variable without closure
            "z = 2 * (a + (b",         // Deeply nested but incomplete parentheses
            "a = (5) * + b",           // Unary plus inside valid expression
            "b = a *= 5",              // Invalid compound assignment to expression
            "c = 5 a + (b)",           // Missing operator between literal and variable
            "d = a + (5",              // Missing closing parenthesis with literal
            "e = (5 + * 4)",           // Invalid operator sequence in parentheses
            "f = + (a + b)",           // Leading unary plus before parentheses
            "g = (a + b) = 10",        // Assignment to expression instead of variable
            "h = 5 + ((a) * b",        // Extra nested parentheses with missing closure
            "i = a += b -= 2",         // Multiple compound assignments
            "j = a++ + b",             // Unsupported post-increment usage
            "k = ++a + b",             // Unsupported prefix increment usage
            "l = a + / 2",             // Consecutive operators
            "m = (a + b))",            // Extra closing parenthesis after valid group
            "n = ((5 + 4) * a))"       // Extra closing parenthesis for entire expression
    })
    void invalid_manyDifferentOperationsAndSituations(String inputExpression) {
        assertFalse(validator.valid(new String[]{inputExpression}));
    }

    @ParameterizedTest
    @ValueSource(strings = {
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
            "z = a * b + c - d / (e + 1)",
            "a = b - 3",
            "a = b * c",
            "r = a / b + 5",       // Division by zero
            "x = 7 * y - 4",
            "z = (a + b) * c",
            "t = a * 5 + c",
            "x = 42 - b / 2",
            "a = a + b - c + d",
            "k = 10 + (b + c) / a",
            "m = 20 - 3 * (c / a) + d",
            "p = (10 + 20) - (a / 5)",
            "b = (x + y) * 2",
            "c = a + b - 5",
            "c = a * 3 - b",
            "e = c / (d + e)",
            "f = (sum + mult) / 2",
            "g = 5 + 3",
            "h = (a + b) - c",
            "i = (a + b) + (d / 3)",
            "j = a + b * c / d - e + f",
            "k = (a + b) - d + e",
            "l = (a + b) + (e * f) - g",
            "m = (a + 1) - c * d + 5",
            "n = (a * 4) + 5",
            "o = (5 * 2) - (a - 1) + b",
            "p = 15 + (10 - 5) * 2",
            "q = 30 - 5 + 7",
            "r = (10 + 15) * 3",
            "s = (x * y) - z",
            "t = (x + y) / z",
            "u = a + (b / 5)",
            "v = (c + 10) * 3",
            "w = 50 - (a + 5) * 2",
            "z = (a * b) + c",
            "y = (a + 10) / 2",
            "z = (10 - 5) * 3 + 1",
            "a = a - (b * 2) + c",
            "b = (a + b) * 4 - d",
            "c = (10 / 2) + 3",
            "d = (a + b) - 10 * 2",
            "e = (a * b) + (c - d)",
            "f = (a + b) * (c - d)",
            "g = (a - b) / 2 + c",
            "h = a + b + c + d - e - f + g + h",
            "i = a + b / (c * d) - e",
            "k = (a + b) + e",
            "l = a * (b - d)",
            "p = a / (b + c) - d"
    })
    void valid_manyDifferentOperationsAndSituations(String inputExpression) {
        assertTrue(validator.valid(new String[]{inputExpression}));
    }
}