package day06

import println
import readInput

fun main() {

    data class Node(
        val x: Int,
        val y: Int,
        var value: Char,
        var direction: Char? = null,
    )

    fun List<Node>.moveGuard(position: Pair<Int, Int>): Boolean {
        val direction = this.find { it.x == position.first && it.y == position.second }!!
        return when (direction.value) {
            '^' -> {
                val next = this.find { it.x == position.first && it.y == position.second - 1 }
                if (next != null) {
                    if (next.value == 'X' && next.direction == '^') {
                        return true
                    } else if (next.value != '#') {
                        next.value = '^'
                        direction.direction = '^'
                        direction.value = 'X'
                        return moveGuard(next.x to next.y)
                    } else {
                        direction.value = '>'
                        return moveGuard(direction.x to direction.y)
                    }
                } else {
                    direction.value = 'X'
                    false
                }
            }
            '>' -> {
                val next = this.find { it.x == position.first + 1 && it.y == position.second }
                if (next != null) {
                    if (next.value == 'X' && next.direction == '>') {
                        return true
                    } else if (next.value != '#') {
                        next.value = '>'
                        direction.direction = '>'
                        direction.value = 'X'
                        return moveGuard(next.x to next.y)
                    } else {
                        direction.value = 'v'
                        return moveGuard(direction.x to direction.y)
                    }
                } else {
                    direction.value = 'X'
                    false
                }
            }
            'v' -> {
                val next = this.find { it.x == position.first && it.y == position.second + 1 }
                if (next != null) {
                    if (next.value == 'X' && next.direction == 'v') {
                        return true
                    } else if (next.value != '#') {
                        next.value = 'v'
                        direction.direction = 'v'
                        direction.value = 'X'
                        return moveGuard(next.x to next.y)
                    } else {
                        direction.value = '<'
                        return moveGuard(direction.x to direction.y)
                    }
                } else {
                    direction.value = 'X'
                    false
                }
            }
            '<' -> {
                val next = this.find { it.x == position.first - 1 && it.y == position.second }
                if (next != null) {
                    if (next.value == 'X' && next.direction == '<') {
                        return true
                    } else if (next.value != '#') {
                        next.value = '<'
                        direction.direction = '<'
                        direction.value = 'X'
                        return moveGuard(next.x to next.y)
                    } else {
                        direction.value = '^'
                        return moveGuard(direction.x to direction.y)
                    }
                } else {
                    direction.value = 'X'
                    false
                }
            }
            else -> false
        }
    }

    fun part1(input: List<String>): Int {
        val grid = mutableListOf<Node>()
        var startingPoint = Pair(0, 0)

        input.forEachIndexed { y, line ->
            line.forEachIndexed { x, value ->
                grid.add(Node(x, y, value))
                if (value == '^') {
                    startingPoint = Pair(x, y)
                }
            }
        }

        grid.moveGuard(startingPoint)

        return grid.count { it.value == 'X' }
    }

    fun part2(input: List<String>): Int {
        var sum = 0

        val grid = mutableListOf<Node>()
        var startingPoint = Pair(0, 0)

        input.forEachIndexed { y, line ->
            line.forEachIndexed { x, value ->
                grid.add(Node(x, y, value))
                if (value == '^') {
                    startingPoint = Pair(x, y)
                }
            }
        }
        val copy = grid.map { Node(it.x, it.y, it.value) }

        grid.moveGuard(startingPoint)

        grid.filter { it.value == 'X' }.map { it.x to it.y }.forEach { obstruction ->
            val newGrid = copy.map { Node(it.x, it.y, it.value) }
            newGrid.find { it.x == obstruction.first && it.y == obstruction.second }?.value = '#'
            try {
                if (newGrid.moveGuard(startingPoint)) {
                    sum++
                }
            } catch (e: StackOverflowError) {
                sum++
            }
        }

        return sum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput(pkg = "day06", name = "Day06_test")
    check(part1(testInput) == 41)
    check(part2(testInput) == 6)

    val input = readInput(pkg = "day06", name = "Day06")
    part1(input).println()
    part2(input).println()
}
