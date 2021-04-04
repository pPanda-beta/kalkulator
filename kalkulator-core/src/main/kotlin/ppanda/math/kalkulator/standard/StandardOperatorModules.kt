package ppanda.math.kalkulator.standard

import ppanda.math.kalkulator.modules.OperatorModules
import ppanda.math.kalkulator.modules.newModule
import ppanda.math.kalkulator.standard.StandardOperators.ADDITION
import ppanda.math.kalkulator.standard.StandardOperators.DIVISION
import ppanda.math.kalkulator.standard.StandardOperators.MULTIPLICATION
import ppanda.math.kalkulator.standard.StandardOperators.SUBTRACTION
import ppanda.math.kalkulator.standard.StandardOperators.UNARY_MINUS
import ppanda.math.kalkulator.standard.StandardOperators.UNARY_PLUS
import ppanda.math.kalkulator.standard.StandardPrecedence.ADD_SUB_PRECEDENCE
import ppanda.math.kalkulator.standard.StandardPrecedence.MULT_DIV_PRECEDENCE
import ppanda.math.kalkulator.standard.StandardPrecedence.UNARY_PRECEDENCE

// In actual system dynamic loading with reflection or service loading should be used. This is the base for plugin framework.
object StandardOperatorModules : OperatorModules(
    unaryModules = listOf(
        newModule(UNARY_PLUS, UNARY_PRECEDENCE) { it },
        newModule(UNARY_MINUS, UNARY_PRECEDENCE) { -it }
    ),
    // BODMAS by https://www.mathsisfun.com/operation-order-bodmas.html
    binaryModules = listOf(
        newModule(ADDITION, ADD_SUB_PRECEDENCE, Double::plus),
        newModule(SUBTRACTION, ADD_SUB_PRECEDENCE, Double::minus),
        newModule(MULTIPLICATION, MULT_DIV_PRECEDENCE, Double::times),
        newModule(DIVISION, MULT_DIV_PRECEDENCE, Double::div),
    )
)
