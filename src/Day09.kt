import kotlin.math.absoluteValue

fun main() {

    class Rope(var head: Pair<Int, Int>, var tail: Pair<Int, Int>) {

        val visited: MutableSet<Pair<Int, Int>> = mutableSetOf(tail)

        fun updateTail(x: Int, y: Int) {
            tail = Pair(x, y)
            visited.add(tail)
        }

        fun moveHorizontal(direction: Int, steps: Int) {
            if (steps > 0) {
                head = head.copy(first = head.first + direction)
                if ((head.first - tail.first).absoluteValue > 1 && head.second == tail.second) {
                    updateTail(tail.first + direction, tail.second)
                } else if ((head.first - tail.first).absoluteValue > 1 && head.second != tail.second) {
                    val vertDir = if (tail.second - head.second > 0) -1 else 1
                    updateTail(tail.first + direction, tail.second + vertDir)
                }
                moveHorizontal(direction, steps - 1)
            }
        }

        fun moveVertical(direction: Int, steps: Int) {
            if (steps > 0) {
                head = head.copy(second = head.second + direction)
                if ((head.second - tail.second).absoluteValue > 1 && head.first == tail.first) {
                    updateTail(tail.first, tail.second + direction)
                } else if ((head.second - tail.second).absoluteValue > 1 && head.first != tail.first) {
                    val hDir = if (tail.first - head.first > 0) -1 else 1
                    updateTail(tail.first + hDir, tail.second + direction)
                }
                moveVertical(direction, steps - 1)
            }
        }
    }

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
        fun moveHorizontal(direction: Int, steps: Int) {
            if(steps > 0) {
                knotPos[0] = knotPos[0].copy(first = knotPos[0].first+direction)
                updateTail()
                moveHorizontal(direction, steps-1)
            }
        }

        fun moveVertical(direction: Int, steps: Int) {
            if(steps > 0) {
                knotPos[0] = knotPos[0].copy(second = knotPos[0].second+direction)
                updateTail()
                moveVertical(direction, steps-1)
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
                "R" -> rope.moveHorizontal(1, dist)
                "L" -> rope.moveHorizontal(-1, dist)
                "U" -> rope.moveVertical(-1, dist)
                "D" -> rope.moveVertical(1, dist)
            }
        }
        return rope
    }

    fun part1(input: List<String>): Int {
        val rope = Rope(Pair(0,0), Pair(0,0))
        for (line in input) {
            val splitLine = line.split(" ")
            val direction = splitLine[0]
            val dist = splitLine[1].toInt()
            when (direction) {
                "R" -> rope.moveHorizontal(1, dist)
                "L" -> rope.moveHorizontal(-1, dist)
                "U" -> rope.moveVertical(-1, dist)
                "D" -> rope.moveVertical(1, dist)
            }
        }
        return rope.visited.size
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
