package day2

enum class Direction {
    forward,
    down,
    up
}

data class Move(val direction: Direction, val units: Int)

class Submarine {
    var depth = 0
    var horizontalPosition = 0

    fun travel(course: List<Move>): Submarine {
        course.forEach {
            when(it.direction) {
                Direction.forward -> horizontalPosition += it.units
                Direction.down -> depth += it.units
                Direction.up -> depth -= it.units
            }
        }
        return this
    }
}

class Submarine2 {
    var aim = 0
    var depth = 0
    var horizontalPosition = 0

    fun travel(course: List<Move>): Submarine2 {
        course.forEach {
            when(it.direction) {
                Direction.forward -> {
                    horizontalPosition += it.units
                    depth += aim * it.units
                }
                Direction.down -> aim += it.units
                Direction.up -> aim -= it.units
            }
        }
        return this
    }
}