package solutions23

import kotlin.math.max

fun parseColoredCubes(s: String): ColoredCubes {
    var red = 0
    var green = 0
    var blue = 0
    val splitted = s.trim().split(",")
    for(item in splitted) {
        val numberColor = item.trim().split(" ")
        if(numberColor[1].contains("red")) red = numberColor[0].toInt()
        if(numberColor[1].contains("green")) green = numberColor[0].toInt()
        if(numberColor[1].contains("blue")) blue += numberColor[0].toInt()
    }
    return ColoredCubes(red, green, blue)
}

data class ColoredCubes(var red: Int, var green: Int, var blue: Int)

class Bag(val content: ColoredCubes = ColoredCubes(0, 0,0)) {

    private fun validateRound(round: ColoredCubes): Boolean =
        round.red <= content.red && round.blue <= content.blue && round.green <= content.green

    // read in string like "Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green"
    // separate by ";" into rounds and check for each round whether it is valid
    // If game was valid, return its ID, otherwise return 0
    fun validateGame(s: String): Int {
        val splitted = s.split(":")
        val id = splitted[0].split(" ")[1].toInt()
        val rounds = splitted[1].split(";")
        for(round in rounds) {
            if(!validateRound(parseColoredCubes(round))) {
                return 0
            }
        }
        return id
    }

    fun updateRound(round: ColoredCubes) {
        content.red = max(round.red, content.red)
        content.green = max(round.green, content.green)
        content.blue = max(round.blue, content.blue)
    }

    fun updateGame(s: String) {
        val splitted = s.split(":")
        val rounds = splitted[1].split(";")
        rounds.forEach {updateRound(parseColoredCubes(it)) }
    }

    fun returnPower(s: String): Int {
        updateGame(s)
        return content.red * content.green * content.blue
    }
}

fun main() {

    fun part1(input: List<String>): Int {
        val bag = Bag(ColoredCubes(12, 13, 14))
        return input.sumOf { bag.validateGame(it) }
    }

    fun part2(input: List<String>): Int = input.sumOf { Bag().returnPower(it) }

    // test if implementation meets criteria from the description:
    val testInput = parseLines("Day02_test")
    check(part1(testInput) == 8)
    check(part2(testInput) == 2286)

    val input = parseLines("Day02")
    println(part1(input))
    println(part2(input))
}
