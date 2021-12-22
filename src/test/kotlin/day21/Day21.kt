package day21

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class Day21 {
    @Test
    fun puzzle1() {
        Game(listOf(Player("1", 4), Player("2", 8))).apply {
            play()
            players.minOf { it.score } * die.rolls shouldBe 739785
        }
        Game(listOf(Player("1", 10), Player("2", 7))).apply {
            play()
            players.minOf { it.score } * die.rolls shouldBe 906093
        }
    }
}

class Game(val players: List<Player>) {
    val die = DeterministicDie(1..100)
    val wins = players.associate { player -> player.name to 0L }.toMutableMap()

    fun play() {
        while (true) {
            players.forEach { player ->
                val dieScore = die.roll(3)
                player.move(dieScore)
                if (player.score >= 1000) {
                    return
                }
            }
        }
    }

    fun playDiracDice() {
        while (true) {
            players.forEach { player ->
                val dieScore = die.roll(3)
                player.move(dieScore)
                if (player.score >= 21) {
                    wins.merge(player.name, 1, Long::plus)
                    return
                }
            }
        }
    }
}

class Player(val name: String, var space: Int, var score: Int = 0) {
    fun move(dieScore: Int) {
        space = when (val next = (space + dieScore) % 10) {
            0 -> 10
            else -> next
        }
        score += space
    }
}

class DeterministicDie(private val range: IntRange) {
    private var _rolls = 0
    var rolls: Int
        get() = _rolls
        private set(value) {
            _rolls = value
        }

    private var values = range.iterator()

    fun roll(times: Int): Int = (1..times).sumOf { roll() }

    private fun roll(): Int {
        if (!values.hasNext()) {
            values = range.iterator()
        }
        return values.next().also { rolls++ }
    }
}