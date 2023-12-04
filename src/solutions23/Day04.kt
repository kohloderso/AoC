package solutions23

import kotlin.math.pow


class ScratchCard(val id: Int, val winningNumbers: List<Int>, val numbers: List<Int>) {

    // this is a bit hacky: -1 because we start counting at 0. Then for one winning number
    // we get 2^0 = 1 in part 1. In part 2 we use inclusive range for increasing the number of instances, so it also works out.
    private val countWinningNumbers: Int = numbers.filter { it in winningNumbers }.size -1

    companion object {
        fun create(s: String): ScratchCard {
            val id = s.split(":")[0].split(" ").filterNot { it==""}[1].toInt()
            val nums = s.split(":")[1].split("|")
            val winningNumbers = nums[0].split(" ").filterNot { it==""}.map { it.toInt() }
            val numbers = nums[1].split(" ").filterNot { it==""}.map { it.toInt() }
            return ScratchCard(id, winningNumbers, numbers)
        }
    }

    fun computeScore(): Double {
        return if(countWinningNumbers >= 0) 2.0.pow(countWinningNumbers)
        else 0.0
    }

    fun updateInstances(cardInstances: MutableList<Int>) {
        val currentInsts = cardInstances[id-1]
        // each of the next n entries will be increased by currentInsts
        for (i in id..id+countWinningNumbers) {
            cardInstances[i] += currentInsts
        }
    }

}

fun main() {

    fun part1(input: List<String>): Int {
        val result = input.sumOf { ScratchCard.create(it).computeScore() }
        return result.toInt()
    }

    fun part2(input: List<String>): Int {
        // create mutable list with entries initialized to 1
        val instances = MutableList(input.size) { 1 }
        input.forEach { ScratchCard.create(it).updateInstances(instances) }
        return instances.sum()
    }

    // test if implementation meets criteria from the description:
    val testInput = parseLines("Day04_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 30)

    val input = parseLines("Day04")
    println(part1(input))
    println(part2(input))
}
