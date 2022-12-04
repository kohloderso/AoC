import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Reads lines from the given input txt file.
 */
fun readInputChunks(name: String): List<List<Int>> {
    val file = File("src/input", "$name.txt")
    val result = mutableListOf(mutableListOf<Int>())
    BufferedReader(FileReader(file)).use { br ->
        for (line in br.lines()) {
            if (line.isEmpty()) {
                result.add(mutableListOf())
            } else {
                result.last().add(line.toInt())
            }
        }
    }
    return result
}

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


/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')
