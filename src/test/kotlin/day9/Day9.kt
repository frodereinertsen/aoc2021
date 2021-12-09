package day9

import InputUtils.readFile
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

data class Point(val height: Int, val rowIndex: Int, val columnIndex: Int)

typealias Grid = List<List<Int>>

class Day9 {

    @Test
    fun puzzle1() {
        exampleInput.toGrid().getRiskLevelSum() shouldBe 15
        readFile("/day9.txt").toGrid().getRiskLevelSum() shouldBe 528
    }

    @Test
    fun puzzle2() {
        exampleInput.toGrid().getThreeLargestBasinsMultiplied() shouldBe 1134
        readFile("/day9.txt").toGrid().getThreeLargestBasinsMultiplied() shouldBe 920448
    }

    private fun Grid.getRiskLevelSum(): Int =
        findLowPoints().let { lowPoints -> lowPoints.sumOf { it.height } + lowPoints.size }

    private fun Grid.getThreeLargestBasinsMultiplied(): Int =
        findBasins().sortedBy { it.size }.reversed().take(3).fold(1) { sum, basin -> sum * basin.size }

    private fun Grid.findLowPoints(): List<Point> {
        val lowPoints = mutableListOf<Point>()

        forEachIndexed { rowIndex, row ->
            row.forEachIndexed { columnIndex, height ->
                val left = if (columnIndex > 0) height < this[rowIndex][columnIndex - 1] else true
                val right = if (columnIndex < row.size - 1) left && height < this[rowIndex][columnIndex + 1] else true
                val top = if (rowIndex > 0) right && height < this[rowIndex - 1][columnIndex] else true
                val bottom = if (rowIndex < size - 1) top && height < this[rowIndex + 1][columnIndex] else true
                if (left && right && top && bottom) {
                    lowPoints.add(Point(height, rowIndex, columnIndex))
                }
            }
        }
        return lowPoints
    }

    private fun Grid.findHigherNeighbors(point: Point): List<Point> {
        val height = point.height
        val rowIndex = point.rowIndex
        val columnIndex = point.columnIndex

        val neighbors = mutableListOf<Point>()

        fun tryToAddNeighbor(neighborRowIndex: Int, neighborColumnIndex: Int) {
            if (neighborRowIndex in indices && neighborColumnIndex in this[neighborRowIndex].indices
            ) {
                val neighborHeight = this[neighborRowIndex][neighborColumnIndex]
                if (neighborHeight in (height + 1)..8) {
                    neighbors.add(Point(neighborHeight, neighborRowIndex, neighborColumnIndex))
                }
            }
        }

        tryToAddNeighbor(rowIndex - 1, columnIndex)
        tryToAddNeighbor(rowIndex + 1, columnIndex)
        tryToAddNeighbor(rowIndex, columnIndex - 1)
        tryToAddNeighbor(rowIndex, columnIndex + 1)

        return neighbors
    }

    private fun Grid.findBasins(): List<Set<Point>> {
        val lowPoints = findLowPoints()
        val basins = mutableListOf<Set<Point>>()

        lowPoints.forEach { lowPoint ->
            val basin = mutableSetOf<Point>()
            fun addToBasin(point: Point) {
                basin.add(point)
                findHigherNeighbors(point).forEach { neighbor ->
                    addToBasin(neighbor)
                }
            }
            addToBasin(lowPoint)
            basins.add(basin)
        }

        return basins
    }
}

private fun String.toGrid(): List<List<Int>> = lines().map { line ->
    line.map { it.digitToInt() }
}

val exampleInput = """
    2199943210
    3987894921
    9856789892
    8767896789
    9899965678
""".trimIndent()
