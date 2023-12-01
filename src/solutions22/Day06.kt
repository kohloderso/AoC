package solutions22

fun main() {

    fun part1(input: String): Int {
        for (i in 3 until input.length){
            if(input.substring(i-3 .. i).groupBy {it}.size == 4) {
                return i + 1
            }
        }
        return -1
    }

    fun part2(input: String): Int  {
        for (i in 13 until input.length){
            if(input.substring(i-13 .. i).groupBy {it}.size == 14) {
                return i + 1
            }
        }
        return -1
    }

    val testInput = parseLines("Day06_test")
    check(part1(testInput[0]) == 7)
    check(part1(testInput[1]) == 5)
    check(part1(testInput[2]) == 6)
    check(part1(testInput[3]) == 10)
    check(part1(testInput[4]) == 11)

    val input = parseLines("Day06")
    println(part1(input[0]))

    check(part2(testInput[0]) == 19)
    check(part2(testInput[1]) == 23)
    check(part2(testInput[2]) == 23)
    check(part2(testInput[3]) == 29)
    check(part2(testInput[4]) == 26)

    println(part2(input[0]))
}
