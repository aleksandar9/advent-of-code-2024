package day15

import println
import readInput

fun main() {

    class Node(
        val x: Int,
        val y: Int,
        var value: Char,
    )

    fun List<Node>.printGrid() {
        val maxX = this.maxOf { it.x }
        val maxY = this.maxOf { it.y }
        for (y in 0..maxY) {
            for (x in 0..maxX) {
                print(this.find { it.x == x && it.y == y }?.value)
            }
            kotlin.io.println()
        }
    }


    fun List<Node>.pushNext(node: Node, direction: Char): Boolean {
        val next = when (direction) {
            '^' -> this.firstOrNull { it.x == node.x && it.y == node.y - 1 }
            'v' -> this.firstOrNull { it.x == node.x && it.y == node.y + 1 }
            '<' -> this.firstOrNull { it.x == node.x - 1 && it.y == node.y }
            '>' -> this.firstOrNull { it.x == node.x + 1 && it.y == node.y }
            else -> return false
        }
        if (next == null) {
            return false
        }
        if (next.value == '.') {
            next.value = node.value
            return true
        } else if (next.value == '#') {
            return false
        } else {
            if (this.pushNext(next, direction)) {
                next.value = node.value
                return true
            } else {
                return false
            }
        }
    }

    fun List<Node>.tryToMove(node: Node, direction: Char): Node {
        val next = when (direction) {
            '^' -> this.firstOrNull { it.x == node.x && it.y == node.y - 1 }
            'v' -> this.firstOrNull { it.x == node.x && it.y == node.y + 1 }
            '<' -> this.firstOrNull { it.x == node.x - 1 && it.y == node.y }
            '>' -> this.firstOrNull { it.x == node.x + 1 && it.y == node.y }
            else -> return node
        }
        if (next == null) {
            return node
        }
        if (next.value == '.') {
            next.value = node.value
            node.value = '.'
            return next
        } else if (next.value == '#') {
            return node
        } else {
            if (this.pushNext(next, direction)) {
                next.value = node.value
                node.value = '.'
                return next
            } else {
                return node
            }
        }
    }

    fun part1(input: List<String>): Int {
        var sum = 0

        val grid = mutableListOf<Node>()
        for (y in 0 until input.indexOf("")) {
            for (x in 0 until input[y].length) {
                grid.add(Node(x, y, input[y][x]))
            }
        }

        val moves = input.subList(input.indexOf("") + 1, input.size).flatMap { it.toList() }

        var robot = grid.first { it.value == '@' }
        moves.forEach { direction ->
            robot = grid.tryToMove(robot, direction)
        }

        return grid.filter { it.value == 'O' }.sumOf { it.x + 100 * it.y }
    }

    fun part2(input: List<String>): Int {
        var sum = 0

        return sum
    }

    // test if implementation meets criteria from the description, like:
    val smallTestInput = readInput(pkg = "day15", name = "Day15_small_test")
    check(part1(smallTestInput) == 2028)
    check(part2(smallTestInput) == 0)

    val testInput = readInput(pkg = "day15", name = "Day15_test")
    check(part1(testInput) == 10092)
    check(part2(testInput) == 0)

    val input = readInput(pkg = "day15", name = "Day15")
    part1(input).println()
    part2(input).println()
}
