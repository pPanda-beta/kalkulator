package ppanda.math.kalkulator


data class Result(val value: String, val range: IntRange, val source: String? = null)

interface TokenType {
    fun consume(s: String, startingPos: Int = 0): Result?
}

data class PatternBasedTokenType(val name: String, val pattern: Regex) : TokenType {
    override fun consume(s: String, startingPos: Int) = pattern
        .find(s, startingPos)
        ?.let { Result(it.value, it.range, s) }

    override fun toString() = name

    companion object {
        fun fromString(name: String, pattern: String) = PatternBasedTokenType(name, pattern.toRegex())
    }
}


object BasicTokenTypes {
    val OPEN_PARENTHESIS = PatternBasedTokenType.fromString("OPEN_PARENTHESIS", "\\(")
    val CLOSE_PARENTHESIS = PatternBasedTokenType.fromString("CLOSE_PARENTHESIS", "\\)")
    val DECIMAL_LITERAL = PatternBasedTokenType.fromString("DECIMAL_LITERAL", "(\\d+)|(\\d*\\.\\d+)")

    val all = setOf(OPEN_PARENTHESIS, CLOSE_PARENTHESIS, DECIMAL_LITERAL)
}

//TODO: equality and hashcode checking should ignore location may be :P
// This is just another representation of Lexeme, may be omitted in future
data class Token(val type: TokenType, val value: String, val location: SourceLocation?) {
    override fun toString(): String = """$type($value at $location)"""
}

class Tokenizer(
    tokenTypes: Collection<TokenType>,
    externalLexer: Lexer? = null
) {
    val allTokenTypes = BasicTokenTypes.all + tokenTypes
    val lexer = externalLexer ?: Lexer(this)

    fun extractTokens(s: String): List<Token> = lexer.extract(s)
        .map { Token(type = it.tokenType, value = it.value, location = it.location) }
}

