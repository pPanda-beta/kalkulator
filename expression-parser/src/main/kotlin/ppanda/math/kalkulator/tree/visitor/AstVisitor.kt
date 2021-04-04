package ppanda.math.kalkulator.tree.visitor

import ppanda.math.kalkulator.tree.*

interface AstVisitor<ResultT, ContextT> {
    fun process(node: Node, context: ContextT): ResultT = node.accept(this, context)

    fun visitNode(node: Node, context: ContextT): ResultT = TODO("Not implemented")

    fun visitExpression(node: Expression, context: ContextT): ResultT =
        visitNode(node, context)

    fun visitLiteral(node: Literal, context: ContextT): ResultT =
        visitExpression(node, context)

    fun visitParenthesizedExpression(node: ParenthesizedExpression, context: ContextT): ResultT =
        visitExpression(node, context)

    fun visitUnaryExpression(node: UnaryExpression, context: ContextT): ResultT =
        visitExpression(node, context)

    fun visitBinaryExpression(node: BinaryExpression, context: ContextT): ResultT =
        visitExpression(node, context)
}