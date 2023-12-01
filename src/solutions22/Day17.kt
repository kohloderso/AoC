package solutions22

fun main() {

    data class State(val jetPos: Int, val chamber: List<CharArray>, val height: Long, val shapeCounter: Long) {
        override fun equals(other: Any?): Boolean {
            if (other is State) {
                return (this.jetPos == other.jetPos &&
                        this.chamber.size == other.chamber.size &&
                        this.chamber.indices.all { this.chamber[it] contentEquals other.chamber[it] })
            } else return false
        }
    }

    class FallingRock(val jetPattern: String, var jetPosition: Int = 0, var shapeID: Int = 0,
                      var shapePos: Array<LongArray> = emptyArray()) {
        var chamber: MutableList<CharArray> = mutableListOf()
        var shapeCounter: Long = 0
        var height: Long = 0
        private val width = 7
        private var chamberOffset = 0 // greatest value where the full width is blocked


        fun addNLines(n: Long) {
            for (i in 1 .. n) {
                chamber.add(CharArray(width) { '.' })
            }
        }

        fun updateChamberOffset() {
            for (i in chamber.indices.drop(1).reversed()) {
                if(chamber[i].all { it != '.' }){
                    chamberOffset += i
                    val intermediate = chamber.takeLast(chamber.size - i)
                    chamber.clear()
                    chamber.addAll(intermediate)
                    return
                }
            }
        }

        // take current shape and make it appear so that its left edge is 2 units away from the left wall
        // and its bottom edge is 3 units from highest rock or bottom.
        // First value of shape pair is the height, second one is the horizontal position.
        // Extend the chamber list so that shape fits into it.
        fun initShape() {
            when (shapeID) {
                0 -> { // -
                    addNLines(height + 4 - chamber.size - chamberOffset)
                    shapePos = Array(4) { i -> longArrayOf(height + 3, i + 2L) }
                }
                1 -> {// +
                    addNLines(height + 6 - chamber.size - chamberOffset)
                    shapePos = Array(5) { i ->
                        // first add the horizontal line of the plus
                        if (i < 3) {
                            longArrayOf(height + 4, i + 2L)
                        } else if (i == 3) { // then add the two missing positions at top and bottom
                            longArrayOf(height + 5, 3)
                        } else {
                            longArrayOf(height + 3, 3)
                        }
                    }
                }
                2 -> {// mirrored L shape
                    addNLines(height + 6 - chamber.size - chamberOffset)
                    shapePos = Array(5) { i ->
                        // first add the horizontal line of the L
                        if (i < 3) {
                            longArrayOf(height + 3, i + 2L)
                        } else { // then add the two missing positions at top
                            longArrayOf(height + i + 1, 4)
                        }
                    }
                }
                3 -> {// vertical line
                    addNLines(height + 7 - chamber.size - chamberOffset)
                    shapePos = Array(4) { i -> longArrayOf(height + 3 + i, 2) }
                }
                4 -> {// box-shape
                    addNLines(height + 5 - chamber.size - chamberOffset)
                    shapePos = Array(4) { i ->
                        // first add the first horizontal line
                        if (i < 2) {
                            longArrayOf(height + 3, i + 2L)
                        } else { // then add the next horizontal line on top
                            longArrayOf(height + 4, i.toLong())
                        }
                    }
                }
            }
        }

        // Take current shape and move it to left (-1) or right (+1) if possible.
        fun moveHorizontal(dir: Int) {
            if (shapePos.all { it[1]+dir in 0 until width &&
                        chamber[(it[0]-chamberOffset).toInt()][(it[1] + dir).toInt()] == '.' })
                shapePos.indices.forEach { shapePos[it][1] = shapePos[it][1] + dir }
        }

        // Take current shape and move it down if possible. Returns true if movement was successful.
        fun moveDown(): Boolean {
            if (shapePos.all { it[0] > 0 &&
                        chamber[(it[0]-1-chamberOffset).toInt()][(it[1]).toInt()] == '.' }) {
                shapePos.indices.forEach { shapePos[it][0] = shapePos[it][0] - 1 }
                return true
            } else return false
        }

        // add positions of current shape to the chamber and update height accordingly
        fun settleShape() {
            shapePos.forEach {
                chamber[(it[0]-chamberOffset).toInt()][(it[1]).toInt()] = '#'
                if(it[0]+1 > height) height = it[0]+1
            }
        }

        fun simulateMovement() {
            // move shape by jet and then down one step
            when(jetPattern[jetPosition]) {
                '<' -> moveHorizontal(-1)
                '>' -> moveHorizontal(+1)
            }
            jetPosition = (jetPosition + 1) % jetPattern.length
            if(moveDown()) simulateMovement()
            else {
                shapeCounter++
                settleShape()
                updateChamberOffset()
//                prettyPrintChamber()
                shapeID = (shapeID + 1) % 5
                initShape()
            }
        }

        fun prettyPrintChamber() {
            for (line in chamber.reversed()) {
                println(line)
            }
            println()
        }

        fun copyChamber(): List<CharArray> {
            val filteredChamber = chamber.filter {it.any {c -> c != '.'}}
            return filteredChamber.drop(filteredChamber.size - 30). map { it.copyOf() }
        }
    }

    fun part1(input: String): Long {
        val simulation = FallingRock(input)
        simulation.initShape()
        while(simulation.shapeCounter < 2022) {
            simulation.simulateMovement()
        }
        return simulation.height
    }

    fun part2(input: String): Long {
        val simulation = FallingRock(input)
        simulation.initShape()
        val states: MutableList<State> = mutableListOf()
        var addHeight: Long = 0
        while(simulation.shapeCounter < 1000000000000) {
            simulation.simulateMovement()
            if(simulation.shapeID == 0 && simulation.chamber.size >= 40 && addHeight == 0L) {
                val newState = State(simulation.jetPosition, simulation.copyChamber(), simulation.height, simulation.shapeCounter)
                if(states.contains(newState)) {
                    val oldState = states.find { it == newState }!!
                    val gainedHeight = newState.height - oldState.height
                    val n = newState.shapeCounter - oldState.shapeCounter
                    val repetitions = (1000000000000 - newState.shapeCounter) / n
                    simulation.shapeCounter += repetitions * n
                    addHeight = repetitions * gainedHeight
                } else {
                    states.add(newState)
                }
            }
            if(simulation.shapeCounter % 10000 == 0L) println (simulation.shapeCounter)
        }
        return simulation.height + addHeight
    }

    val test = parseLines("Day17_test")
//    check(part1(test[0]) == 3068L)

    println(part2(test[0]))

    val input = parseLines("Day17")
//    println(part1(input[0]))
    println(part2(input[0]))

}
