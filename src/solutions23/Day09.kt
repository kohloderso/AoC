package solutions23



fun main() {

    fun nextSeq(seq: List<Int>): List<Int> = seq.zip(seq.drop(1)).map { it.second - it.first }

    fun computeNextVal(seq: List<Int>): Int {
        if (seq.all { it == 0 }) {
            return 0
        } else {
            val nextVal = computeNextVal(nextSeq(seq))
            return nextVal + seq.last()
        }
    }

    fun computePreviousVal(seq: List<Int>): Int {
        if (seq.all { it == 0 }) {
            return 0
        } else {
            val prevVal = computePreviousVal(nextSeq(seq))
            return seq.first() - prevVal
        }
    }

    fun parseSequences(input: List<String>): List<List<Int>> =
        input.map { it.split(" ").filterNot { it == " " }.map { it.toInt() } }

    fun part1(input: List<String>): Int {
        val sequences = parseSequences(input)
        val nextVals = sequences.map { computeNextVal(it) }
        return nextVals.sum()
    }

    fun part2(input: List<String>): Int {
        val sequences = parseSequences(input)
        val prevVals = sequences.map { computePreviousVal(it) }
        return prevVals.sum()
    }

    val testInput = parseLines("Day09_test")
    check(part1(testInput) == 114)
    check(part2(testInput) == 2)

    val input = parseLines("Day09")
    println(part1(input))
    println(part2(input))
}
