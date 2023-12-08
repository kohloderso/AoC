package solutions23


class NodeNetwork(val rlInstructions: String, val rlMap: Map<String, Pair<String, String>>) {

    fun countSteps(): Int {
        var steps = 0
        var currentPos = 0
        var currentNode = "AAA"
        var currentChar = rlInstructions[currentPos]
        while (currentNode != "ZZZ") {
            currentNode = when(currentChar) {
                'L' -> rlMap[currentNode]?.first?: currentNode
                'R' -> rlMap[currentNode]?.second?: currentNode
                else -> currentNode
            }

            steps++
            currentPos = (currentPos + 1) % rlInstructions.length
            currentChar = rlInstructions[currentPos]
        }
        return steps
    }

    // too slow, of course ...
    fun countSteps2(): Long {
        var steps:Long = 0
        var currentPos = 0
        var currentNodes = rlMap.keys.filter { it.endsWith("A") }
        var currentChar = rlInstructions[currentPos]
        while (currentNodes.any { !it.endsWith("Z") }) {
            currentNodes = currentNodes.map {currentNode ->
                when(currentChar) {
                    'L' -> rlMap[currentNode]?.first?: currentNode
                    'R' -> rlMap[currentNode]?.second?: currentNode
                    else -> currentNode
                }
            }
            steps++
            currentPos = (currentPos + 1) % rlInstructions.length
            currentChar = rlInstructions[currentPos]
            if(steps % 10000000 == 0L) println(steps)
            if(steps == Long.MAX_VALUE)  {
                println("reached Limit")
                return Long.MAX_VALUE
            }
        }
        return steps
    }

    fun determineCycle(s: String): List<Pair<String, Int>> {
        val stepMap: MutableList<Pair<String, Int>> = mutableListOf()
        var currentPos = 0
        var currentChar = rlInstructions[currentPos]
        var currentNode = s
        while(!stepMap.contains(Pair(currentNode, currentPos))) {
            stepMap.add(Pair(currentNode, currentPos))
            currentNode = when(currentChar) {
                'L' -> rlMap[currentNode]?.first?: currentNode
                'R' -> rlMap[currentNode]?.second?: currentNode
                else -> currentNode
            }
            currentPos = (currentPos + 1) % rlInstructions.length
            currentChar = rlInstructions[currentPos]
        }
        return stepMap.dropWhile { it!= Pair(currentNode, currentPos) }
    }

    fun countStepsSmart(): Long {
        val startNodes = rlMap.keys.filter { it.endsWith("A") }
        val cycles = startNodes.map { determineCycle(it) }
        val lcm = lcmList(cycles.map { it.size.toLong() })
        return lcm
    }
}


fun main() {

    fun parseMapEntry(entry: String): Pair<String, Pair<String, String>> {
        val split = entry.split(" = ")
        val key = split[0].trim()
        val values = split[1].trim('(', ')').split(", ")
        return key to Pair(values[0], values[1])
    }

    fun part1(input: List<List<String>>): Int {
        val rlInstructions = input[0][0]
        val rlMap = input[1].associate { parseMapEntry(it) }
        val network = NodeNetwork(rlInstructions, rlMap)
        return network.countSteps()
    }

    fun part2(input: List<List<String>>): Long {
        val rlInstructions = input[0][0]
        val rlMap = input[1].associate { parseMapEntry(it) }
        val network = NodeNetwork(rlInstructions, rlMap)
        return network.countStepsSmart()
    }

    val testInput = parseChunks("Day08_test")
    check(part1(testInput) == 2)
    val testInput2 = parseChunks("Day08_test2")
    check(part2(testInput2) == 6L)

    val input = parseChunks("Day08")
    println(part1(input))
    println(part2(input))
}
