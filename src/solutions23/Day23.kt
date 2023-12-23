package solutions23

fun main() {

    val notLeft = setOf('>', '^', 'v')
    val notRight = setOf('<', '^', 'v')
    val notUp = setOf('>', '<', 'v')
    val notDown = setOf('>', '^', '<')


    fun findLongestPath(grid: Array<CharArray>, usedTiles: Array<Array<Boolean>>, goalCol: Int, currentRow: Int, currentCol: Int): Int {
        var i = currentRow
        var j = currentCol
        while(true) {
            usedTiles[i][j] = true
            // if we are at the goal, we count
            if(i == grid.size - 1 && j == goalCol)
                return usedTiles.sumOf { row -> row.count { it } }-1

            val neighbours = mutableListOf<Pair<Int, Int>>()
            if (i - 1 >= 0 && grid[i-1][j] != '#' && !notUp.contains(grid[i][j]) && !usedTiles[i-1][j])
                neighbours.add(i-1 to j)
            if (i + 1 < grid.size && grid[i+1][j] != '#' && !notDown.contains(grid[i][j]) && !usedTiles[i+1][j])
                neighbours.add(i+1 to j)
            if (j - 1 >= 0 && grid[i][j-1] != '#' && !notLeft.contains(grid[i][j]) && !usedTiles[i][j-1])
                neighbours.add(i to j-1)
            if (j + 1 < grid[0].size && grid[i][j+1] != '#' && !notRight.contains(grid[i][j]) && !usedTiles[i][j+1])
                neighbours.add(i to j+1)
            if(neighbours.isEmpty()) return 0
            if(neighbours.size > 1) {
                // follow the different options
                val results = neighbours.map { findLongestPath(grid, Array(grid.size) { usedTiles[it].clone() }, goalCol, it.first, it.second) }
                return results.maxOrNull() ?: 0
            }
            // in case neighbours contains only one element, we follow it
            i = neighbours[0].first
            j = neighbours[0].second
        }
    }

    fun findLongestPathDry(grid: Array<CharArray>, usedTiles: Array<Array<Boolean>>, goalCol: Int, currentRow: Int, currentCol: Int): Int {
        var i = currentRow
        var j = currentCol
        while(true) {
            usedTiles[i][j] = true
            // if we are at the goal, we count
            if(i == grid.size - 1 && j == goalCol)
                return usedTiles.sumOf { row -> row.count { it } }-1

            val neighbours = mutableListOf<Pair<Int, Int>>()
            if (i - 1 >= 0 && grid[i-1][j] != '#' && !usedTiles[i-1][j])
                neighbours.add(i-1 to j)
            if (i + 1 < grid.size && grid[i+1][j] != '#' && !usedTiles[i+1][j])
                neighbours.add(i+1 to j)
            if (j - 1 >= 0 && grid[i][j-1] != '#' && !usedTiles[i][j-1])
                neighbours.add(i to j-1)
            if (j + 1 < grid[0].size && grid[i][j+1] != '#' && !usedTiles[i][j+1])
                neighbours.add(i to j+1)
            if(neighbours.isEmpty()) return 0
            if(neighbours.size > 1) {
                // follow the different options
                val results = neighbours.map { findLongestPathDry(grid, Array(grid.size) { usedTiles[it].clone() }, goalCol, it.first, it.second) }
                return results.maxOrNull() ?: 0
            }
            // in case neighbours contains only one element, we follow it
            i = neighbours[0].first
            j = neighbours[0].second
        }
    }

    fun part1(input: List<String>): Int {
        val grid: Array<CharArray> = Array(input.size) { i -> input[i].toCharArray() }
        val startCol = grid[0].indexOf('.')
        val goalCol = grid.last().indexOf('.')
        val result = findLongestPath(grid, Array(grid.size) { Array(grid[0].size) { false } }, goalCol, 0, startCol)
        return result
    }

    fun part2(input: List<String>): Int {
        val grid: Array<CharArray> = Array(input.size) { i -> input[i].toCharArray() }
        val startCol = grid[0].indexOf('.')
        val goalCol = grid.last().indexOf('.')
        val result = findLongestPathDry(grid, Array(grid.size) { Array(grid[0].size) { false } }, goalCol, 0, startCol)
        return result
    }

    val testInput = parseLines("Day23_test")
    check(part1(testInput) == 94)
    check(part2(testInput) == 154)

    val input = parseLines("Day23")
    println(part1(input))
    println(part2(input))

}
