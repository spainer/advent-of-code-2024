package day09

import readInput

private fun part1(layout: List<DiskBlock>): Long {
    val split = layout.flatMap { it.split() }
    val defrag = split.defrag()
    return defrag.checksum()
}

private fun part2(layout: List<DiskBlock>): Long {
    return layout.defrag().checksum()
}

private data class DiskBlock(val id: Long, val start: Int, val size: Int)

private fun DiskBlock.split(): List<DiskBlock> {
    return generateSequence(start) { it + 1 }.take(size).map { DiskBlock(id, it, 1) }.toList()
}

private fun String.parse(): List<DiskBlock> {
    val result = mutableListOf<DiskBlock>()
    var start = 0
    this.forEachIndexed { index, c ->
        val size = c.digitToInt()
        if (index % 2 == 0) {
            result += DiskBlock(index / 2L, start, size)
        }
        start += size
    }
    return result.toList()
}

private fun List<DiskBlock>.firstSpaceOfSize(size: Int): Int {
    return this.withIndex().zipWithNext().firstOrNull { (first, second) ->
        second.value.start - (first.value.start + first.value.size) >= size
    }?.first?.index ?: -1
}

private fun List<DiskBlock>.defrag(): List<DiskBlock> {
    val result = toMutableList()
    var idx = result.lastIndex
    var lastMovedId = result.maxOf { it.id ?: 0 }
    while (idx > 0) {
        val block = result[idx]
        if (block.id > lastMovedId) {
            idx--
            continue
        }
        lastMovedId = block.id

        val spaceIdx = result.firstSpaceOfSize(block.size)
        if (spaceIdx < 0 || spaceIdx >= idx) {
            idx--
            continue
        }

        val blockBefore = result[spaceIdx]
        result.removeAt(idx)
        result.add(spaceIdx + 1, DiskBlock(block.id, blockBefore.start + blockBefore.size, block.size))
    }
    return result.toList()
}

private fun List<DiskBlock>.checksum(): Long {
    var sum = 0L
    for (block in this) {
        sum += generateSequence(block.start) { it + 1 }.take(block.size).sumOf { it * block.id }
    }
    return sum
}

fun main() {
    val testInput = readInput("day09/Day09_test").first().parse()
    check(part1(testInput) == 1928L)
    check(part2(testInput) == 2858L)

    val input = readInput("day09/Day09").first().parse()
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}