package day09

import println
import readInput
import java.math.BigInteger

fun main() {

    fun part1(input: List<String>): BigInteger {
        var sum = BigInteger.ZERO

        val list = mutableListOf<String>()
        input[0].map(Char::digitToInt).forEachIndexed { index, digit ->
            if (index.mod(2) == 0) {
                list.addAll(MutableList(digit) { (index / 2).toString() })
            } else {
                list.addAll(MutableList(digit) { "." })
            }
        }

        var j = list.size - 1
        list.forEachIndexed { index, n ->
            if (n == "." && index < j) {
                while (true) {
                    val lastString = list[j]
                    j--
                    if (lastString != ".") {
                        list[index] = lastString
                        list[j + 1] = "."
                        break
                    }
                }
            }
        }

        list.filterNot { it == "." }.forEachIndexed { index, s ->
            sum += (BigInteger.valueOf(index.toLong()) * BigInteger.valueOf(s.toLong()))
        }

        return sum
    }

    fun List<String>.findLastFreeBlock(size: Int): Pair<Int, Int> {
        var currentSize = 0
        for (j in this.size - 1 downTo 0) {
            if (this[j] == ".") {
                currentSize++
            } else {
                currentSize = 0
            }
            if (currentSize == size) {
                return j to j + size - 1
            }
        }
        return -1 to -1
    }

    fun MutableList<String>.swap(sourceIndex: Int, destinationIndex: Int, length: Int) {
        val tempList = MutableList(length) { this[sourceIndex] }
        for (i in sourceIndex until sourceIndex + length) {
            this[i] = this[destinationIndex]
        }
        for (i in destinationIndex until destinationIndex + length) {
            this[i] = tempList[0]
        }
    }

    fun part2(input: List<String>): BigInteger {
        var sum = BigInteger.ZERO

        val list = mutableListOf<String>()
        input[0].map(Char::digitToInt).forEachIndexed { index, digit ->
            if (index.mod(2) == 0) {
                list.addAll(MutableList(digit) { (index / 2).toString() })
            } else {
                list.addAll(MutableList(digit) { "." })
            }
        }

        val reversedList = list.asReversed()
        var i = 0
        while (i < reversedList.size) {
            val n = reversedList[i]
            if (n != ".") {
                val until = reversedList.lastIndexOf(n)
                val blockLength = until - i + 1
                val lastFreeBlock = reversedList.findLastFreeBlock(blockLength)
                if (lastFreeBlock != -1 to -1 && lastFreeBlock.first > i) {
                    reversedList.swap(i, lastFreeBlock.first, blockLength)
                }
                i = until + 1
            } else {
                i++
            }
        }

        reversedList.reversed().forEachIndexed { index, s ->
            if (s != ".") {
                sum += (BigInteger.valueOf(index.toLong()) * BigInteger.valueOf(s.toLong()))
            }
        }

        return sum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput(pkg = "day09", name = "Day09_test")
    check(part1(testInput) == BigInteger.valueOf(1928))
    check(part2(testInput) == BigInteger.valueOf(2858))

    val input = readInput(pkg = "day09", name = "Day09")
    part1(input).println()
    part2(input).println()
}
