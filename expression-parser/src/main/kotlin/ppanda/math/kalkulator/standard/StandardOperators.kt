package ppanda.math.kalkulator.standard

import ppanda.math.kalkulator.*
import ppanda.math.kalkulator.standard.Precedence.ADD_SUB_PRECEDENCE
import ppanda.math.kalkulator.standard.Precedence.MULT_DIV_PRECEDENCE
import ppanda.math.kalkulator.standard.Precedence.UNARY_PRECEDENCE


object StandardTokenTypes {
    val PLUS = PatternBasedTokenType.fromString("PLUS", "\\+")
    val MINUS = PatternBasedTokenType.fromString("MINUS", "-")
    val MULT = PatternBasedTokenType.fromString("MULT", "\\*")
    val DIVIDE = PatternBasedTokenType.fromString("DIVIDE", "/")
}

internal object Precedence {
    const val UNARY_PRECEDENCE = 1
    const val ORDER_PRECEDENCE = 2
    const val MULT_DIV_PRECEDENCE = 3
    const val ADD_SUB_PRECEDENCE = 5
}


object StandardOperators : Operators {
    val UNARY_PLUS = UnaryOperator(StandardTokenTypes.PLUS)
    val UNARY_MINUS = UnaryOperator(StandardTokenTypes.MINUS)
    val ADDITION = BinaryOperator(StandardTokenTypes.PLUS, Associativity.LEFT_TO_RIGHT)
    val SUBTRACTION = BinaryOperator(StandardTokenTypes.MINUS, Associativity.LEFT_TO_RIGHT)

    val MULTIPLICATION = BinaryOperator(StandardTokenTypes.MULT, Associativity.LEFT_TO_RIGHT)
    val DIVISION = BinaryOperator(StandardTokenTypes.DIVIDE, Associativity.LEFT_TO_RIGHT)


    private val BODMAS_PRECEDENCE = mapOf(
        UNARY_PLUS to UNARY_PRECEDENCE,
        UNARY_MINUS to UNARY_PRECEDENCE,
        DIVISION to MULT_DIV_PRECEDENCE,
        MULTIPLICATION to MULT_DIV_PRECEDENCE,
        ADDITION to ADD_SUB_PRECEDENCE,
        SUBTRACTION to ADD_SUB_PRECEDENCE
    )


    override fun all(): Collection<Operator> = BODMAS_PRECEDENCE.keys

    fun precedence(): OperatorPrecedence {
        return compareBy(BODMAS_PRECEDENCE::get).reversed()
    }
}


