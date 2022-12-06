class Move(private val n: Int, private val from: Int, private val to: Int) {
     fun applyMove9000(crateList: Array<MutableList<Char>>) {
         for (i in 0 until n) {
             val c = crateList[from-1].removeLast()
             crateList[to-1].add(c)
         }
    }
    fun applyMove9001(crateList: Array<MutableList<Char>>) {
        val cs = crateList[from-1].takeLast(n)
        crateList[to-1].addAll(cs)
        for (i in 0 until n) {
            crateList[from-1].removeLast()
        }
    }
}

fun main() {

    fun parseCrates(crateList: List<String>, crateNumbers: String): Array<MutableList<Char>> {
        val nCrates = crateNumbers.split(" ").last { it.isNotEmpty() }.toInt()
        val crates: Array<MutableList<Char>> = Array(nCrates) { mutableListOf() }
        for (line in crateList.reversed()) {
            val items = line.chunked(4)
            for (i in 0 until nCrates) {
                if(items.size > i) {
                    val item = items[i][1]
                    if(item.isLetter()) {
                        crates[i].add(item)
                    }
                }
            }
        }
        return crates
    }

    fun parseMoves(lines: List<String>): List<Move> {
        val moves = lines.map { line ->
            val content = line.split(" ")
            Move(content[1].toInt(), content[3].toInt(), content[5].toInt())
        }
        return moves
    }

    fun part1(moves: List<Move>, crates: Array<MutableList<Char>>): List<Char> {
        for (move in moves) {
            move.applyMove9000(crates)
        }
        return crates.map { it.last() }
    }

    fun part2(moves: List<Move>, crates: Array<MutableList<Char>>): List<Char> {
        for (move in moves) {
            move.applyMove9001(crates)
        }
        return crates.map { it.last() }
    }

    val input = parseChunks("Day05")
    val crates = parseCrates(input[0].dropLast(1), input[0].last())
    val moves = parseMoves(input[1])

    val result = part2(moves, crates)
    println(result.joinToString(""))


}
