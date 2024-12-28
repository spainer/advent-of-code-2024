package day02

import readInput
import kotlin.math.abs

private fun part1(reports: List<List<Int>>) = reports.count { it.isSafe }

private fun part2(reports: List<List<Int>>) = reports.count { it.isSafe || it.isCorrectable }

private fun List<String>.toReports(): List<List<Int>> = map { line ->
    line.split(" ").map { it.toInt() }
}

private val List<Int>.isSafe: Boolean get() {
    val diffs = zipWithNext().map { it.second - it.first }
    return diffs.all { abs(it) in 1..3 } && (diffs.all { it > 0 } || diffs.all { it < 0 })
}

private val List<Int>.isCorrectable: Boolean get()  = indices.any { (subList(0, it) + subList(it + 1, size)).isSafe }

fun main() {
    val testInput = readInput("day02/Day02_test")
    val testReports = testInput.toReports()
    check(part1(testReports) == 2)
    check(part2(testReports) == 4)

    val input = readInput("day02/Day02")
    val reports = input.toReports()

    println("Part 1: ${part1(reports)}")
    println("Part 2: ${part2(reports)}")
}