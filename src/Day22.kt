fun main() {
    open class MonkeyMap(val array: Array<String>) {
        var rowId = 0
        var colId = 0
        var dirId = 0
        val directions = listOf(Pair(1,0), Pair(0,1), Pair(-1, 0), Pair(0,-1))

        // determine leftmost open tile of topmost row
        init {
            colId = array[rowId].indexOfFirst { it == '.' }
        }
        open fun moveNSteps(n: Int) {
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

    class MonkeyCube(array: Array<String>) : MonkeyMap(array) {
        // specific solution for input (does not work for test file)
        val one = array.sliceArray(0..49).map { it.slice(50..99) }
        val two = array.sliceArray(0..49).map { it.slice(100..149) }
        val three = array.sliceArray(50..99).map { it.slice(50..99) }
        val four = array.sliceArray(100..149).map { it.slice(50..99) }
        val five = array.sliceArray(100..149).map { it.slice(0..49) }
        val six = array.sliceArray(150..199).map { it.slice(0..49) }
        var currentSquare = one

        init {
            colId = 0
            rowId = 0
        }

        fun findNextSquare(): Pair<List<String>, List<Int>> {
            return when(currentSquare) {
                one -> when(dirId) {
                        0 -> Pair(two, listOf(rowId, 0, 0))
                        1 -> Pair(three, listOf(0, colId, 1))
                        2 -> Pair(five, listOf(49 - rowId, 0, 0))
                        else -> Pair(six, listOf(colId, 0, 0))
                    }
                two -> when(dirId) {
                        0 -> Pair(four, listOf(49 - rowId, 49, 2))
                        1 -> Pair(three, listOf(colId, 49, 2))
                        2 -> Pair(one, listOf(rowId, 49, 2))
                        else -> Pair(six, listOf(49, colId, 3))
                    }
                three -> when(dirId) {
                        0 -> Pair(two, listOf(49, rowId, 3))
                        1 -> Pair(four, listOf(0, colId, 1))
                        2 -> Pair(five, listOf(0, rowId, 1))
                        else -> Pair(one, listOf(49, colId, 3))
                    }
                four -> when(dirId) {
                        0 -> Pair(two, listOf(49 - rowId, 49, 2))
                        1 -> Pair(six, listOf(colId, 49, 2))
                        2 -> Pair(five, listOf(rowId, 49, 2))
                        else -> Pair(three, listOf(49, colId, 3))
                    }
                five -> when(dirId) {
                        0 -> Pair(four, listOf(rowId, 0, 0))
                        1 -> Pair(six, listOf(0, colId, 1))
                        2 -> Pair(one, listOf(49-rowId, 0, 0))
                        else -> Pair(three, listOf(colId, 0, 0))
                    }
                six -> when(dirId) {
                        0 -> Pair(four, listOf(49, rowId, 3))
                        1 -> Pair(two, listOf(0, colId, 1))
                        2 -> Pair(one, listOf(0, rowId, 1))
                        else -> Pair(five, listOf(49, colId, 3))
                }
                else -> Pair(currentSquare, emptyList())
            }
        }
        override fun moveNSteps(n: Int) {
            for(i in 0 until n) {
                var nextRow = rowId+directions[dirId].second
                var nextCol = colId+directions[dirId].first
                var nextSquare = currentSquare
                var nextDir = dirId
                if(!(0..49).contains(nextRow) || !(0..49).contains(nextCol)) {
                    val nexts = findNextSquare()
                    nextSquare = nexts.first
                    nextRow = nexts.second[0]
                    nextCol = nexts.second[1]
                    nextDir = nexts.second[2]
                }
                if(nextSquare[nextRow][nextCol] == '#') {
                    // ran into a wall -> stop moving and leave loop
                    break
                } else {
                    // found a free tile -> update position
                    colId = nextCol
                    rowId = nextRow
                    currentSquare = nextSquare
                    dirId = nextDir
                }
            }
        }

        fun translateRowCol(): Pair<Int, Int> {
            return when(currentSquare) {
                one -> Pair(rowId, colId + 50)
                two -> Pair(rowId, colId + 100)
                three -> Pair(rowId + 50, colId + 50)
                four -> Pair(rowId + 100, colId + 50)
                five -> Pair(rowId + 100, colId)
                six -> Pair(rowId + 150, colId)
                else -> Pair(0, 0)
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

    fun part2(array: Array<String>, directions: String): Int {
        val monkeyMap = MonkeyCube(array)
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
        val result = monkeyMap.translateRowCol()
        return 1000 * (result.first+1) + 4 * (result.second+1) + monkeyMap.dirId
    }


//    val test = parseChunks("Day22_test")
//    val arrayTest = test[0].toTypedArray()
//    check(part1(arrayTest, test[1][0]) == 6032)


    val start = System.currentTimeMillis()

    val input = parseChunks("Day22")
    val array = input[0]
    val maxLength = array.maxOf { it.length }
    val arrayBuffered = array.map { it.padEnd(maxLength, ' ') }.toTypedArray()
//    println(part1(arrayBuffered, input[1][0]))

    println(part2(arrayBuffered, input[1][0]))

    println("time: " + (System.currentTimeMillis() - start))

}
