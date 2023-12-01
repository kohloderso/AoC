package solutions23

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import kotlin.math.absoluteValue

class MarkedInt(val value: Int, var marked: Boolean = false)

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

fun parseRPC(name: String): List<Pair<Int, Int>> {
    val lines = File(input_folder, "$name.txt").readLines()
    val result = lines.map{ line ->
        val vals = line.split(' ')
        val opponent =
            when(vals[0]) {
                "A" -> 1
                "B" -> 2
                "C" -> 3
                else -> -1
            }
        val self =
            when(vals[1]) {
                "X" -> 1
                "Y" -> 2
                "Z" -> 3
                else -> -1
            }
        Pair(opponent, self)
    }
    return result
}

fun parseRucksack(name: String): List<Pair<String, String>> {
    val lines = File(input_folder, "$name.txt").readLines()
    val result = lines.map{ line ->
        val n = line.length
        check(n % 2 == 0)
        val first = line.take(n/2)
        val second = line.drop(n/2)
        Pair(first, second)
    }
    return result
}

fun parseLines(name: String) =
    File(input_folder, "$name.txt").readLines()

fun parseSectionPairs(name: String): List<Pair<IntRange, IntRange>> {
    val lines = File(input_folder, "$name.txt").readLines()
    val result = lines.map { line ->
        val vals = line.split(',')
        val first = vals[0].split('-')
        val second = vals[1].split('-')
        Pair(first[0].toInt()..first[1].toInt(), second[0].toInt()..second[1].toInt())
    }
    return result
}

fun parseMarkedIntGrid(name: String): List<Array<MarkedInt>> {
    val lines = File(input_folder, "$name.txt").readLines()
    val result: MutableList<Array<MarkedInt>> = mutableListOf()
    for (line in lines) {
        result.add(line.map { char -> MarkedInt(char.digitToInt()) }.toTypedArray())
    }
    return result
}

fun parseIntGrid(name: String): List<Array<Int>> {
    val lines = File(input_folder, "$name.txt").readLines()
    val result: MutableList<Array<Int>> = mutableListOf()
    for (line in lines) {
        result.add(line.map { char -> char.digitToInt() }.toTypedArray())
    }
    return result
}

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

// utility functions not used right now
fun gcd(a: Long, b: Long): Long = if (b == 0L) a else gcd(b, a % b)

fun lcm(a: Long, b: Long): Long = (a*b).absoluteValue / gcd(a, b)

fun lcmList(xs: List<Long>): Long = xs.fold(1) {a, x -> lcm(a,x) }

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