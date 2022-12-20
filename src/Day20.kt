fun main() {
    fun mixInts(list: List<MarkedInt>): List<MarkedInt> {
        var newList = list
        var next = newList.withIndex().find { !it.value.marked }
        while (next != null) {
            newList = newList.take(next.index) + newList.drop(next.index + 1)
            val value = next.value.value
            val insertId = Math.floorMod(next.index + value,newList.size) // floorMod to avoid negative values
            newList = newList.take(insertId) + listOf(MarkedInt(value,true)) + newList.drop(insertId)
            next = newList.withIndex().find { !it.value.marked }
        }
        return newList
    }

    fun mixLongs(list: List<IndexedValue<Long>>, mixingOrder: List<IndexedValue<Long>>): List<IndexedValue<Long>> {
        var newList = list
        for(value in mixingOrder) {
            val index = newList.indexOf(value)
            newList = newList.take(index) + newList.drop(index + 1)
            val insertId = Math.floorMod(((index + value.value) % newList.size).toInt(),newList.size) // floorMod to avoid negative values, first mod to get an Int
            newList = newList.take(insertId) + listOf(value) + newList.drop(insertId)
        }
        return newList
    }

    fun part1(input: List<String>): Int {
        val list = input.map {MarkedInt(it.toInt(), false)}
        val mixed = mixInts(list)
        val id0 = mixed.indexOfFirst { it.value == 0 }
        val id1 = (id0 + 1000) % mixed.size
        val id2 = (id0 + 2000) % mixed.size
        val id3 = (id0 + 3000) % mixed.size
        return mixed[id1].value + mixed[id2].value + mixed[id3].value
    }

    fun part2(input: List<String>): Long {
        val key = 811589153
        val list = input.map { it.toLong() * key }.withIndex().toList()
        var mixed = list
        for (i in 0 until 10) {
            mixed = mixLongs(mixed, list)
            println(i)
        }
        val id0 = mixed.indexOfFirst { it.value == 0L }
        val id1 = (id0 + 1000) % mixed.size
        val id2 = (id0 + 2000) % mixed.size
        val id3 = (id0 + 3000) % mixed.size
        return mixed[id1].value + mixed[id2].value + mixed[id3].value
    }



    val test = parseLines("Day20_test")

    val start = System.currentTimeMillis()
//    check(part1(test) == 3)
    check(part2(test) == 1623178306L)


    val input = parseLines("Day20")
//    val blueprints = parseBlueprints(input)
//    println(part1(input))
    println(part2(input))
//
    println("time: " + (System.currentTimeMillis() - start))



}
