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

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')
