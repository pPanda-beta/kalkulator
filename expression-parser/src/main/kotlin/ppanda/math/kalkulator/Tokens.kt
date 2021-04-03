package ppanda.math.kalkulator

import ppanda.math.kalkulator.exceptions.UnrecognizedLexException


interface TokenType {
    fun matches(lex: Lex): Boolean
}

data class PatternBasedTokenType(val name: String, val pattern: Regex) : TokenType {
    override fun matches(lex: Lex) = pattern.matches(lex.value)
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
data class Token(val type: TokenType, val value: String, var location: SourceLocation) {
    override fun toString(): String = """$type($value at $location)"""
}

class Tokenizer(tokenTypes: Collection<TokenType>) {
    private val allTokenTypes = BasicTokenTypes.all + tokenTypes

    fun mapToTokens(lexemes: List<Lex>) = lexemes.map {
        Token(
            type = determineTokenType(it),
            value = it.value,
            location = it.location
        )
    }

    private fun determineTokenType(lex: Lex) = allTokenTypes.firstOrNull { type -> type.matches(lex) }
        ?: throw UnrecognizedLexException(lex)
}

