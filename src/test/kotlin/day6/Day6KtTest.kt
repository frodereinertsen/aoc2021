package day6

import InputUtils.readFile
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class Day6KtTest {

    @Test
    fun example1() {
        val fishDays = toFishDays("3,4,3,1,2")
        val fishMap = fishing(fishDays)

        assertEquals(5934, fishMap.values.sumOf { it })
    }

    @Test
    fun task1() {
        val fishDays = toFishDays(readFile("/day6.txt"))
        val fishMap = fishing(fishDays)

        assertEquals(356190, fishMap.values.sumOf { it })
    }

    @Test
    fun example2() {
        val fishDays = toFishDays("3,4,3,1,2")
        val fishMap = fishing(fishDays, 256)

        assertEquals(26984457539, fishMap.values.sumOf { it })
    }

    @Test
    fun task2() {
        val fishDays = toFishDays(readFile("/day6.txt"))
        val fishMap = fishing(fishDays, 256)

        assertEquals(1617359101538, fishMap.values.sumOf { it })
    }

    private fun toFishDays(input: String): MutableMap<Int, Long> {
        val fishDays = mutableMapOf<Int, Long>()
        input.split(",").forEach { fishDay ->
            fishDays.merge(fishDay.toInt(), 1, Long::plus)
        }
        return fishDays
    }
}