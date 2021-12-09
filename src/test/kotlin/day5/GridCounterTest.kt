package day5

import InputUtils.readFile
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class GridCounterTest {

    private val exampleInput = """
    0,9 -> 5,9
    8,0 -> 0,8
    9,4 -> 3,4
    2,2 -> 2,1
    7,0 -> 7,4
    6,4 -> 2,0
    0,9 -> 2,9
    3,4 -> 1,4
    0,0 -> 8,8
    5,5 -> 8,2
""".trimIndent()

    @Test
    fun example1() {
        val lines = exampleInput.toLines()
        val gridCounter = GridCounter().apply {
            countHorizontalAndVertical(lines)
        }
        assertEquals(5, gridCounter.numPointsWithAtLeastTwoLines())
    }

    @Test
    fun task1() {
        val lines = readFile("/day5.txt").toLines()
        val gridCounter = GridCounter().apply {
            countHorizontalAndVertical(lines)
        }
        assertEquals(6564, gridCounter.numPointsWithAtLeastTwoLines())
    }

    @Test
    fun example2() {
        val lines = exampleInput.toLines()
        val gridCounter = GridCounter().apply {
            count(lines)
        }
        assertEquals(12, gridCounter.numPointsWithAtLeastTwoLines())
    }

    @Test
    fun task2() {
        val lines = readFile("/day5.txt").toLines()
        val gridCounter = GridCounter().apply {
            count(lines)
        }
        assertEquals(19172, gridCounter.numPointsWithAtLeastTwoLines())
    }
}

private fun String.toLines(): List<Line> {
    val lines = mutableListOf<Line>()
    lines().map { line ->
        val (x1, y1, x2, y2) = """([\d]+),([\d]+) -> ([\d]+),([\d]+)""".toRegex().find(line)!!.destructured
        lines.add(Line(x1.toInt(), y1.toInt(), x2.toInt(), y2.toInt()))
    }
    return lines
}
