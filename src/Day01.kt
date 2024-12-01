import kotlin.math.abs

private fun part1(left: List<Int>, right: List<Int>): Int {
    return left.sorted().zip(right.sorted())
        .sumOf { abs(it.first - it.second) }
}

private fun part2(left: List<Int>, right: List<Int>): Int {
    val amounts = right.groupBy { it }
        .mapValues { (_, v) -> v.size }

    return left.sumOf { it * (amounts[it] ?: 0) }
}

private fun List<String>.toLocationLists(): Pair<List<Int>, List<Int>> {
    val pairs = map { it.split("\\s+".toRegex()) }
        .map { arr -> arr.map { it.toInt() } }

    val left = pairs.map { it[0] }
    val right = pairs.map { it[1] }

    return left to right
}

fun main() {
    val testInput = readInput("Day01_test")
    val (testLeft, testRight) = testInput.toLocationLists()
    check(part1(testLeft, testRight) == 11)

    val input = readInput("Day01")
    val (left, right) = input.toLocationLists()

    println("Result part 1: ${part1(left, right)}")
    println("Result part 2: ${part2(left, right)}")
}
