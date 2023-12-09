package solutions23


class EngineScheme(partNumbers: List<Int>) {

    companion object {
        //anything that is not a dot or a digit is a symbol
        private fun isSymbol(c: Char): Boolean = c != '.' && c !in '0'..'9'

        private fun collectNeighbourParts(scheme: List<String>, i: Int, j: Int): Set<Triple<Int, Int, Int>> {
            val result = mutableSetOf<Triple<Int, Int, Int>>()
            for (i in i-1..i+1) {
               for (j in j-1..j+1) {
                   if (i >= 0 && i < scheme.size && j >= 0 && j < scheme.size) {
                       if (scheme[i][j].isDigit()) {
                           //find first digit on the left belonging to this number
                           var k = j
                           while (k >= 0 && scheme[i][k].isDigit()) {
                               k -= 1
                           }
                           //find last digit on the right belonging to this number
                           var l = j
                           while (l < scheme[i].length && scheme[i][l].isDigit()) {
                               l += 1
                           }
                           result.add(Triple(i, k+1, l))
                       }
                   }
               }
            }
            return result
        }

        fun create(content: List<String>): EngineScheme {
            val partIds = mutableSetOf<Triple<Int, Int, Int>>()
            content.indices.forEach { i ->
                content[i].indices.forEach { j ->
                    if (isSymbol(content[i][j])) {
                        partIds.addAll(collectNeighbourParts(content, i, j))
                    }
                }
            }
            val parts = partIds.toList().map { content[it.first].substring(it.second, it.third).toInt() }
            return EngineScheme(parts)
        }

        private fun getNumber(scheme: List<String>, triple: Triple<Int, Int, Int>): Int {
            return scheme[triple.first].substring(triple.second, triple.third).toInt()
        }

        private fun collectGearRatio(scheme: List<String>, i: Int, j: Int): Int {
            val result = mutableSetOf<Triple<Int, Int, Int>>()
            for (i in i-1..i+1) {
                for (j in j-1..j+1) {
                    if (i >= 0 && i < scheme.size && j >= 0 && j < scheme.size) {
                        if (scheme[i][j].isDigit()) {
                            //find first digit on the left belonging to this number
                            var k = j
                            while (k >= 0 && scheme[i][k].isDigit()) {
                                k -= 1
                            }
                            //find last digit on the right belonging to this number
                            var l = j
                            while (l < scheme[i].length && scheme[i][l].isDigit()) {
                                l += 1
                            }
                            result.add(Triple(i, k+1, l))
                        }
                    }
                }
            }
            if(result.size == 2) {
                return getNumber(scheme, result.first()) * getNumber(scheme, result.last())
            } else return 0
        }



        fun computeGearRatios(content: List<String>): Int {
            var sum = 0
            content.indices.forEach { i ->
                content[i].indices.forEach { j ->
                    if (content[i][j]=='*') {
                        sum += collectGearRatio(content, i, j)
                    }
                }
            }
            return sum
        }
    }

    val sum = partNumbers.sum()
}

fun main() {

    fun part1(input: List<String>): Int {
        val scheme = EngineScheme.create(input)
        return scheme.sum
    }

    fun part2(input: List<String>): Int = EngineScheme.computeGearRatios(input)

    // test if implementation meets criteria from the description:
    val testInput = parseLines("Day03_test")
    check(part1(testInput) == 4361)
    check(part2(testInput) == 467835)

    val input = parseLines("Day03")
    println(part1(input))
    println(part2(input))
}
