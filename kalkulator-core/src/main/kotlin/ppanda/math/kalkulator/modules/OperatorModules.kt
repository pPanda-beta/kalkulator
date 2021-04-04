package ppanda.math.kalkulator.modules

import ppanda.math.kalkulator.BinaryOperator
import ppanda.math.kalkulator.Operator
import ppanda.math.kalkulator.Operators
import ppanda.math.kalkulator.UnaryOperator


interface OperatorModule {
    fun operator(): Operator
    fun precedenceWeight(): Int
}

interface UnaryOperatorModule : OperatorModule {
    override fun operator(): UnaryOperator

    fun eval(operand: Double): Double
}

interface BinaryOperatorModule : OperatorModule {
    override fun operator(): BinaryOperator

    fun eval(leftOperand: Double, rightOperand: Double): Double
}

fun newModule(operator: UnaryOperator, precedenceWeight: Int, evaluator: (Double) -> Double): UnaryOperatorModule =
    object : UnaryOperatorModule {
        override fun operator() = operator
        override fun precedenceWeight(): Int = precedenceWeight
        override fun eval(operand: Double): Double = evaluator(operand)
    }

fun newModule(
    operator: BinaryOperator,
    precedenceWeight: Int,
    evaluator: (Double, Double) -> Double
): BinaryOperatorModule =
    object : BinaryOperatorModule {
        override fun operator() = operator
        override fun precedenceWeight(): Int = precedenceWeight
        override fun eval(leftOperand: Double, rightOperand: Double): Double = evaluator(leftOperand, rightOperand)
    }


class OperatorModuleNotFound(operator: Operator) : Exception(""" No module loaded for $operator """)

// First class collection for operators
open class OperatorModules(
    val unaryModules: Collection<UnaryOperatorModule>,
    val binaryModules: Collection<BinaryOperatorModule>
) {
    val allModulesByOperator = (unaryModules + binaryModules).associateBy(OperatorModule::operator)

    private val unaryModulesByOperator = unaryModules.associateBy(UnaryOperatorModule::operator)
    private val binaryModulesByOperator = binaryModules.associateBy(BinaryOperatorModule::operator)

    val allOperators = object : Operators {
        override fun all(): Collection<Operator> = allModulesByOperator.keys
    }

    val precedence = compareBy<Operator> { allModulesByOperator.getValue(it).precedenceWeight() }.reversed()


    fun eval(operator: UnaryOperator, operand: Double): Double =
        unaryModulesByOperator[operator]?.eval(operand)
            ?: throw OperatorModuleNotFound(operator)

    fun eval(operator: BinaryOperator, leftOperand: Double, rightOperand: Double): Double =
        binaryModulesByOperator[operator]?.eval(leftOperand, rightOperand)
            ?: throw OperatorModuleNotFound(operator)
}

