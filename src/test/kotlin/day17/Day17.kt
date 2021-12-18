package day17

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import kotlin.math.abs
import kotlin.math.max

class Day17 {
    @Test
    fun puzzle1() {
        OceanTrench(20..30, -10..-5).findMaxY() shouldBe 45
        OceanTrench(269..292, -68..-44).findMaxY() shouldBe 2278
    }

    @Test
    fun puzzle2() {
        OceanTrench(20..30, -10..-5).countAll() shouldBe 112
        OceanTrench(269..292, -68..-44).countAll() shouldBe 996
    }
}

class OceanTrench(private val xTargetRange: IntRange, private val yTargetRange: IntRange) {

    fun findMaxY(): Int {
        val yCandidates = getYCandidates()
        return yCandidates.values.maxOf { positions -> positions.maxOf { y -> y } }
    }

    fun countAll(): Int {
        val yCandidates = getYCandidates()
        val xCandidates = getXCandidates(yCandidates.values.maxOf { it.size })
        return xCandidates.possibleNumberOfSteps(xTargetRange).values.sumOf { xSteps ->
            yCandidates.possibleNumberOfSteps(yTargetRange).values.count { ySteps ->
                xSteps.intersect(ySteps).isNotEmpty()
            }
        }
    }

    private fun getYCandidates(): Map<Int, List<Int>> {
        val dyCandidates = yTargetRange.first..-yTargetRange.first
        return dyCandidates.associateWith { dyCandidate ->
            tryYCandidate(dyCandidate)
        }.filter { it.value.last() in yTargetRange }
    }

    private fun getXCandidates(maxSteps: Int): Map<Int, List<Int>> {
        val dxCandidates = 1..xTargetRange.last
        return dxCandidates.associateWith { dxCandidate -> tryXCandidate(dxCandidate, maxSteps) }
            .filter { it.value.last() in xTargetRange }
    }

    private fun tryXCandidate(dx: Int, maxSteps: Int): List<Int> {
        val trajectorySequence = generateSequence(IndexedValue(0, 0)) { (index, x) ->
            val xMove = max(dx - index, 0)
            if (x > xTargetRange.last || xMove == 0 && x !in xTargetRange) {
                null
            } else {
                IndexedValue(index + 1, x + xMove)
            }

        }
        return trajectorySequence.takeWhile { it.index <= maxSteps && it.value <= xTargetRange.last }.map { it.value }
            .toList()
    }

    private fun tryYCandidate(dy: Int): List<Int> {
        val trajectorySequence = generateSequence(IndexedValue(0, 0)) { (index, y) ->
            val position = y + dy - index
            IndexedValue(index + 1, position)
        }
        return trajectorySequence.map { it.value }.takeWhile { it >= yTargetRange.first }.toList()
    }
}

private fun Map<Int, List<Int>>.possibleNumberOfSteps(range: IntRange): Map<Int, Set<Int>> {
    return map { (delta, positions) ->
        val stepsBefore = positions.takeWhile { it !in range }.count()
        val stepSet = List(positions.filter { it in range }.size) { index -> stepsBefore + index }.toSet()
        delta to stepSet
    }.toMap()
}
