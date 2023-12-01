package solutions23

fun main() {

    fun parseDigit(s: String): Int? {
        return if(s.startsWith("zero", true)) 0
        else if (s.startsWith("one", true)) 1
        else if (s.startsWith("two", true)) 2
        else if (s.startsWith("three", true)) 3
        else if (s.startsWith("four", true)) 4
        else if (s.startsWith("five", true)) 5
        else if (s.startsWith("six", true)) 6
        else if (s.startsWith("seven", true)) 7
        else if (s.startsWith("eight", true)) 8
        else if (s.startsWith("nine", true)) 9
        else null
    }


    fun getRealFirstDigit(s: String): Int {
        val firstDigitId = s.indices.firstOrNull { s[it].isDigit() }
        val firstTextDigitId = s.indices.firstOrNull { parseDigit(s.substring(it)) != null }
        if(firstDigitId == null || (firstTextDigitId!= null && firstTextDigitId < firstDigitId))
            return parseDigit(s.substring(firstTextDigitId!!))!!
        else return s[firstDigitId].digitToInt()
    }

    fun getRealSecondDigit(s: String): Int {
        val secondDigitId = s.indices.lastOrNull { s[it].isDigit() }
        val secondTextDigitId = s.indices.lastOrNull { parseDigit(s.substring(it)) != null }
        if(secondDigitId == null || (secondTextDigitId!= null && secondTextDigitId > secondDigitId))
            return parseDigit(s.substring(secondTextDigitId!!))!!
        else return s[secondDigitId].digitToInt()
    }

    fun getFirstInt(s: String): Int = s.first { it.isDigit() }.digitToInt()

    fun getNumber(s: String): Int = getFirstInt(s)*10 + getFirstInt(s.reversed())

    fun getRealNumber(s: String): Int = getRealFirstDigit(s)*10 + getRealSecondDigit(s)

    fun part1(input: List<String>): Int {
        val res = IntArray(input.size)
        input.withIndex().forEach {
            res[it.index] = getNumber(it.value)
        }
        return res.sum()
    }

    fun part2(input: List<String>): Int {
        val res = IntArray(input.size)
        input.withIndex().forEach {
            res[it.index] = getRealNumber(it.value)
        }
        return res.sum()
    }

    // test if implementation meets criteria from the description:
    val testInput = parseLines("Day01_test")
    check(part1(testInput) == 142)
    val testInput2 = parseLines("Day01_test2")
    check(part2(testInput2) == 281)

    val input = parseLines("Day01")
    println(part1(input))
    println(part2(input))
}
