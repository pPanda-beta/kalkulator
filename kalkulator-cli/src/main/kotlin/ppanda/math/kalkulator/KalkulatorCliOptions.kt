package ppanda.math.kalkulator

import picocli.CommandLine
import picocli.CommandLine.Command
import picocli.CommandLine.Option

@Command(name = "kalkulator", mixinStandardHelpOptions = true, version = ["1.0"], showDefaultValues = true)
class KalkulatorCliOptions {
    @Option(names = ["-e", "--expression"], description = ["Expression to evaluate"])
    lateinit var expression: String

    companion object {
        fun buildFrom(args: Array<String>): KalkulatorCliOptions = KalkulatorCliOptions()
            .also { CommandLine(it).parseArgs(*args) }
    }
}