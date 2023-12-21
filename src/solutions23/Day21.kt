package solutions23

fun main() {

    // return start point as index of distArray
    fun findStartPoint(gardenGrid: List<String>): Pair<Int, Int> {
        for (i in gardenGrid.indices) {
            for (j in gardenGrid[0].indices) {
                if(gardenGrid[i][j] == 'S') {
                    return i to j
                }
            }
        }
        return -1 to -1
    }

    fun part1(input: List<String>, steps: Int): Int {
        val gardenGrid = input.map { line -> line.map { when(it) {'#' -> false; else -> true} }.toTypedArray() }.toTypedArray()
        val startPoint = findStartPoint(input)
        // compute shortest paths
        val distArray = dijkstra(gardenGrid, startPoint.first, startPoint.second)
        // count all even values which are <= steps and have same parity as steps
        val result = distArray.sumOf { it.count { d -> d <= steps && d % 2 == steps % 2 } }
        return result
    }

    fun part2(input: List<List<String>>): Long {
       return 0
    }

    val testInput = parseLines("Day21_test")

    check(part1(testInput, 6) == 16)
    //check(part2(testInput) == 167409079868000L)

    val input = parseLines("Day21")
    println(part1(input, 64))
    //println(part2(input))
}
