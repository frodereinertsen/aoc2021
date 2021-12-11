package day11

import InputUtils.readFile
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.lang.Integer.max
import java.lang.Integer.min

class Day11 {

    @Test
    fun puzzle1() {
        OctopusWorld(exampleInput.toGrid()).numFlashesIn100Steps() shouldBe 1656
        OctopusWorld(readFile("/day11.txt").toGrid()).numFlashesIn100Steps() shouldBe 1637
    }

    @Test
    fun puzzle2() {
        OctopusWorld(exampleInput.toGrid()).stepWhenAllFlash() shouldBe 195
        OctopusWorld(readFile("/day11.txt").toGrid()).stepWhenAllFlash() shouldBe 242
    }
}

data class Octopus(var energyLevel: Int, var isFlashing: Boolean = false) {
    fun increaseEnergyAndSetFlashingIfOverThreshold() {
        energyLevel++
        if (energyLevel > 9) {
            isFlashing = true
        }
    }

    fun resetFlashing() {
        isFlashing = false
        energyLevel = 0
    }
}

typealias Grid = List<List<Octopus>>

class OctopusWorld(private val grid: Grid) {

    private var numberOfFlashes = 0

    fun numFlashesIn100Steps(): Int {
        for (step in 1..100) {
            grid.apply {
                step()
                resetFlashing()
            }
        }

        return numberOfFlashes
    }

    fun stepWhenAllFlash(): Int {
        var step = 1
        while (true) {
            grid.apply {
                step()
                if (allAreFlashing()) {
                    return step
                }
                resetFlashing()
            }
            step++
        }
    }

    private fun Grid.step() {
        forEachIndexed { rowIndex, line ->
            line.forEachIndexed { columnIndex, _ ->
                tryToIncreaseAndPossiblyFlash(rowIndex, columnIndex)
            }
        }
    }

    private fun Grid.flash(flashRowIndex: Int, flashColumnIndex: Int) {
        numberOfFlashes += 1
        forEachNeighbor(flashRowIndex, flashColumnIndex) { rowIndex, columnIndex ->
            tryToIncreaseAndPossiblyFlash(rowIndex, columnIndex)
        }
    }

    private fun Grid.tryToIncreaseAndPossiblyFlash(
        rowIndex: Int,
        columnIndex: Int
    ) {
        this[rowIndex][columnIndex].apply {
            if (!isFlashing) {
                increaseEnergyAndSetFlashingIfOverThreshold()
                if (isFlashing) {
                    flash(rowIndex, columnIndex)
                }
            }
        }
    }

    private fun Grid.forEachNeighbor(
        flashRowIndex: Int,
        flashColumnIndex: Int,
        action: (rowIndex: Int, columnIndex: Int) -> Unit
    ) {
        fun Grid.adjacentRows() =
            max(0, flashRowIndex - 1)..min(size - 1, flashRowIndex + 1)

        fun Grid.adjacentColumns(rowIndex: Int) =
            max(0, flashColumnIndex - 1)..min(this[rowIndex].size - 1, flashColumnIndex + 1)

        fun isNeighbor(
            rowIndex: Int,
            columnIndex: Int
        ) = !(rowIndex == flashRowIndex && columnIndex == flashColumnIndex)

        for (rowIndex in adjacentRows()) {
            for (columnIndex in adjacentColumns(rowIndex)) {
                if (isNeighbor(rowIndex, columnIndex)) {
                    action(rowIndex, columnIndex)
                }
            }
        }
    }

    private fun Grid.resetFlashing() {
        flatten().filter { it.isFlashing }.forEach {
            it.resetFlashing()
        }
    }
}

private fun Grid.allAreFlashing(): Boolean = flatten().all { it.isFlashing }

private fun String.toGrid(): Grid = lines().map { line ->
    line.trim().map { Octopus(it.digitToInt()) }
}

val exampleInput = """
    5483143223
    2745854711
    5264556173
    6141336146
    6357385478
    4167524645
    2176841721
    6882881134
    4846848554
    5283751526
""".trimIndent()
