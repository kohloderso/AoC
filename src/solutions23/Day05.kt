package solutions23


class EntryRange(val start: Long, val end: Long) {
    fun intersect(r: EntryRange): EntryRange? {
        val start = maxOf(start, r.start)
        val end = minOf(end, r.end)
        return if(start <= end) EntryRange(start, end) else null
    }

    fun minus(r: EntryRange): List<EntryRange> {
        val r1 = if(start <= r.start - 1) EntryRange(start, r.start - 1) else null
        val r2 = if(end >= r.end + 1) EntryRange(r.end + 1, end) else null
        return listOfNotNull(r1, r2)
    }

    fun contains(n: Long): Boolean = n in start..end

    val length = end - start + 1
}

class AlmanacMap(val entries: List<Pair<EntryRange, Long>>) {
    companion object {
        fun create(input: List<String>): AlmanacMap {
            val lists = input.map { it.split(" ") }
            return AlmanacMap(lists.map { Pair(EntryRange(it[1].toLong(), it[1].toLong()+it[2].toLong()-1), it[0].toLong()) })
        }
    }


    fun lookup(value: Long): Long {
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
                        entry.second + (subEntry.start - entry.first.start) + subEntry.length - 1)
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

class Almanac(val seeds: List<Long>, val seedRanges: List<EntryRange>, val maps: List<AlmanacMap>) {

    companion object {
        fun create(seedString: String, mapStrings: List<List<String>>): Almanac {
            val seeds = seedString.split(":")[1].split(" ").filterNot { it==""}.map { it.toLong() }
            // combine every two consecutive entries of seeds into a pair
            val seedRanges = seeds.chunked(2).map { EntryRange(it[0], it[0] + it[1] -1) }
            val maps = mapStrings.map { AlmanacMap.create(it.drop(1)) }
            return Almanac(seeds, seedRanges, maps)
        }
    }

    fun lookup(seed: Long): Long {
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

    val lowestEndResult: Long = seeds.minOfOrNull { lookup(it) } ?: 0

    val lowestEndResultByRange: Long = lookupAllRanges().minOfOrNull { it.start }?: 0

}

fun main() {

    fun part1(input: List<List<String>>): Long {
        val almanac = Almanac.create(input[0][0], input.drop(1))
        return almanac.lowestEndResult
    }

    fun part2(input: List<List<String>>): Long {
        val almanac = Almanac.create(input[0][0], input.drop(1))
        return almanac.lowestEndResultByRange
    }

    // test if implementation meets criteria from the description:
    val testInput = parseChunks("Day05_test")
    check(part1(testInput) == 35L)
    check(part2(testInput) == 46L)

    val input = parseChunks("Day05")
    println(part1(input))
    println(part2(input))
}
