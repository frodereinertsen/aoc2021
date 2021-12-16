package day16

import InputUtils.readFile
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class Day16 {
    @Test
    fun sumVersions() {
        Parser("8A004A801A8002F478").parseExpression().sumVersions() shouldBe 16
        Parser("620080001611562C8802118E34").parseExpression().sumVersions() shouldBe 12
        Parser("C0015000016115A2E0802F182340").parseExpression().sumVersions() shouldBe 23
        Parser("A0016C880162017C3686B18A3D4780").parseExpression().sumVersions() shouldBe 31
    }

    @Test
    fun puzzle1() {
        Parser(readFile("/day16.txt")).parseExpression().sumVersions() shouldBe 879
    }

    @Test
    fun operators() {
        Parser("C200B40A82").parseExpression().evaluate() shouldBe 3
        Parser("04005AC33890").parseExpression().evaluate() shouldBe 54
        Parser("880086C3E88112").parseExpression().evaluate() shouldBe 7
        Parser("CE00C43D881120").parseExpression().evaluate() shouldBe 9
        Parser("D8005AC2A8F0").parseExpression().evaluate() shouldBe 1
        Parser("F600BC2D8F").parseExpression().evaluate() shouldBe 0
        Parser("9C005AC2F8F0").parseExpression().evaluate() shouldBe 0
        Parser("9C0141080250320F1802104A08").parseExpression().evaluate() shouldBe 1
    }

    @Test
    fun puzzle2() {
        Parser(readFile("/day16.txt")).parseExpression().evaluate() shouldBe 539051801941L
    }

}

sealed class Expression(private val version: Int) {
    fun sumVersions(): Int {
        var versionSum = 0
        fun sum(expression: Expression) {
            versionSum += expression.version
            expression.subExpressions.forEach {
                sum(it)
            }
        }
        sum(this)
        return versionSum
    }

    val subExpressions: MutableList<Expression> = mutableListOf()

    abstract fun evaluate(): Long
}

class Literal(private val value: Long, version: Int) : Expression(version) {
    companion object {
        const val type = 4
    }

    override fun evaluate(): Long {
        return value
    }
}

sealed class Operator(version: Int) : Expression(version) {
    companion object {
        fun fromType(version: Int, type: Int): Operator {
            return when (type) {
                Sum.type -> Sum(version)
                Product.type -> Product(version)
                Minimum.type -> Minimum(version)
                Maximum.type -> Maximum(version)
                GreaterThan.type -> GreaterThan(version)
                LessThan.type -> LessThan(version)
                EqualTo.type -> EqualTo(version)
                else -> throw Exception("oh no")
            }
        }
    }
}

class Sum(version: Int) : Operator(version) {
    companion object {
        const val type = 0
    }

    override fun evaluate(): Long {
        return subExpressions.sumOf { it.evaluate() }
    }
}

class Product(version: Int) : Operator(version) {
    companion object {
        const val type = 1
    }

    override fun evaluate(): Long {
        return subExpressions.fold(1L) { acc, p -> acc * p.evaluate() }
    }
}

class Minimum(version: Int) : Operator(version) {
    companion object {
        const val type = 2
    }

    override fun evaluate(): Long {
        return subExpressions.minOf { it.evaluate() }
    }
}

class Maximum(version: Int) : Operator(version) {
    companion object {
        const val type = 3
    }

    override fun evaluate(): Long {
        return subExpressions.maxOf { it.evaluate() }
    }
}

class GreaterThan(version: Int) : Operator(version) {
    companion object {
        const val type = 5
    }

    override fun evaluate(): Long {
        return if (subExpressions[0].evaluate() > subExpressions[1].evaluate()) 1 else 0
    }
}

class LessThan(version: Int) : Operator(version) {
    companion object {
        const val type = 6
    }

    override fun evaluate(): Long {
        return if (subExpressions[0].evaluate() < subExpressions[1].evaluate()) 1 else 0
    }
}

class EqualTo(version: Int) : Operator(version) {
    companion object {
        const val type = 7
    }

    override fun evaluate(): Long {
        return if (subExpressions[0].evaluate() == subExpressions[1].evaluate()) 1 else 0
    }
}

class Parser(hexCode: String) {
    private val binary = hexCode.hexToBinary()
    private var parseIndex = 0
    private val lengthType11DigitsNumberOfSubPackets = 1
    private val lengthType15DigitsTotalLength = 0

    fun parseExpression(): Expression {
        val version = read(3).toDecimal()
        when (val type = read(3).toDecimal()) {
            Literal.type -> {
                return Literal(readLiteralValue(), version)
            }
            else -> {
                return Operator.fromType(version, type).apply {
                    val lengthType = read(1).toDecimal()
                    val length = read(if (lengthType == lengthType11DigitsNumberOfSubPackets) 11 else 15).toDecimal()
                    if (lengthType == lengthType15DigitsTotalLength) {
                        val startIndex = parseIndex
                        while (parseIndex < startIndex + length) {
                            subExpressions.add(parseExpression())
                        }
                    } else {
                        repeat(length) {
                            subExpressions.add(parseExpression())
                        }
                    }
                }
            }
        }
    }

    private fun readLiteralValue(): Long {
        var numberBits = ""
        do {
            val group = read(5)
            numberBits += group.substring(1)
        } while (group.first() == '1')
        return numberBits.toDecimalLong()
    }

    private fun read(digits: Int): String = binary.substring(parseIndex, parseIndex + digits).also {
        parseIndex += digits
    }
}

private fun String.toDecimal(): Int = toInt(2)
private fun String.toDecimalLong(): Long = toLong(2)
private fun String.hexToBinary(): String {
    return map { it.toString().toInt(16).toBinaryString().padStart(4, '0') }.joinToString("")
}
private fun Int.toBinaryString(): String = Integer.toBinaryString(this) ?: throw Exception("ouch")
