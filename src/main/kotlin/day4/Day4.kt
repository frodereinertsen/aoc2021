package day4

data class Bingo(val lastDraw: Int, val board: Board) {
    fun superSum(): Int = lastDraw * board.rows.flatten().sumOf { if (it.drawn) 0 else it.value }
}

data class Number(val value: Int, var drawn: Boolean = false)

class Board(val rows: List<List<Number>>, var hasBingo: Boolean = false) {
    fun play(drawnNumber: Int): Bingo? {
        rows.forEachIndexed { rowIndex, columns ->
            columns.forEachIndexed { columnIndex, number ->
                if (number.value == drawnNumber) {
                    number.drawn = true
                    if (columns.all { it.drawn } || rows.all { it[columnIndex].drawn }) {
                        return Bingo(drawnNumber, this)
                    }
                }
            }
        }

        return null
    }
}

class BingoGame(private val numbers: List<Int>, private val boards: List<Board>) {
    fun playToWin(): Bingo? {
        numbers.forEach { number ->
            boards.forEach { board ->
                val bingo = board.play(number)
                if (bingo != null) {
                    return bingo
                }
            }
        }
        return null
    }

    fun playToLose(): Bingo? {
        numbers.forEach { number ->
            boards.forEach { board ->
                val bingo = board.play(number)
                if (bingo != null) {
                    board.hasBingo = true
                    if (boards.all { it.hasBingo }) {
                        return bingo
                    }
                }
            }
        }
        return null
    }
}