package solutions22

fun main() {
    class SNAFU(val value: String) {

        fun toLong(): Long {
            // start from right to left
            var snafu = value
            var result = 0L
            while(snafu.isNotEmpty()) {
                val next = snafu.first()
                snafu = snafu.drop(1)
                val dec = when(next) {
                    '0' -> 0
                    '1' -> 1
                    '2' -> 2
                    '-' -> -1
                    '=' -> -2
                    else -> 0
                }
                result = result * 5L + dec
            }
            return result
        }
    }

    fun SNAFU(value: Long): SNAFU {
        var x = value
        var string = ""
        while(x > 0) {
            val rest = x % 5
            x /= 5
            when(rest) {
                0L -> string = "0$string"
                1L -> string = "1$string"
                2L -> string = "2$string"
                3L -> {
                    string = "=$string"
                    x += 1
                }
                else -> {
                    string = "-$string"
                    x += 1
                }
            }
        }
        return SNAFU(string)
    }

    fun part1(input: List<String>): String {
        val sum = input.sumOf { SNAFU(it).toLong() }
        return SNAFU(sum).value
    }


    for(i in 1 until 40) {
        check(SNAFU(i.toLong()).toLong() == i.toLong())
    }

    val test = parseLines("Day25_test")
    check(part1(test) == "2=-1=0")

    val start = System.currentTimeMillis()

    val input = parseLines("Day25")
    println(part1(input))


    println("time: " + (System.currentTimeMillis() - start))

}
