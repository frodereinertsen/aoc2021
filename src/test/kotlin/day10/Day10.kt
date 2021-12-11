package day10

import InputUtils.readFile
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.math.floor

enum class ChunkChar(val open: Char, val close: Char, val illegalCharacterValue: Int, val autoCompleteScore: Int) {
    PARANTHESIS('(', ')', 3, 1),
    SQUARE_BRACKET('[', ']', 57, 2),
    CURLY_BRACKET('{', '}', 1197, 3),
    LESS_GREATER_THAN('<', '>', 25137, 4);

    companion object {
        fun isOpeningChar(char: Char): Boolean = values().map { it.open }.contains(char)
        fun isClosingChar(char: Char): Boolean = values().map { it.close }.contains(char)
        fun fromOpeningChar(char: Char): ChunkChar = values().first { it.open == char }
        fun fromClosingChar(char: Char): ChunkChar = values().first { it.close == char }
    }
}

class ChunkChecker(line: String) {
    val stack = Stack<ChunkChar>()
    var illegalChar: ChunkChar? = null

    init {
        val lineIterator = line.trim().iterator()
        while (illegalChar == null && lineIterator.hasNext()) {
            val char = lineIterator.nextChar()
            when {
                ChunkChar.isOpeningChar(char) -> {
                    stack.push(ChunkChar.fromOpeningChar(char))
                }
                ChunkChar.isClosingChar(char) -> {
                    val lastOpening = stack.peek()
                    val closingChar = ChunkChar.fromClosingChar(char)
                    if (lastOpening != closingChar) {
                        illegalChar = closingChar
                    } else {
                        stack.pop()
                    }
                }
            }
        }
    }

    fun isIncomplete(): Boolean = stack.size > 0
}

class Day10 {
    @Test
    fun puzzle1() {
        findIllegalCharacterSum(exampleInput) shouldBe 26397
        findIllegalCharacterSum(readFile("/day10.txt")) shouldBe 296535
    }

    @Test
    fun puzzle2() {
        findAutoCompleteScore(exampleInput) shouldBe 288957
        findAutoCompleteScore(readFile("/day10.txt")) shouldBe 4245130838
    }

    private fun findAutoCompleteScore(input: String): Long {
        val list = input.lines().map { ChunkChecker(it) }.filter { it.isIncomplete() && it.illegalChar == null }.map {
            it.stack.reversed().fold(0L) { sum, current ->
                (sum * 5) + current.autoCompleteScore }
        }.sorted()

        return list[floor(list.size.toDouble() / 2).toInt()]
    }

    private fun findIllegalCharacterSum(input: String): Int =
        input.lines().map { ChunkChecker(it) }.filter { it.illegalChar != null }
            .sumOf { it.illegalChar!!.illegalCharacterValue }
}

val exampleInput = """
            [({(<(())[]>[[{[]{<()<>>
            [(()[<>])]({[<{<<[]>>(
            {([(<{}[<>[]}>{[]{[(<()>
            (((({<>}<{<{<>}{[]{[]{}
            [[<[([]))<([[{}[[()]]]
            [{[{({}]{}}([{[{{{}}([]
            {<[[]]>}<{[{[{[]{()[[[]
            [<(<(<(<{}))><([]([]()
            <{([([[(<>()){}]>(<<{{
            <{([{{}}[<[[[<>{}]]]>[]]                        
        """.trimIndent()
