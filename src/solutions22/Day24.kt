package solutions22

import java.util.SortedSet
import kotlin.math.absoluteValue

fun main() {
    class Blizzard(var xPos: Int, var yPos: Int, val moveX: Int, val moveY: Int) {
        fun updatePos(maxX: Int, maxY: Int) {
            xPos = if(xPos + moveX >= maxX) 1 else if(xPos + moveX <= 0) maxX-1 else xPos + moveX
            yPos = if(yPos + moveY >= maxY) 1 else if(yPos + moveY <= 0) maxY-1 else yPos + moveY
        }
    }

    data class PosTime(val x: Int, val y: Int, val time: Int)

    fun getPosTimeComp(goalX: Int, goalY: Int) =  Comparator<PosTime> { a, b ->
        if(a == null || b == null)  0
        else if(a.time == b.time && a.x == b.x && a.y == b.y) 0
        else {
            val aEst = a.time + (goalX - a.x).absoluteValue + (goalY - a.y).absoluteValue
            val bEst = b.time + (goalX - b.x).absoluteValue + (goalY - b.y).absoluteValue
            if(aEst <= bEst) -1
            else 1
        }
    }

    class Valley(val startX: Int, val startY: Int,
                 val goalX: Int, val goalY: Int,
                 val wallX: Int, val wallY: Int,
                 blizzardsLR: List<Blizzard>, blizzardsUD: List<Blizzard>) {

        val occupiedLR: MutableList<Array<Array<Boolean>>> = mutableListOf()
        val occupiedUD: MutableList<Array<Array<Boolean>>> = mutableListOf()

        init {
            // blizzards repeat
            for(i in 0 until wallX-1) {
                val array = Array(wallY+1) { i -> Array(wallX+1)
                    // mark walls as occupied
                    { j -> j==0 || j == wallX || (i==0 && j!=1) || (i==wallY && j!= wallX-1) } }
                for(bliz in blizzardsLR) {
                    array[bliz.yPos][bliz.xPos] = true
                    bliz.updatePos(wallX, wallY)
                }
                occupiedLR.add(array)
            }
            for(i in 0 until wallY-1) {
                val array = Array(wallY+1) { i -> Array(wallX+1)
                // mark walls as occupied
                { j -> j==0 || j == wallX || (i==0 && j!=1) || (i==wallY && j!= wallX-1) } }
                for(bliz in blizzardsUD) {
                    array[bliz.yPos][bliz.xPos] = true
                    bliz.updatePos(wallX, wallY)
                }
                occupiedUD.add(array)
            }
        }

        fun determineNeighbours(posTime: PosTime): List<PosTime> {
            // check which of the five options are not occupied
            val nexts: MutableList<PosTime> = mutableListOf()
            val x = posTime.x
            val y = posTime.y
            val t = posTime.time+1
            if(!occupiedLR[t%occupiedLR.size][y][x] && !occupiedUD[t%occupiedUD.size][y][x])
                nexts.add(PosTime(x, y, t))
            if(y+1 <= wallY && !occupiedLR[t%occupiedLR.size][y+1][x] && !occupiedUD[t%occupiedUD.size][y+1][x])
                nexts.add(PosTime(x, y+1, t))
            if(y-1 >= 0 && !occupiedLR[t%occupiedLR.size][y-1][x] && !occupiedUD[t%occupiedUD.size][y-1][x])
                nexts.add(PosTime(x, y-1, t))
            if(x+1 <= wallX && !occupiedLR[t%occupiedLR.size][y][x+1] && !occupiedUD[t%occupiedUD.size][y][x+1])
                nexts.add(PosTime(x+1, y, t))
            if(x-1 >= 0 && !occupiedLR[t%occupiedLR.size][y][x-1] && !occupiedUD[t%occupiedUD.size][y][x-1])
                nexts.add(PosTime(x-1, y, t))
            return nexts
        }

        fun dijkstraDist(startX: Int = this.startX, startY: Int = this.startY,
                         goalX: Int = this.goalX, goalY: Int = this.goalY,
                         time: Int = 0): Int {
            val visited: MutableSet<PosTime> = mutableSetOf()
            val queue: SortedSet<PosTime> = sortedSetOf(getPosTimeComp(goalX, goalY),PosTime(startX, startY, time))
            while(queue.isNotEmpty()) {
                val next = queue.first()
                if(next.x == goalX && next.y == goalY) return next.time
                queue.remove(next)
                visited.add(next)
                queue.addAll(determineNeighbours(next) - visited)
            }
            return -1
        }
    }

    fun parseBlizzards(input: List<String>): Pair<List<Blizzard>, List<Blizzard>> {
        val blizzardLR: MutableList<Blizzard> = mutableListOf()
        val blizzardUD: MutableList<Blizzard> = mutableListOf()
        for(i in input.indices) {
            for(j in input[i].indices) {
                when(input[i][j]) {
                    '>' -> blizzardLR.add(Blizzard(j, i, 1, 0))
                    '<' -> blizzardLR.add(Blizzard(j, i, -1, 0))
                    'v' -> blizzardUD.add(Blizzard(j, i, 0, 1))
                    '^' -> blizzardUD.add(Blizzard(j, i, 0, -1))
                }
            }
        }
        return Pair(blizzardLR, blizzardUD)
    }

    fun part1(input: List<String>): Int {
        val (testBlizLR, testBlizUD) = parseBlizzards(input)
        val valleyTest = Valley(1, 0, input[0].length-2, input.size-1, input[0].length-1, input.size-1, testBlizLR, testBlizUD)
        return valleyTest.dijkstraDist()
    }

    fun part2(input: List<String>): Int {
        val (testBlizLR, testBlizUD) = parseBlizzards(input)
        val valleyTest = Valley(1, 0, input[0].length-2, input.size-1, input[0].length-1, input.size-1, testBlizLR, testBlizUD)
        val first = valleyTest.dijkstraDist()
        val second = valleyTest.dijkstraDist(valleyTest.goalX, valleyTest.goalY, valleyTest.startX, valleyTest.startY, first)
        return valleyTest.dijkstraDist(time = second)
    }



    val test = parseLines("Day24_test")

    check(part1(test) == 18)
    check(part2(test) == 54)



    val start = System.currentTimeMillis()

    val input = parseLines("Day24")
//    println(part1(input))
    println(part2(input))

    println("time: " + (System.currentTimeMillis() - start))

}
