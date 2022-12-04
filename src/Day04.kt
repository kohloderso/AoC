fun main() {

    fun part1(input: List<Pair<IntRange, IntRange>>): Int {
        val containedRanges = input.filter { (first, second) ->
            first.all { second.contains(it) } || second.all { first.contains(it) }
        }
        return containedRanges.size
    }

    fun part2(input: List<Pair<IntRange, IntRange>>): Int {
        val containedRanges = input.filter { (first, second) ->
            first.any { second.contains(it) }
        }
        return containedRanges.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = parseSectionPairs("Day04_test")
    println(testInput)

    check(part1(testInput) == 2)

    val input = parseSectionPairs("Day04")
    println(part1(input))
    println(part2(input))
}
