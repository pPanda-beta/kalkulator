package ppanda.math.kalkulator.exceptions

import ppanda.math.kalkulator.SourceLocation
import ppanda.math.kalkulator.Token
import ppanda.math.kalkulator.tree.Expression

open class ParseException(
    msg: String,
    location: SourceLocation? = null,
    cause: Throwable? = null
) : Exception(msg + location?.let { "near $location" }, cause)

class NoMoreTokenException(expectation: String) :
    ParseException(""" Expected $expectation but got nothing """)

class UnknownTokenException(token: Token, expectation: String) :
    ParseException(""" $token can not be recognized expecting $expectation """, token.location)

class ExtraTokenException(nodes: Iterable<Expression>) :
    ParseException(""" Unconsumed nodes found $nodes """)

