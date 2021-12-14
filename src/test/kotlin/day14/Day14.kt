package day14

import InputUtils.readFile
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

@OptIn(ExperimentalStdlibApi::class)
class Day14 {

    @Test
    fun puzzle1() {
        check(exampleInput, 10) shouldBe 1588
        check(readFile("/day14.txt"), 10) shouldBe 2657
    }

    @Test
    fun puzzle2() {
        check(exampleInput, 40) shouldBe 2188189693529
        check(readFile("/day14.txt"), 40) shouldBe 2911561572630
    }

    private fun check(input: String, steps: Int): Long {
        val template = input.toTemplate()
        val rules = input.toInstructions()

        val initial = template.windowed(2).groupingBy { it }.eachCount().mapValues { it.value.toLong() }
        val pairsCount = (0 until steps).fold(initial) { current, _ ->
            buildMap {
                current.forEach { (pair, count) ->
                    rules[pair]?.let<String, Unit> {
                        val first = pair[0] + it
                        val second = it + pair[1]
                        put(first, getOrDefault(first, 0) + count)
                        put(second, getOrDefault(second, 0) + count)
                    }
                }
            }
        }
        val charsCount = buildMap<Char, Long> {
            put(template.last(), 1)
            pairsCount.forEach { (pair, count) ->
                put(pair[0], getOrDefault(pair[0], 0) + count)
            }
        }
        return charsCount.maxOf { it.value } - charsCount.minOf { it.value }
    }

}

private fun String.toInstructions() = lines()
    .takeLastWhile { it.isNotEmpty() }
    .groupBy({ it.substringBefore(" -> ") }, { it.substringAfter(" -> ") })
    .mapValues { it.value.single() }

private fun String.toTemplate(): String = lines().first()

private val exampleInput = """
        NNCB

        CH -> B
        HH -> N
        CB -> H
        NH -> C
        HB -> C
        HC -> B
        HN -> C
        NN -> C
        BH -> H
        NC -> B
        NB -> B
        BN -> B
        BB -> N
        BC -> B
        CC -> N
        CN -> C
    """.trimIndent()
