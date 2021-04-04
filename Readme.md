## A kotlin based extensible calculator

### Mentions

* The execution mechanism is highly influenced by my work [presto-utils](https://github.com/pPanda-beta/presto-utils)
* The visitor pattern is inspired by [trino-parser](https://github1s.com/trinodb/trino/blob/HEAD/core/trino-parser/src/main/java/io/trino/sql/tree/AstVisitor.java)  
* Some part of it uses [Shunting-yard algorithm](https://en.wikipedia.org/wiki/Shunting-yard_algorithm#A_simple_conversion)

### For users
#### How to run ?

Directly :
```shell script
./gradlew ':kalkulator-cli:run' --args="  -e '5*4 + 3/2' "
```

OR

Build and then run
```shell script
./gradlew clean shadowJar 
java -jar ./kalkulator-cli/build/libs/kalkulator-cli-*-all.jar -e '5*4 + 3/2'
```

### For developers

#### How to implement a new operator?

1. Create a token pattern e.g. `val MODULO_TOK = PatternBasedTokenType.fromString("MODULO", "%")`
2. Create an operator  
    ```kotlin
    val MODULO = BinaryOperator(MODULO_TOK, Associativity.LEFT_TO_RIGHT)
    ```
3. Create an operator module to add precedence and evaluation code  
    ```kotlin
    newModule(MODULO, MULT_DIV_PRECEDENCE, Double::mod)
    ```
    Check more at [StandardOperatorModules](kalkulator-core/src/main/kotlin/ppanda/math/kalkulator/standard/StandardOperatorModules.kt)


#### How to implement a ternary operator?
 
 Well since we are not using any parser generator like `antlr4` or `lark` 
 it is hard to introduce a new grammar rule. The following components need to be modified
 * Parser (maybe lexer as well)
 * AstVisitor
 * ExpressionEvaluator (as a sub class of AstVisitor)


#### TODO
1. Try to make parenthesis as a first class unary operator
2. Re-implement expression-parser using a parser generator like `antlr4` or `lark`

