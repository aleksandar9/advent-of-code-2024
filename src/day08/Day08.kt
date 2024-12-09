package day08

import println
import readInput
import kotlin.math.abs

fun main() {

    data class Node(
        val x: Int,
        val y: Int,
        val antenna: Char,
        var antiNode: Boolean = false,
    )

    fun <T> elementPairs(list: List<T>): List<Pair<T, T>> {
        val result = mutableListOf<Pair<T, T>>()

        for (i in 0 until list.size - 1) {
            for (j in i + 1 until list.size) {
                result.add(list[i] to list[j])
            }
        }

        return result
    }

    fun List<Node>.calculateAntiNodes(node1: Node, node2: Node) {
        val deltaX = node2.x - node1.x
        val deltaY = node2.y - node1.y
        this.find { it.x == node1.x - deltaX && it.y == node1.y - deltaY }?.antiNode = true
        this.find { it.x == node2.x + deltaX && it.y == node2.y + deltaY }?.antiNode = true
    }

    fun List<Node>.calculateUpdatedAntiNodes(node1: Node, node2: Node) {
        val deltaX = abs(node2.x - node1.x)
        val deltaY = abs(node2.y - node1.y)

        val firstXProgression = if (node2.x > node1.x) {
            node1.x downTo 0 step deltaX
        } else {
            node1.x..this.maxOf { it.x } step deltaX
        }
        val firstYProgression = if (node2.y > node1.y) {
            node1.y downTo 0 step deltaY
        } else {
            node1.y..this.maxOf { it.y } step deltaY
        }
        val firstResult = sequence {
            val first = firstXProgression.iterator()
            val second = firstYProgression.iterator()
            while (first.hasNext() && second.hasNext()) {
                yield(first.next() to second.next())
            }
        }.toList()

        val secondXProgression = if (node2.x > node1.x) {
            node2.x..this.maxOf { it.x } step deltaX
        } else {
            node2.x downTo 0 step deltaX
        }
        val secondYProgression = if (node2.y > node1.y) {
            node2.y..this.maxOf { it.y } step deltaY
        } else {
            node2.y downTo 0 step deltaY
        }
        val secondResult = sequence {
            val first = secondXProgression.iterator()
            val second = secondYProgression.iterator()
            while (first.hasNext() && second.hasNext()) {
                yield(first.next() to second.next())
            }
        }.toList()

        (firstResult + secondResult).forEach { coordinates ->
            this.find { it.x == coordinates.first && it.y == coordinates.second }?.antiNode = true
        }
    }

    fun part1(input: List<String>): Int {
        val grid = mutableListOf<Node>()

        input.forEachIndexed { y, line ->
            line.forEachIndexed { x, value ->
                grid.add(Node(x, y, value))
            }
        }

        val groups = grid
            .groupBy { it.antenna }
            .filterKeys { it.isLetterOrDigit() }
            .filterValues { it.size > 1 }
            .mapValues { elementPairs(it.value) }

        groups.forEach { (_, nodes) ->
            nodes.forEach { (node1, node2) ->
                grid.calculateAntiNodes(node1, node2)
            }
        }

        return grid.count { it.antiNode }
    }

    fun part2(input: List<String>): Int {
        val grid = mutableListOf<Node>()

        input.forEachIndexed { y, line ->
            line.forEachIndexed { x, value ->
                grid.add(Node(x, y, value))
            }
        }

        val groups = grid
            .groupBy { it.antenna }
            .filterKeys { it.isLetterOrDigit() }
            .filterValues { it.size > 1 }
            .mapValues { elementPairs(it.value) }

        groups.forEach { (_, nodes) ->
            nodes.forEach { (node1, node2) ->
                grid.calculateUpdatedAntiNodes(node1, node2)
            }
        }

        return grid.count { it.antiNode }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput(pkg = "day08", name = "Day08_test")
    check(part1(testInput) == 14)
    check(part2(testInput) == 34)

    val input = readInput(pkg = "day08", name = "Day08")
    part1(input).println()
    part2(input).println()
}
