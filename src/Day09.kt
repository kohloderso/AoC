import kotlin.math.absoluteValue

fun main() {

    class ExtendedRope(val knotPos: MutableList<Pair<Int, Int>>) {

        val visited: MutableSet<Pair<Int, Int>> = mutableSetOf(knotPos.last())

        fun updateTail(i: Int) {
            if((knotPos[i-1].first - knotPos[i].first).absoluteValue > 1) {
                val hDir = if (knotPos[i - 1].first - knotPos[i].first > 0) 1 else -1
                val vDir = if ((knotPos[i - 1].second - knotPos[i].second).absoluteValue > 0) {
                    if (knotPos[i - 1].second - knotPos[i].second > 0) 1 else -1
                } else 0
                knotPos[i] = Pair(knotPos[i].first+hDir, knotPos[i].second+vDir)
            } else if ((knotPos[i-1].second - knotPos[i].second).absoluteValue > 1) {
                val vDir = if (knotPos[i - 1].second - knotPos[i].second > 0) 1 else -1
                val hDir = if ((knotPos[i - 1].first - knotPos[i].first).absoluteValue > 0) {
                    if (knotPos[i - 1].first - knotPos[i].first > 0) 1 else -1
                } else 0
                knotPos[i] = Pair(knotPos[i].first+hDir, knotPos[i].second+vDir)
            }
        }

        fun updateTail() {
            knotPos.indices.drop(1).map {updateTail(it)}
            visited.add(knotPos.last())
        }
        fun move(xDir: Int, yDir: Int, steps: Int) {
            if(steps > 0) {
                knotPos[0] =Pair(knotPos[0].first+xDir, knotPos[0].second+yDir)
                updateTail()
                move(xDir, yDir, steps-1)
            }
        }
    }

    fun moveNKnots(input: List<String>, n: Int): ExtendedRope {
        val rope = ExtendedRope( MutableList(n) { Pair(0,0) })
        for (line in input) {
            val splitLine = line.split(" ")
            val direction = splitLine[0]
            val dist = splitLine[1].toInt()
            when (direction) {
                "R" -> rope.move(1, 0, dist)
                "L" -> rope.move(-1, 0, dist)
                "U" -> rope.move(0, -1, dist)
                "D" -> rope.move(0,1, dist)
            }
        }
        return rope
    }

    fun part1(input: List<String>): Int {
        return moveNKnots(input, 2).visited.size
    }

    fun part2(input: List<String>): Int {
        return moveNKnots(input, 10).visited.size

    }


    val test = parseLines("Day09_test")
    check(part1(test) == 13)


    val input = parseLines("Day09")
    println(part1(input))

    check(moveNKnots(test, 2).visited.size == 13)
    check(moveNKnots(test, 10).visited.size == 1)

    println(part2(input))

}
