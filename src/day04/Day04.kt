package day04

import println
import readInput

fun main() {

    class Node(
        val x: Int,
        val y: Int,
        var value: Char,
    )

    fun List<Node>.findAdjacentMs(from: Node): List<Node> {
        val right = this.find { it.x == from.x + 1 && it.y == from.y && it.value == 'M' }
        val left = this.find { it.x == from.x - 1 && it.y == from.y && it.value == 'M' }
        val up = this.find { it.x == from.x && it.y == from.y - 1 && it.value == 'M' }
        val down = this.find { it.x == from.x && it.y == from.y + 1 && it.value == 'M' }
        val rightUp = this.find { it.x == from.x + 1 && it.y == from.y - 1 && it.value == 'M' }
        val leftDown = this.find { it.x == from.x - 1 && it.y == from.y + 1 && it.value == 'M' }
        val leftUp = this.find { it.x == from.x - 1 && it.y == from.y - 1 && it.value == 'M' }
        val rightDown = this.find { it.x == from.x + 1 && it.y == from.y + 1 && it.value == 'M' }

        return listOfNotNull(right, left, up, down, rightUp, leftDown, leftUp, rightDown)
    }

    fun nextNode(firstNode: Node, secondNode: Node): Pair<Int, Int> {
        val newX = if (firstNode.x < secondNode.x) {
            secondNode.x + 1
        } else if (firstNode.x > secondNode.x) {
            secondNode.x - 1
        } else {
            secondNode.x
        }

        val newY = if (firstNode.y < secondNode.y) {
            secondNode.y + 1
        } else if (firstNode.y > secondNode.y) {
            secondNode.y - 1
        } else {
            secondNode.y
        }

        return newX to newY
    }

    fun List<Node>.findXmas(fromX: Node): Int {
        val xmasList = mutableListOf<List<Node>>()

        this.findAdjacentMs(fromX).forEach { m ->
            val next = nextNode(fromX, m)
            val a = this.find { it.x == next.first && it.y == next.second && it.value == 'A' }
            if (a != null) {
                val nextS = nextNode(m, a)
                val s = this.find { it.x == nextS.first && it.y == nextS.second && it.value == 'S' }
                if (s != null) xmasList.add(listOf(fromX, m, a, s))
            }
        }

        return xmasList.count()
    }

    fun List<Node>.findX_mas(fromA: Node): Boolean {
        val ms = arrayOf('M', 'S')
        val rightUp = this.find { it.x == fromA.x + 1 && it.y == fromA.y - 1 && it.value in ms }
        val leftDown = this.find { it.x == fromA.x - 1 && it.y == fromA.y + 1 && it.value in ms }
        val leftUp = this.find { it.x == fromA.x - 1 && it.y == fromA.y - 1 && it.value in ms }
        val rightDown = this.find { it.x == fromA.x + 1 && it.y == fromA.y + 1 && it.value in ms }

        val diagonalOne = listOfNotNull(rightUp, leftDown).map { it.value }.distinct().size
        val diagonalTwo = listOfNotNull(leftUp, rightDown).map { it.value }.distinct().size

        return diagonalOne + diagonalTwo == 4
    }

    fun part1(input: List<String>): Int {
        var sum = 0

        val grid = mutableListOf<Node>()
        input.forEachIndexed { y, string ->
            string.forEachIndexed { x, char ->
                grid.add(Node(x, y, char))
            }
        }

        grid.forEach { node ->
            if (node.value == 'X') {
                sum += grid.findXmas(node)
            }
        }

        return sum
    }

    fun part2(input: List<String>): Int {
        var sum = 0

        val grid = mutableListOf<Node>()
        input.forEachIndexed { y, string ->
            string.forEachIndexed { x, char ->
                grid.add(Node(x, y, char))
            }
        }

        grid.forEach { node ->
            if (node.value == 'A' && grid.findX_mas(node)) {
                sum++
            }
        }

        return sum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput(pkg = "day04", name = "Day04_test")
    check(part1(testInput) == 18)
    check(part2(testInput) == 9)

    val input = readInput(pkg = "day04", name = "Day04")
    part1(input).println()
    part2(input).println()
}
