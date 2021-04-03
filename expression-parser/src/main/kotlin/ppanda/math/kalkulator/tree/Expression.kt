package ppanda.math.kalkulator.tree

import ppanda.math.kalkulator.BinaryOperator
import ppanda.math.kalkulator.Token
import ppanda.math.kalkulator.UnaryOperator
import java.util.*


open class Node(val token: Token?) {
    val location = token?.location
}

open class Expression(token: Token?) : Node(token) {
}

class ParenthesizedExpression(token: Token?) : Expression(token) {
}

class Literal(token: Token?, val value: Double = token?.value?.toDouble()!!) : Expression(token) {
    constructor(value: Double) : this(null, value)

    override fun toString(): String = "Literal($value)"


    override fun hashCode(): Int = Objects.hash(value)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Literal

        if (value != other.value) return false

        return true
    }
}

open class UnaryExpression(
    val operator: UnaryOperator, val operand: Expression, token: Token? = null
) : Expression(token) {
    override fun toString(): String = "$operator($operand)"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is UnaryExpression) return false

        if (operator != other.operator) return false
        if (operand != other.operand) return false

        return true
    }

    override fun hashCode(): Int = Objects.hash(operator, operand, token)
}

open class BinaryExpression(
    val operator: BinaryOperator, val left: Expression, val right: Expression, token: Token? = null
) : Expression(token) {
    override fun toString(): String = "$operator($left, $right)"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is BinaryExpression) return false

        if (operator != other.operator) return false
        if (left != other.left) return false
        if (right != other.right) return false

        return true
    }

    override fun hashCode(): Int = Objects.hash(operator, left, right)
}


