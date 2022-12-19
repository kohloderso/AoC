fun main() {
    data class Valve(val name: String, val flowRate: Int, val connections: List<String>)

    data class State(val currentValve: Int, val closedValves: Set<Int>,
                     val totalFlow: Int = 0, var timer: Int = 30)

    data class State2(val meCurrentValve: Int, val eleCurrentValve: Int, val closedValves: Set<Int>,
                     val totalFlow: Int = 0, var meTimer: Int = 26, var eleTimer: Int = 26, var meActive: Boolean = true)


    // Floyd Warshall like method
    fun computeDistances(input: List<Valve>): Array<Array<Int?>> {
        val array = Array(input.size) { arrayOfNulls<Int>(input.size) }
        // initialize array
        for (i in array.indices) {
            array[i][i] = 0
            for (connection in input[i].connections) {
                val id = input.indexOfFirst { it.name == connection }
                array[i][id] = 1
            }
        }
        floydWarshall(array)
        return array
    }

    fun part1(input: List<Valve>): Int {
        val distances = computeDistances(input)
        val currentId = input.indexOfFirst { it.name == "AA" }
        val closedValves = input.withIndex().filter { it.value.flowRate > 0}.map { it.index } // not interested in valves that don't add to flow rate
        val initState = State(currentId, closedValves.toSet())
        val states = mutableListOf(initState)
        val finishedStates: MutableSet<State> = mutableSetOf()
        while (states.isNotEmpty()) {
            val current = states[0]
            // compute all possible next states
            var nextStatePossible = false
            for(valve in current.closedValves) {
                val dist = distances[current.currentValve][valve]
                if(dist != null) {
                    val remainingTime = current.timer - (dist + 1)
                    if(remainingTime >= 0) {
                        states.add(State(valve, current.closedValves-valve,
                            current.totalFlow + input[valve].flowRate*remainingTime,
                            remainingTime))
                        nextStatePossible = true
                    }
                }
            }
            states.remove(current)
            if(!nextStatePossible) {
                current.timer = 0 // no next state possible - let timer run out
                finishedStates.add(current)
            }
        }

        return finishedStates.maxOf { it.totalFlow }
    }

    fun computeMaxFlow(current: State2, input: List<Valve>, distances: Array<Array<Int?>>): Int {
        val results = mutableListOf(current.totalFlow)
        if(current.meActive && current.meTimer < 26) results.add(computeMaxFlow(current.copy(meActive = false), input, distances))
        for(valve in current.closedValves) {
            if (current.meTimer == 26 && current.eleTimer == 26) println(valve)
            if (current.meActive) {
                val distMe = distances[current.meCurrentValve][valve]
                if (distMe != null) {
                    val remainingTime = current.meTimer - (distMe + 1)
                    if (remainingTime >= 0) {
                        results.add(
                            computeMaxFlow(
                                State2(
                                    valve, current.eleCurrentValve, current.closedValves - valve,
                                    current.totalFlow + input[valve].flowRate * remainingTime,
                                    remainingTime, current.eleTimer, true
                                ), input, distances
                            )
                        )
                    }
                }
            } else {
                val distEle = distances[current.eleCurrentValve][valve]
                if (distEle != null) {
                    val remainingTime = current.eleTimer - (distEle + 1)
                    if (remainingTime >= 0) {
                        results.add(
                            computeMaxFlow(
                                State2(
                                    current.meCurrentValve, valve, current.closedValves - valve,
                                    current.totalFlow + input[valve].flowRate * remainingTime,
                                    current.meTimer, remainingTime, false
                                ), input, distances
                            )
                        )
                    }
                }
            }
        }
        return results.max()
    }

    fun part2(input: List<Valve>): Int {
        val distances = computeDistances(input)
        val currentId = input.indexOfFirst { it.name == "AA" }
        val closedValves = input.withIndex().filter { it.value.flowRate > 0}.map { it.index } // not interested in valves that don't add to flow rate
        val initState = State2(currentId, currentId, closedValves.toSet())
        return (computeMaxFlow(initState, input, distances))
    }



    fun parseValves(input: List<String>): List<Valve> {
        val result: MutableList<Valve> = mutableListOf()
        for (s in input) {
            val split = s.split(" ")
            val name = split[1]
            val flowRate = split[4].split('=')[1].dropLast(1).toInt()
            val connections = split.drop(9).map { it.take(2) } // get rid of trailing commas
            result.add(Valve(name, flowRate, connections))
        }
        return result
    }

    val test = parseLines("Day16_test")
    val testValves = parseValves(test)
    check(part1(testValves) == 1651)

    println(part2(testValves))

    val start = System.currentTimeMillis()

    val input = parseLines("Day16")
    val valves = parseValves(input)
//    println(part1(valves))Day16.kt
    println(part2(valves))

    println("time: " + (System.currentTimeMillis() - start))

}
