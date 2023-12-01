package solutions22

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

    data class Operation(val eval: (Long, Long) -> Long, val solveLeft: (Long, Long) -> Long, val solveRight: (Long, Long) -> Long)

    val plusOp = Operation({x,y -> x+y}, {x,y -> x-y}, {x,y -> x-y})
    val minusOp = Operation({x,y -> x-y}, {x,y -> x+y}, {x,y -> y-x})
    val timesOp = Operation({x,y -> x*y}, {x,y -> x/y}, {x,y -> x/y})
    val divOp = Operation({x,y -> x/y}, {x,y -> x*y}, {x,y -> y/x})

    class MonkeyOp() {
        var value: Long? = null
        var eval: (Map<String, MonkeyOp>) -> Long? = { null }
        var solve: (Map<String, MonkeyOp>, Long) -> Long? = {_,x -> x }

        constructor(value: Long) : this() {
            this.value = value
            this.eval = { value }
            this.solve = { _,x -> if(x == value) x else null }
        }
        constructor(left: String, right: String, op: Operation) : this() {
            eval = { map ->
                if(value != null) value
                else {
                    val op1 = map[left]?.eval?.invoke(map)
                    val op2 = map[right]?.eval?.invoke(map)
                    if (op1 != null && op2 != null) {
                        value = op.eval(op1, op2)
                        value
                    } else null
                }
            }
            solve = { map, x ->
                val op1 = map[left]?.eval?.invoke(map)
                val op2 = map[right]?.eval?.invoke(map)
                if(op1 == null && op2 != null) {
                    map[left]?.solve?.invoke(map, op.solveLeft(x, op2))
                } else if(op1 != null && op2 == null) {
                    map[right]?.solve?.invoke(map, op.solveRight(x, op1))
                } else null
            }
        }
    }

    fun part2(input: List<String>): Long? {
        val monkeyMap = mutableMapOf<String,MonkeyOp>()
        var left: String = ""
        var right: String = ""
        for(line in input) {
            val split = line.split(":")
            val name = split[0]
            val yell = split[1].trim()
            if(yell.toLongOrNull() != null) {
                monkeyMap[name] =
                    if(name == "humn") MonkeyOp()
                    else MonkeyOp(yell.toLong())
            } else {
                val op = yell.split(" ")
                if(name == "root") {
                    left = op[0]
                    right = op[2]
                }
                when(op[1]) {
                    "+" -> monkeyMap[name] = MonkeyOp(op[0], op[2], plusOp)
                    "-" -> monkeyMap[name] = MonkeyOp(op[0], op[2], minusOp)
                    "/" -> monkeyMap[name] = MonkeyOp(op[0], op[2], divOp)
                    "*" -> monkeyMap[name] = MonkeyOp(op[0], op[2], timesOp)
                }
            }
        }
        val leftRes = monkeyMap[left]?.eval?.invoke(monkeyMap)
        val rightRes = monkeyMap[right]?.eval?.invoke(monkeyMap)
        return if(leftRes != null) {
            monkeyMap[right]?.solve?.invoke(monkeyMap, leftRes)
        } else if (rightRes != null) {
            monkeyMap[left]?.solve?.invoke(monkeyMap, rightRes)
        } else null
    }

    val test = parseLines("Day21_test")


//    check(part1(test) == 152L)
    check(part2(test) == 301L)

    val input = parseLines("Day21")
//    println(part1(input))
    val start = System.currentTimeMillis()
    println(part2(input))

   println("time: " + (System.currentTimeMillis() - start))
}
