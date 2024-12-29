package day07

import readInput

private fun part1(equations: List<Equation>): Long {
    val operators = setOf(Operator.PLUS, Operator.MULTIPLY)
    return equations
        .filter { it.result in it.operands.possibleResults(operators) }
        .sumOf { it.result }
}

private fun part2(equations: List<Equation>): Long {
    val operators = setOf(Operator.PLUS, Operator.MULTIPLY, Operator.CONCATENATE)
    return equations
        .filter { it.result in it.operands.possibleResults(operators) }
        .sumOf { it.result }
}

private enum class Operator {
    PLUS, MULTIPLY, CONCATENATE
}

private data class Equation(val result: Long, val operands: List<Long>)

private fun List<String>.parseEquations(): List<Equation> = map { line ->
    val (resultStr, operandsStr) = line.split(':')
    Equation(
        resultStr.toLong(),
        operandsStr.trim().split(' ').map { it.toLong() }
    )
}

private fun List<Long>.possibleResults(operators: Set<Operator>): Set<Long> {
    val initial = setOf(this[0])
    return slice(1..<size).fold(initial) { acc, value -> buildSet {
        for (operator in operators) {
            val results = when (operator) {
                Operator.PLUS -> acc.map { it + value }
                Operator.MULTIPLY -> acc.map { it * value }
                Operator.CONCATENATE -> acc.map { "$it$value".toLong() }
            }
            addAll(results)
        }
    }}
}

fun main() {
    val testEquations = readInput("day07/Day07_test").parseEquations()
    check(part1(testEquations) == 3749L)
    check(part2(testEquations) == 11387L)

    val equations = readInput("day07/Day07").parseEquations()
    println("Part 1: ${part1(equations)}")
    println("Part 2: ${part2(equations)}")
}