package day05

import readInput

private fun part1(updates: List<List<Int>>, rules: List<OrderingRule>): Int {
    val validUpdates = updates.filter { it.isValid(rules) }
    val middles = validUpdates.map { it.middle }
    return middles.sum()
}

private fun part2(updates: List<List<Int>>, rules: List<OrderingRule>): Int {
    val incorrect = updates.filter { !it.isValid(rules) }
    val corrected = incorrect.map { it.correct(rules) }
    return corrected.sumOf { it.middle }
}

private typealias OrderingRule = Pair<Int, Int>

private val <T> List<T>.middle: T get() = this[size / 2]

private fun List<String>.parse(): Pair<List<List<Int>>, List<OrderingRule>> {
    val top = takeWhile { it.isNotEmpty() }
    val bottom = takeLastWhile { it.isNotEmpty() }

    val rules = top.map { line -> line.split('|').let { it[0].toInt() to it[1].toInt() } }
    val updates = bottom.map { line -> line.split(',').map { it.toInt() } }
    return updates to rules
}

private fun List<Int>.complies(rule: OrderingRule): Boolean {
    val firstIdx = indexOf(rule.first)
    val secondIdx = indexOf(rule.second)
    return firstIdx < 0 || secondIdx < 0 || firstIdx < secondIdx
}

private fun List<Int>.isValid(rules: List<OrderingRule>) = rules.all { this.complies(it) }

private fun List<Int>.correct(rules: List<OrderingRule>): List<Int> {
    val result = this.toMutableList()
    while (true) {
        val rule = rules.firstOrNull { !result.complies(it) } ?: return result.toList()
        val firstIdx = result.indexOf(rule.first)
        val secondIdx = result.indexOf(rule.second)
        val first = result[firstIdx]
        result[firstIdx] = result[secondIdx]
        result[secondIdx] = first
    }
}

fun main() {
    val (testUpdates, testRules) = readInput("day05/Day05_test").parse()
    check(part1(testUpdates, testRules) == 143)
    check(part2(testUpdates, testRules) == 123)

    val (updates, rules) = readInput("day05/Day05").parse()
    println("Part 1: ${part1(updates, rules)}")
    println("Part 2: ${part2(updates, rules)}")
}