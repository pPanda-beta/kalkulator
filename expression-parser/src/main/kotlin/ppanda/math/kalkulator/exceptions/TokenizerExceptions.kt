package ppanda.math.kalkulator.exceptions

import ppanda.math.kalkulator.Lex

class UnrecognizedLexException(val lex: Lex) : Exception(""" Can not understand ${lex.value} at ${lex.location} """)
