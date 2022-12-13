fun main() {
    class NestedList<T:Comparable<T>>(private var value: T? = null,
                        private val elements: MutableList<NestedList<T>> = mutableListOf()
    ) : Comparable<NestedList<T>>{

        constructor(elements: List<NestedList<T>>) : this(null, elements.toMutableList())

        constructor(string: String, stringToType: (String) -> T?) : this() {
            if(string != "") {
                val value = stringToType(string)
                if (value != null) {
                    this.value = value
                } else {
                    // drop first and last characters corresponding to '[' and ']'
                    // TODO: might want to check that
                    val childrenStrings = parseParenString(string, '[', ']', ',')
                    val children = childrenStrings.map { NestedList(it, stringToType) }
                    elements.addAll(children)
                }
            }
        }

        override fun compareTo(other: NestedList<T>): Int {
            if(this.value != null && other.value != null) {
                return if(this.value!! > other.value!!) 1
                else if (this.value!! < other.value!!) -1
                else 0
            } else if (this.value == null && other.value != null) {
                return (this.compareTo(NestedList(listOf(other))))
            } else if (this.value != null && other.value == null) {
                return (NestedList(listOf(this)).compareTo(other))
            } else {
                for(i in this.elements.indices) {
                    if (i >= other.elements.size) {
                        return 1
                    } else if (this.elements[i] > other.elements[i]) {
                        return 1
                    } else if (this.elements[i] < other.elements[i]) {
                        return -1
                    }
                }
                // left list ran out of indices
                // -> check if both lists have same length
                return if(this.elements.size == other.elements.size) 0
                else -1
            }
        }
    }


    fun part1(input: List<Pair<NestedList<Int>, NestedList<Int>>>): Int {
        val correctPairs = input.withIndex().filter { it.value.first <= it.value.second }
        return correctPairs.sumOf { it.index +1 }
    }

    fun part2(input: List<NestedList<Int>>): Int {
        val divider1 = NestedList(listOf(NestedList(2)))
        val divider2 = NestedList(listOf(NestedList(6)))
        val sorted = (input+divider1+divider2).sorted()
        val id1 = sorted.indexOf(divider1)+1
        val id2 = sorted.indexOf(divider2)+1
        return id1*id2
    }

    fun parseListPairs(input: List<List<String>>): List<Pair<NestedList<Int>, NestedList<Int>>> {
        val result = mutableListOf<Pair<NestedList<Int>, NestedList<Int>>>()
        for (pair in input) {
            val list1 = NestedList(pair[0]) { x -> x.toIntOrNull() }
            val list2 = NestedList(pair[1]) { x -> x.toIntOrNull() }
            result.add(Pair(list1, list2))
        }
        return result
    }

    val testInput = parseListPairs(parseChunks("Day13_test"))
    check(part1(testInput) == 13)

    val input = parseListPairs(parseChunks("Day13"))
    println(part1(input))

    val test2 = parseLines("Day13_test").filter {it != "" } . map { line -> NestedList(line) { x -> x.toIntOrNull() }}
    check(part2(test2) == 140)
    val input2 = parseLines("Day13").filter {it != "" } . map { line -> NestedList(line) { x -> x.toIntOrNull() }}
    println(part2(input2))
}
