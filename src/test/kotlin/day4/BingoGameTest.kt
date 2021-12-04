package day4

import InputUtils.readFile
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.util.regex.Pattern

internal class BingoGameTest {

    private val exampleInput = """
            7,4,9,5,11,17,23,2,0,14,21,24,10,16,13,6,15,25,12,22,18,20,8,19,3,26,1

            22 13 17 11  0
             8  2 23  4 24
            21  9 14 16  7
             6 10  3 18  5
             1 12 20 15 19
            
             3 15  0  2 22
             9 18 13 17  5
            19  8  7 25 23
            20 11 10 24  4
            14 21 16 12  6
            
            14 21 17 24  4
            10 16 15  9 19
            18  8 23 26 20
            22 11 13  6  5
             2  0 12  3  7
        """.trimIndent()

    @Test
    fun example1() {
        val bingo = createGame(exampleInput).play()

        assertEquals(4512, bingo?.superSum())
    }

    @Test
    fun task1() {
        val bingo = createGame(readFile("/day4.txt")).play()

        assertEquals(89001, bingo?.superSum())
    }

    @Test
    fun example2() {
        val bingo = createGame(exampleInput).playToLose()

        assertEquals(1924, bingo?.superSum())
    }

    @Test
    fun task2() {
        val bingo = createGame(readFile("/day4.txt")).playToLose()

        assertEquals(7296, bingo?.superSum())
    }

    private fun createGame(input: String): BingoGame {
        val drawNumbers = input.split("\n").first().split(",").map { it.toInt() }
        val boards = input.split("\n").drop(1).windowed(6, step = 6).map { lines ->
            val numbers = mutableListOf<List<Number>>()
            lines.filter { it.isNotEmpty() }
                .map { line ->
                    numbers.add(line.trim().split(Pattern.compile("\\s+")).map { it.toInt() }.map { Number(it) })
                }
            Board(numbers.toList())
        }
        return BingoGame(drawNumbers, boards)
    }
}