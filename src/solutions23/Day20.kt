package solutions23

abstract class Module(val destinations: List<String>) {
    // 0 for low pulse, 1 for high pulse
    // returns list of new destinations and new pulse
    abstract fun handlePulse(pulse: Int, sender: Module): List<Triple<String, Int, Module>>
    abstract fun exportState(): String
    open fun addPredecessor(predecessor: Module) {}
}

class FliFlopModule(destinations: List<String>) : Module(destinations) {
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

class ConjunctionModule(destinations: List<String>) : Module(destinations) {
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

class BroadcasterModule(destinations: List<String>) : Module(destinations) {
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

    // returns true if module with moduleName receives a low pulse
    fun pushButtonWithModuleCheck(moduleMap: Map<String, Module>, moduleName: String): Boolean {
        // initialize with broadcaster, low pulse and some random sender, since sender is not important for first transmission
        var transmission = listOf(Triple("broadcaster", 0,moduleMap.values.first()))
        while(transmission.isNotEmpty()) {
            val newList = mutableListOf<Triple<String, Int, Module>>()
            for((moduleString, pulse, sender) in transmission) {
                if(moduleString == moduleName && pulse == 0) return true
                val module = moduleMap[moduleString]
                if(module != null) {
                    newList.addAll(module.handlePulse(pulse, sender))
                }
            }
            transmission = newList
        }
        return false
    }

    // return lowPulses, highPulses for each element of the cycle
    fun detectCycle(moduleMap: Map<String, Module>, cutoff: Int): List<Pair<Int, Int>> {
        val states = mutableListOf(moduleMap.mapValues { it.value.exportState() })
        val pulses = mutableListOf<Pair<Int, Int>>()
        while(pulses.size < cutoff) {
            val (lowPulse, highPulse, newState) = pushButton(moduleMap)
            pulses.add(Pair(lowPulse, highPulse))
            if(states.contains(newState)) return pulses
            states.add(newState)
        }
        return pulses
    }

    // return lowPulses, highPulses for each element of the cycle
    fun detectLowPulse(moduleMap: Map<String, Module>, moduleName: String = "rx"): Long {
        var buttonPresses = 0L
        while(true) {
            val res = pushButtonWithModuleCheck(moduleMap, moduleName)
            buttonPresses++
            if(res) return buttonPresses
            if(buttonPresses % 1000000 == 0L) println("${buttonPresses} button presses")
        }
        return buttonPresses
    }


    fun initModules(input: List<String>): Map<String, Module> {
        val moduleMap = mutableMapOf<String, Module>()
        for(line in input) {
            val split = line.split(" -> ")
            val destinations = split[1].split(", ")
            val (name, module) =
                if(split[0].contains("%")) {
                    Pair(split[0].removePrefix("%"), FliFlopModule(destinations))
                } else if(split[0].contains("&")) {
                    Pair(split[0].removePrefix("&"), ConjunctionModule(destinations))
                } else {
                    Pair("broadcaster", BroadcasterModule(destinations))
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
        val cyclePulses = detectCycle(moduleMap, totalButtonPresses)
        val iterations = totalButtonPresses / cyclePulses.size
        val remainder = totalButtonPresses % cyclePulses.size
        val lowPulse = cyclePulses.sumOf { it.first } * iterations.toLong() + cyclePulses.take(remainder).sumOf { it. first }
        val highPulse = cyclePulses.sumOf { it.second } * iterations.toLong() + cyclePulses.take(remainder).sumOf { it.second }
        return lowPulse * highPulse
    }

    fun part2(input: List<String>): Long {
        val moduleMap = initModules(input)
        return detectLowPulse(moduleMap)
    }

    val testInput = parseLines("Day20_test")
    check(part1(testInput) == 32000000L)
    val testInput2 = parseLines("Day20_test2")
    check(part1(testInput2) == 11687500L)

    val input = parseLines("Day20")
    println(part1(input))
    println(part2(input))
}
