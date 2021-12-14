package day6

fun fishing(initialFishDays: Map<Int, Long>, days: Int = 80): Map<Int, Long> {
    var fishDays = initialFishDays.toMutableMap()
    repeat(days) {
        val newFishDays = mutableMapOf<Int, Long>()
        for (fishDay in 0..7) {
            newFishDays[fishDay] = fishDays[fishDay + 1] ?: 0
        }
        fishDays[0]?.let { numZeroes ->
            newFishDays.merge(6, numZeroes, Long::plus)
            newFishDays[8] = numZeroes
        }
        fishDays = newFishDays
    }
    return fishDays.toMap()
}