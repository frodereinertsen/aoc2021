package day3

import InputUtils.readFile
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class Day3KtTest {

    @Test
    fun example1() {
        val input = """
    00100
    11110
    10110
    10111
    10101
    01111
    00111
    11100
    10000
    11001
    00010
    01010
""".trimIndent()

        val report = input.toIntMatrix()

        val powerConsumtion = calculatePowerConsumtion(report)

        assertEquals(198, powerConsumtion)
    }

    @Test
    fun example2() {
        val input = """
    00100
    11110
    10110
    10111
    10101
    01111
    00111
    11100
    10000
    11001
    00010
    01010
""".trimIndent()

        val report = input.toIntMatrix()

        val oxygenGeneratorRating = calculateOxygenGeneratorRating(report)
        val cO2ScrubberRating = calculateCO2ScrubberRating(report)

        assertEquals(230, oxygenGeneratorRating * cO2ScrubberRating)
    }

    @Test
    fun task1() {
        val report = readFile("/day3.txt").toIntMatrix()

        val powerConsumtion = calculatePowerConsumtion(report)

        assertEquals(3912944, powerConsumtion)
    }

    @Test
    fun task2() {
        val report = readFile("/day3.txt").toIntMatrix()

        val oxygenGeneratorRating = calculateOxygenGeneratorRating(report)
        val cO2ScrubberRating = calculateCO2ScrubberRating(report)

        assertEquals(4996233, oxygenGeneratorRating * cO2ScrubberRating)
    }
}

private fun String.toIntMatrix(): Array<IntArray> = lines().map {
    it.map { it.digitToInt() }.toIntArray()
}.toTypedArray()
