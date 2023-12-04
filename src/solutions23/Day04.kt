package solutions23

import kotlin.math.pow


class ScratchCard(val winningNumbers: List<Int>, val numbers: List<Int>) {

    companion object {
        fun create(s: String): ScratchCard {
            val nums = s.split(":")[1].split("|")
            val winningNumbers = nums[0].trim().split(" ").filterNot { it==""}.map { it.toInt() }
            val numbers = nums[1].trim().split(" ").filterNot { it==""}.map { it.toInt() }
            return ScratchCard(winningNumbers, numbers)
        }
    }

    fun computeScore(): Double {
        var counter = -1
        for(number in numbers) {
            if(winningNumbers.contains(number)) counter += 1
        }
        return if(counter >= 0) 2.0.pow(counter)
        else 0.0
    }

}

fun main() {

    fun part1(input: List<String>): Int {
        val result = input.sumOf { ScratchCard.create(it).computeScore() }
        return result.toInt()
    }

    fun part2(input: List<String>): Int = EngineScheme.computeGearRatios(input)

    // test if implementation meets criteria from the description:
    val testInput = parseLines("Day04_test")
    check(part1(testInput) == 13)
//    check(part2(testInput) == 467835)

    val input = parseLines("Day04")
    println(part1(input))
//    println(part2(input))
}
