package day10

import println
import readInput

fun main() {

    class Node(
        val x: Int,
        val y: Int,
        val height: Int,
        var counter: Int = 0,
    )

    fun List<Node>.calculateTrailheadScore(node: Node) {
        if (node.height == 9) {
            node.counter++
        }
        val right = this.find { it.x == node.x + 1 && it.y == node.y }
        val left = this.find { it.x == node.x - 1 && it.y == node.y }
        val up = this.find { it.x == node.x && it.y == node.y - 1 }
        val down = this.find { it.x == node.x && it.y == node.y + 1 }
        val neighbours = listOfNotNull(up, right, down, left)
        neighbours.forEach {
            if (node.height == it.height - 1) {
                this.calculateTrailheadScore(it)
            }
        }
    }

    fun part1(input: List<String>): Int {
        var sum = 0

        val grid = mutableListOf<Node>()
        input.forEachIndexed { y, line ->
            line.forEachIndexed { x, value ->
                grid.add(Node(x, y, value.digitToInt()))
            }
        }

        grid.forEach {
            if (it.height == 0) {
                grid.calculateTrailheadScore(it)
                sum += grid.count { it.height == 9 && it.counter > 0 }
                grid.forEach { it.counter = 0 }
            }
        }

        return sum
    }

    fun part2(input: List<String>): Int {
        var sum = 0

        val grid = mutableListOf<Node>()
        input.forEachIndexed { y, line ->
            line.forEachIndexed { x, value ->
                grid.add(Node(x, y, value.digitToInt()))
            }
        }

        grid.forEach {
            if (it.height == 0) {
                grid.calculateTrailheadScore(it)
                sum += grid.sumOf { it.counter }
                grid.forEach { it.counter = 0 }
            }
        }

        return sum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput(pkg = "day10", name = "Day10_test")
    check(part1(testInput) == 36)
    check(part2(testInput) == 81)

    val input = readInput(pkg = "day10", name = "Day10")
    part1(input).println()
    part2(input).println()
}
