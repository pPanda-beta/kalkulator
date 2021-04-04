package ppanda.math.kalkulator

import io.kotest.core.spec.style.AnnotationSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.verify


class KalkulatorDriverTest : AnnotationSpec() {

    @Test
    fun `should take commandline input and print the result`() = mockkConstructor(Kalkulator::class) {
        val expressionInString = "5 * 10 + 4 <<<< We really dont care since mocked "
        every { anyConstructed<Kalkulator>().parseAndEvaluate(expressionInString) } returns -111.0

        val mockPrinter = mockk<(String) -> Unit>(relaxed = true)

        KalkulatorDriver.run(
            arrayOf(
                "--expression", expressionInString
            ),
            mockPrinter
        )

        verify { mockPrinter("Result is -111.0") }
    }
}
