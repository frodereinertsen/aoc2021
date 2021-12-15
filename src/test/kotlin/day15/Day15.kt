package day15

import InputUtils.readFile
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class Day15 {
    @Test
    fun puzzle1() {
        exampleInput.toCavern().apply { traverse() }.riskThroughCavern() shouldBe 40
        readFile("/day15.txt").toCavern().apply { traverse() }.riskThroughCavern() shouldBe 714
    }

    @Test
    fun puzzle2() {
        exampleInput.multiplyToCavern().apply { traverse() }.riskThroughCavern() shouldBe 315
        readFile("/day15.txt").multiplyToCavern().apply { traverse() }.riskThroughCavern() shouldBe 2948
    }
}

private fun Cavern.startPosition(): Position = this[0][0]
private fun Cavern.endPosition(): Position = this[this.size - 1][this[0].size - 1]
private fun Cavern.traverse() {
    val startPosition = startPosition().also { it.minRiskToHere = 0 }
    val nodesToEvaluate = mutableSetOf(startPosition)
    while (nodesToEvaluate.isNotEmpty()) {
        val current = nodesToEvaluate.minByOrNull { it.minRiskToHere }!!.also {
            nodesToEvaluate.remove(it)
            it.evaluated = true
        }
        neighborsOf(current).filter { !it.evaluated }.forEach { neighbor ->
            if (current.minRiskToHere + current.riskLevel < neighbor.minRiskToHere) {
                neighbor.minRiskToHere = current.minRiskToHere + current.riskLevel
                neighbor.predecessor = current
                nodesToEvaluate.add(neighbor)
            }
        }
    }
}

private fun Cavern.riskThroughCavern(): Int =
    endPosition().minRiskToHere - startPosition().riskLevel + endPosition().riskLevel

private fun Cavern.neighborsOf(position: Position): Set<Position> {
    val (x, y) = position
    return mutableSetOf<Position>().also { set ->
        if (x > 0) set.add(this[y][x - 1])
        if (y > 0) set.add(this[y - 1][x])
        if (x < this[y].size - 1) set.add(this[y][x + 1])
        if (y < this.size - 1) set.add(this[y + 1][x])
    }
}

private fun Cavern.print() {
    forEach { row ->
        println(row.map { it.riskLevel }.joinToString(""))
    }
}

typealias Cavern = List<List<Position>>

fun String.toCavern() = lines().mapIndexed { y, line ->
    line.mapIndexed { x, riskLevel ->
        Position(x, y, riskLevel.digitToInt())
    }
}

fun String.multiplyToCavern(): Cavern {
    val original = toCavern()
    val cavern = List(original.size * 5) { mutableListOf<Position>() }
    for (yMultiplier in 0..4) {
        for (y in original.indices) {
            val row = cavern[y + (original.size * yMultiplier)]
            for (xMultiplier in 0..4) {
                for (x in original.first().indices) {
                    val riskLevel = original[y][x].riskLevel + xMultiplier + yMultiplier
                    row.add(
                        Position(
                            x + (original.first().size * xMultiplier),
                            y + (original.size * yMultiplier),
                            if (riskLevel > 9) riskLevel % 9 else riskLevel
                        )
                    )
                }
            }
        }
    }
    return cavern
}


data class Position(
    val x: Int,
    val y: Int,
    val riskLevel: Int,
    var evaluated: Boolean = false,
    var predecessor: Position? = null,
    var minRiskToHere: Int = Int.MAX_VALUE
) {

    fun printTrail() {
        val trail = mutableListOf<Position>()
        var position: Position? = this
        while (position != null) {
            trail.add(position)
            position = position.predecessor
        }
        println(trail.reversed().map { it.x to it.y }.joinToString())
    }

    fun printScores() {
        val trail = mutableListOf<Position>()
        var position: Position? = this
        while (position != null) {
            trail.add(position)
            position = position.predecessor
        }
        println(trail.reversed().map { it.riskLevel }.joinToString(" + "))
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Position

        if (x != other.x) return false
        if (y != other.y) return false
        if (riskLevel != other.riskLevel) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        result = 31 * result + riskLevel
        return result
    }
}

val exampleInput = """
    1163751742
    1381373672
    2136511328
    3694931569
    7463417111
    1319128137
    1359912421
    3125421639
    1293138521
    2311944581
""".trimIndent()