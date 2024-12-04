
private fun part1(program: String): Int {
    return mulRegex1.findAll(program).sumOf { match ->
        match.groupValues[1].toInt() * match.groupValues[2].toInt()
    }
}

private fun part2(program: String): Int {
    var active = true
    return mulRegex2.findAll(program).sumOf { match ->
        when (match.value) {
            "do()" -> {
                active = true
                0
            }
            "don't()" -> {
                active = false
                0
            }
            else -> if (active) match.groupValues[1].toInt() * match.groupValues[2].toInt() else 0
        }
    }
}

private val mulRegex1 = """mul\((\d+),(\d+)\)""".toRegex()
private val mulRegex2 = """do\(\)|don't\(\)|mul\((\d+),(\d+)\)""".toRegex()

fun main() {
    val testInput1 = readInput("Day03_test").first()
    val testInput2 = readInput("Day03_test")[1]
    check(part1(testInput1) == 161)
    check(part2(testInput2) == 48)

    val input = readInput("Day03").first()
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}