package day03

import println
import readInput

fun main() {

    val regex = Regex("(mul\\((\\d{1,3},\\d{1,3})\\))")

    fun part1(input: List<String>): Int {
        var sum = 0

        input.forEach {
            val matches = regex.findAll(it)
            matches.forEach { match ->
                val (a, b) = match.value.substring(4, match.value.length - 1).split(",").map(String::toInt)
                sum += a * b
            }
        }

        return sum
    }

    val enablingRegex = Regex("(mul\\((\\d{1,3},\\d{1,3})\\))|(don't\\(\\))|(do\\(\\))")

    fun part2(input: List<String>): Int {
        var sum = 0
        var enabler = "do()"

        input.forEach {
            val matches = enablingRegex.findAll(it)
            matches.forEach { match ->
                val value = match.value
                if (value.startsWith("mul")) {
                    val (a, b) = value.substring(4, value.length - 1).split(",").map(String::toInt)
                    if (enabler == "do()") sum += a * b
                } else {
                    enabler = value
                }
            }
        }

        return sum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput(pkg = "day03", name = "Day03_test")
    check(part1(testInput) == 161)
    check(part2(testInput) == 48)

    val input = readInput(pkg = "day03", name = "Day03")
    part1(input).println()
    part2(input).println()
}
