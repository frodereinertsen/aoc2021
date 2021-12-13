package day13

import InputUtils.readFile
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class Day13 {
    @Test
    fun puzzle1() {
        PasswordFinder(exampleInput).fold(1).flatten().count { it == dot } shouldBe 17
        PasswordFinder(readFile("/day13.txt")).fold(1).flatten().count { it == dot } shouldBe 735
    }

    @Test
    fun puzzle2() {
        PasswordFinder(exampleInput).fold().print()
        PasswordFinder(readFile("/day13.txt")).fold().print()
    }
}

typealias Grid = List<List<Char>>
typealias MutableGrid = List<MutableList<Char>>
typealias Folds = List<Pair<String, Int>>

const val dot = '#'
const val empty = '.'

class PasswordFinder(input: String) {
    private val grid: Grid
    private val folds: Folds

    init {
        grid = input.toGrid()
        folds = input.toFolds()
    }

    fun fold(times: Int = folds.size): Grid {
        var foldGrid = grid

        folds.take(times).forEach { (axis, foldIndex) ->
            val newFoldGrid: List<MutableList<Char>>
            when (axis) {
                "x" -> {
                    newFoldGrid = createGrid(foldGrid.size, foldIndex)
                    foldGrid.forEachIndexed { y, row ->
                        row.forEachIndexed { x, char ->
                            if (char == dot) {
                                newFoldGrid[y][if (x < foldIndex) x else foldIndex - (x - foldIndex)] = dot
                            }
                        }
                    }
                }
                else -> {
                    newFoldGrid = createGrid(foldIndex, foldGrid.first().size)
                    foldGrid.forEachIndexed { y, row ->
                        row.forEachIndexed { x, char ->
                            if (char == dot) {
                                newFoldGrid[if (y < foldIndex) y else foldIndex - (y - foldIndex)][x] = dot
                            }
                        }
                    }
                }
            }
            foldGrid = newFoldGrid
        }

        return foldGrid
    }

    private fun createGrid(
        rowSize: Int,
        columnSize: Int
    ): MutableGrid = List(rowSize) {
        MutableList(columnSize) { empty }
    }

    private fun String.toGrid(): Grid {
        val coordinates = lines().takeWhile { it.isNotEmpty() }.map {
            val split = it.split(",")
            split[0].toInt() to split[1].toInt()
        }

        val grid = createGrid(
            coordinates.maxOf { it.second } + 1,
            coordinates.maxOf { it.first } + 1)

        coordinates.forEach { (x, y) ->
            grid[y][x] = dot
        }

        return grid
    }

    private fun String.toFolds(): Folds = lines().dropWhile { !it.startsWith("fold along") }.map {
        val (axis, index) = """fold along (x|y)=(\d+)""".toRegex().find(it)!!.destructured
        axis to index.toInt()
    }
}

private fun Grid.print() {
    forEach {
        println(it.joinToString(""))
    }
    println()
}

val exampleInput = """
    6,10
    0,14
    9,10
    0,3
    10,4
    4,11
    6,0
    6,12
    4,1
    0,13
    10,12
    3,4
    3,0
    8,4
    1,10
    2,14
    8,10
    9,0

    fold along y=7
    fold along x=5
""".trimIndent()