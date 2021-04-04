package ppanda.math.kalkulator

import ppanda.math.kalkulator.exceptions.UnrecognizedSourceException

data class SourceLocation(val beginLineNo: Int = 1, val beginCharNo: Int) {
    override fun toString(): String = """[L$beginLineNo:$beginCharNo]"""
}

data class Lex(val tokenType: TokenType, val location: SourceLocation?, val value: String) {
    override fun toString(): String = """$value ${location?.toString() ?: ""}"""
}

object ALLOWED_NON_TOKEN_PATTERNS {
    val patterns = listOf(
        "\\s+".toRegex()
    )

    fun matches(s: String) = patterns.any { it.matches(s) }
}

class Lexer(private val tokenizer: Tokenizer) {

    fun extract(source: String): List<Lex> = source.split("\n*")
        .withIndex()
        .flatMap { splitSingleLine(it.index + 1, it.value) }

    private fun splitSingleLine(lineNo: Int, value: String): List<Lex> {
        var currentPos = 0;
        val lexemes = mutableListOf<Lex>()
        while (true) {
            val (tokenType, result) = findNextMatch(value, currentPos) ?: break
            lexemes += Lex(
                tokenType,
                SourceLocation(beginLineNo = lineNo, beginCharNo = 1 + result.range.first),
                result.value
            )

            ensureSafeSkipping(value, lineNo, currentPos, result)

            currentPos = result.range.last + 1
        }
        return lexemes
    }

    private fun ensureSafeSkipping(
        value: String,
        lineNo: Int,
        currentPos: Int,
        result: Result
    ) {
        if (result.range.first > currentPos) {
            val unrecognizedSource = value.substring(currentPos, result.range.first)
            if (!ALLOWED_NON_TOKEN_PATTERNS.matches(unrecognizedSource)) {
                throw UnrecognizedSourceException(
                    unrecognizedSource,
                    SourceLocation(beginLineNo = lineNo, beginCharNo = 1 + currentPos)
                )
            }
        }
    }

    private fun findNextMatch(s: String, startingPos: Int): Pair<TokenType, Result>? {
        return tokenizer
            .allTokenTypes
            .asSequence() // TODO: Need to sort token types to reduce ambiguity
            .map { it to it.consume(s, startingPos) }
            .filter { (_, result) -> result != null }
            .map { (type, result) -> type to result!! }
            .minByOrNull { (_, result) -> result.range.first }
    }
}

