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


/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')
