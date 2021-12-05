package day5

data class Line(val x1: Int, val y1: Int, val x2: Int, val y2: Int)

class GridCounter {
    private val counter: MutableMap<Pair<Int, Int>, Int> = mutableMapOf()

    fun numPointsWithAtLeastTwoLines(): Int = counter.values.count { it >= 2 }

    fun count(lines: List<Line>) {
        countHorizontalAndVertical(lines)
        countDiagonal(lines)
    }

    fun countHorizontalAndVertical(lines: List<Line>) {
        lines.filter { (x1, y1, x2, y2) ->
            x1 == x2 || y1 == y2
        }.forEach { (x1, y1, x2, y2) ->
            for (x in x1 toward x2) {
                for (y in y1 toward y2) {
                    counter.merge(x to y, 1, Int::plus)
                }
            }
        }
    }

    private fun countDiagonal(lines: List<Line>) {
        lines.filter { (x1, y1, x2, y2) ->
            x1 != x2 && y1 != y2
        }.forEach { (x1, y1, x2, y2) ->
            for ((x, y) in (x1 toward x2).zip(y1 toward y2)) {
                counter.merge(x to y, 1, Int::plus)
            }
        }
    }
}

private infix fun Int.toward(to: Int): IntProgression {
    val step = if (this > to) -1 else 1
    return IntProgression.fromClosedRange(this, to, step)
}
