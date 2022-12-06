fun main() {

    fun scoreRound(opponent: Int, self: Int): Int {
        val score =
            when (self) {
                opponent -> 3
                opponent % 3 + 1 -> 6
                else -> 0
            }
        return (score + self)
    }

    fun determineRPC(opponent: Int, outcome: Int) =
        when (outcome) {
            2 -> opponent
            3 -> opponent % 3 + 1
            else -> if(opponent >= 2) opponent - 1 else 3
        }

    fun part1(input: List<Pair<Int, Int>>): Int {
        return input.fold(0) {sum, round -> sum + scoreRound(round.first, round.second)}
    }

    fun part2(input: List<Pair<Int, Int>>): Int {
        return input.fold(0) {sum, round ->
            val self = determineRPC(round.first, round.second)
            sum + scoreRound(round.first, self)}
    }


    val testInput = parseRPC("Day02_test")


    check(scoreRound(2,1) == 1)
    check(scoreRound(1,2) == 8)
    check(scoreRound(1, 3) == 3)
    check(scoreRound(2, 3) == 9)
    check(scoreRound(3, 2) == 2)
    check(scoreRound(3, 1) == 7)
    check(scoreRound(1, 1) == 4)
    check(scoreRound(2, 2) == 5)
    check(scoreRound(3, 3) == 6)

    check(part2(testInput) == 12)

    val input = parseRPC("Day02")
    //println(part1(input))
    println(part2(input))
}
