package solutions23

class PipeGrid(val grid: Array<CharArray>, private val startRow: Int, private val startCol: Int) {

    private val markedPos = Array(grid.size) { BooleanArray(grid[0].size) { false }}

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

        // 'S' should be treated like 7
        fun pipeRight(c: Char): Boolean = when (c) {
            'F', '-', 'L' -> true
            else -> false
        }
        fun pipeLeft(c: Char): Boolean = when (c) {
            'J', '-', '7' -> true
            else -> false
        }
        fun pipeDown(c: Char): Boolean = when (c) {
            '|', 'F', '7', 'S' -> true
            else -> false
        }
        fun pipeUp(c: Char): Boolean = when (c) {
            'J', '|', 'L' -> true
            else -> false
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



    /**
     * Insert extra fields between each row and column and around the grid.
     * Only copy the pipe parts that belong to the relevant cycle.
     */
    fun padGrid(sLetter: Char): Array<Array<Char>> {
        val paddedGrid = Array(grid.size*2+1) { i ->
            Array(grid[0].size*2+1) { j ->
                if(i % 2 == 1 && j % 2 == 1 && markedPos[i/2][j/2]) {
                    if(grid[i/2][j/2] == 'S') sLetter
                    else grid[i/2][j/2]
                } else '.'
            }
        }
        for(i in paddedGrid.indices) {
            for(j in paddedGrid[0].indices) {
                // check if there is a vertical connection between two adjacent fields
                if(i % 2 == 0 && i-1 >= 0 && i+1 < paddedGrid.size && pipeDown(paddedGrid[i-1][j]) && pipeUp(paddedGrid[i+1][j])) {
                    paddedGrid[i][j] = '|'
                }
                // check if there is horizontal connection between two adjacent fields
                if(j % 2 == 0 && j-1 >= 0 && j+1 < paddedGrid[0].size && pipeRight(paddedGrid[i][j-1]) && pipeLeft(paddedGrid[i][j+1])) {
                    paddedGrid[i][j] = '-'
                }
            }
        }
        return paddedGrid
    }


    fun floodFillCount(sLetter: Char): Int {
        val paddedGrid = padGrid(sLetter)
        var count = 0
        val queue = ArrayDeque<Pair<Int,Int>>()
        queue.add(Pair(0, 0))
        while(queue.isNotEmpty()) {
            val (row, col) = queue.removeFirst()
            // these are the areas where we're not allowed to go
            if(row < 0 || row >= paddedGrid.size || col < 0 || col >= paddedGrid[0].size) continue
            // check whether it's a free and unvisited field
            if(paddedGrid[row][col] != '.') continue
            // mark all visited tiles
            paddedGrid[row][col] = 'O'
            // if we are inside the normal grid, increment the count
            if(row % 2 == 1 && col % 2 == 1) count++
            queue.add(Pair(row+1, col))
            queue.add(Pair(row-1, col))
            queue.add(Pair(row, col+1))
            queue.add(Pair(row, col-1))
        }
        return count
    }

    fun computeInsideTiles(sLetter: Char): Int {
        val cycleLength = findCycleLengthAndMarkCycle()
        // start at upper left corner of padded grid
        val outsideTiles = floodFillCount(sLetter)
        // everything that's not outside and does not belong to the cycle itself is inside
        return grid.size * grid[0].size - outsideTiles - cycleLength
    }
}

fun main() {

    fun part1(input: List<String>): Int {
        val grid = PipeGrid.parse(input)
        return grid.findCycleLengthAndMarkCycle()/2
    }

    fun part2(input: List<String>, sLetter: Char): Int {
        val grid = PipeGrid.parse(input)
        return grid.computeInsideTiles(sLetter)
    }

    val testInput = parseLines("Day10_test")
    check(part1(testInput) == 8)
    val test2 = part2(parseLines("Day10_test2"), 'F')
    check(test2 == 8)
    val test3 = part2(parseLines("Day10_test3"), '7')
    check(test3 == 10)
    val testInput4 = parseLines("Day10_test4")
    check(part2(testInput4, 'F') == 4)

    val input = parseLines("Day10")
    println(part1(input))
    println(part2(input, '7'))
}
