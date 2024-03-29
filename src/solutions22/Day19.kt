package solutions22

import java.lang.Integer.max
import kotlin.math.abs

fun main() {

    data class State(val resources: List<Int> = listOf(0,0,0,0), // entries are numbers of ore, clay, obsidian, geodes
                     val robots: List<Int> = listOf(1,0,0,0), var timer: Int = 24)

    class Blueprint(val makeRobots: List<(List<Int>) -> Pair<List<Int>, List<Int>>>, val maxRoboters: List<Int>) {

        fun computeNextStates(state: State): Set<State> {
            // if timer is up, nothing can be built
            if(state.timer <= 0) return emptySet()

            val nextStates: MutableSet<State> = mutableSetOf()
            for((neededRes, rob) in makeRobots.map { it(listOf(0,0,0,0)) }) {
                val newRobs = rob.zip(state.robots) {x, y -> x + y}
                // if max number of robots is not reached yet try to build robot
                if(newRobs.zip(maxRoboters) {x, y -> x <= y} .all { it } ) {
                    // compute time at which this type of robot can be built, null if it can't be built at all, because of missing robots
                    var time: Int? = 1 // minimum time is 1
                    for((index, res) in neededRes.withIndex()) {
                        if(res != 0) {
                            if(state.robots[index] != 0) {
                                time = max(time!!, Math.ceilDiv(abs(res) - state.resources[index],state.robots[index])+1)
                            } else {
                                time = null
                                break
                            }
                        }
                    }
                    if(time != null && state.timer - time >= 0) {
                        val newRes = state.resources.zip(state.robots) { x, y -> x + time*y }.zip(neededRes) { x, y -> x + y}
                        check(newRes.all { it >= 0 }) // sanity check
                        nextStates.add(State(newRes, newRobs, state.timer-time))
                    }
                }
            }
            if(nextStates.isEmpty()) {
                // if nothing else can be done, run down timer and harvest
                nextStates.add(State(
                    state.resources.zip(state.robots) { x, y -> x + state.timer * y }, // each robot harvests one of the corresponding resource per remaining time
                    state.robots,
                    0
                ))

            }
            return nextStates
        }
    }

    fun computeMaxGeodes(current: State, blueprint: Blueprint, currentMax: Int): Int {
        if(current.timer <= 0) {
            return if(current.resources.last() > currentMax) current.resources.last()
            else currentMax
        }
        if(current.timer == 1) {
            return if(current.resources.last()+current.robots.last() > currentMax) current.resources.last()+current.robots.last()
            else currentMax
        }

        // compute estimate of best possible maximum for this state
        var resourceEstimate = current.resources.withIndex().map { (i,resource) -> resource + current.timer * current.robots[i] + (1 until current.timer).sum() }
        var nGeodeRobots = 0
        for(i in 0 until current.timer) {
            val pair = blueprint.makeRobots[0](resourceEstimate)
            resourceEstimate = pair.first
            if(resourceEstimate.all { it >= 0}) nGeodeRobots++
        }
        val time = if(current.timer - nGeodeRobots >= 0) current.timer - nGeodeRobots else 0
        val bestEstimate = current.resources.last() + current.timer * current.robots.last() + (time until current.timer).sum()

        if(bestEstimate <= currentMax)
            return currentMax
        val nexts = blueprint.computeNextStates(current)
        var max = currentMax
        for(next in nexts) {
            val res = computeMaxGeodes(next, blueprint, max)
            if(res > max) max = res
        }
        return max
    }

    fun part1(input: List<Blueprint>): Int {
        var qualityLvl = 0
        // treat each blueprint separately
        for((i,blueprint) in input.withIndex()) {
            val start = System.currentTimeMillis()
            val maxGeodes = computeMaxGeodes(State(), blueprint,0)
            qualityLvl += maxGeodes * (i+1)
            println("$i finished")
            println("time: " + (System.currentTimeMillis() - start))
        }
        return qualityLvl
    }

    fun part2(input: List<Blueprint>): Int {
        var mul = 1
        // treat each blueprint separately
        for((i,blueprint) in input.withIndex().take(3)) {
            val start = System.currentTimeMillis()
            val maxGeodes = computeMaxGeodes(State(listOf(0,0,0,0), listOf(1,0,0,0), 32), blueprint,0)
            mul *= maxGeodes
            println("$i finished")
            println("time: " + (System.currentTimeMillis() - start))
        }
        return mul
    }

    fun parseBlueprints(input: List<String>): List<Blueprint> {
        val orePattern = "([0-9]+) ore".toRegex()
        val obsidianPattern = "([0-9]+) obsidian".toRegex()
        val clayPattern = "([0-9]+) clay".toRegex()
        val result: MutableList<Blueprint> = mutableListOf()
        for (s in input) {
            val split = s.split(":")[1].split(".").filter{ it != ""}
            val makeFunctions: MutableList<(List<Int>) -> Pair<List<Int>, List<Int>>> = mutableListOf()
            // add option to not build any robot
//            makeFunctions.add { resources -> Pair(resources, listOf(0,0,0,0)) }
            var maxOre = 0
            var maxClay = 0
            var maxObs = 0
            for ((i,robotText) in split.withIndex()) {
                val orePrice = (orePattern.find(robotText)?.groupValues?.get(1) ?: "0").toInt()
                if(orePrice > maxOre) maxOre = orePrice
                val clayPrice = (clayPattern.find(robotText)?.groupValues?.get(1) ?: "0").toInt()
                if(clayPrice > maxClay) maxClay = clayPrice
                val obsidianPrice = (obsidianPattern.find(robotText)?.groupValues?.get(1) ?: "0").toInt()
                if(obsidianPrice > maxObs) maxObs = obsidianPrice
                val price = listOf(orePrice, clayPrice, obsidianPrice, 0) // last 0 is geode price
                makeFunctions.add { resources ->
                    Pair(
                        resources.zip(price) { x, y -> x - y },
                        (0..3).map { if (it == i) 1 else 0 })
                }
            }
            result.add(Blueprint(makeFunctions.reversed(),listOf(maxOre, maxClay, maxObs, 100))) // reverse list to greedily make geode robots
        }
        return result
    }

    val test = parseLines("Day19_test")
    val testBlueprints = parseBlueprints(test)


//    check(part1(testBlueprints) == 33)

    val start = System.currentTimeMillis()
    val input = parseLines("Day19")
    val blueprints = parseBlueprints(input)
    println(part2(blueprints))

    println("time: " + (System.currentTimeMillis() - start))



}
