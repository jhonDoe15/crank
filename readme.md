# crank - multi expression calculator
Our calculator supports multiple expressions one after the other with variable assignment in each expression, to either another expression or a literal value.
default implementation used runs in serial mode as it's the fastest for most operations but for more complex expressions per line a dependency based multithreaded implementation is available.
We recommend checking before commiting the to multithreaded one since for non-complex cases it can be 2-8x slower than the serial one.

input example:
```
i = 0
j = i
x = i + 5
y = (5 + 3) * 10
i += y
```
output example:
```
(i=80,j=0,x=5,y=80)
```

# Off the bat improvements
- from profiling the code, was key time-consuming part (14-18%) is the split operation on the assignment operator. From previous experience I'm confident that manual splitting of the expression is not a good idea. Handcrafted implementation could optimize that section by upto 70% in some cases. Other sections would require switching from the regex system to something custom with less background validations
- currently in parentheses we support binary operations, but not serial ones (i.e. a + b + c). The current parser is already capable of handling this, but first we'd like to improve regex clarity, either by variable extraction to make building the regex easier\clearer or with a bigger change to the validation system


# Future roadmap
- Support for more operators (unary operators on variables in expression side like '++' and '--')
- Native support for negative numbers , currently requires minus operations to work but manageable (i.e. instead of '-10' you can use '0 - 10')
- Validator support for multiple operations in parentheses (calculators already supports this)
- Better support for multi-threading and concurrency
- Support for web development and APIs access