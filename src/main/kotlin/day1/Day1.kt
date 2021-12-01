package day1

fun numIncreases(depths: List<Int>, windowSize: Int): Int {
    var numIncreases = 0
    val memo = mutableMapOf<Int, Int>()

    fun sumOfWindow(index: Int): Int = memo.getOrPut(index) {
        var sum = 0
        for (i in index downTo (index - windowSize + 1)) {
            sum += depths[i]
        }
        return sum
    }

    for (index in windowSize until depths.size) {
        if (sumOfWindow(index) > sumOfWindow(index - 1)) {
            numIncreases++
        }
    }

    return numIncreases
}