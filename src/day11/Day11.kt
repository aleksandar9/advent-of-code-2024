package day11

import println
import readInput
import java.util.concurrent.ConcurrentHashMap

fun main() {

    fun part1(input: List<String>): Int {
        var list = input[0].split(" ").toTypedArray()

        var counter = 0
        while (counter < 25) {
            counter++
            val newElements = mutableListOf<String>()
            list.forEachIndexed { index, s ->
                if (s.all { it == '0' }) {
                    list[index] = "1"
                } else if (s.length % 2 == 0) {
                    list[index] = s.substring(0, s.length / 2)
                    newElements.add(s.substring(s.length / 2).trimStart('0'))
                } else {
                    list[index] = (s.toLong() * 2024).toString()
                }
            }
            list = list.plus(newElements)
        }

        return list.size
    }

    fun MutableMap<String, Long>.increaseCounter(key: String, counter: Long) {
        val value = this.getOrDefault(key, 0) + counter
        this[key] = value
    }

    fun MutableMap<String, Long>.decreaseCounter(key: String, counter: Long) {
        val value = this.getOrDefault(key, 0) - counter
        this[key] = if (value < 0) 0 else value
    }

    fun part2(input: List<String>): Long {
        val map = input[0].split(" ").associateWithTo(ConcurrentHashMap()) { 1L }

        var counter = 0
        while (counter < 75) {
            counter++
            map.filter { it.value > 0 }.forEach { (s, counter) ->
                if (s.all { it == '0' }) {
                    map[s] = 0
                    map.increaseCounter("1", counter)
                } else if (s.length % 2 == 0) {
                    map.decreaseCounter(s, counter)
                    map.increaseCounter(s.substring(0, s.length / 2), counter)
                    map.increaseCounter(s.substring(s.length / 2).trimStart('0'), counter)
                } else {
                    map.decreaseCounter(s, counter)
                    map.increaseCounter((s.toLong() * 2024).toString(), counter)
                }
            }
        }

        return map.values.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput(pkg = "day11", name = "Day11_test")
    check(part1(testInput) == 55312)
    check(part2(testInput) == 65601038650482L)

    val input = readInput(pkg = "day11", name = "Day11")
    part1(input).println()
    part2(input).println()
}
