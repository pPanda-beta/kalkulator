package ppanda.math.kalkulator

import ppanda.math.kalkulator.Associativity.LEFT_TO_RIGHT
import ppanda.math.kalkulator.Associativity.RIGHT_TO_LEFT
import ppanda.math.kalkulator.modules.OperatorModules
import ppanda.math.kalkulator.tree.*
import ppanda.math.kalkulator.tree.visitor.AstVisitor

class ExpressionEvaluator(
    val modules: OperatorModules
) : AstVisitor<Double, Nothing?> {

    fun evaluate(expression: Expression) = process(expression, null)

    override fun visitLiteral(node: Literal, context: Nothing?): Double = node.value

    override fun visitParenthesizedExpression(node: ParenthesizedExpression, context: Nothing?): Double =
        evaluate(node.child)


    override fun visitUnaryExpression(node: UnaryExpression, context: Nothing?): Double =
        modules.eval(node.operator, evaluate(node.operand))


    override fun visitBinaryExpression(node: BinaryExpression, context: Nothing?): Double =
        when (node.operator.associativity) {
            LEFT_TO_RIGHT -> evaluate(node.left)
                .let { it to evaluate(node.right) }
                .let { (left, right) -> modules.eval(node.operator, left, right) }

            RIGHT_TO_LEFT -> evaluate(node.right)
                .let { evaluate(node.left) to it }
                .let { (left, right) -> modules.eval(node.operator, left, right) }
        }

}