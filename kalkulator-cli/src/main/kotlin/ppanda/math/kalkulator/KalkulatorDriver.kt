package ppanda.math.kalkulator

class KalkulatorDriver {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            run(args)
        }

        // Visible only for testing
        internal fun run(args: Array<String>, printer: (String) -> Unit = ::println) {
            val options = KalkulatorCliOptions.buildFrom(args)
            val result = Kalkulator().parseAndEvaluate(options.expression)
            printer("Result is $result")
        }
    }
}

