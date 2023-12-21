package solutions23

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import kotlin.math.absoluteValue

private val input_folder = "src/solutions23/input"

fun parseChunks(name: String): List<List<String>> {
    val file = File(input_folder, "$name.txt")
    val result = mutableListOf(mutableListOf<String>())
    BufferedReader(FileReader(file)).use { br ->
        for (line in br.lines()) {
            if (line.isEmpty()) {
                result.add(mutableListOf())
            } else {
                result.last().add(line)
            }
        }
    }
    return result
}

fun parseIntChunks(name: String) =
    parseChunks(name).map { it.map { line -> line.toInt() } }

fun parseLines(name: String) =
    File(input_folder, "$name.txt").readLines()


/**
 * Split a string representing a list into corresponding sub-lists.
 * Assuming balanced parentheses.
 */
fun parseParenString(input: String, l: Char, r:Char, sep:Char): List<String> {
    var depth = 0
    val result = mutableListOf<String>()
    var currentArg = ""
    for(c in input) {
        when(c) {
            sep -> {
                if (depth == 1) {
                    result.add(currentArg)
                    currentArg = ""
                } else {
                    currentArg += c
                }
            }
            l -> {
                depth++
                if(depth > 1) currentArg += c
            }
            r -> {
                if(depth > 1) currentArg += c
                depth--
            }
            else -> currentArg += c
        }
    }
    result.add(currentArg)
    return result
}

inline fun<reified A> transpose(grid: Array<Array<A>>): Array<Array<A>> {
    val cols = grid[0].size
    val rows = grid.size
    return Array(cols) { j ->
        Array(rows) { i ->
            grid[i][j]
        }
    }
}

//compute cartesian product of a list of lists
inline fun<reified A> cartesianProd(lists: List<List<A>>): List<List<A>> {
    return lists.fold(listOf(listOf<A>())) { acc, list ->
        acc.flatMap { list1 ->
            list.map { elem ->
                list1 + elem
            }
        }
    }
}



// utility functions not used right now
fun gcd(a: Long, b: Long): Long = if (b == 0L) a else gcd(b, a % b)

fun lcm(a: Long, b: Long): Long = (a*b).absoluteValue / gcd(a, b)

fun lcmList(xs: List<Long>): Long = xs.fold(1) {a, x -> lcm(a,x) }

/**
 * Simple generic Dijkstra implementation where the input is a grid consisting of
 * two types of tiles: the ones which are allowed are true, the ones which are blocked are set to false.
 * Returns a grid of the same size as the input containing the shortest paths from the start.
 */
fun dijkstra(grid: Array<Array<Boolean>>, startX: Int, startY: Int): Array<Array<Int>> {
    val distArray = Array(grid.size) { Array(grid[0].size) { Int.MAX_VALUE } }
    distArray[startX][startY] = 0
    val nextNodes = ArrayDeque<Triple<Int, Int, Int>>()
    nextNodes.add(Triple(0, startX, startY))
    while (nextNodes.isNotEmpty()) {
        val (dist, i, j) = nextNodes.removeFirst()
        // check the four direct neighbours
        if (i - 1 >= 0 && grid[i - 1][j] && distArray[i - 1][j] > dist + 1) {
            distArray[i - 1][j] = dist + 1
            nextNodes.add(Triple(dist + 1, i - 1, j))
        }
        if (i + 1 < grid.size && grid[i + 1][j] && distArray[i + 1][j] > dist + 1) {
            distArray[i + 1][j] = dist + 1
            nextNodes.add(Triple(dist + 1, i + 1, j))
        }
        if (j - 1 >= 0 && grid[i][j - 1] && distArray[i][j - 1] > dist + 1) {
            distArray[i][j - 1] = dist + 1
            nextNodes.add(Triple(dist + 1, i, j - 1))
        }
        if (j + 1 < grid[0].size && grid[i][j + 1] && distArray[i][j + 1] > dist + 1) {
            distArray[i][j + 1] = dist + 1
            nextNodes.add(Triple(dist + 1, i, j + 1))
        }
    }
    return distArray
}

fun floydWarshall(dist: Array<Array<Int?>>) {
    for (k in dist.indices) {
        for (i in dist.indices) {
            for (j in dist.indices) {
                val dist1 = dist[i][k]
                val dist2 = dist[k][j]
                val distOld = dist[i][j]
                if(dist1 != null && dist2 != null) {
                    if(distOld != null) {
                        if (dist1 + dist2 < distOld) {
                            dist[i][j] = dist1 + dist2
                        }
                    } else {
                        dist[i][j] = dist1 + dist2
                    }
                }
            }
        }
    }
}