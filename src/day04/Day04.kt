package day04

import readInput

private fun part1(input: List<String>): Int {
    val limits = Coordinate(input[0].length, input.size)

    var sum = 0
    for (yStart in 0..<limits.y) {
        for (xStart in 0 ..<limits.x) {
            for (dir in Direction.entries) {
                val str = generateSequence(Coordinate(xStart, yStart)) { it.next(dir) }
                    .takeWhile { it in limits }
                    .take(4)
                    .map { input.get(it) }
                    .joinToString("")
                if (str == "XMAS") sum++
            }
        }
    }
    return sum
}

private fun part2(input: List<String>): Int {
    val limits = Coordinate(input[0].length, input.size)

    var sum = 0
    for (yStart in 1..<limits.y-1) {
        for (xStart in 1..<limits.x-1) {
            val coord = Coordinate(xStart, yStart)
            if (input.get(coord) != 'A') continue
            val leftUpper = input.get(coord.next(Direction.UP_LEFT))
            val rightUpper = input.get(coord.next(Direction.UP_RIGHT))
            val rightLower = input.get(coord.next(Direction.DOWN_RIGHT))
            val leftLower = input.get(coord.next(Direction.DOWN_LEFT))
            val dir1 = (leftUpper == 'M' && rightLower =='S') || (leftUpper == 'S' && rightLower =='M')
            val dir2 = (leftLower == 'M' && rightUpper =='S') || (leftLower == 'S' && rightUpper =='M')
            if (dir1 && dir2) sum++
        }
    }
    return sum
}

private enum class Direction {
    UP, UP_RIGHT, RIGHT, DOWN_RIGHT, DOWN, DOWN_LEFT, LEFT, UP_LEFT
}

private data class Coordinate(val x: Int, val y: Int)

private fun Coordinate.next(direction: Direction) = when (direction) {
    Direction.UP -> Coordinate(x, y - 1)
    Direction.UP_RIGHT -> Coordinate(x + 1, y - 1)
    Direction.RIGHT -> Coordinate(x + 1, y)
    Direction.DOWN_RIGHT -> Coordinate(x + 1, y + 1)
    Direction.DOWN -> Coordinate(x, y + 1)
    Direction.DOWN_LEFT -> Coordinate(x - 1, y + 1)
    Direction.LEFT -> Coordinate(x - 1, y)
    Direction.UP_LEFT -> Coordinate(x - 1, y - 1)
}

private fun Coordinate.limit(max: Coordinate): Coordinate {
    var newX = x
    var newY = y
    if (x < 0) {
        newX = max.x - 1
    } else if (x >= max.x) {
        newX = 0
    }
    if (y < 0) {
        newY = max.y - 1
    } else if (y >= max.y) {
        newY = 0
    }
    return Coordinate(newX, newY)
}

private operator fun Coordinate.contains(other: Coordinate): Boolean {
    return other.x in 0..<x && other.y in 0..<y
}

private fun List<String>.get(coordinate: Coordinate) = this[coordinate.y][coordinate.x]

fun main() {
    val testInput = readInput("day04/Day04_test")
    check(part1(testInput) == 18)
    
    val input = readInput("day04/Day04")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}