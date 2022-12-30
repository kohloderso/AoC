import kotlin.math.absoluteValue

fun main() {

    class Elf(var x: Int, var y: Int) {
        var propX: Int? = null
        var propY: Int? = null
    }

    fun countEmptyTiles(elves: Array<Elf>): Int {
        val minX = elves.minOf { it.x }
        val maxX = elves.maxOf { it.x }
        val minY = elves.minOf { it.y }
        val maxY = elves.maxOf { it.y }
        var counter = 0
        for(x in minX..maxX) {
            for(y in minY..maxY) {
                if(elves.all { it.x != x || it.y != y }) {
                    counter++
                }
            }
        }
        return counter
    }

    fun prettyPrint(elves: Array<Elf>) {
        val minX = elves.minOf { it.x }
        val maxX = elves.maxOf { it.x }
        val minY = elves.minOf { it.y }
        val maxY = elves.maxOf { it.y }
        for(y in minY..maxY) {
            for(x in minX..maxX) {
                if(elves.any { it.x == x && it.y == y }) {
                    print('#')
                } else print('.')
            }
            println()
        }
    }

    fun parseElves(input: List<String>): Array<Elf> {
        val elves: MutableList<Elf> = mutableListOf()
        for(y in input.indices) {
            for(x in input[y].indices) {
                if(input[y][x] == '#') elves.add(Elf(x, y))
            }
        }
        return elves.toTypedArray()
    }

    fun moveElves(elves: Array<Elf>, rounds: Int? = 10): Int {
        val checkNorth: (Int, Int) -> Pair<Int, Int>? = { x, y ->
            if(!elves.any { (it.x - x).absoluteValue <= 1 && it.y == y-1})
                Pair(x, y-1)
            else null
        }
        val checkSouth: (Int, Int) -> Pair<Int, Int>? = { x, y ->
            if(!elves.any { (it.x - x).absoluteValue <= 1 && it.y == y+1})
                Pair(x, y+1)
            else null
        }
        val checkWest: (Int, Int) -> Pair<Int, Int>? = { x, y ->
            if(!elves.any { (it.y - y).absoluteValue <= 1 && it.x == x-1})
                Pair(x-1, y)
            else null
        }
        val checkEast: (Int, Int) -> Pair<Int, Int>? = { x, y ->
            if(!elves.any { (it.y - y).absoluteValue <= 1 && it.x == x+1})
                Pair(x+1, y)
            else null
        }
        val checkDirs = listOf(checkNorth,checkSouth,checkWest,checkEast)

        var round = 0
        while (round < (rounds ?: Int.MAX_VALUE)) {
//            println(round)
//            prettyPrint(elves)
            //propose moves
            for(elf in elves) {
                elf.propX = null
                elf.propY = null
                if(elves.any { it != elf && (it.y - elf.y).absoluteValue <= 1 && (it.x - elf.x).absoluteValue <= 1 }) {
                    // not enough space -> elf does not need to move
                    for(i in round until round + 4) {
                        val newPos = checkDirs[i%checkDirs.size](elf.x, elf.y)
                        if(newPos != null) {
                            elf.propX = newPos.first
                            elf.propY = newPos.second
                            break
                        }
                    }
                }
            }
            // do moves
            for(elf in elves) {
                if(elf.propX != null && elves.all { !(it.propX == elf.propX && it.propY == elf.propY)  || it == elf } ) {
                    elf.x = elf.propX!!
                    elf.y = elf.propY!!
                }
            }
            round++
            if(elves.all {it.propX == null && it.propY == null})
                break // elves are done moving
        }
        return round
    }


    fun part1(elves: Array<Elf>, rounds: Int = 10): Int {
        moveElves(elves, rounds)
        return countEmptyTiles(elves)
    }

    fun part2(elves: Array<Elf>): Int {
        return moveElves(elves, null)
    }


//    val test = parseLines("Day23_test")
//    check(part1(parseElves(test), 10) == 110)
//    check(part2(parseElves(test)) == 20)


    val start = System.currentTimeMillis()
    val input = parseLines("Day23")
//    println(part1(parseElves(input), 10))
    println(part2(parseElves(input)))

    println("time: " + (System.currentTimeMillis() - start))
}
