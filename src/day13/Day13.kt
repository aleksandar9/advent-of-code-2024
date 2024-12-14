package day13

import println
import readInput
import java.math.BigDecimal
import java.math.BigInteger

fun main() {

    fun calculate(buttonA: Pair<Int, Int>, buttonB: Pair<Int, Int>, prize: Pair<Int, Int>): Int {
        if (
            (buttonA.first * 100 + buttonB.first * 100) < prize.first ||
            (buttonA.second * 100 + buttonB.second * 100) < prize.second
        ) {
            return 0
        } else {
            for (i in 0..100) {
                val a = buttonA.first * i
                val j = (prize.first - a) / buttonB.first
                if ((prize.first - a) % buttonB.first == 0 && j in 0..100) {
                    if (buttonA.second * i + buttonB.second * j == prize.second) {
                        return 3 * i + j
                    }
                }
            }
            return 0
        }
    }

    fun BigDecimal.isIntegerValue(): Boolean {
        return this.signum() == 0 || this.scale() <= 0 || this.stripTrailingZeros().scale() <= 0
    }

    fun calculate2(
        buttonA: Pair<BigInteger, BigInteger>,
        buttonB: Pair<BigInteger, BigInteger>,
        prize: Pair<BigInteger, BigInteger>,
    ): BigInteger {
        val j = (buttonA.first * prize.second - buttonA.second * prize.first).toBigDecimal().setScale(2) /
            (buttonA.first * buttonB.second - buttonA.second * buttonB.first).toBigDecimal().setScale(2)
        return if (j.isIntegerValue() && j > BigDecimal.ZERO) {
            val i = (prize.first - buttonB.first * j.toBigInteger()).toBigDecimal().setScale(2) / buttonA.first.toBigDecimal().setScale(2)
            if (!i.isIntegerValue() || i < BigDecimal.ZERO) {
                return BigInteger.ZERO
            } else {
                BigInteger.valueOf(3) * i.toBigInteger() + j.toBigInteger()
            }
        } else {
            BigInteger.ZERO
        }
    }

    fun part1(input: List<String>): Int {
        var sum = 0

        input.chunked(4).forEach {
            val buttonA = it[0]
            val buttonB = it[1]
            val prize = it[2]

            val a_x = buttonA.substringAfter("X+").substringBefore(',').toInt()
            val a_y = buttonA.substringAfter("Y+").toInt()

            val b_x = buttonB.substringAfter("X+").substringBefore(',').toInt()
            val b_y = buttonB.substringAfter("Y+").toInt()

            val prize_x = prize.substringAfter("X=").substringBefore(',').toInt()
            val prize_y = prize.substringAfter("Y=").toInt()

            sum += calculate(Pair(a_x, a_y), Pair(b_x, b_y), Pair(prize_x, prize_y))
        }

        return sum
    }

    fun part2(input: List<String>): BigInteger {
        var sum = BigInteger.ZERO
        val extra = "10000000000000".toBigInteger()

        input.chunked(4).forEach {
            val buttonA = it[0]
            val buttonB = it[1]
            val prize = it[2]

            val a_x = buttonA.substringAfter("X+").substringBefore(',').toBigInteger()
            val a_y = buttonA.substringAfter("Y+").toBigInteger()

            val b_x = buttonB.substringAfter("X+").substringBefore(',').toBigInteger()
            val b_y = buttonB.substringAfter("Y+").toBigInteger()

            val prize_x = extra + prize.substringAfter("X=").substringBefore(',').toBigInteger()
            val prize_y = extra + prize.substringAfter("Y=").toBigInteger()

            sum += calculate2(Pair(a_x, a_y), Pair(b_x, b_y), Pair(prize_x, prize_y))
        }

        return sum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput(pkg = "day13", name = "Day13_test")
    check(part1(testInput) == 480)
    check(part2(testInput) == BigInteger.valueOf(875318608908))

    val input = readInput(pkg = "day13", name = "Day13")
    part1(input).println()
    part2(input).println()
}
