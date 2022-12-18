import kotlin.math.absoluteValue

fun main() {

    fun part1(input: List<List<Int>>): Int {
        var sideCounter = 0
        for (cube in input) {
            sideCounter += 6
            // check if two of the three entries are the same as for some other cube
            val adj = input.filter { (it[0] == cube[0] && it[1] == cube[1] && (it[2] - cube[2]).absoluteValue == 1)
                    || ((it[0] - cube[0]).absoluteValue == 1 && it[1] == cube[1] && it[2] == cube[2])
                    || (it[0] == cube[0] && (it[1] - cube[1]).absoluteValue==1 && it[2] == cube[2])}.size
            sideCounter -= adj
        }
        return sideCounter
    }

    fun part2(input: List<List<Int>>): Int {
        val freeAirCubes: MutableSet<List<Int>> = mutableSetOf()
        val blockedAirCubes: MutableSet<List<Int>> = mutableSetOf()
        val rockCubes = input.toSet()
        val minx = rockCubes.minOf { it[0] }
        val miny = rockCubes.minOf { it[1] }
        val minz = rockCubes.minOf { it[2] }
        val maxx = rockCubes.maxOf { it[0] }
        val maxy = rockCubes.maxOf { it[1] }
        val maxz = rockCubes.maxOf { it[2] }
        var sideCounter = 0

        fun isFree(air: List<Int>): Boolean? {
            if(freeAirCubes.contains(air)) return true
            if(blockedAirCubes.contains(air) || rockCubes.contains(air)) return false
            // airCube is free if one of its coordinates is smaller than the minimum of any cube
            // or larger than the maximum of any cube
            if(air[0] <= minx || air[0] >= maxx || air[1] <= miny || air[1] >= maxy || air[2] <= minz || air[2] >= maxz){
                freeAirCubes.add(air)
                return true
            }
            return null
        }

        fun checkFreeAirPocket(air: List<Int>): Boolean {
            if (isFree(air) != null) return isFree(air)!!

            val visited: MutableSet<List<Int>> = mutableSetOf(air)
            val adjacentSides = mutableSetOf(
                listOf(air[0] + 1, air[1], air[2]),
                listOf(air[0] - 1, air[1], air[2]),
                listOf(air[0], air[1] + 1, air[2]),
                listOf(air[0], air[1] - 1, air[2]),
                listOf(air[0], air[1], air[2] + 1),
                listOf(air[0], air[1], air[2] - 1)
            )
            adjacentSides.removeAll(rockCubes)
            adjacentSides.removeAll(blockedAirCubes)

            while (adjacentSides.isNotEmpty()) {
                val next = adjacentSides.find { true }!!
                adjacentSides.remove(next)
                visited.add(next)
                if (isFree(next) != null && isFree(next)!!) {
                    freeAirCubes.addAll(visited)
                    return true
                } else {
                    adjacentSides.addAll(setOf(
                        listOf(next[0] + 1, next[1], next[2]),
                        listOf(next[0] - 1, next[1], next[2]),
                        listOf(next[0], next[1] + 1, next[2]),
                        listOf(next[0], next[1] - 1, next[2]),
                        listOf(next[0], next[1], next[2] + 1),
                        listOf(next[0], next[1], next[2] - 1)
                    ))
                }
                adjacentSides.removeAll(rockCubes)
                adjacentSides.removeAll(blockedAirCubes)
                adjacentSides.removeAll(visited)
            }
            blockedAirCubes.addAll(visited)
            return false
        }


        for (cube in input) {
            // check if two of the three entries are the same as for some other cube
            val adjacentSides = mutableSetOf(
                listOf(cube[0]+1, cube[1], cube[2]),
                listOf(cube[0]-1, cube[1], cube[2]),
                listOf(cube[0], cube[1]+1, cube[2]),
                listOf(cube[0], cube[1]-1, cube[2]),
                listOf(cube[0], cube[1], cube[2]+1),
                listOf(cube[0], cube[1], cube[2]-1)
            )
            adjacentSides.removeAll(rockCubes)
            for (adj in adjacentSides) {
                if(!freeAirCubes.contains(adj) && !blockedAirCubes.contains(adj)) { // this has not yet been explored
                    checkFreeAirPocket(adj)
                }
            }
            adjacentSides.removeAll(blockedAirCubes)
            sideCounter += adjacentSides.size
        }
        return sideCounter
    }


    val test = parseLines("Day18_test").map { it.split(",").map { x -> x.toInt() }}

    check(part1(test) == 64)

    check(part2(test) == 58)

    val input = parseLines("Day18").map { it.split(",").map { x -> x.toInt() }}
    println(part1(input))
    println(part2(input))

}
