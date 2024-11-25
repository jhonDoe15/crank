package org.example.calculator.validation;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.String.format;

public class ExpressionValidator {
    static final Logger log = Logger.getLogger(ExpressionValidator.class.getName());
    final static String validationRegex;

    static {
        String POSSIBLE_OPERATORS_REGEX = "(\\+|\\-|\\*|\\/)";
        String START_POSITION_VARIABLE_AND_ASSIGNMENT_SIGNS_REGEX = "\\w\\s*" + POSSIBLE_OPERATORS_REGEX + "{0,1}=";
        String VARIABLE_OR_LITERAL = "\\s*(\\w|\\d)+)\\s*";
        validationRegex = "^" + START_POSITION_VARIABLE_AND_ASSIGNMENT_SIGNS_REGEX + "(((\\s*((\\w|\\d)+|\\((\\w|\\d)+\\s*" + POSSIBLE_OPERATORS_REGEX + "\\s*(\\w|\\d)+\\))\\s*(\\+|\\-|\\*|\\/){1})+(\\s*((\\w|\\d)+|\\((\\w|\\d)+\\s*(\\+|\\-|\\*|\\/)\\s*(\\w|\\d)+\\))))|" + VARIABLE_OR_LITERAL + "$";
    }

    static final Pattern pattern = Pattern.compile(validationRegex);

    public boolean valid(String[] expressions) {

        // prime matcher for faster operation with reset later
        Matcher matcher = pattern.matcher(expressions[0]);

        //noinspection ForLoopReplaceableByForEach
        for (int i = 0; i < expressions.length; i++) {
            if (!matcher.reset(expressions[i]).matches()) {
                log.severe(format("Error with expression %s", expressions[i]));
                return false;
            }
        }

        return true;
    }
}

