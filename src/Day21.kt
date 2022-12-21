fun main() {

    fun part1(input: List<String>): Long? {
        val monkeyMap = mutableMapOf<String,() -> Long?>()
        for(line in input) {
            val split = line.split(":")
            val name = split[0]
            val yell = split[1].trim()
            if(yell.toLongOrNull() != null) {
                monkeyMap[name] = { yell.toLong() }
            } else {
                val op = yell.split(" ")
                when(op[1]) {
                    "+" -> monkeyMap[name] = {
                        val op1 = monkeyMap[op[0]]?.invoke()
                        val op2 = monkeyMap[op[2]]?.invoke()
                        if (op1 != null && op2 != null) {
                            op1 + op2
                        } else null
                    }
                    "-" -> monkeyMap[name] = {
                        val op1 = monkeyMap[op[0]]?.invoke()
                        val op2 = monkeyMap[op[2]]?.invoke()
                        if (op1 != null && op2 != null) {
                            op1 - op2
                        } else null
                    }
                    "/" -> monkeyMap[name] = {
                        val op1 = monkeyMap[op[0]]?.invoke()
                        val op2 = monkeyMap[op[2]]?.invoke()
                        if (op1 != null && op2 != null) {
                            op1 / op2
                        } else null
                    }
                    "*" -> monkeyMap[name] = {
                        val op1 = monkeyMap[op[0]]?.invoke()
                        val op2 = monkeyMap[op[2]]?.invoke()
                        if (op1 != null && op2 != null) {
                            op1 * op2
                        } else null
                    }
                }
            }
        }
        val result = monkeyMap["root"]?.invoke()
        return result
    }

    val test = parseLines("Day21_test")

    val start = System.currentTimeMillis()
//    check(part1(test) == 152L)
//    check(part2(test) == 1623178306L)
//
//
    val input = parseLines("Day21")
////    val blueprints = parseBlueprints(input)
    println(part1(input))
//    println(part2(input))
////
   println("time: " + (System.currentTimeMillis() - start))
}
