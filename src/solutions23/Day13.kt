package solutions23

import solutions22.transpose

fun main() {

    // returns number rows above the mirror
    fun<A> findReflectionLine(grid: Array<Array<A>>): Int {
        for(i in 1 until grid.size) {
            // take i lines above and check if the next i lines below are mirrored
            if((0 until i).all{ 2*i-(it+1) >= grid.size || grid[it].contentEquals(grid[2*i-(it+1)]) })
                return i
        }
        return -1
    }

    fun part1(input: List<List<String>>): Int {
        val grids = input.map { grid -> grid.map { row -> Array(row.length) {i -> row[i]} }.toTypedArray() }
        val horizontalRefls = grids.map { findReflectionLine(it) }
        var result = 0
        for(i in grids.indices) {
            if(horizontalRefls[i] != -1) {
                result += horizontalRefls[i] * 100
            } else {
                val verticalRefl = findReflectionLine(transpose(grids[i]))
                if(verticalRefl == -1) println("No reflection found")
                else result += verticalRefl
            }
        }
        return result
    }

    fun part2(input: List<List<String>>): Int {
        // for each grid and each element in the grid try flipping it and find a reflection line different from the original
        val grids = input.map { grid -> grid.map { row -> Array(row.length) {i -> row[i]} }.toTypedArray() }
        // save the original reflections
        val horizontalRefls = grids.map { findReflectionLine(it) }
        val verticalRefls = grids.map { findReflectionLine(transpose(it)) }

        var result = 0
        for(i in grids.indices) {
            val grid = grids[i]
            rows@for(flipRow in grid.indices) {
                for(flipCol in grid[0].indices) {
                    when(grid[flipRow][flipCol]) {
                        '#' -> grid[flipRow][flipCol] = '.'
                        '.' -> grid[flipRow][flipCol] = '#'
                    }
                    val horizontalRefl = findReflectionLine(grid)
                    if(horizontalRefl != -1 && horizontalRefl != horizontalRefls[i]) {
                        result += horizontalRefl * 100
                        break@rows
                    }
                    val verticalRefl = findReflectionLine(transpose(grid))
                    if(verticalRefl != -1 && verticalRefl != verticalRefls[i]) {
                        result += verticalRefl
                        break@rows
                    }
                    // flip back
                    when(grid[flipRow][flipCol]) {
                        '#' -> grid[flipRow][flipCol] = '.'
                        '.' -> grid[flipRow][flipCol] = '#'
                    }
                }
            }
        }
        return result
    }

    val testInput = parseChunks("Day13_test")
    check(part1(testInput) == 405)
    check(part2(testInput) == 400)

    val input = parseChunks("Day13")
    println(part1(input))
    println(part2(input))
}

