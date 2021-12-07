package day7

import InputUtils.readFile
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class Day7KtTest {

    @Test
    fun example1() {
        val exampleInput = "16,1,2,0,4,2,7,1,2,14"
        val positions = exampleInput.toIntList()
        assertEquals(37, minimalDistance(positions, distanceInSteps))
    }

    @Test
    fun task1() {
        val input = readFile("/day7.txt")
        val positions = input.toIntList()
        assertEquals(345035, minimalDistance(positions, distanceInSteps))
    }

    @Test
    fun example2() {
        val exampleInput = "16,1,2,0,4,2,7,1,2,14"
        val positions = exampleInput.toIntList()
        assertEquals(168, minimalDistance(positions, distanceInSumOfStepValues))
    }

    @Test
    fun task2() {
        val input = readFile("/day7.txt")
        val positions = input.toIntList()
        assertEquals(97038163, minimalDistance(positions, distanceInSumOfStepValues))
    }
}

private fun String.toIntList(): List<Int> = split(",").map { it.toInt() }
