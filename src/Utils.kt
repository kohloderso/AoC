import java.io.BufferedReader
import java.io.File
import java.io.FileReader

class MarkedInt(val value: Int, var marked: Boolean = false)

fun parseChunks(name: String): List<List<String>> {
    val file = File("src/input", "$name.txt")
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
    val lines = File("src/input", "$name.txt").readLines()
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
    val lines = File("src/input", "$name.txt").readLines()
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
    File("src/input", "$name.txt").readLines()

fun parseSectionPairs(name: String): List<Pair<IntRange, IntRange>> {
    val lines = File("src/input", "$name.txt").readLines()
    val result = lines.map { line ->
        val vals = line.split(',')
        val first = vals[0].split('-')
        val second = vals[1].split('-')
        Pair(first[0].toInt()..first[1].toInt(), second[0].toInt()..second[1].toInt())
    }
    return result
}

fun parseMarkedIntGrid(name: String): List<Array<MarkedInt>> {
    val lines = File("src/input", "$name.txt").readLines()
    val result: MutableList<Array<MarkedInt>> = mutableListOf()
    for (line in lines) {
        result.add(line.map { char -> MarkedInt(char.digitToInt()) }.toTypedArray())
    }
    return result
}

fun parseIntGrid(name: String): List<Array<Int>> {
    val lines = File("src/input", "$name.txt").readLines()
    val result: MutableList<Array<Int>> = mutableListOf()
    for (line in lines) {
        result.add(line.map { char -> char.digitToInt() }.toTypedArray())
    }
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
