package day1

import InputUtils.readFile
import InputUtils.toIntList
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class Day1Test {

    @Test
    fun example1() {
        val input = """
        199
        200
        208
        210
        200
        207
        240
        269
        260
        263
    """.trimIndent().toIntList()

        assertEquals(7, numIncreases(input, 1))
    }

    @Test
    fun task1() {
        val depths = readFile("/day1.txt").toIntList()

        assertEquals(1521, numIncreases(depths, 1))
    }

    @Test
    fun example2() {
        val input = """
        199
        200
        208
        210
        200
        207
        240
        269
        260
        263
    """.trimIndent().toIntList()

        assertEquals(5, numIncreases(input, 3))
    }

    @Test
    fun task2() {
        val depths = readFile("/day1.txt").toIntList()

        assertEquals(1543, numIncreases(depths, 3))
    }
}