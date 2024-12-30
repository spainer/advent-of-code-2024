package day08

import readInput

private fun part1(map: AntennaMap): Int {
    val antinodes = map.antenna.values.asSequence()
        .flatMap { it cross it }
        .filter { it.first != it.second }
        .map { antennas -> 2 * antennas.second - antennas.first }
        .filter { it.x in 0..<map.size.first && it.y in 0..<map.size.second }
        .toSet()
    return antinodes.size
}

private fun part2(map: AntennaMap): Int {
    val antinodes = map.antenna.values.asSequence()
        .flatMap { it cross it }
        .filter { it.first != it.second }
        .flatMap {
            val diff = it.second - it.first
            generateSequence(it.first + diff) { acc -> acc + diff }
                .takeWhile { acc -> acc.x in 0..<map.size.first && acc.y in 0..<map.size.second }
        }
        .toSet()
    return antinodes.size
}

private data class Vector(val x: Int, val y: Int)

private operator fun Vector.plus(v: Vector) = Vector(x + v.x, y + v.y)

private operator fun Vector.minus(v: Vector) = Vector(x - v.x, y - v.y)

private operator fun Int.times(v: Vector) = Vector(this * v.x, this * v.y)

private data class AntennaMap(
    val size: Pair<Int, Int>,
    val antenna: Map<Char, List<Vector>>
)

private fun List<String>.parse(): AntennaMap {
    val rows = this.size
    val cols = this[0].length

    val antenna = mutableMapOf<Char, MutableList<Vector>>()
    this.forEachIndexed { rowIdx, line ->
        line.withIndex()
            .filter { it.value != '.' }
            .map { it.value to Vector(it.index, rowIdx) }
            .groupByTo(antenna, { it.first }, { it.second })
    }

    return AntennaMap(cols to rows, antenna.mapValues { it.value.toList() })
}

private infix fun <T> Collection<T>.cross(other: Collection<T>): List<Pair<T, T>> {
    val result = mutableListOf<Pair<T, T>>()
    for (v1 in this) {
        for (v2 in other) {
            result.add(v1 to v2)
        }
    }
    return result.toList()
}

fun main() {
    val testMap = readInput("day08/Day08_test").parse()
    check(part1(testMap) == 14)
    check(part2(testMap) == 34)

    val data = readInput("day08/Day08").parse()
    println("Part 1: ${part1(data)}")
    println("Part 2: ${part2(data)}")
}