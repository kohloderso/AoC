package solutions23

fun main() {

    fun getFirstInt(s: String): Int = s.first { it.isDigit() }.digitToInt()

    fun getNumber(s: String): Int = getFirstInt(s)*10 + getFirstInt(s.reversed())

    fun part1(input: List<String>): Int {
        val sums = IntArray(input.size)
        input.withIndex().forEach {
            sums[it.index] = getNumber(it.value)
        }
        return sums.sum()
    }

    // test if implementation meets criteria from the description:
    val testInput = parseLines("Day01_test")
    check(part1(testInput) == 142)

    //check(part2(testInput) == 45000)

    val input = parseLines("Day01")
    println(part1(input))
    //println(part2(input))
}
