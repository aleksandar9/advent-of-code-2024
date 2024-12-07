package day07

import println
import readInput
import java.math.BigInteger
import kotlin.math.pow

fun main() {

    fun generateOperators(length: Int, operatorsList: List<Char>): List<List<Char>> {
        val operators = mutableListOf<List<Char>>()
        val size = operatorsList.size.toDouble().pow(length - 1).toInt()

        for (i in 0 until size) {
            val binary = i.toString(operatorsList.size).padStart(length - 1, '0')
            val operator = binary.map { operatorsList[it.digitToInt()] }
            operators.add(operator)
        }

        return operators
    }

    fun calculate(lhs: BigInteger, rhs: BigInteger, operator: Char): BigInteger {
        return when (operator) {
            '+' -> lhs + rhs
            '*' -> lhs * rhs
            '|' -> BigInteger.valueOf((lhs.toString() + rhs.toString()).toLong())
            else -> BigInteger.ZERO
        }
    }

    fun part1(input: List<String>): BigInteger {
        var sum = BigInteger.ZERO

        input.forEach {
            var isResultValid = false
            val separator = it.indexOf(':')
            val total = it.substring(0, separator).toBigInteger()
            val parts = it.substringAfter(":").trim().split(" ").map(String::toBigInteger)

            val operators = generateOperators(parts.size, listOf('+', '*'))
            operators.forEach { currentOperators ->
                var result = parts[0]
                for (i in 0 until parts.size - 1) {
                    result = calculate(result, parts[i + 1], currentOperators[i])
                    if (result > total) break
                }
                if (result == total) {
                    isResultValid = true
                }
            }
            if (isResultValid) sum += total
        }

        return sum
    }

    fun part2(input: List<String>): BigInteger {
        var sum = BigInteger.ZERO

        input.forEach {
            var isResultValid = false
            val separator = it.indexOf(':')
            val total = it.substring(0, separator).toBigInteger()
            val parts = it.substringAfter(":").trim().split(" ").map(String::toBigInteger)

            val operators = generateOperators(parts.size, listOf('+', '*', '|'))
            operators.forEach { currentOperators ->
                var result = parts[0]
                for (i in 0 until parts.size - 1) {
                    result = calculate(result, parts[i + 1], currentOperators[i])
                    if (result > total) break
                }
                if (result == total) {
                    isResultValid = true
                }
            }
            if (isResultValid) sum += total
        }

        return sum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput(pkg = "day07", name = "Day07_test")
    check(part1(testInput) == BigInteger.valueOf(3749))
    check(part2(testInput) == BigInteger.valueOf(11387))

    val input = readInput(pkg = "day07", name = "Day07")
    part1(input).println()
    part2(input).println()
}
