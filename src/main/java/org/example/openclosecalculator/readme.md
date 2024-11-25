# Extended Parser that supports more complex and nested parenthesis structures
this version is a major redesign of the original parser, focusing mainly on easy extensibility and supporting more operators by adding the operator registry a new object that implements the interface

we have problems with negative numbers and operations for example `-(1 + 2)` should result in `-3` and in order to overcome simple cases like `-2` we change the expression to the following structure '(0 - $1)' thoguh obviously with parenthesis this wouldn't work. the two current options are limiting the syntax to make it easier to parse or improving the current design to natively support negative expressions.
