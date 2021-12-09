package day8

import InputUtils.readFile
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import kotlin.math.pow


typealias Input = List<Day8.Line>

class Day8 {
    data class Line(val codes: List<Set<Char>>, val display: List<Set<Char>>)

    val sample = parse("/day8-sample.txt")
    val input = parse("/day8.txt")

    @Test
    fun puzzle1() {
        sample.count1478() shouldBe 26
        println("Day  8, Puzzle 1: ${input.flatMap { it.display }.count { it.size in listOf(2, 3, 4, 7) }} easy codes")
    }

    @Test
    fun puzzle2() {
        sample.sumValues() shouldBe 61229
        println("Day  8, Puzzle 2: ${input.sumValues()} summed values")
    }

    fun Input.count1478() = flatMap { it.display }.count { it.size in listOf(2, 3, 4, 7) }

    fun Input.sumValues() = map { it.decode() }.sum()

    fun Line.decode(): Int {
        val decoder = codes
            .sortedByDescending { if (it.size in listOf(2, 3, 4, 7)) 8 else it.size }
            .fold(MutableList(10) { setOf<Char>()}) { decoded, code ->
                val digit = when {
                    code.size == 2 -> 1
                    code.size == 3 -> 7
                    code.size == 4 -> 4
                    code.size == 7 -> 8
                    code.size == 6 && code.containsAll(decoded[4]) -> 9
                    code.size == 6 && code.containsAll(decoded[1]) -> 0
                    code.size == 6 -> 6
                    code.size == 5 && decoded[6].containsAll(code) -> 5
                    code.size == 5 && decoded[9].containsAll(code) -> 3
                    else -> 2
                }
                decoded.also { it[digit] = code }
            }
            .withIndex()
            .associate { (digit, code) -> code to digit }

        return display.reversed()
            .mapIndexed { n, digit -> decoder[digit]!! * 10f.pow(n) }
            .sum().toInt()
    }

    fun String.parseLine() = split("|")
        .map { part -> part.trim().split(Regex("\\W+")).map { it.toSet() } }
        .let { (codes, digits) -> Line(codes, digits) }

    fun parse(resource: String) = readFile(resource)
        .lines()
        .filter { it.isNotBlank() }
        .map { it.parseLine() }
}