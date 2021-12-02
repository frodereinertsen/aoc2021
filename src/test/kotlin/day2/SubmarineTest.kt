package day2

import InputUtils.readFile
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class SubmarineTest {

    @Test
    fun example1() {
        val input = """
            forward 5
            down 5
            forward 8
            up 3
            down 8
            forward 2
        """.trimIndent()

        val course = toCourse(input)

        val submarine = Submarine().travel(course)

        assertEquals(150, submarine.horizontalPosition * submarine.depth)
    }

    @Test
    fun example2() {
        val input = """
            forward 5
            down 5
            forward 8
            up 3
            down 8
            forward 2
        """.trimIndent()

        val course = toCourse(input)

        val submarine = Submarine2().travel(course)

        assertEquals(900, submarine.horizontalPosition * submarine.depth)
    }

    @Test
    fun task1() {
        val input = readFile("/day2.txt")

        val course = toCourse(input)

        val submarine = Submarine().travel(course)

        assertEquals(1938402, submarine.horizontalPosition * submarine.depth)
    }

    @Test
    fun task2() {
        val input = readFile("/day2.txt")

        val course = toCourse(input)

        val submarine = Submarine2().travel(course)

        assertEquals(1938402, submarine.horizontalPosition * submarine.depth)
    }

    private fun toCourse(input: String): List<Move> {
        val course = input.split("\n")
            .map {
                it.split(" ").let { directionAndUnits ->
                    Move(Direction.valueOf(directionAndUnits[0]), directionAndUnits[1].toInt())
                }
            }
        return course
    }

}