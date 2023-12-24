package solutions23

abstract class Module(val name: String, val destinations: List<String>) {
    // 0 for low pulse, 1 for high pulse
    // returns list of new destinations and new pulse
    abstract fun handlePulse(pulse: Int, sender: Module): List<Triple<String, Int, Module>>
    abstract fun exportState(): String
    open fun addPredecessor(predecessor: Module) {}
}

class FliFlopModule(name: String, destinations: List<String>) : Module(name, destinations) {
    private var on = 0
    override fun handlePulse(pulse: Int, sender: Module): List<Triple<String, Int, Module>> = when(pulse) {
        0 -> {
            on = (on+1)%2
            destinations.map { Triple(it, on, this) }
        }
        else -> emptyList()
    }

    override fun exportState() = on.toString()

}

class ConjunctionModule(name: String, destinations: List<String>) : Module(name, destinations) {
    // remember the pulses from all predecessors
    var predecessorMap = mutableMapOf<Module, Int>()

    override fun handlePulse(pulse: Int, sender: Module): List<Triple<String, Int, Module>> {
        predecessorMap[sender] = pulse
        return if(predecessorMap.values.all { it == 1 }) {
            destinations.map { Triple(it, 0, this) }
        } else {
            destinations.map { Triple(it, 1, this) }
        }
    }

    override fun exportState() = predecessorMap.values.joinToString(", ")

    override fun addPredecessor(predecessor: Module) {
        predecessorMap[predecessor] = 0
    }
}

class BroadcasterModule(name: String, destinations: List<String>) : Module(name, destinations) {
    override fun handlePulse(pulse: Int, sender: Module): List<Triple<String, Int, Module>> =
        destinations.map { Triple(it, pulse, this) }

    override fun exportState() = ""
}

fun main() {

    fun pushButton(moduleMap: Map<String, Module>): Triple<Int, Int, Map<String, String>> {
        var countLowPulse = 0
        var countHighPulse = 0
        // initialize with broadcaster, low pulse and some random sender, since sender is not important for first transmission
        var transmission = listOf(Triple("broadcaster", 0,moduleMap.values.first()))
        while(transmission.isNotEmpty()) {
            val newList = mutableListOf<Triple<String, Int, Module>>()
            for((moduleString, pulse, sender) in transmission) {
                val module = moduleMap[moduleString]
                if(module != null) {
                    newList.addAll(module.handlePulse(pulse, sender))
                }
                // increment corresponding counters
                when(pulse) {
                    0 -> countLowPulse++
                    1 -> countHighPulse++
                }
            }
            transmission = newList
        }
        return Triple(countLowPulse, countHighPulse, moduleMap.mapValues { it.value.exportState() })
    }


    // return lowPulses, highPulses for each element of the cycle
    fun simulateButtonPresses(moduleMap: Map<String, Module>, cutoff: Int): List<Pair<Int, Int>> {
        val states = mutableListOf(moduleMap.mapValues { it.value.exportState() })
        val pulses = mutableListOf<Pair<Int, Int>>()
        while(pulses.size < cutoff) {
            val (lowPulse, highPulse, newState) = pushButton(moduleMap)
            pulses.add(Pair(lowPulse, highPulse))
            states.add(newState)
        }
        return pulses
    }

    // return cycle length
    fun detectCycle(moduleMap: Map<String, Module>, moduleName: String): Int {
        val states = mutableListOf(moduleMap.mapValues { it.value.exportState() })
        while(true) {
            val (_, _, newState) = pushButton(moduleMap)
            if(newState[moduleName] == "1" && states.last()[moduleName] == "0") {
                println("${states.size}: got high pulse")
            }
            if(states.contains(newState)) {
                val highPulses = states.indices.filter { states[it][moduleName] == "1" }
                return states.size
            }
            states.add(newState)
        }
    }


    fun initModules(input: List<String>): Map<String, Module> {
        val moduleMap = mutableMapOf<String, Module>()
        for(line in input) {
            val split = line.split(" -> ")
            val destinations = split[1].split(", ")
            val (name, module) =
                if(split[0].contains("%")) {
                    val name = split[0].removePrefix("%")
                    Pair(name, FliFlopModule(name, destinations))
                } else if(split[0].contains("&")) {
                    val name = split[0].removePrefix("&")
                    Pair(name, ConjunctionModule(name, destinations))
                } else {
                    val name = "broadcaster"
                    Pair(name, BroadcasterModule(name, destinations))
                }
            moduleMap[name] = module
        }
        // initialize predecessors for all conjunction modules
        for(pred in moduleMap.values) {
            for(dest in pred.destinations) {
                moduleMap[dest]?.addPredecessor(pred)
            }
        }
        return moduleMap
    }

    fun part1(input: List<String>, totalButtonPresses: Int = 1000): Long {
        val moduleMap = initModules(input)
        val pulses = simulateButtonPresses(moduleMap, totalButtonPresses)
        val lowPulse = pulses.sumOf { it.first }.toLong()
        val highPulse = pulses.sumOf { it.second }.toLong()
        return lowPulse * highPulse
    }

    fun part2(input: List<String>): Long {
        val moduleMap = initModules(input)
        // solution by inspecting the input:
        // The predecessor of rx is module tg which is a ConjunctionModule.
        // It only sends a low pulse if all its predecessors have just received a high pulse.
        // Moreover, all predecessors only send their pulse to tg, no other module.
        // idea: inspect problem where we remove all except one of these predecessors
        // and find cycle length. Look for iteration where this predecessor receives a high pulse.
        val tg = moduleMap["tg"] as ConjunctionModule
        simulateButtonPresses(moduleMap, 2)
        val preds = tg.predecessorMap.keys.map { it.name }
        val cycles = preds.map { module ->
            detectCycle(moduleMap.filterKeys { (!preds.contains(it) || it == module) && it != "tg"}, module).toLong()
        }
        return lcmList(cycles)
    }

    val testInput = parseLines("Day20_test")
    check(part1(testInput) == 32000000L)
    val testInput2 = parseLines("Day20_test2")
    check(part1(testInput2) == 11687500L)

    val input = parseLines("Day20")
    println(part1(input))
    println(part2(input))
}
