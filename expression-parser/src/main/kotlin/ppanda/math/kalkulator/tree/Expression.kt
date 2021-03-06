package ppanda.math.kalkulator.tree

import ppanda.math.kalkulator.BinaryOperator
import ppanda.math.kalkulator.Token
import ppanda.math.kalkulator.UnaryOperator
import ppanda.math.kalkulator.tree.visitor.AstVisitor
import ppanda.math.kalkulator.utils.dataclass.WithEqualsAndHashCode


open class Node(val token: Token?) : WithEqualsAndHashCode {
    val location = token?.location

    override fun propsForEquality(): Array<Any?> = emptyArray()
    override fun hashCode(): Int = standardHashcode()
    override fun equals(other: Any?): Boolean = standardEquality(other)

    open fun <ResultT, ContextT> accept(visitor: AstVisitor<ResultT, ContextT>, context: ContextT): ResultT =
        visitor.visitNode(this, context)
}

open class Expression(token: Token?) : Node(token) {

    override fun <ResultT, ContextT> accept(visitor: AstVisitor<ResultT, ContextT>, context: ContextT): ResultT =
        visitor.visitExpression(this, context)
}

class ParenthesizedExpression(val child: Expression, beginningToken: Token? = null) : Expression(beginningToken) {
    override fun toString(): String = " ( $child ) "

    override fun propsForEquality(): Array<Any?> = arrayOf(child)

    override fun <ResultT, ContextT> accept(visitor: AstVisitor<ResultT, ContextT>, context: ContextT): ResultT =
        visitor.visitParenthesizedExpression(this, context)
}


class Literal(token: Token?, val value: Double = token?.value?.toDouble()!!) : Expression(token) {
    constructor(value: Double) : this(null, value)

    override fun toString(): String = "Literal($value)"

    override fun propsForEquality(): Array<Any?> = arrayOf(value)

    override fun <ResultT, ContextT> accept(visitor: AstVisitor<ResultT, ContextT>, context: ContextT): ResultT =
        visitor.visitLiteral(this, context)
}

open class UnaryExpression(
    val operator: UnaryOperator, val operand: Expression, token: Token? = null
) : Expression(token) {
    override fun toString(): String = "$operator($operand)"

    override fun propsForEquality(): Array<Any?> = arrayOf(operator, operand)

    override fun <ResultT, ContextT> accept(visitor: AstVisitor<ResultT, ContextT>, context: ContextT): ResultT =
        visitor.visitUnaryExpression(this, context)
}

open class BinaryExpression(
    val operator: BinaryOperator, val left: Expression, val right: Expression, token: Token? = null
) : Expression(token) {
    override fun toString(): String = "$operator($left, $right)"

    override fun propsForEquality(): Array<Any?> = arrayOf(operator, left, right)

    override fun <ResultT, ContextT> accept(visitor: AstVisitor<ResultT, ContextT>, context: ContextT): ResultT =
        visitor.visitBinaryExpression(this, context)
}


