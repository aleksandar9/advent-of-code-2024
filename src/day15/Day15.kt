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

    fun List<Node>.fillTheGaps(nodes: List<Node>): List<Node> {
        val result = mutableListOf<Node>()
        nodes.forEachIndexed { index, node ->
            if (node.value == '[' && nodes.getOrNull(index + 1)?.value != ']') {
                result.add(node)
                result.add(this.find { it.x == node.x + 1 && it.y == node.y && it.value == ']' }!!)
            } else if (node.value == ']' && nodes.getOrNull(index - 1)?.value != '[') {
                result.add(this.find { it.x == node.x - 1 && it.y == node.y && it.value == '[' }!!)
                result.add(node)
            } else {
                result.add(node)
            }
        }

        return result.distinct().sortedBy(Node::x)
    }

    fun List<Node>.pushWider(nodesToPush: List<Node>, direction: Char): Boolean {
        val deltaY = if (direction == '^') -1 else 1
        val initialNext = nodesToPush.mapNotNull { node ->
            this.find { it.x == node.x && it.y == node.y + deltaY }
        }.sortedBy(Node::x).toMutableList()

        val nextNodes = this.fillTheGaps(initialNext)

        if (nextNodes.all { it.value == '.' }) {
            nodesToPush.zip(nextNodes).forEach { (node, next) -> next.value = node.value }
            return true
        } else if (nextNodes.any { it.value == '#' }) {
            return false
        } else {
            if (this.pushWider(nextNodes.filterNot { it.value == '.' }, direction)) {
                nextNodes.forEach { next ->
                    next.value = nodesToPush.firstOrNull { it.x == next.x }?.value ?: '.'
                }
                return true
            } else {
                return false
            }
        }
    }

    fun List<Node>.tryToMove(node: Node, direction: Char): Node {
        val leftRight = setOf('<', '>')
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
        } else if (next.value == 'O' || direction in leftRight) {
            if (this.pushNext(next, direction)) {
                next.value = node.value
                node.value = '.'
                return next
            } else {
                return node
            }
        } else {
            val next2 = if (next.value == '[') {
                this.find { it.x == next.x + 1 && it.y == next.y && it.value == ']' }!!
            } else {
                this.find { it.x == next.x - 1 && it.y == next.y && it.value == '[' }!!
            }
            if (this.pushWider(listOf(next, next2).sortedBy(Node::x), direction)) {
                next.value = node.value
                next2.value = '.'
                node.value = '.'
                return next
            } else {
                return node
            }
        }
    }

    fun part1(input: List<String>, printAllMoves: Boolean = false): Int {
        val grid = mutableListOf<Node>()
        for (y in 0 until input.indexOf("")) {
            for (x in 0 until input[y].length) {
                grid.add(Node(x, y, input[y][x]))
            }
        }

        val moves = input.subList(input.indexOf("") + 1, input.size).flatMap { it.toList() }

        if (printAllMoves) {
            println("Initial state:")
            grid.printGrid()
            println()
        }

        var robot = grid.first { it.value == '@' }
        moves.forEach { direction ->
            robot = grid.tryToMove(robot, direction)
            if (printAllMoves) {
                println("Move $direction:")
                grid.printGrid()
                println()
            }
        }

        return grid.filter { it.value == 'O' }.sumOf { it.x + 100 * it.y }
    }

    fun part2(input: List<String>, printAllMoves: Boolean = false): Int {
        val grid = mutableListOf<Node>()

        for (y in 0 until input.indexOf("")) {
            for (x in 0 until input[y].length) {
                if (input[y][x] == '@') {
                    grid.add(Node(2 * x, y, '@'))
                    grid.add(Node(2 * x + 1, y, '.'))
                } else if (input[y][x] == 'O') {
                    grid.add(Node(2 * x, y, '['))
                    grid.add(Node(2 * x + 1, y, ']'))
                } else if (input[y][x] == '#') {
                    grid.add(Node(2 * x, y, '#'))
                    grid.add(Node(2 * x + 1, y, '#'))
                } else {
                    grid.add(Node(2 * x, y, '.'))
                    grid.add(Node(2 * x + 1, y, '.'))
                }
            }
        }

        val moves = input.subList(input.indexOf("") + 1, input.size).flatMap { it.toList() }

        if (printAllMoves) {
            println("Initial state:")
            grid.printGrid()
            println()
        }

        var robot = grid.first { it.value == '@' }
        moves.forEach { direction ->
            robot = grid.tryToMove(robot, direction)
            if (printAllMoves) {
                println("Move $direction:")
                grid.printGrid()
                println()
            }
        }

        return grid.filter { it.value == '[' }.sumOf { it.x + 100 * it.y }
    }

    // test if implementation meets criteria from the description, like:
    val smallTestInputPart1 = readInput(pkg = "day15", name = "Day15_small_test_1")
    check(part1(smallTestInputPart1, printAllMoves = true) == 2028)
    val smallTestInputPart2 = readInput(pkg = "day15", name = "Day15_small_test_2")
    check(part2(smallTestInputPart2, printAllMoves = true) == 618)

    val testInput = readInput(pkg = "day15", name = "Day15_test")
    check(part1(testInput) == 10092)
    check(part2(testInput) == 9021)

    val input = readInput(pkg = "day15", name = "Day15")
    part1(input).println()
    part2(input).println()
}
