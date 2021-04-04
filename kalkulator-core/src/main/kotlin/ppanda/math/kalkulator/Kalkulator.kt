package ppanda.math.kalkulator

import ppanda.math.kalkulator.modules.OperatorModules
import ppanda.math.kalkulator.standard.StandardOperatorModules

class Kalkulator(
    val modules: OperatorModules = StandardOperatorModules,
    val parser: ExpressionParser = ExpressionParser(modules.allOperators, modules.precedence),
    val evaluator: ExpressionEvaluator = ExpressionEvaluator(modules)
) {
    fun parseAndEvaluate(expressionInString: String) = parser.parse(expressionInString)
        .let { evaluator.evaluate(it) }
}