package solutions23

import java.math.BigInteger

class EntryRange(val start: BigInteger, val end: BigInteger) {
    fun intersect(r: EntryRange): EntryRange? {
        val start = maxOf(start, r.start)
        val end = minOf(end, r.end)
        return if(start <= end) EntryRange(start, end) else null
    }

    fun minus(r: EntryRange): List<EntryRange> {
        val r1 = if(start <= r.start - BigInteger.ONE) EntryRange(start, r.start - BigInteger.ONE) else null
        val r2 = if(end >= r.end + BigInteger.ONE) EntryRange(r.end + BigInteger.ONE, end) else null
        return listOfNotNull(r1, r2)
    }

    fun contains(n: BigInteger): Boolean = n in start..end

    val length = end - start + BigInteger.ONE
}

class AlmanacMap(val entries: List<Pair<EntryRange, BigInteger>>) {
    companion object {
        fun create(input: List<String>): AlmanacMap {
            val lists = input.map { it.split(" ").map { it.toBigInteger() } }
            return AlmanacMap(lists.map { Pair(EntryRange(it[1], it[1]+it[2]-BigInteger.ONE), it[0]) })
        }
    }


    fun lookup(value: BigInteger): BigInteger {
        for(entry in entries) {
            if(entry.first.contains(value)) {
                return entry.second + (value - entry.first.start)
            }
        }
        return value
    }

    fun lookup(ranges: List<EntryRange>): List<EntryRange> {
        val rangesToCheck = ranges.toMutableList()
        val result = mutableListOf<EntryRange>()
        while(rangesToCheck.isNotEmpty()) {
            val currentRange = rangesToCheck.removeAt(0)
            var lookupSuccess = false
            for(entry in entries) {
                if(entry.first.intersect(currentRange)!= null) {
                    val subEntry = entry.first.intersect(currentRange)!!
                    val remainingRanges = currentRange.minus(subEntry)
                    rangesToCheck.addAll(remainingRanges)
                    val resultEntry = EntryRange(entry.second + (subEntry.start - entry.first.start),
                        entry.second + (subEntry.start - entry.first.start) + subEntry.length - BigInteger.ONE)
                    result.add(resultEntry)
                    lookupSuccess = true
                    break
                }
            }
            if(!lookupSuccess) result.add(currentRange)
        }
        return result
    }
}

class Almanac(val seeds: List<BigInteger>, val seedRanges: List<EntryRange>, val maps: List<AlmanacMap>) {

    companion object {
        fun create(seedString: String, mapStrings: List<List<String>>): Almanac {
            val seeds = seedString.split(":")[1].split(" ").filterNot { it==""}.map { it.toBigInteger() }
            // combine every two consecutive entries of seeds into a pair
            val seedRanges = seeds.chunked(2).map { EntryRange(it[0], it[0]+it[1]-BigInteger.ONE) }
            val maps = mapStrings.map { AlmanacMap.create(it.drop(1)) }
            return Almanac(seeds, seedRanges, maps)
        }
    }

    fun lookup(seed: BigInteger): BigInteger {
        var result = seed
        // for each map determine the entry for result and update result accordingly
        maps.forEach { map -> result = map.lookup(result) }
        return result
    }

    fun lookupAllRanges(): List<EntryRange> {
        var result = seedRanges
        maps.forEach { map -> result = map.lookup(result) }
        return result
    }

    val lowestEndResult: BigInteger = seeds.minOfOrNull { lookup(it) } ?: BigInteger.ZERO

    val lowestEndResultByRange: BigInteger = lookupAllRanges().minOfOrNull { it.start }?: BigInteger.ZERO

}

fun main() {

    fun part1(input: List<List<String>>): BigInteger {
        val almanac = Almanac.create(input[0][0], input.drop(1))
        return almanac.lowestEndResult
    }

    fun part2(input: List<List<String>>): BigInteger {
        val almanac = Almanac.create(input[0][0], input.drop(1))
        return almanac.lowestEndResultByRange
    }

    // test if implementation meets criteria from the description:
    val testInput = parseChunks("Day05_test")
    check(part1(testInput) == 35.toBigInteger())
    check(part2(testInput) == 46.toBigInteger())

    val input = parseChunks("Day05")
    println(part1(input))
    println(part2(input))
}
