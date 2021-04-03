package ppanda.math.kalkulator

enum class Associativity {
    LEFT_TO_RIGHT,
    RIGHT_TO_LEFT
}

sealed class Operator(internal val tokenType: TokenType, val associativity: Associativity) {
}


open class UnaryOperator(
    tokenType: TokenType, associativity: Associativity = Associativity.RIGHT_TO_LEFT
) : Operator(tokenType, associativity) {
    override fun toString(): String = "Unary$tokenType"
}


open class BinaryOperator(tokenType: TokenType, associativity: Associativity) : Operator(tokenType, associativity) {
    override fun toString(): String = "Binary$tokenType"
}


interface Operators {
    fun all(): Collection<Operator>

    fun hasOperatorFor(token: Token): Boolean = all().any { it.tokenType == token.type }

    fun getBinaryOp(token: Token): BinaryOperator? = all()
        .filterIsInstance<BinaryOperator>()
        .firstOrNull { it.tokenType == token.type }

    fun getUnaryOp(token: Token): UnaryOperator? = all()
        .filterIsInstance<UnaryOperator>()
        .firstOrNull { it.tokenType == token.type }

    fun tokenTypes() = all().map { it.tokenType }
}

typealias OperatorPrecedence = Comparator<Operator>

