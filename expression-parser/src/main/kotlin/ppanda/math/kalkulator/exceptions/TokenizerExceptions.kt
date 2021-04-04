package ppanda.math.kalkulator.exceptions

import ppanda.math.kalkulator.SourceLocation

class UnrecognizedSourceException(sourceChunk: String, location: SourceLocation) :
    Exception(""" Can not understand '$sourceChunk' at $location """)
