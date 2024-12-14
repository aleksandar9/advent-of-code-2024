package day12

import println
import readInput

fun main() {

    data class Node(
        val x: Int,
        val y: Int,
        val plant: Char,
        var plotID: Int = 0,
    )

    fun List<Node>.calculateNeighbours(from: Node, plotId: Int): Int {
        val right = this.find { it.x == from.x + 1 && it.y == from.y && it.plotID == plotId }
        val left = this.find { it.x == from.x - 1 && it.y == from.y && it.plotID == plotId }
        val up = this.find { it.x == from.x && it.y == from.y - 1 && it.plotID == plotId }
        val down = this.find { it.x == from.x && it.y == from.y + 1 && it.plotID == plotId }
        return listOfNotNull(left, up, right, down).size
    }

    fun List<Node>.calculateCornerNeighbours(node: Node): Int {
        var corners = 0

        val right = this.find { it.x == node.x + 1 && it.y == node.y && it.plotID != node.plotID }
        val left = this.find { it.x == node.x - 1 && it.y == node.y && it.plotID != node.plotID }
        val up = this.find { it.x == node.x && it.y == node.y - 1 && it.plotID != node.plotID }
        val down = this.find { it.x == node.x && it.y == node.y + 1 && it.plotID != node.plotID }

        val rightUp =
            this.find { it.x == node.x + 1 && it.y == node.y - 1 && it.plotID != node.plotID }
        val leftDown =
            this.find { it.x == node.x - 1 && it.y == node.y + 1 && it.plotID != node.plotID }
        val leftUp =
            this.find { it.x == node.x - 1 && it.y == node.y - 1 && it.plotID != node.plotID }
        val rightDown =
            this.find { it.x == node.x + 1 && it.y == node.y + 1 && it.plotID != node.plotID }

        val vertical = listOfNotNull(up, down).size
        val horizontal = listOfNotNull(left, right).size
        if (horizontal + vertical == 3) {
            corners += 2 // 2 external corners
        } else if (horizontal == 1 && vertical == 1) {
            corners += 1 // 1 external corner
        }

        if (left == null && down == null && leftDown != null) {
            corners += 1 // 1 internal corner
        }
        if (left == null && up == null && leftUp != null) {
            corners += 1 // 1 internal corner
        }
        if (right == null && up == null && rightUp != null) {
            corners += 1 // 1 internal corner
        }
        if (right == null && down == null && rightDown != null) {
            corners += 1 // 1 internal corner
        }
        return corners
    }

    fun List<Node>.printGrid() {
        val maxX = this.maxOf { it.x }
        val maxY = this.maxOf { it.y }
        for (y in 0..maxY) {
            for (x in 0..maxX) {
                print(this.find { it.x == x && it.y == y }?.plant)
            }
            kotlin.io.println()
        }
    }

    fun List<Node>.mapPlot(from: Node, id: Int) {
        from.plotID = id
        val right = this.find { it.x == from.x + 1 && it.y == from.y && it.plant == from.plant && it.plotID == 0 }
        val left = this.find { it.x == from.x - 1 && it.y == from.y && it.plant == from.plant && it.plotID == 0 }
        val up = this.find { it.x == from.x && it.y == from.y - 1 && it.plant == from.plant && it.plotID == 0 }
        val down = this.find { it.x == from.x && it.y == from.y + 1 && it.plant == from.plant && it.plotID == 0 }
        val neighbours = listOfNotNull(left, up, right, down)
        neighbours.forEach {
            this.mapPlot(it, id)
        }
    }

    fun List<Node>.calculateFence(plot: List<Node>): Int {
        val plotId = plot[0].plotID
        return when (plot.size) {
            0 -> 0
            1 -> 4
            2 -> 6
            3 -> 8
            else -> {
                var sum = 0
                this.forEach {
                    if (it.plotID != plotId) {
                        sum += this.calculateNeighbours(it, plotId)
                    }
                }
                sum
            }
        }
    }

    fun List<Node>.calculateDiscountFence(plot: List<Node>): Int {
        return when (plot.size) {
            0 -> 0
            1 -> 4
            2 -> 4
            else -> {
                var sum = 0
                plot.forEach {
                    sum += this.calculateCornerNeighbours(it)
                }
                sum
            }
        }
    }

    fun part1(input: List<String>): Int {
        var sum = 0

        val grid = mutableListOf<Node>()
        for (y in 0..input.size + 1) {
            for (x in 0..input[0].length + 1) {
                if (y == 0 || y == input.size + 1 || x == 0 || x == input[0].length + 1) {
                    grid.add(Node(x, y, '.'))
                } else {
                    grid.add(Node(x, y, input[y - 1][x - 1]))
                }
            }
        }

        var plotId = 1
        while (true) {
            val nextPlant = grid.find { it.plotID == 0 && it.plant != '.' }
            if (nextPlant != null) {
                grid.mapPlot(nextPlant, plotId)
                plotId++
            } else {
                break
            }
        }

        val plots = grid.filterNot { it.plotID == 0 }.groupBy { it.plotID }

        plots.values.forEach { plot ->
            sum += plot.size * grid.calculateFence(plot)
        }

        return sum
    }

    fun part2(input: List<String>): Int {
        var sum = 0

        val grid = mutableListOf<Node>()
        for (y in 0..input.size + 1) {
            for (x in 0..input[0].length + 1) {
                if (y == 0 || y == input.size + 1 || x == 0 || x == input[0].length + 1) {
                    grid.add(Node(x, y, '.'))
                } else {
                    grid.add(Node(x, y, input[y - 1][x - 1]))
                }
            }
        }

        var plotId = 1
        while (true) {
            val nextPlant = grid.find { it.plotID == 0 && it.plant != '.' }
            if (nextPlant != null) {
                grid.mapPlot(nextPlant, plotId)
                plotId++
            } else {
                break
            }
        }

        val plots = grid.filterNot { it.plotID == 0 }.groupBy { it.plotID }

        plots.values.forEach { plot ->
            sum += plot.size * grid.calculateDiscountFence(plot)
        }

        return sum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput(pkg = "day12", name = "Day12_test")
    check(part1(testInput) == 1930)
    check(part2(testInput) == 1206)

    val input = readInput(pkg = "day12", name = "Day12")
    part1(input).println()
    part2(input).println()
}
