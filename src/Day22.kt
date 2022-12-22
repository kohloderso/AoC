fun main() {
    class MonkeyMap(val array: Array<String>) {
        var rowId = 0
        var colId = 0
        var dirId = 0
        val directions = listOf(Pair(1,0), Pair(0,1), Pair(-1, 0), Pair(0,-1))

        // determine leftmost open tile of topmost row
        init {
            colId = array[rowId].indexOfFirst { it == '.' }
        }
        fun moveNSteps(n: Int) {
            val facingH = directions[dirId].first
            val facingV = directions[dirId].second
            for(i in 0 until n) {
                var nextRow = Math.floorMod(rowId+facingV, array.size)
                var nextCol = Math.floorMod(colId+facingH, array[nextRow].length)
                // find first non-empty tile
                while(array[nextRow][nextCol] == ' ') {
                    nextRow = Math.floorMod(nextRow+facingV, array.size)
                    nextCol = Math.floorMod(nextCol+facingH, array[nextRow].length)
                }
                if(array[nextRow][nextCol] == '#') {
                    // ran into a wall -> stop moving and leave loop
                    break
                } else {
                    // found a free tile -> update position
                    colId = nextCol
                    rowId = nextRow
                }
            }
        }
        fun turn(direction: Char) {
            when(direction) {
                'L' -> dirId = Math.floorMod(dirId - 1, directions.size)
                'R' -> dirId = Math.floorMod(dirId + 1, directions.size)
            }
        }
    }

    fun part1(array: Array<String>, directions: String): Int {
        val monkeyMap = MonkeyMap(array)
        var remainingDirs = directions
        while(remainingDirs.isNotEmpty()) {
            val steps = remainingDirs.takeWhile { it.isDigit()}.toInt()
            remainingDirs = remainingDirs.dropWhile { it.isDigit() }
            monkeyMap.moveNSteps(steps)
            if(remainingDirs.isNotEmpty()) {
                val dir = remainingDirs[0]
                remainingDirs = remainingDirs.drop(1)
                monkeyMap.turn(dir)
            }
        }
        return 1000 * (monkeyMap.rowId+1) + 4 * (monkeyMap.colId+1) + monkeyMap.dirId
    }


//    val test = parseChunks("Day22_test")
//    val arrayTest = test[0].toTypedArray()
//    check(part1(arrayTest, test[1][0]) == 6032)


    val start = System.currentTimeMillis()

    val input = parseChunks("Day22")
    val array = input[0].toTypedArray()
    println(part1(array, input[1][0]))

//    println(part2(input))

   println("time: " + (System.currentTimeMillis() - start))
}
