package solutions23

import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

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

    fun computeLeftGrid(grid: Array<Array<Boolean>>, initCol: List<Int>): Array<Array<Int>> {
        val queue = ArrayDeque<Triple<Int, Int, Int>>()
        for(row in grid.indices) {
            val dist = initCol[row] + 1
            queue.add(Triple(dist, row, grid[0].size-1))
        }
        return dijkstra(grid, queue)
    }
    fun computeRightGrid(grid: Array<Array<Boolean>>, initCol: List<Int>): Array<Array<Int>> {
        val queue = ArrayDeque<Triple<Int, Int, Int>>()
        for(row in grid.indices) {
            val dist = initCol[row] + 1
            queue.add(Triple(dist, row, 0))
        }
        return dijkstra(grid, queue)
    }

    fun computeBottomGrid(grid: Array<Array<Boolean>>, initRow: List<Int>): Array<Array<Int>> {
        val queue = ArrayDeque<Triple<Int, Int, Int>>()
        for(col in grid[0].indices) {
            val dist = initRow[col] + 1
            queue.add(Triple(dist, 0, col))
        }
        return dijkstra(grid, queue)
    }

    fun computeTopGrid(grid: Array<Array<Boolean>>, initRow: List<Int>): Array<Array<Int>> {
        val queue = ArrayDeque<Triple<Int, Int, Int>>()
        for(col in grid[0].indices) {
            val dist = initRow[col] + 1
            queue.add(Triple(dist, grid.size-1, col))
        }
        return dijkstra(grid, queue)
    }

    fun initTopBottomDist(grid: Array<Array<Boolean>>): Array<Array<Int>> {
        val distMatrix = Array(grid[0].size) { Array (grid[0].size) { Int.MAX_VALUE } }
        for(i in grid[0].indices) {
            val dists = dijkstra(grid, ArrayDeque(setOf(Triple(0,0, i))))
            distMatrix[i] = dists.last()
        }
        return distMatrix
    }

    fun computeOpposite(values: List<Int>, oppositeDists: Array<Array<Int>>): List<Int> {
        val n = values.size
        return values.indices.map { i ->
            var currentBest = Int.MAX_VALUE
            for (j in max(i - n / 2, 0) until min(i + n / 2, n)) {
                if (values[j] + oppositeDists[j][i] < currentBest) {
                    currentBest = values[j] + oppositeDists[j][i]
                }
            }
            currentBest
        }
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
        val initDists = dijkstra(gardenGrid, ArrayDeque(setOf(Triple(0,startPoint.first, startPoint.second))))
        val topBottomDists = initTopBottomDist(gardenGrid)
        val leftRightDists = initTopBottomDist(transpose(gardenGrid))
        val counts = listOf(initDists.sumOf { it.count { d -> d <= steps && d % 2 == 0 } },
            initDists.sumOf { it.count { d -> d <= steps && d % 2 == 1 } })
        counter += counts[0]

        fun considerGrid(i: Int, j: Int, topRow: List<Int>, bottomRow: List<Int>, leftCol: List<Int>, rightCol: List<Int>) {
            if(visitedGrids.contains(i to j)) return
            visitedGrids.add(i to j)
            if(i >= 0) {
                val newTop = bottomRow.map { it+1 }
                val newBottom = computeOpposite(newTop, topBottomDists)
                val newLeft = leftCol.indices.map { i -> leftCol.last() + i + 1 }
                val newRight = rightCol.indices.map { i -> rightCol.last() + i + 1}
                if(bottomRow.all { it <= steps }) {
                    counter += counts[abs((i+j) % 2)]
                    considerGrid(i+1, j, newTop, newBottom, newLeft, newRight)
                } else {
                    val res = computeBottomGrid(gardenGrid, bottomRow).sumOf { it.count { d -> d <= steps && d % 2 == 0 } }
                    counter += res
                    if(res != 0) considerGrid(i+1, j, newTop, newBottom, newLeft, newRight)
                    if(res == 0) visitedGrids.add(i+1 to j)
                }
            }
            if(i <= 0) {
                val newBottom = bottomRow.map { it+1 }
                val newTop = computeOpposite(newBottom, topBottomDists)
                val newLeft = leftCol.indices.map { i -> leftCol.last() + i + 1 }
                val newRight = rightCol.indices.map { i -> rightCol.last() + i + 1}
                if(bottomRow.all { it <= steps }) {
                    counter += counts[abs((i+j) % 2)]
                    considerGrid(i-1, j, newTop, newBottom, newLeft, newRight)
                } else {
                    val res = computeTopGrid(gardenGrid, topRow).sumOf { it.count { d -> d <= steps && d % 2 == 0 } }
                    counter += res
                    if(res != 0) considerGrid(i-1, j, newTop, newBottom, newLeft, newRight)
                    if(res == 0) visitedGrids.add(i-1 to j)
                }
            }
            if(j >= 0) {
                val newBottom = bottomRow.indices.map { i -> bottomRow.last() + i + 1 }
                val newTop = bottomRow.indices.map { i -> topRow.last() + i + 1 }
                val newLeft = rightCol.map { it+1 }
                val newRight = computeOpposite(newLeft, leftRightDists)
                if(newRight.all { it <= steps }) {
                    counter += counts[abs((i+j) % 2)]
                    considerGrid(i, j+1, newTop, newBottom, newLeft, newRight)
                } else {
                    val res = computeRightGrid(gardenGrid, rightCol).sumOf { it.count { d -> d <= steps && d % 2 == 0 } }
                    counter += res
                    if(res != 0) considerGrid(i, j+1, newTop, newBottom, newLeft, newRight)
                    if(res == 0) visitedGrids.add(i to j+1)
                }
            }
            if(j <= 0) {
                val newBottom = bottomRow.indices.map { i -> bottomRow.first() + i + 1 }
                val newTop = bottomRow.indices.map { i -> topRow.first() + i + 1 }
                val newRight = leftCol.map { it + 1 }
                val newLeft = computeOpposite(newRight, leftRightDists)
                if(newLeft.all { it <= steps }) {
                    counter += counts[abs((i+j) % 2)]
                    considerGrid(i, j-1, newTop, newBottom, newLeft, newRight)
                } else {
                    val res = computeLeftGrid(gardenGrid, leftCol).sumOf { it.count { d -> d <= steps && d % 2 == 0 } }
                    counter += res
                    if(res != 0) considerGrid(i, j-1, newTop, newBottom, newLeft, newRight)
                    if(res == 0) visitedGrids.add(i to j-1)
                }
            }

        }
        considerGrid(0, 0, initDists.first().toList(), initDists.last().toList(),
            initDists.indices.map { initDists[it][0] }, initDists.indices.map { initDists[it][initDists[0].size-1] })
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
