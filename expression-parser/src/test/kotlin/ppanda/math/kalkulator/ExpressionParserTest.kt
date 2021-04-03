package ppanda.math.kalkulator

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import ppanda.math.kalkulator.exceptions.NoMoreTokenException
import ppanda.math.kalkulator.exceptions.UnknownTokenException
import ppanda.math.kalkulator.standard.StandardOperators.ADDITION
import ppanda.math.kalkulator.standard.StandardOperators.DIVISION
import ppanda.math.kalkulator.standard.StandardOperators.MULTIPLICATION
import ppanda.math.kalkulator.standard.StandardOperators.UNARY_MINUS
import ppanda.math.kalkulator.tree.BinaryExpression
import ppanda.math.kalkulator.tree.Literal
import ppanda.math.kalkulator.tree.UnaryExpression


class ExpressionParserTest : AnnotationSpec() {
    val parser = ExpressionParser()

    @Test
    fun `should parse simple unary expression`() {
        parser.parse("- 5") shouldBe UnaryExpression(
            UNARY_MINUS, 5.0.asLit
        )

    }

    @Test
    fun `should throw error for excess operators`() {
        shouldThrowExactly<NoMoreTokenException> { parser.parse("-") }
        shouldThrowExactly<NoMoreTokenException> { parser.parse("- 5 - ") }
        shouldThrowExactly<NoMoreTokenException> { parser.parse("- - - 5 * 3 /") }
    }

    @Test
    fun `should throw error for excess operands`() {
        shouldThrowExactly<UnknownTokenException> { parser.parse(" 5 2 ") }
        shouldThrowExactly<UnknownTokenException> { parser.parse(" 5 + + 2 / 3 5 ") }
    }

    @Test
    fun `should parse binary expression`() {
        val tree = parser.parse(" 2 + 12 / 3 * 2")

        val _12by3 = BinaryExpression(DIVISION, 12.0.asLit, 3.0.asLit)
        val _12by3x2 = BinaryExpression(MULTIPLICATION, _12by3, 2.0.asLit)

        val _12by3x2plus2 = BinaryExpression(ADDITION, 2.0.asLit, _12by3x2)

        tree shouldBe _12by3x2plus2
    }
}

val Double.asLit: Literal
    get() = Literal(this)