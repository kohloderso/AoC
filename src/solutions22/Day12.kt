package solutions22

import kotlin.Float.Companion.POSITIVE_INFINITY

fun main() {

    class Node(val posX: Int, val posY: Int, var dist:Float = POSITIVE_INFINITY, var predecessor: Node? = null)

    class HillGrid(val grid: Array<Array<Int>>, val xStart: Int, val yStart: Int, val xGoal: Int, val yGoal: Int) {

        fun determineNeighbors(i: Int, j: Int, nodes: Array<Array<Node>>): List<Node> {
            val neighbors = mutableListOf<Node>()
            // TODO smart sorting of neighbors?
            if(i > 0 && grid[i-1][j] - grid[i][j] <= 1) {
                neighbors.add(nodes[i-1][j])
            }
            if(i < grid.size-1 && grid[i+1][j] - grid[i][j] <= 1) {
                neighbors.add(nodes[i+1][j])
            }
            if(j > 0 && grid[i][j-1] - grid[i][j] <= 1) {
                neighbors.add(nodes[i][j-1])
            }
            if(j < grid[0].size-1 && grid[i][j+1] - grid[i][j] <= 1) {
                neighbors.add(nodes[i][j+1])
            }
            return neighbors
        }

        fun updateDist(node: Node, nodeSet: Set<Node>, predecessor: Node, distPredNode: Int) {
            if(nodeSet.contains(node)) {
                val newDist = predecessor.dist + distPredNode
                if(node.dist > newDist) {
                    node.dist = newDist
                    node.predecessor = predecessor
               }
            }
        }
        fun dijkstraDist(xStart:Int = this.xStart, yStart:Int = this.yStart): Float {
            val nodes: Array<Array<Node>> = Array(grid.size) {i -> Array(grid[0].size) {j ->  Node(j, i)}}
            nodes[yStart][xStart].dist = 0F
            val nodeSet = nodes.flatten().toMutableSet()
            while(nodeSet.isNotEmpty()) {
                val next = nodeSet.minBy { node -> node.dist}
                if(next.posX == xGoal && next.posY == yGoal) return next.dist
                nodeSet.remove(next)
                val i = next.posY
                val j = next.posX
                val neighbors = determineNeighbors(i, j, nodes)
                for (neighbor in neighbors) {
                    updateDist(neighbor, nodeSet, next, 1)
                }
            }
            return -1F
        }

        fun lowestPoss(): List<Pair<Int, Int>> {
            val poss = mutableListOf<Pair<Int, Int>>()
            for (i in grid.indices) {
                for (j in grid[i].indices) {
                    if(grid[i][j] == 0) {
                        poss.add(Pair(i, j))
                    }
                }
            }
            return poss
        }
    }


    fun part1(hills: HillGrid): Float {
        return hills.dijkstraDist()
    }

    fun part2(hills: HillGrid): Float {
        val lowestPoss = hills.lowestPoss()
        val results = lowestPoss.map { (i, j) -> hills.dijkstraDist(j, i)}
        return results.min()
    }

    fun parseLetterGrid(input: List<String>): HillGrid {
        val array = Array(input.size) { Array(input[0].length) { -1 } }
        var startX = -1
        var startY = -1
        var goalX = -1
        var goalY = -1
        for (i in input.indices) {
            for (j in input[i].indices) {
                when (input[i][j]) {
                    'S' -> {
                        startX = j
                        startY = i
                        array[i][j] = 0
                    }
                    'E' -> {
                        goalX = j
                        goalY = i
                        array[i][j] = 'z'.code - 'a'.code
                    }
                    else -> array[i][j] = input[i][j].code - 'a'.code
                }
            }
        }
        return HillGrid(array, startX, startY, goalX, goalY)
    }


    val test = (parseLines("Day12_test"))
    val testHills = parseLetterGrid(test)
    println(part1(testHills))
    println(part2(testHills))

    val start = System.currentTimeMillis()

    val input = (parseLines("Day12"))
    val hills = parseLetterGrid(input)
    println(part1(hills))

    println("time: " + (System.currentTimeMillis() - start))

    println(part2(hills))

    println("time: " + (System.currentTimeMillis() - start))

}
