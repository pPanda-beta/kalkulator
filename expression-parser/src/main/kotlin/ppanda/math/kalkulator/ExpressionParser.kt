package ppanda.math.kalkulator

import ppanda.math.kalkulator.BasicTokenTypes.DECIMAL_LITERAL
import ppanda.math.kalkulator.exceptions.ExtraTokenException
import ppanda.math.kalkulator.exceptions.NoMoreTokenException
import ppanda.math.kalkulator.exceptions.UnknownTokenException
import ppanda.math.kalkulator.standard.StandardOperators
import ppanda.math.kalkulator.tree.BinaryExpression
import ppanda.math.kalkulator.tree.Expression
import ppanda.math.kalkulator.tree.Literal
import ppanda.math.kalkulator.tree.UnaryExpression
import java.util.*


class ExpressionParser(
    val operators: Operators = StandardOperators,
    val operatorPrecedence: OperatorPrecedence = StandardOperators.precedence(),
    val lexer: Lexer = Lexer(),
    val tokenizer: Tokenizer = Tokenizer(operators.tokenTypes())
) {

    fun parse(s: String): Expression {
        val lexemes: List<Lex> = lexer.extract(s)
        val tokens = tokenizer.mapToTokens(lexemes)
        return ParsingContext(this, tokens.listIterator()).expr()
    }
}


/*
Hand cranked parsers are never good.
TODO: switch to a parser generator like antlr4

 */
class ParsingContext(
    val parser: ExpressionParser,
    val iterator: ListIterator<Token>
) {
    private val operatorPrecedenceRearranger = OperatorPrecedenceRearranger(parser.operatorPrecedence)


    fun hasNext() = iterator.hasNext()
    fun next() = iterator.next()
    fun peekNext() = iterator.next().also { iterator.previous() }

    fun unaryOrLitOrParen(): Expression {
        if (!hasNext()) {
            throw NoMoreTokenException(expectation = "unary operator or literal")
        }
        val token = next()

        val unaryOp = parser.operators.getUnaryOp(token)
        if (unaryOp != null) {
            return UnaryExpression(unaryOp, expr(), token)
        }

        if (token.type == DECIMAL_LITERAL) {
            return Literal(token)
        }

        throw UnknownTokenException(token, expectation = "unary operator or literal")
    }

    fun expr(): Expression {
        val firstOperand = unaryOrLitOrParen()
        val nodes = mutableListOf<Any>(firstOperand)
        while (hasNext()) {
            val token = next()
            val binaryOp = parser.operators.getBinaryOp(token)
                ?: throw UnknownTokenException(token, expectation = "binary operator")
            val nextOperand = unaryOrLitOrParen()

            nodes.add(Pair(binaryOp, token))
            nodes.add(nextOperand)
        }

        return operatorPrecedenceRearranger.buildBinaryExpressions(nodes)
    }

}

/*
This is a modified version of
Check more at https://en.wikipedia.org/wiki/Shunting-yard_algorithm#A_simple_conversion
 */
class OperatorPrecedenceRearranger(private val operatorPrecedence: OperatorPrecedence) {

    fun buildBinaryExpressions(nodes: MutableList<Any>): Expression {
        val operatorStack = Stack<Pair<BinaryOperator, Token>>()
        val output = Stack<Expression>()

        val thereExistSomeOperator = { !operatorStack.empty() }

        val hasEqualOrHighPrecedence: (BinaryOperator) -> Boolean =
            { op -> operatorPrecedence.compare(operatorStack.peek().first, op) >= 0 }

        nodes.forEach { node ->
            when (node) {
                is Expression -> output.push(node)
                is Pair<*, *> -> {
                    val (binaryOp, _) = node as Pair<BinaryOperator, Token>
                    while (thereExistSomeOperator() && hasEqualOrHighPrecedence(binaryOp)) {
                        buildNextExpression(operatorStack, output)
                    }
                    operatorStack.push(node)
                }
            }
        }

        while (thereExistSomeOperator()) {
            buildNextExpression(operatorStack, output)
        }

        if (output.size > 1) {
            throw ExtraTokenException(output)
        }

        return output.pop()
    }

    private fun buildNextExpression(
        operatorStack: Stack<Pair<BinaryOperator, Token>>,
        output: Stack<Expression>
    ) {
        val (op, pastToken) = operatorStack.pop()
        val rightOperand = output.pop()
        val leftOperand = output.pop()
        output.push(BinaryExpression(op, leftOperand, rightOperand, pastToken))
    }


}

