package solutions23

class Brick(var occupiedSpace: List<Triple<Int, Int, Int>>) {

    companion object {
        fun parse(s: String): Brick {
            val split = s.split("~")
            val c1 = split[0].split(",").map { it.toInt() }
            val c2 = split[1].split(",").map { it.toInt() }
            return brickFromCoords(Triple(c1[0], c1[1], c1[2]), Triple(c2[0], c2[1], c2[2]))
        }

        fun brickFromCoords(c1: Triple<Int, Int, Int>, c2: Triple<Int, Int, Int>): Brick =
            if (c1.first == c2.first && c1.second == c2.second)
                Brick((c1.third..c2.third).map { Triple(c1.first, c1.second, it) })
            else if (c1.first == c2.first && c1.third == c2.third)
               Brick((c1.second..c2.second).map { Triple(c1.first, it, c1.third) })
            else
               Brick((c1.first..c2.first).map { Triple(it, c1.second, c1.third) })

    }

    var hasMoved = false

    fun coordsDown() = this.occupiedSpace.map { Triple(it.first, it.second, it.third-1) }

    fun moveDown() {
        this.occupiedSpace = coordsDown()
        hasMoved = true
    }

    fun onGround(): Boolean = occupiedSpace.any { it.third == 1 }

    fun intersects(coords: List<Triple<Int, Int, Int>>): Boolean = occupiedSpace.any { coords.contains(it) }
}

fun main() {

    fun movingBricks(bricks: List<Brick>): Boolean {
        for (brick in bricks.filter{ !it.onGround() }) {
            // try to decrement all z variables, if any triples intersect with any other brick coord
            // the brick can't move down
            val newCoords = brick.coordsDown()
            if(bricks.none { it != brick && it.intersects(newCoords) }) {
                return true
            }
        }
        return false
    }

    fun settleBricks(bricks: List<Brick>): List<Brick> {
        var moving = true
        while(moving) {
            moving = false
            for (brick in bricks.filter { !it.onGround() }) {
                // try to decrement all z variables, if any triples intersect with any other brick coord
                // the brick can't move down
                val newCoords = brick.coordsDown()
                if (bricks.none { it != brick && it.intersects(newCoords) }) {
                    brick.moveDown()
                    moving = true
                    continue
                }
            }
        }
        return bricks
    }

    fun setupBricks(input: List<String>): List<Brick> {
        val initBricks = input.map { Brick.parse(it) }.toMutableList()
        return settleBricks(initBricks)
    }

    fun countMovingBricks(bricks: List<Brick>): Int {
        settleBricks(bricks)
        return bricks.count { it.hasMoved }
    }

    fun part1(settledBricks: List<Brick>): Int {
        var counter = 0
        for(brick in settledBricks) {
            val remaining = settledBricks.minus(brick)
            if(!movingBricks(remaining)) counter++
        }
        return counter
    }

    fun part2(settledBricks: List<Brick>): Int {
        var counter = 0
        for(i in settledBricks.indices) {
            // clone remaining bricks
            val remaining = settledBricks.take(i).plus(settledBricks.drop(i+1)).map { Brick(it.occupiedSpace) }
            counter += countMovingBricks(remaining)
            if(i % 100 == 0) println("$i of ${settledBricks.size}")
        }
        return counter
    }

    val testInput = parseLines("Day22_test")
    val settledTest = setupBricks(testInput)
    check(part1(settledTest) == 5)
    check(part2(settledTest) == 7)


    val input = parseLines("Day22")
    val settled = setupBricks(input)
    println(part1(settled))
    println(part2(settled))

    //This is slow, try to optimize by moving the bricks down all the way, not just one step at a time.
}
