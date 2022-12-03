fun main() {

    fun computeValue(c: Char) : Int {
        val code = c.code
        val prio = if (code <= 'Z'.code) code - 'A'.code + 27 else code - 'a'.code + 1
        return prio
    }

    fun checkRucksack(rucksack: Pair<String, String>) : Int {
        val char = rucksack.first.find {
            rucksack.second.contains(it)
        } ?: ' '
        return computeValue(char)
    }

    fun checkRucksackGroup(group: List<String>) : Int {
        val char = group.first().find {c ->
            group.all {rucksack -> rucksack.contains(c) }
        } ?: ' '
        return computeValue(char)
    }

    fun part1(input: List<Pair<String, String>>): Int {
        return input.fold(0) {sum, rucksack -> sum + checkRucksack(rucksack)}
    }

    fun part2(input: List<String>): Int {
        val groups = input.chunked(3)
        return groups.fold(0) {sum, group -> sum + checkRucksackGroup(group)}
    }



//    fun part2(input: List<Pair<Int, Int>>): Int {
//        return input.fold(0) {sum, round ->
//            val self = determineRPC(round.first, round.second)
//            sum + scoreRound(round.first, self)}
//    }

    // test if implementation meets criteria from the description, like:
    val testInput = parseLines("Day03_test")
    check(checkRucksack(Pair("abL", "dLfC")) == 38)


    check(part2(testInput) == 70)

    val input = parseLines("Day03")
   // println(part1(input))
    println(part2(input))
}
