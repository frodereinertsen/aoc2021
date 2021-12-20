package day18

import InputUtils.readFile
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test


class Day18 {

    @Test
    fun magnitude() {
        """
            [[[0,[5,8]],[[1,7],[9,6]]],[[4,[1,2]],[[1,4],2]]]
            [[[5,[2,8]],4],[5,[[9,9],0]]]
            [6,[[[6,2],[5,6]],[[7,6],[4,7]]]]
            [[[6,[0,7]],[0,9]],[4,[9,[9,0]]]]
            [[[7,[6,4]],[3,[1,3]]],[[[5,5],1],9]]
            [[6,[[7,3],[3,2]]],[[[3,8],[5,7]],4]]
            [[[[5,4],[7,7]],8],[[8,3],8]]
            [[9,3],[[9,9],[6,[4,9]]]]
            [[2,[[7,7],7]],[[5,8],[[9,3],[0,2]]]]
            [[[[5,2],5],[8,[3,7]]],[[5,[7,5]],[4,4]]]
        """.trimIndent().combineSnailFishExpressions().apply {
            toString() shouldBe "[[[[6,6],[7,6]],[[7,7],[7,0]]],[[[7,7],[7,7]],[[7,8],[9,9]]]]"
            magnitude() shouldBe 4140
        }
    }

    @Test
    fun puzzle1() {
        readFile("/day18.txt").combineSnailFishExpressions().magnitude() shouldBe 4289
    }

    @Test
    fun permutations() {
        """
            [[[0,[5,8]],[[1,7],[9,6]]],[[4,[1,2]],[[1,4],2]]]
            [[[5,[2,8]],4],[5,[[9,9],0]]]
            [6,[[[6,2],[5,6]],[[7,6],[4,7]]]]
            [[[6,[0,7]],[0,9]],[4,[9,[9,0]]]]
            [[[7,[6,4]],[3,[1,3]]],[[[5,5],1],9]]
            [[6,[[7,3],[3,2]]],[[[3,8],[5,7]],4]]
            [[[[5,4],[7,7]],8],[[8,3],8]]
            [[9,3],[[9,9],[6,[4,9]]]]
            [[2,[[7,7],7]],[[5,8],[[9,3],[0,2]]]]
            [[[[5,2],5],[8,[3,7]]],[[5,[7,5]],[4,4]]]
        """.trimIndent().lines().permutations(length = 2).maxOf { permutation ->
            permutation.combineSnailFishExpressions().magnitude()
        } shouldBe 3993
    }

    @Test
    fun puzzle2() {
        readFile("/day18.txt").lines().permutations(length = 2).maxOf { permutation ->
            permutation.combineSnailFishExpressions().magnitude()
        } shouldBe 4807
    }
}

private fun List<String>.combineSnailFishExpressions(): SnailFish =
    map { it.toSnailFish() }.reduce { combined, snailFishPair ->
        (combined + snailFishPair).reduce()
    }

private fun String.combineSnailFishExpressions(): SnailFish =
    lines().combineSnailFishExpressions()


private fun String.toSnailFish(): SnailFish {
    var parseIndex = 0

    fun parseSnailFishValue(): SnailFish {
        when (val char = this[parseIndex++]) {
            '[' -> {
                val pair = SnailFish.Pair(
                    left = parseSnailFishValue().also { parseIndex++ }, //comma
                    right = parseSnailFishValue()
                )
                parseIndex++ //right square bracket
                return pair
            }
            in '0'..'9' -> {
                var numString = "" + char
                while (this[parseIndex].isDigit()) {
                    numString += this[parseIndex++]
                }
                return SnailFish.Regular(numString.toInt())
            }
            else -> {
                throw Exception("oh no")
            }
        }
    }

    return parseSnailFishValue() as SnailFish.Pair
}

