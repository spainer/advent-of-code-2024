package day06

import readInput

private fun part1(board: Board): Int {
    return board.guardSeqeuence
        .map { it.first }
        .takeWhile { it in board }
        .distinct()
        .count()
}

private fun part2(board: Board): Int {
    var loopPositions = 0
    for (y in board.indices) {
        for (x in board[0].indices) {
            if (board.get(Coordinate(x, y)) == Field.START) continue
            val otherBoard = board.set(Coordinate(x, y), Field.BLOCKED)
            val seq = otherBoard.guardSeqeuence.takeWhile { it.first in otherBoard }
            if (seq.hasLoop()) loopPositions++
        }
    }
    return loopPositions
}

private enum class Field {
    EMPTY, START, BLOCKED
}

private enum class Direction {
    UP, RIGHT, DOWN, LEFT
}

private data class Coordinate(val x: Int, val y: Int)

private typealias Board = List<List<Field>>

private typealias GuardPosition = Pair<Coordinate, Direction>

private fun Board.get(coordinate: Coordinate) = if (coordinate in this) this[coordinate.y][coordinate.x] else null

private val Board.start: Coordinate get() {
    val rowIndex = indexOfFirst { it.contains(Field.START) }
    val colIndex = this[rowIndex].indexOf(Field.START)
    return Coordinate(colIndex, rowIndex)
}

private val Board.guardSeqeuence: Sequence<GuardPosition> get() = generateSequence(start to Direction.UP) { last ->
    var nextDirection = last.second
    var nextCoordinate = last.first.next(nextDirection)
    while (get(nextCoordinate) == Field.BLOCKED) {
        nextDirection = when(nextDirection) {
            Direction.UP -> Direction.RIGHT
            Direction.RIGHT -> Direction.DOWN
            Direction.DOWN -> Direction.LEFT
            Direction.LEFT -> Direction.UP
        }
        nextCoordinate = last.first.next(nextDirection)
    }
    nextCoordinate to nextDirection
}

private operator fun Board.contains(coordinate: Coordinate): Boolean {
    if (coordinate.y !in indices) return false
    return coordinate.x in this[0].indices
}

private fun Board.set(coordinate: Coordinate, field: Field): Board {
    val linesBefore = this.subList(0, coordinate.y)
    val linesAfter = this.subList(coordinate.y + 1, this.size)

    val line = this[coordinate.y]
    val colsBefore = line.subList(0, coordinate.x)
    val colsAfter = line.subList(coordinate.x + 1, line.size)
    val changedLine = colsBefore + field + colsAfter

    return linesBefore + listOf(changedLine) + linesAfter
}

private fun Coordinate.next(direction: Direction) = when (direction) {
    Direction.UP -> Coordinate(x, y - 1)
    Direction.RIGHT -> Coordinate(x + 1, y)
    Direction.DOWN -> Coordinate(x, y + 1)
    Direction.LEFT -> Coordinate(x - 1, y)
}

private fun Sequence<GuardPosition>.hasLoop(): Boolean {
    val oldPositions = mutableSetOf<GuardPosition>()
    val it = iterator()
    while (it.hasNext()) {
        if (!oldPositions.add(it.next())) return true
    }
    return false
}

private fun List<String>.parse(): Board = map { row ->
    row.map { when (it) {
        '.' -> Field.EMPTY
        '^' -> Field.START
        '#' -> Field.BLOCKED
        else -> error("Unexpected field: $it")
    }}
}

fun main() {
    val testInput = readInput("day06/Day06_test").parse()
    check(part1(testInput) == 41)
    check(part2(testInput) == 6)

    val input = readInput("day06/Day06").parse()
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}