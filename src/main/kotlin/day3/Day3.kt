package day3

import kotlin.math.floor

fun calculatePowerConsumtion(report: Array<IntArray>): Int {
    var gammaBinary = ""
    var epsilonBinary = ""

    for (rowIndex in 0 until report.first().size) {
        val numOnes = report.count { row ->
            row[rowIndex] == 1
        }
        if (numOnes > floor(report.size.toDouble() / 2)) {
            gammaBinary += "1"
            epsilonBinary += "0"
        } else {
            gammaBinary += "0"
            epsilonBinary += "1"
        }
    }

    return gammaBinary.toInt(2) * epsilonBinary.toInt(2)
}

fun calculateOxygenGeneratorRating(report: Array<IntArray>, rowIndex: Int = 0): Int {
    if (report.size == 1) {
        return report.first().joinToString("").toInt(2)
    }

    val numOnes = report.count { row ->
        row[rowIndex] == 1
    }
    val numZeroes = report.size - numOnes

    return if (numOnes >= numZeroes) {
        calculateOxygenGeneratorRating(report.filter { it[rowIndex] == 1 }.toTypedArray(), rowIndex + 1)
    } else {
        calculateOxygenGeneratorRating(report.filter { it[rowIndex] == 0 }.toTypedArray(), rowIndex + 1)
    }
}

fun calculateCO2ScrubberRating(report: Array<IntArray>, rowIndex: Int = 0): Int {
    if (report.size == 1) {
        return report.first().joinToString("").toInt(2)
    }

    val numOnes = report.count { row ->
        row[rowIndex] == 1
    }
    val numZeroes = report.size - numOnes

    return if (numOnes >= numZeroes) {
        calculateCO2ScrubberRating(report.filter { it[rowIndex] == 0 }.toTypedArray(), rowIndex + 1)
    } else {
        calculateCO2ScrubberRating(report.filter { it[rowIndex] == 1 }.toTypedArray(), rowIndex + 1)
    }
}