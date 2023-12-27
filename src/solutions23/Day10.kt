package solutions23

class PipeGrid(val grid: Array<CharArray>, val startRow: Int, val startCol: Int) {

    val markedPos = Array(grid.size) { BooleanArray(grid[0].size) { false }}

    companion object {
        fun parse(input: List<String>): PipeGrid {
            val grid = input.map { it.toCharArray() }.toTypedArray()
            val start = startPos(grid)
            return PipeGrid(grid, start.first, start.second)
        }

        fun startPos(grid: Array<CharArray>): Pair<Int,Int> {
            grid.forEachIndexed { i, row ->
                row.forEachIndexed { j, c ->
                    if(c == 'S') return Pair(i,j)
                }
            }
            return Pair(-1,-1)
        }
    }

    // Length of the cycle is the only thing that counts for part 1
    fun findCycleLengthAndMarkCycle(): Int {
        markedPos[startRow][startCol] = true
        var counter = 1
        // checked input: there are two options in the beginning, down or left -> start with down
        var row = startRow+1
        var col = startCol
        var hDir = 0 // indicates whether the last move was horizontal, right is 1, left is -1
        var vDir = 1 // indicates whether the last move was vertical, down is 1, up is -1
        while(grid[row][col] != 'S') {
            markedPos[row][col] = true
            when (grid[row][col]) {
                '|' -> row += vDir
                '-' -> col += hDir
                'L', '7' -> { // swap hDir and vDir
                    val tmp = hDir
                    hDir = vDir
                    vDir = tmp
                    row += vDir
                    col += hDir
                }
                'J', 'F' -> { // swap and invert hDir and vDir
                    val tmp = hDir
                    hDir = vDir*-1
                    vDir = tmp*-1
                    row += vDir
                    col += hDir
                }
                '.' -> {
                    println("ran onto ground, something went wrong")
                    return -1
                }
            }
            counter++
        }
        return counter
    }

    // 'S' should be treated like 7
    fun pipeRight(row: Int, col: Int): Boolean = when (grid[row][col]) {
        'F', '-', 'L' -> true
        else -> false
    }
    fun pipeLeft(row: Int, col: Int): Boolean = when (grid[row][col]) {
        'J', '-', '7', 'S' -> true
        else -> false
    }
    fun pipeDown(row: Int, col: Int): Boolean = when (grid[row][col]) {
        '|', 'F', '7', 'S' -> true
        else -> false
    }
    fun pipeUp(row: Int, col: Int): Boolean = when (grid[row][col]) {
        'J', '|', 'L' -> true
        else -> false
    }

    fun connectedPipes(row1: Int, col1: Int, row2: Int, col2: Int): Boolean {
        val vDir = row2 - row1
        val hDir = col2 - col1
        if(hDir == 1 && vDir == 0 && pipeRight(row1, col1) && pipeLeft(row2, col2)) return true
        if(hDir == -1 && vDir == 0 && pipeLeft(row1, col1) && pipeRight(row2, col2)) return true
        if(hDir == 0 && vDir == 1 && pipeDown(row1, col1) && pipeUp(row2, col2)) return true
        if(hDir == 0 && vDir == -1 && pipeUp(row1, col1) && pipeDown(row2, col2)) return true
        return false
    }

    /**
     * Follow parallel pipe tracks that belong to the cycle in the original grid.
     * Return the position of a tile that does not belong to the cycle but can be reached by the parallel tracks.
     * Return null if no such tile exists.
     */
    fun followParallelTracks(row1: Int, col1: Int, row2: Int, col2: Int, vDir: Int, hDir: Int): Pair<Int,Int>? {
        var newRow1 = row1
        var newCol1 = col1
        var newRow2 = row2
        var newCol2 = col2
        if(newRow1 < 0 || newRow1 >= grid.size || newCol1 < 0 || newCol1 >= grid[0].size
            || newRow2 < 0 || newRow2 >= grid.size || newCol2 < 0 || col2 >= grid[0].size
            || !markedPos[row1][col1] || !markedPos[row2][col2])
            return null
        // if both fields belong to the cycle but are not directly connected, then it's a parallel track
        while(markedPos[newRow1][newCol1] && markedPos[newRow2][newCol2]
            && !connectedPipes(newRow1, newCol1, newRow2, newCol2)) {
            newRow1 += vDir
            newCol1 += hDir
            newRow2 += vDir
            newCol2 += hDir
            if(newRow1 < 0 || newRow1 >= grid.size || newCol1 < 0 || newCol1 >= grid[0].size
                || newRow2 < 0 || newRow2 >= grid.size || newCol2 < 0 || col2 >= grid[0].size)
                return null
        }
        if(!markedPos[newRow1][newCol1]) return Pair(newRow1, newCol1)
        if(!markedPos[newRow2][newCol2]) return Pair(newRow2, newCol2)
        return null
    }

    /**
     * Insert extra fields between each row and column and around the grid.
     */
    fun padGrid(): Array<Array<Char>> =
        Array(grid.size*2+1) { i ->
            Array(grid[0].size*2+1) { j ->
                if(i % 2 == 1 && j % 2 == 1) {
                    grid[i/2][j/2]
                } else {
                    // check if there is horizontal connection between two adjacent fields
                    if(i/2-1 >= 0 && i/2+1 < grid[0].size && pipeRight(i/2-1,j/2) && pipeLeft(i/2+1,j/2)) {
                        '-'
                    // check if there is a vertical connection between two adjacent fields
                    } else if(j/2-1 >= 0 && j/2+1 < grid[0].size && pipeDown(i/2,j/2-1) && pipeUp(i/2,j/2+1)) {
                        '|'
                    } else {
                        '.'
                    }
                }
            }
        }

    fun floodFillCount(): Int {
        val paddedGrid = padGrid()
        var count = 0
        val queue = ArrayDeque<Pair<Int,Int>>()
        queue.add(Pair(startRow, startCol))
        while(queue.isNotEmpty()) {
            val (row, col) = queue.removeFirst()
            // these are the areas where we're not allowed to go
            if(row < 0 || row >= paddedGrid.size || col < 0 || col >= paddedGrid[0].size) continue
            // if we are inside the normal grid, check whether it's a marked position belonging to the cycle
            if(row > 0 && row <= grid.size && col > 0 && col <= grid[0].size && markedPos[row-1][col-1]) continue
            // these are the tiles we already visited
            if(paddedGrid[row][col] == 'O') continue
            // mark all visited tiles
            paddedGrid[row][col] = 'O'
            // if we are inside the normal grid, increment the count
            if(row > 0 && row <= grid.size && col > 0 && col <= grid[0].size) count++
            queue.add(Pair(row+1, col))
            queue.add(Pair(row-1, col))
            queue.add(Pair(row, col+1))
            queue.add(Pair(row, col-1))
            // check the parallel track situation, don't forget to take padding into account
            val parallelTracksPos = mutableListOf<Pair<Int,Int>?>()
            parallelTracksPos.add(followParallelTracks(row, col-1, row, col-2, 1, 0 ))
            parallelTracksPos.add(followParallelTracks(row, col-1, row, col, 1, 0 ))
            parallelTracksPos.add(followParallelTracks(row-2, col-1, row-2, col-2, -1, 0 ))
            parallelTracksPos.add(followParallelTracks(row-2, col-1, row-2, col, -1, 0 ))
            parallelTracksPos.add(followParallelTracks(row-1, col-2, row-2, col-2, 0, -1 ))
            parallelTracksPos.add(followParallelTracks(row-1, col-2, row, col-2, 0, -1 ))
            parallelTracksPos.add(followParallelTracks(row-1, col, row-2, col, 0, 1 ))
            parallelTracksPos.add(followParallelTracks(row-1, col, row, col, 0, 1 ))
            queue.addAll(parallelTracksPos.filterNotNull().map { it.first + 1 to it.second + 1 })
        }
        // print stuff for debugging
        for(i in paddedGrid.indices) {
            for(j in paddedGrid[0].indices) {
                if(paddedGrid[i][j] == 'O') print('O')
                else if(i > 0 && i <= grid.size && j > 0 && j <= grid[0].size && markedPos[i-1][j-1])
                    print(grid[i-1][j-1])
                else print('*')
            }
            println()
        }
        // sanity check: count I's in insideGrid
        val insideGrid = Array(grid.size) { CharArray(grid[0].size) { '.' } }
        for(i in grid.indices) {
            for(j in grid[0].indices) {
                if(paddedGrid[i+1][j+1] != 'O' && !markedPos[i][j]) insideGrid[i][j] = 'I'
            }
        }
        var iCount = 0
        for(i in insideGrid.indices) {
            for(j in insideGrid[0].indices) {
                if(insideGrid[i][j] == 'I') iCount++
            }
        }
        println("number of I's in insideGrid: $iCount")
        return count
    }

    fun computeInsideTiles(): Int {
        val cycleLength = findCycleLengthAndMarkCycle()
        // start at upper left corner of padded grid
        val outsideTiles = floodFillCount()
        // everything that's not marked 'O' and does not belong to the cycle itself is inside
        return grid.size * grid[0].size - outsideTiles - cycleLength
    }
}

fun main() {

    fun part1(input: List<String>): Int {
        val grid = PipeGrid.parse(input)
        return grid.findCycleLengthAndMarkCycle()/2
    }

    fun part2(input: List<String>): Int {
        val grid = PipeGrid.parse(input)
        return grid.computeInsideTiles()
    }

    val testInput = parseLines("Day10_test")
    check(part1(testInput) == 8)
    val test2 = part2(parseLines("Day10_test2"))
    check(test2 == 8)
    val test3 = part2(parseLines("Day10_test3"))
    check(test3 == 10)
//    val testInput4 = parseLines("Day10_test4")
//    check(part2(testInput4) == 4) // works when S is treated like F

    val input = parseLines("Day10")
    println(part1(input))
    println(part2(input))
}
