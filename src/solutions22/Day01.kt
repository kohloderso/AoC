package solutions22

fun main() {

    fun part1(input: List<List<Int>>): Int {
        val sums = IntArray(input.size)
        // sum up all sublists
        input.withIndex().forEach {
            sums[it.index] = it.value.sum()
        }
        return sums.max()
    }

    fun part2(input: List<List<Int>>): Int {
        val sums = IntArray(input.size)
        // sum up all sublists
        input.withIndex().forEach {
            sums[it.index] = it.value.sum()
        }
        var top3calories = 0
        for (i in 0 until 3) {
            val maxIdx = sums.indices.maxBy { sums[it] }
            top3calories += sums[maxIdx]
            sums[maxIdx] = 0
        }
        return top3calories
    }

    // test if implementation meets criteria from the description, like:
    //val testInput = readInputChunks("Day01_test")

    //check(part2(testInput) == 45000)

    val input = parseIntChunks("Day01")
    //println(part1(input))
    println(part2(input))
}
