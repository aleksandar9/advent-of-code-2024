package day02

import println
import readInput
import kotlin.math.abs

fun main() {

    fun sameDirection(level1: Int, level2: Int): Boolean {
        return level1 > 0 && level2 > 0 || level1 < 0 && level2 < 0
    }

    fun part1(input: List<String>): Int {
        var sum = 0

        input.forEach {
            val report = it.split(' ').map(String::toInt)
            report.asSequence().forEachIndexed { index, level ->
                if (index > 0) {
                    val nextDistance = report[index - 1] - level
                    if (abs(nextDistance) > 3 || !sameDirection(report[0] - report[1], nextDistance)) {
                        return@forEach
                    }
                }
            }
            sum++
        }

        return sum
    }

    fun part2(input: List<String>): Int {
        var sum = 0

        input.forEach {
            val report = it.split(' ').map(String::toInt).toMutableList()
            var errorIndex = -1

            run originalReport@{
                report.asSequence().forEachIndexed { index, level ->
                    if (index > 0) {
                        val nextDistance = report[index - 1] - level
                        if (abs(nextDistance) > 3 || !sameDirection(report[0] - report[1], nextDistance)) {
                            errorIndex = index - 1
                            return@originalReport
                        }
                    }
                }
            }

            if (errorIndex == -1) {
                sum++
                return@forEach
            }

            var dampenedError = 0

            run firstReport@{
                val firstReport = report.filterIndexed { index, _ -> index != errorIndex - 1 }
                firstReport.asSequence().forEachIndexed { index, level ->
                    if (index > 0) {
                        val nextDistance = firstReport[index - 1] - level
                        if (abs(nextDistance) > 3 || !sameDirection(firstReport[0] - firstReport[1], nextDistance)) {
                            dampenedError++
                            return@firstReport
                        }
                    }
                }
            }

            run secondReport@{
                val secondReport = report.filterIndexed { index, _ -> index != errorIndex }
                secondReport.asSequence().forEachIndexed { index, level ->
                    if (index > 0) {
                        val nextDistance = secondReport[index - 1] - level
                        if (abs(nextDistance) > 3 || !sameDirection(secondReport[0] - secondReport[1], nextDistance)) {
                            dampenedError++
                            return@secondReport
                        }
                    }
                }
            }

            run thirdReport@{
                val thirdReport = report.filterIndexed { index, _ -> index != errorIndex + 1 }
                thirdReport.asSequence().forEachIndexed { index, level ->
                    if (index > 0) {
                        val nextDistance = thirdReport[index - 1] - level
                        if (abs(nextDistance) > 3 || !sameDirection(thirdReport[0] - thirdReport[1], nextDistance)) {
                            dampenedError++
                            return@thirdReport
                        }
                    }
                }
            }

            if (dampenedError < 3) sum++
        }

        return sum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput(pkg = "day02", name = "Day02_test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    val input = readInput(pkg = "day02", name = "Day02")
    part1(input).println()
    part2(input).println()
}
