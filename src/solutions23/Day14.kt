package solutions23

fun main() {


    // move all 'O' stones as far north as possible
    fun moveNorth(grid: Array<Array<Char>>) {
        var movement = false
        do {
            movement = false
            for(i in 1 until grid.size) {
                for(j in grid[0].indices) {
                    if(grid[i][j] == 'O' && grid[i-1][j] == '.') {
                        grid[i][j] = '.'
                        grid[i-1][j] = 'O'
                        movement = true
                    }
                }
            }
        } while (movement)
    }

    fun computeLoad(grid: Array<Array<Char>>): Int {
        var result = 0
        for(i in grid.indices) {
            val stones = grid[i].count { it == 'O' }
            result += stones * (grid.size - i)
        }
        return result
    }

    fun performCycle(grid: Array<Array<Char>>): Array<Array<Char>> {
        // make deep copy of grid array
        var currentGrid = Array(grid.size) { row -> grid[row].clone() }
        for(i in 0 until 4) {
            moveNorth(currentGrid)
            currentGrid = rotateRight(currentGrid)
        }
        return currentGrid
    }

    fun part1(input: List<String>): Int {
        val grid = input.map { row -> Array(row.length) {i -> row[i]} }.toTypedArray()
        moveNorth(grid)
        return computeLoad(grid)
    }

    fun part2(input: List<String>): Int {
        val grid = input.map { row -> Array(row.length) {i -> row[i]} }.toTypedArray()
        val states = mutableListOf(grid)
        var repeatsAt = 0
        while(states.size < 1000000000) {
            val newGrid = performCycle(states.last())
            repeatsAt = states.indexOfFirst { state -> state.indices.all{ i -> state[i].contentEquals(newGrid[i]) } }
            if(repeatsAt != -1) break
            states.add(newGrid)
        }
        // states.size - repeatsAt is the length of the cycle after which the pattern repeats
        val remainingCycles = (1000000000-(repeatsAt-1)) % (states.size - repeatsAt)
        return if(remainingCycles == 0) computeLoad(states.last())
        else computeLoad(states[repeatsAt+remainingCycles-1])
    }

    val testInput = parseLines("Day14_test")
    check(part1(testInput) == 136)
    check(part2(testInput) == 64)

    val input = parseLines("Day14")
    println(part1(input))
    println(part2(input))
}
