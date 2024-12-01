package day01

import println
import readInput
import kotlin.math.abs

fun main() {

    fun parseInput(input: String): List<Int> {
        val inputRegex = Regex("(\\d+)\\s+(\\d+)\$")
        val (first, second) = inputRegex.find(input)!!.destructured

        return listOf(first.toInt(), second.toInt())
    }

    fun part1(input: List<String>): Int {
        var sum = 0

        val firstList = mutableListOf<Int>()
        val secondList = mutableListOf<Int>()
        input.forEach {
            val numbers = parseInput(it)
            firstList.add(numbers[0])
            secondList.add(numbers[1])
        }
        firstList.sort()
        secondList.sort()

        firstList.forEachIndexed { index, number ->
            sum += abs(number - secondList[index])
        }

        return sum
    }

    fun part2(input: List<String>): Int {
        var sum = 0

        val firstList = mutableListOf<Int>()
        val secondList = mutableListOf<Int>()
        input.forEach {
            val numbers = parseInput(it)
            firstList.add(numbers[0])
            secondList.add(numbers[1])
        }

        firstList.forEach { number ->
            sum += number * secondList.count { it == number }
        }

        return sum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput(pkg = "day01", name = "Day01_test")
    check(part1(testInput) == 11)
    check(part2(testInput) == 31)

    val input = readInput(pkg = "day01", name = "Day01")
    part1(input).println()
    part2(input).println()
}
