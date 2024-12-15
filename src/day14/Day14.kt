package day14

import println
import readInput
import java.io.File

fun main() {

    class Node(
        val x: Int,
        val y: Int,
        var robots: Int = 0,
    )

    fun List<Node>.moveRobots(input: List<String>, width: Int, height: Int, times: Int) {
        input.forEach {
            val (robot, velocity) = it.split(" ")
            val (x, y) = robot.substringAfter("p=").split(",").map { it.toInt() }
            val (vx, vy) = velocity.substringAfter("v=").split(",").map { it.toInt() }

            val updatedX = ((x + vx * times) % width + width) % width
            val updatedY = ((y + vy * times) % height + height) % height

            this.find { it.x == updatedX && it.y == updatedY }!!.robots += 1
        }
    }

    fun List<Node>.printToFile(width: Int, height: Int, filename: String) {
        File(filename).printWriter().use { out ->
            for (y in 0 until height) {
                for (x in 0 until width) {
                    val node = this.find { it.x == x && it.y == y }!!
                    if (node.robots > 0) {
                        out.print(node.robots)
                    } else {
                        out.print(".")
                    }
                }
                out.println()
            }
        }
    }

    fun part1(input: List<String>, width: Int, height: Int): Int {
        var result = 1

        val grid = mutableListOf<Node>()
        for (y in 0 until height) {
            for (x in 0 until width) {
                val node = Node(x, y)
                grid.add(node)
            }
        }

        grid.moveRobots(input, width, height, times = 100)

        result *= grid.filter { it.x in 0 until width / 2 && it.y in 0 until height / 2 }.sumOf { it.robots }
        result *= grid.filter { it.x in width / 2 + 1 until width && it.y in 0 until height / 2 }.sumOf { it.robots }
        result *= grid.filter { it.x in 0 until width / 2 && it.y in height / 2 + 1 until height }.sumOf { it.robots }
        result *= grid.filter { it.x in width / 2 + 1 until width && it.y in height / 2 + 1 until height }
            .sumOf { it.robots }

        return result
    }

    fun part2(input: List<String>, width: Int, height: Int): Int {
        var result = 0

        var counter = 0
        while (counter < 10000) {
            counter++

            val grid = mutableListOf<Node>()
            for (y in 0 until height) {
                for (x in 0 until width) {
                    val node = Node(x, y)
                    grid.add(node)
                }
            }

            grid.moveRobots(input, width, height, times = counter)

            for (y in 0 until height) {
                var lineCounter = 0
                for (x in 0 until width) {
                    val node = grid.find { it.x == x && it.y == y }!!
                    if (node.robots > 0) {
                        lineCounter++
                    } else {
                        lineCounter = 0
                    }
                    if (lineCounter > 10) {
                        result = counter
                        grid.printToFile(width, height, filename = "ChristmasTree.txt")
                        break
                    }
                }
            }
        }

        return result
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput(pkg = "day14", name = "Day14_test")
    check(part1(testInput, width = 11, height = 7) == 12)

    val input = readInput(pkg = "day14", name = "Day14")
    part1(input, width = 101, height = 103).println()
    part2(input, width = 101, height = 103).println()
}
