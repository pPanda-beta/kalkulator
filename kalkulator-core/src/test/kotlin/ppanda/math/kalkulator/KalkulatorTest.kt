package ppanda.math.kalkulator

import io.kotest.core.datatest.forAll
import io.kotest.core.spec.style.ExpectSpec
import io.kotest.matchers.doubles.shouldBeExactly


data class Case(val expr: String, val result: Double)

class KalkulatorTest : ExpectSpec({


    context("Given the default kalkulator") {
        val kalkulator = Kalkulator()

        // Following https://www.mathsisfun.com/operation-order-bodmas.html
        forAll<Case>(
            "Simple literal" to Case(" 5", 5.0),
            "Simple unary expression" to Case(" -765", -765.0),
            "Simple binary expression" to Case("45 / 3", 15.0),
            "Simple binary expression with precedence" to Case("7 + 6 * 52 + 3", 322.0),
            "Parenthesis with binary expressions" to Case("52 / (7 + 6)  + 3", 7.0),
            "Unnecessary parenthesis " to Case("52 / (7 + ((6)) )  + 3", 7.0),
            "Multiple unary expressions" to Case("2 + - - - 5", -3.0),

            ) { (expr, result) ->
            kalkulator.parseAndEvaluate(expr) shouldBeExactly result
        }
    }


})
