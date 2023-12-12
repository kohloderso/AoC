package solutions23

import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class Universe(val grid: Array<Array<Char>>, val expansionParam: Int) {

    fun markEmptyRowColumnFields() {
        // iterate over rows of the grid and mark all rows that only contain '.' with '+'
        for(i in grid.indices) {
            if(grid[i].all {it == '.'}) {
                grid[i] = Array(grid[0].size) { j -> '+' }
            }
        }
        // iterate over columns of the grid and mark all columns that only contain '.' or '+' with '+'
        for(i in 0 until grid[0].size) {
            if(grid.all {it[i] == '.' || it[i] == '+'}) {
                grid.forEach { row -> row[i] = '+' }
            }
        }
    }

    fun shortestPath(x: Pair<Int, Int>, y: Pair<Int, Int>): Long {
        val x1 = min(x.first, y.first)
        val x2 = max(x.first, y.first)
        val y1 = min(x.second, y.second)
        val y2 = max(x.second, y.second)
        // counter number of rows between x and y which contain a '+'
        val emptyRows = grid.indices.count { i -> i in x1..x2 && grid[i][0] == '+' }
        // counter number of columns between x and y which contain a '+'
        val emptyColumns = grid[0].indices.count { i -> i in y1..y2 && grid[0][i] == '+' }
        return abs(x.first - y.first).toLong() - emptyRows + emptyRows*expansionParam +
                abs(x.second - y.second) - emptyColumns + emptyColumns*expansionParam
    }

    // Find all coordinates in the grid where we have '#'
    fun getGalaxyCoordinates(): List<Pair<Int, Int>> {
        val coordinates = mutableListOf<Pair<Int, Int>>()
        for(i in grid.indices) {
            for(j in grid[0].indices) {
                if(grid[i][j] == '#') coordinates.add(i to j)
            }
        }
        return coordinates
    }

    fun sumGalaxyShortestPaths(): Long {
        val coordinates = getGalaxyCoordinates()
        var sum = 0L
        for(i in coordinates.indices) {
            for(j in i+1 until coordinates.size) {
                sum += shortestPath(coordinates[i], coordinates[j])
            }
        }
        return sum
    }



}

fun main() {

    fun part1(input: List<String>): Long {
        val grid = Universe(input.map { line -> Array(line.length) { i -> line[i]} }.toTypedArray(), 2)
        grid.markEmptyRowColumnFields()
        return grid.sumGalaxyShortestPaths()
    }

    fun part2(input: List<String>): Long {
        val grid = Universe(input.map { line -> Array(line.length) { i -> line[i]} }.toTypedArray(), 1000000)
        grid.markEmptyRowColumnFields()
        return grid.sumGalaxyShortestPaths()
    }

    val testInput = parseLines("Day11_test")
    check(part1(testInput) == 374L)

    val input = parseLines("Day11")
    println(part1(input))
    println(part2(input))
}
