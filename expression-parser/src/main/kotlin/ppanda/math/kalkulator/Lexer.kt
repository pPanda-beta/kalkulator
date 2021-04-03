package ppanda.math.kalkulator

data class SourceLocation(val beginLineNo: Int = 1, val beginCharNo: Int) {
    override fun toString(): String = """[L$beginLineNo:$beginCharNo]"""
}

data class Lex(var location: SourceLocation, val value: String) {
    override fun toString(): String = """$value ${location.toString() ?: ""}"""
}

val NON_SPACE = "\\S+".toRegex()

class Lexer {
    fun extract(source: String): List<Lex> = source.split("\n")
        .withIndex()
        .flatMap { splitSingleLine(it.index + 1, it.value) }

    private fun splitSingleLine(lineNo: Int, value: String): List<Lex> = NON_SPACE
        .findAll(value)
        .map { match ->
            Lex(
                location = SourceLocation(
                    beginLineNo = lineNo,
                    beginCharNo = match.range.first
                ),
                value = match.value
            )
        }.toList()

}

