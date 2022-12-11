fun main() {

    class Monkey(var items: MutableList<Long>, val op: (Long) -> Long, val test: (Long) -> Int) {
        var modulo: Long = 0L
        var inspectedItems = 0L
        fun throwItems(monkeys: List<Monkey>) {
            val oldItems = items
            items = mutableListOf()
            for (item in oldItems) {
                val worryLvl = op(item) / 3
                val nextMonkey = test(worryLvl)
                monkeys[nextMonkey].items.add(worryLvl)
                inspectedItems++
            }
        }

        fun throwItems2(monkeys: List<Monkey>) {
            val oldItems = items
            items = mutableListOf()
            for (item in oldItems) {
                val worryLvl = op(item).mod(modulo)
                val nextMonkey = test(worryLvl)
                monkeys[nextMonkey].items.add(worryLvl)
                inspectedItems++
            }
        }
    }


    fun part1(monkeys: List<Monkey>): Long {
        for (i in 1 .. 20) {
            for (monkey in monkeys) {
                monkey.throwItems(monkeys)
            }
        }
        val sortedMonkeys = monkeys.sortedByDescending{m -> m.inspectedItems}
        return (sortedMonkeys[0].inspectedItems *  sortedMonkeys[1].inspectedItems)
    }

    fun part2(monkeys: List<Monkey>): Long {
        for (i in 1 .. 10000) {
            for (monkey in monkeys) {
                monkey.throwItems2(monkeys)
            }
        }
        val sortedMonkeys = monkeys.sortedByDescending{m -> m.inspectedItems}
        return (sortedMonkeys[0].inspectedItems *  sortedMonkeys[1].inspectedItems)
    }

    fun parseMonkeys(input: List<List<String>>): List<Monkey> {
        val monkeys = mutableListOf<Monkey>()
        var divisor = 1L
        for (block in input) {

            val items = block[1].split(':')[1].split(',')
            val opText = block[2].split('=')[1].trim().split(" ")
            val arg1 = opText[0].trim()
            val arg2 = opText[2].trim()
            val op:(Long) -> Long = when(opText[1].trim()) {
                "*" -> { old ->
                    val a1 = if(arg1 == "old") old else arg1.toLong()
                    val a2 = if(arg2 == "old") old else arg2.toLong()
                    a1 * a2}
                "+" -> { old ->
                    val a1 = if(arg1 == "old") old else arg1.toLong()
                    val a2 = if(arg2 == "old") old else arg2.toLong()
                    a1 + a2}
                else -> {old -> old}
            }
            val testDiv = block[3].split(" ").last().toLong()
            divisor *= testDiv
            val mTrue = block[4].split(" ").last().toInt()
            val mFalse = block[5].split(" ").last().toInt()
            val test:(Long) -> Int = {x -> if(x % testDiv == 0L) mTrue else mFalse}
            val monkey = Monkey(items.map{it.trim().toLong()}.toMutableList(), op, test)
            monkeys.add(monkey)
        }
        monkeys.forEach{it.modulo = divisor}
        return monkeys
    }

    val test = parseMonkeys(parseChunks("Day11_test"))
//    check(part1(test) == 10605)
//    println(part2(test))

    val start = System.currentTimeMillis()
    val monkeys = parseMonkeys(parseChunks("Day11"))
//    println(part1(monkeys))
    println(part2(monkeys))

    println("time: " + (System.currentTimeMillis() - start))

}
