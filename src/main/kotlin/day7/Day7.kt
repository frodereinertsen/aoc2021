package day7

import kotlin.math.abs

val distanceInSteps: (position: Int, alignmentPosition: Int) -> Int =
    { position, alignmentPosition -> abs(position - alignmentPosition) }

val distanceInSumOfStepValues: (position: Int, alignmentPosition: Int) -> Int =
    { position, alignmentPosition -> (0..abs(position - alignmentPosition)).sum() }


fun minimalDistance(positions: List<Int>, distanceFunction: (position: Int, alignmentPosition: Int) -> Int): Int {
    var alignmentPosition = positions.minOrNull()!!
    var minDistance = Int.MAX_VALUE
    while(true) {
        positions.sumOf { distanceFunction(it, alignmentPosition) }.let {
            if (it < minDistance) {
                minDistance = it
            } else {
                return minDistance
            }
        }
        alignmentPosition++
    }
}

