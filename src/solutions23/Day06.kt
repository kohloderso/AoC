package solutions23

import java.math.BigInteger
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.pow
import kotlin.math.sqrt

class Race(val duration: Int, val recordDistance: Int) {

    fun solveIneq(): IntRange {
        val r1 = duration.toDouble()/2 + sqrt((duration.toDouble()/2).pow(2) - recordDistance)
        val r2 = duration.toDouble()/2 - sqrt((duration.toDouble()/2).pow(2) - recordDistance)
        return if(r1 < r2) ceil(r1+0.0001).toInt()..floor(r2-0.00001).toInt()
        else ceil(r2+0.00001).toInt()..floor(r1-0.00001).toInt() // hacky af
    }

    fun computeMargin(): Int = solveIneq().count()
}

class RaceBig(val duration: BigInteger, val recordDistance: BigInteger) {

    fun solveIneq(): Double {
        val r1 = duration.toDouble() / 2 + sqrt((duration.toDouble() / 2).pow(2) - recordDistance.toDouble())
        val r2 = duration.toDouble() / 2 - sqrt((duration.toDouble() / 2).pow(2) - recordDistance.toDouble())
        return if (r1 < r2) floor(r2 - 0.00001) - ceil(r1 + 0.00001) + 1
        else floor(r1 - 0.00001) - ceil(r2 + 0.00001) + 1
    }

}



fun main() {

    fun part1(input: List<String>): BigInteger {
        val times = input[0].split(":")[1].split(" ").filterNot { it == "" }.map { it.toInt() }
        val records = input[1].split(":")[1].split(" ").filterNot { it == "" }.map { it.toInt() }
        val races = times.zip(records).map { Race(it.first, it.second) }
        // multiply all margins of the races
        return races.map { it.computeMargin() }.fold(BigInteger.ONE) { acc, i -> acc * i.toBigInteger() }
    }

    fun part2(input: List<String>): Double {
        val time = input[0].split(":")[1].filterNot { it == ' '}.toBigInteger()
        val record = input[1].split(":")[1].filterNot { it == ' '}.toBigInteger()
        val race = RaceBig(time, record)
        return race.solveIneq()
    }

    // test if implementation meets criteria from the description:
    val testInput = parseLines("Day06_test")
    check(part1(testInput) == 288.toBigInteger())
    check(part2(testInput) == 71503.0)

    val input = parseLines("Day06")
    println(part1(input))
    println(part2(input).toLong())
}
