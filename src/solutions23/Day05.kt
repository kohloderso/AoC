package solutions23

import java.math.BigInteger

class AlmanacMap(val entries: List<Triple<BigInteger, BigInteger, BigInteger>>) {
    companion object {
        fun create(input: List<String>): AlmanacMap {
            val lists = input.map { it.split(" ").map { it.toBigInteger() } }
            return AlmanacMap(lists.map { Triple(it[1], it[0], it[2]) })
        }
    }

    fun lookup(value: BigInteger): BigInteger {
        for(entry in entries) {
            if(entry.first <= value && value < entry.first + entry.third) {
                return entry.second + (value - entry.first)
            }
        }
        return value
    }
}

class Almanac(val seeds: List<BigInteger>, val maps: List<AlmanacMap>) {

    companion object {
        fun create(seedString: String, mapStrings: List<List<String>>): Almanac {
            val seeds = seedString.split(":")[1].split(" ").filterNot { it==""}.map { it.toBigInteger() }
            val maps = mapStrings.map { AlmanacMap.create(it.drop(1)) }
            return Almanac(seeds, maps)
        }
    }

    fun lookup(seed: BigInteger): BigInteger {
        var result = seed
        // for each map determine the entry for result and update result accordingly
        maps.forEach { map -> result = map.lookup(result) }
        return result
    }

    val lowestEndResult: BigInteger = seeds.minOfOrNull { lookup(it) } ?: BigInteger.ZERO

}

fun main() {

    fun part1(input: List<List<String>>): BigInteger {
       val almanac = Almanac.create(input[0][0], input.drop(1))
        return almanac.lowestEndResult
    }

    fun part2(input: List<String>): Int = EngineScheme.computeGearRatios(input)

    // test if implementation meets criteria from the description:
    val testInput = parseChunks("Day05_test")
    check(part1(testInput) == 35.toBigInteger())
//    check(part2(testInput) == 467835)

    val input = parseChunks("Day05")
    println(part1(input))
//    println(part2(input))
}
