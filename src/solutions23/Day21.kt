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
        val distArray = dijkstra(gardenGrid, ArrayDeque(setOf(Triple(0,startPoint.first, startPoint.second))))
        // count all even values which are <= steps and have same parity as steps
        val result = distArray.sumOf { it.count { d -> d <= steps && d % 2 == steps % 2 } }
        return result
    }

    fun part2(input: List<String>, steps: Int): Long {
        val gardenGrid = input.map { line -> line.map { when(it) {'#' -> false; else -> true} }.toTypedArray() }.toTypedArray()
        val visitedGrids = mutableSetOf<Pair<Int, Int>>()
        var counter = 0L
        val startPoint = findStartPoint(input)

        fun considerGrid(i: Int, j: Int, initValues: ArrayDeque<Triple<Int, Int, Int>>) {
            if(visitedGrids.contains(i to j)) return
            // compute distances for this grid
            val distArray = dijkstra(gardenGrid, initValues)
            // count all even values which are <= steps and have same parity as steps
            val result = distArray.sumOf { it.count { d -> d <= steps && d % 2 == steps % 2 } }
            counter += result
            visitedGrids.add(i to j)
            if(result == 0) return // no chance of finding new solutions here
            // consider neighbours
            if(i >= 0 && !visitedGrids.contains(i+1 to j)) { // check down
                // the top row can be initialized with the bottom row of the current grid + 1
                val nextInit = ArrayDeque<Triple<Int, Int, Int>>()
                for(col in distArray.indices) {
                    val dist = distArray[0][col] + 1
                    if(dist <= steps)
                        nextInit.add(Triple(dist, distArray.size-1, col))
                }
                if(nextInit.any { it.first <= steps })
                    considerGrid(i + 1, j, nextInit)
            }
            if(i <= 0 && !visitedGrids.contains(i-1 to j)) { // check to the up
                // the bottom row can be initialized with the top row of the current grid + 1
                val nextInit = ArrayDeque<Triple<Int, Int, Int>>()
                for(col in distArray[0].indices) {
                    val dist = distArray[distArray.size-1][col] + 1
                    if(dist <= steps)
                        nextInit.add(Triple(dist, 0, col))
                }
                if(nextInit.any { it.first <= steps })
                    considerGrid(i - 1, j, nextInit)
            }
            if(j >= 0 && !visitedGrids.contains(i to j+1)) { // check right
                // the left column can be initialized with the rightmost column of the current grid + 1
                val nextInit = ArrayDeque<Triple<Int, Int, Int>>()
                for(row in distArray.indices) {
                    val dist = distArray[row][distArray[0].size-1] + 1
                    if(dist <= steps)
                        nextInit.add(Triple(dist, row, 0))
                }
                if(nextInit.any { it.first <= steps })
                    considerGrid(i, j+1, nextInit)
            }
            if(j <= 0 && !visitedGrids.contains(i to j-1)) { // check left
                // the right column can be initialized with the leftmost column of the current grid + 1
                val nextInit = ArrayDeque<Triple<Int, Int, Int>>()
                for(row in distArray.indices) {
                    val dist = distArray[row][0] + 1
                    if(dist <= steps)
                        nextInit.add(Triple(dist, row, distArray[0].size - 1))
                }
                if(nextInit.any { it.first <= steps })
                    considerGrid(i, j-1, nextInit)
            }
        }
        considerGrid(0, 0, ArrayDeque(setOf(Triple(0,startPoint.first, startPoint.second))))
        return counter
    }

    val testInput = parseLines("Day21_test")

    check(part1(testInput, 6) == 16)
    check(part2(testInput,6) == 16L)
    check(part2(testInput,10) == 50L)
    check(part2(testInput,50) == 1594L)
    check(part2(testInput,100) == 6536L)


    val input = parseLines("Day21")
    println(part1(input, 64))
    println(part2(input, 26501365))
}
