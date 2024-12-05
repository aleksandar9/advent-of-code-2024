package day05

import println
import readInput

fun main() {

    data class Rule(
        val before: Int,
        val after: Int,
    )

    fun List<Int>.isReportValid(rules: List<Rule>): Boolean {
        this.forEachIndexed { pageIndex, page ->
            rules.forEach { rule ->
                if (page == rule.before) {
                    if (this.subList(0, pageIndex).contains(rule.after)) {
                        return false
                    }
                } else if (page == rule.after) {
                    if (this.subList(pageIndex, this.size).contains(rule.before)) {
                        return false
                    }
                }
            }
        }
        return true
    }

    fun part1(input: List<String>): Int {
        var sum = 0

        val rules = mutableListOf<Rule>()

        input.forEach {
            if (it.contains('|')) {
                val rule = it.split('|')
                rules.add(Rule(rule[0].toInt(), rule[1].toInt()))
            }

            if (it.contains(',')) {
                val report = it.split(',').map(String::toInt)
                if (report.isReportValid(rules)) sum += report[report.size / 2]
            }
        }

        return sum
    }

    fun part2(input: List<String>): Int {
        var sum = 0

        val rules = mutableListOf<Rule>()

        input.forEach {
            if (it.contains('|')) {
                val rule = it.split('|')
                rules.add(Rule(rule[0].toInt(), rule[1].toInt()))
            }

            if (it.contains(',')) {
                val report = it.split(',').map(String::toInt).toIntArray()

                var reportValidity = true
                for (i in 0..<report.size) {
                    for (j in 0 until report.size - i - 1) {
                        rules.find { it.before == report[j + 1] && it.after == report[j] }?.let {
                            // Swap the elements
                            val temp = report[j]
                            report[j] = report[j + 1]
                            report[j + 1] = temp
                            reportValidity = false
                        }
                    }
                }
                if (!reportValidity) sum += report[report.size / 2]
            }
        }

        return sum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput(pkg = "day05", name = "Day05_test")
    check(part1(testInput) == 143)
    check(part2(testInput) == 123)

    val input = readInput(pkg = "day05", name = "Day05")
    part1(input).println()
    part2(input).println()
}
