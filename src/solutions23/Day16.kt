package solutions23

import kotlin.math.max

class Contraption(val array: Array<CharArray>) {
    val result = Array(array.size) { Array(array[0].size) { emptySet<Char>() } }
    val checkedSides = Array(array.size) { Array(array[0].size) { emptySet<Char>() } }
    var currentMax = 0

    companion object {
        fun dirToChar(vDir: Int, hDir: Int) =
            when(Pair(vDir, hDir)) {
                Pair(1, 0) -> 'v'
                Pair(0, 1) -> '>'
                Pair(-1, 0) -> '^'
                Pair(0, -1) -> '<'
                else -> '.'
            }
    }

    fun resetResult() {
        for (row in result) {
            for (j in row.indices) {
                row[j] = emptySet()
            }
        }
    }

    // do this after finishing following the light beam
    fun markCheckedSides() {
        for (i in listOf(0, array.size - 1)) {
            for (j in 0 until array[i].size) {
                checkedSides[i][j] = checkedSides[i][j].union(result[i][j])
            }
        }
        for (j in listOf(0, array[0].size - 1)) {
            for (i in array.indices) {
                checkedSides[i][j] = checkedSides[i][j].union(result[i][j])
            }
        }
    }

    fun followLightBeam(x: Int, y: Int, vDir:Int, hDir:Int) {
        if(x < 0 || x >= array.size || y < 0 || y >= array[0].size)
            return
        // mark the current position as visited
        if(result[x][y].contains(dirToChar(vDir, hDir))) return
        else result[x][y] = result[x][y] + dirToChar(vDir, hDir)
        // follow the light bean
        when(array[x][y]) {
            '.' -> followLightBeam(x + vDir, y + hDir, vDir, hDir)
            '/' -> {
                val nvDir = hDir*-1
                val nhDir = vDir*-1
                followLightBeam(x + nvDir, y + nhDir, nvDir, nhDir)
            }
            '\\' -> followLightBeam(x + hDir, y + vDir, hDir, vDir)
            '|' -> {
                if(hDir == 0) followLightBeam(x + vDir, y + hDir, vDir, hDir)
                else {
                    followLightBeam(x + 1, y, 1, 0)
                    followLightBeam(x - 1, y, -1, 0)
                }
            }
            '-' -> {
                if(vDir == 0) followLightBeam(x + vDir, y + hDir, vDir, hDir)
                else {
                    followLightBeam(x, y+1, 0, 1)
                    followLightBeam(x, y -1, 0, -1)
                }
            }
        }
    }

    fun checkOneOption(x: Int, y: Int, vDir:Int, hDir:Int) {
        if(checkedSides[x][y].contains(dirToChar(vDir, hDir))) return
        followLightBeam(x, y, vDir, hDir)
        currentMax = max(currentMax, energizedTiles())
        markCheckedSides()
        resetResult()
    }

    fun checkAllOptions() {
        // check top
        for (j in 0 until array[0].size) {
            checkOneOption(0, j, 1, 0)
        }
        // check bottom
        for (j in 0 until array[0].size) {
            checkOneOption(array.size-1, j, -1, 0)
        }
        // check left
        for (i in 0 until array.size) {
            checkOneOption(i, 0, 0, 1)
        }
        for (i in 0 until array.size) {
            checkOneOption(i, array[0].size-1, 0, -1)
        }
    }

    // return sum of all the array elements that are true
    fun energizedTiles() = result.sumOf { it.sumOf { list -> if(list.isNotEmpty()) 1.toInt() else 0 } }

}

fun main() {

    fun part1(input: List<String>): Int {
        val array = input.map { it.toCharArray() }.toTypedArray()
        val contraption = Contraption(array)
        contraption.followLightBeam(0, 0, 0, 1)
        return contraption.energizedTiles()
    }

    fun part2(input: List<String>): Int {
        val array = input.map { it.toCharArray() }.toTypedArray()
        val contraption = Contraption(array)
        contraption.checkAllOptions()
        return contraption.currentMax
    }

    val testInput = parseLines("Day16_test")
    check(part1(testInput) == 46)
    check(part2(testInput) == 51)


    val input = parseLines("Day16")
    println(part1(input))
    println(part2(input))
}
