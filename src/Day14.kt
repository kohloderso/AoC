fun main() {
    class RockGrid(width: Int, height: Int, ) {
        val grid = Array(height+1) { Array(width+1) { '.' } }
        var sandY = 0
        var sandX = 500
        var sandCounter = 0

        // returns false when sand moves outside grid, i.e., flowed into abyss
        fun moveSand(): Boolean {
            if(sandY >= grid.size-1) {
                return false
            }
            if(grid[sandY+1][sandX] == '.') {
                sandY++
            } else if(grid[sandY+1][sandX-1] == '.') {
                sandY++
                sandX--
            } else if(grid[sandY+1][sandX+1] == '.') {
                sandX++
                sandY++
            } else { // sand comes to rest
                grid[sandY][sandX] = 'o'
                sandX = 500
                sandY = 0
                sandCounter++
            }
            return true
        }

        // returns false when sand is at 500,0 and can't move down
        fun moveSand2(): Boolean {
            if(sandY >= grid.size-1) {  // sand comes to rest
                grid[sandY][sandX] = 'o'
                sandX = 500
                sandY = 0
                sandCounter++
            } else if(grid[sandY+1][sandX] == '.') {
                sandY++
            } else if(grid[sandY+1][sandX-1] == '.') {
                sandY++
                sandX--
            } else if(grid[sandY+1][sandX+1] == '.') {
                sandX++
                sandY++
            } else { // sand comes to rest
                grid[sandY][sandX] = 'o'
                sandCounter++
                if(sandX == 500 && sandY == 0) return false
                sandX = 500
                sandY = 0
            }
            return true
        }

        fun addRockBetween(x: Int, y: Int, x2: Int, y2: Int) {
            val xs = listOf(x,x2)
            val ys = listOf(y,y2)
            for(i in ys.min()..ys.max()) {
                for (j in xs.min()..xs.max()) {
                    grid[i][j] = '#'
                }
            }
        }

        fun addRockPath(path: List<Pair<Int, Int>>) {
            path.zipWithNext().forEach { addRockBetween(it.first.first, it.first.second, it.second.first, it.second.second) }
        }

        fun prettyPrint(xMin: Int = 0, yMin: Int = 0) {
            for(i in yMin until grid.size) {
                for (j in xMin until grid[i].size) {
                    print(grid[i][j])
                }
                println()
            }
        }
    }

    fun parseRockPath(input: String): List<Pair<Int, Int>> {
        val result = input.split("->").map {
                item ->
                    val values = item.split(",")
                    Pair(values[0].trim().toInt(), values[1].trim().toInt())
        }
        return result
    }

    fun part1(input: List<List<Pair<Int, Int>>>): Int {
        val xMax = input.maxOf { list -> list.maxOf { pair -> pair.first } }
        val yMax = input.maxOf { list -> list.maxOf { pair -> pair.second } }
        val grid = RockGrid(xMax, yMax)
        input.forEach { grid.addRockPath(it) }
//        grid.prettyPrint(494, 0)
        var moving = true
        while(moving) {
            moving = grid.moveSand()
        }
        return grid.sandCounter
    }

    fun part2(input: List<List<Pair<Int, Int>>>): Int {
        val xMax = input.maxOf { list -> list.maxOf { pair -> pair.first } }
        val yMax = input.maxOf { list -> list.maxOf { pair -> pair.second } }
        val grid = RockGrid(xMax*2, yMax+1) // one more line to have space at bottom
        input.forEach { grid.addRockPath(it) }
        var moving = true
        while(moving) {
            moving = grid.moveSand2()
        }
        //grid.prettyPrint(494, 0)
        return grid.sandCounter
    }

    val test = parseLines("Day14_test")
    val rockpathsTest = test.map { line -> parseRockPath(line) }
    check(part1(rockpathsTest) == 24)
    check(part2(rockpathsTest) == 93)

    val input = parseLines("Day14")
    val rockpaths = input.map { line -> parseRockPath(line) }
    println(part1(rockpaths))
    println(part2(rockpaths))

}
