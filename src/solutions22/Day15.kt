package solutions22

import java.lang.Integer.max
import java.lang.Integer.min
import kotlin.math.absoluteValue

fun main() {
    class Sensor(val x: Int, val y: Int, val bx: Int, val by: Int) {
        // Manhattan distance
        val dist = (x - bx).absoluteValue + (y - by).absoluteValue

        fun coveredPoss(row: Int, set: MutableSet<Int>) {
            val d = dist - (y - row).absoluteValue
            if(d >= 0) set.addAll(x-d..x+d)
        }

        fun coveredPossInRange(row: Int, set: MutableSet<IntRange>, max: Int) {
            val d = dist - (y - row).absoluteValue
            if(d >= 0) set.add(max(x-d, 0)..min(x+d, max))
        }
    }

    fun parseSensorLine(input: String): Sensor {
        val xPattern = "(x=)(-?[0-9]+)".toRegex()
        val yPattern = "(y=)(-?[0-9]+)".toRegex()
        val xs = xPattern.findAll(input).map { it.groupValues[2].toInt() }.toList()
        val ys = yPattern.findAll(input).map { it.groupValues[2].toInt() }.toList()
        return Sensor(xs.elementAt(0), ys.elementAt(0), xs.elementAt(1), ys.elementAt(1))
    }

    fun part1(sensors: List<Sensor>, row: Int): Int {
        val beaconsInRow: MutableSet<Int> = mutableSetOf()
        val coveredBySensors: MutableSet<Int> = mutableSetOf()
        sensors.forEach { sensor ->
            if(sensor.by == row)
                beaconsInRow.add(sensor.bx)
        }
        sensors.forEach { it.coveredPoss(row, coveredBySensors) }
        return (coveredBySensors-beaconsInRow).size
    }

    fun part2(sensors: List<Sensor>, size: Int): Long {
        for(i in 0 .. size) {
            val impossiblePos = sortedSetOf<IntRange>( Comparator { o1, o2 ->
                if(o1 == o2) 0
                else if(o1.first < o2.first) -1
                else if(o1.first == o2.first && o1.last < o2.last) -1
                else 1
            })
            sensors.forEach { it.coveredPossInRange(i, impossiblePos, size) }
            // find out whether there is a free tile in this row
            var range = impossiblePos.first()
            for(j in 1 until impossiblePos.size) {
                val nextRange = impossiblePos.elementAt(j)
                if(range.contains(nextRange.first)) {
                    if(range.last < nextRange.last)
                        range = range.first .. nextRange.last
                } else {
                    val col = range.last + 1
                    return 4000000L * col + i
                }
            }
        }

        return -1
    }


//    val test = parseLines("Day15_test")
//    val testSensors = test.map { line -> parseSensorLine(line) }
//    check(part1(testSensors, 10) == 26)
//    check(part2(testSensors, 20) == 56000011L)

    val start = System.currentTimeMillis()
    val input = parseLines("Day15")
    val sensors = input.map { line -> parseSensorLine(line) }
//    println(part1(sensors, 2000000))
    println(part2(sensors, 4000000))
    println("time: " + (System.currentTimeMillis() - start))
}
