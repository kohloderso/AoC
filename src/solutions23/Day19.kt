package solutions23

class Rule(val guards: Map<String, IntRange>, val destination: String) {
    companion object {
        const val MIN = 1
        const val MAX = 4000
        fun parse(s: String): Rule {
            val split = s.split(":")
            if(split[0].contains('<')) {
                val lr = split[0].split("<")
                return Rule(mapOf(lr[0] to (MIN until lr[1].toInt())), split[1])
            }
            if(split[0].contains('>')) {
                val lr = split[0].split(">")
                return Rule(mapOf(lr[0] to (lr[1].toInt()+1 .. MAX)), split[1])
            }
            return Rule(mapOf(), s)
        }
    }
}

fun main() {

    fun parseItems(s: String): Map<String, Int> {
        val items = mutableMapOf<String, Int>()
        val split = s.removeSurrounding("{", "}").split(",")
        split.forEach {
            val split2 = it.split("=")
            items[split2[0]] = split2[1].toInt()
        }
        return items
    }

    fun initWorkflows(workflowStrings: List<String>): Map<String, List<Rule>> {
        val workflows = mutableMapOf<String, List<Rule>>()
        workflowStrings.forEach {
            val rules = mutableListOf<Rule>()
            val split = it.split("{")
            val ruleStrings = split[1].removeSuffix("}").split(",")
            ruleStrings.forEach {
                rules.add(Rule.parse(it))
            }
            workflows[split[0]] = rules
        }
        // add accepting workflow
        workflows["A"] = emptyList()
        // add rejecting workflow
        workflows["R"] = emptyList()
        return workflows
    }

    fun initItems(items: List<String>): Map<String, Set<Int>> {
        val itemsMap = mutableMapOf<String, Set<Int>>()
        items.forEach {
            itemsMap[it] = (Rule.MIN..Rule.MAX).toSet()
        }
        return itemsMap
    }

    fun getAcceptedValues(valueSets: Set<Map<String, Set<Int>>>, rules: List<Rule>, workflows: Map<String, List<Rule>>): Set<Map<String, Set<Int>>> {
        return if(rules.isEmpty()) {
            emptySet()
        } else {
            val r = rules.first()
            val map1 = valueSets.map{ values -> values.mapValues { (key, value) -> value.intersect(r.guards[key]?.toSet() ?: value) }}.toSet()
            val res1 = when (r.destination) {
                "A" -> map1
                "R" -> emptySet()
                else -> getAcceptedValues(map1, workflows[r.destination]!!, workflows)
            }
            val map2 = valueSets.map{ values -> values.mapValues { (key, value) -> value.minus((r.guards[key]?.toSet()?: emptySet()).toSet()) }}.toSet()
            val res2 = getAcceptedValues(map2, rules.drop(1), workflows)
            val res = res1.union(res2)
            return res
        }
    }


    fun part1(input: List<List<String>>): Int {
        val workflows = initWorkflows(input[0])
        val items = input[1].map { parseItems(it) }
        val itemsMap = initItems(listOf("s", "m", "x", "a"))
        val acceptedValues = getAcceptedValues(setOf(itemsMap), workflows["in"]!!, workflows)
        val acceptedItems = items.filter { acceptedValues.any { map -> it.all { (key, value) -> map[key]?.contains(value) == true } } }
        val result = acceptedItems.sumOf { it.values.sum() }
        return result
    }

    fun part2(input: List<List<String>>): Long {
        val workflows = initWorkflows(input[0])
        val itemsMap = initItems(listOf("s", "m", "x", "a"))
        val acceptedValues = getAcceptedValues(setOf(itemsMap), workflows["in"]!!, workflows)
        // compute all possible combinations, since all maps contain distinct values, we can simply multiply the size of each set in each map
        return acceptedValues.sumOf { it.values.fold(1L) { acc, value -> acc * value.size } }
    }

    val testInput = parseChunks("Day19_test")

    check(part1(testInput) == 19114)
    check(part2(testInput) == 167409079868000L)



    val input = parseChunks("Day19")
    println(part1(input))
    println(part2(input))
}
