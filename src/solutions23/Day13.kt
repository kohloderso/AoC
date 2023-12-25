package solutions23

fun main() {


    // find one-off reflection lie
    fun <A> findReflectionLine(grid: Array<Array<A>>, smudges: Int = 0): Int {
        for(i in 1 until grid.size) {
            var misses = 0
            // take i lines above and check if the next i lines below are mirrored
            for(j in 0 until i) {
                if (2 * i - (j + 1) < grid.size) {
                    misses += grid[j].zip(grid[2 * i - (j + 1)]).count { (x, y) -> x != y }
                    if (misses > smudges) break
                }
            }
            if(misses == smudges) {
                return i
            }
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
        val horizontalRefls = grids.map { findReflectionLine(it, 1) }
        var result = 0
        for(i in grids.indices) {
            if(horizontalRefls[i] != -1) {
                result += horizontalRefls[i] * 100
            } else {
                val verticalRefl = findReflectionLine(transpose(grids[i]), 1)
                if(verticalRefl == -1) println("No reflection found")
                else result += verticalRefl
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

