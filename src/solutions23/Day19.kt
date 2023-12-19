package solutions23


class Rule(val condition: (Map<String, Int>) -> Boolean, val destination: String) {
    companion object {
        fun parse(s: String): Rule {
            val split = s.split(":")
            if(split[0].contains('<')) {
                val lr = split[0].split("<")
                return Rule({ it[lr[0]]!! < lr[1].toInt() }, split[1])
            }
            if(split[0].contains('>')) {
                val lr = split[0].split(">")
                return Rule({ it[lr[0]]!! > lr[1].toInt() }, split[1])
            }
            return Rule({ true }, s)
        }
    }
}
class Workflow(val name: String, val rules: List<Rule>) {
    companion object {
        fun addWorkflowToMap(s: String, workflows: MutableMap<String, Workflow>) {
            val rules = mutableListOf<Rule>()
            val split = s.split("{")
            val ruleStrings = split[1].removeSuffix("}").split(",")
            ruleStrings.forEach {
                rules.add(Rule.parse(it))
            }
            val wf = Workflow(split[0], rules)
            workflows[split[0]] = wf
        }
    }

    fun nextWorkflow(input: Map<String, Int>, workflows: Map<String, Workflow>): Workflow {
        val nextWfString = rules.firstOrNull { rule -> rule.condition(input) }?.destination?: ""
        return workflows.getValue(nextWfString)
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

    fun initWorkflows(workflowStrings: List<String>): Map<String, Workflow> {
        val workflows = mutableMapOf<String, Workflow>()
        workflowStrings.forEach {
            Workflow.addWorkflowToMap(it, workflows)
        }
        // add accepting workflow
        workflows["A"] = Workflow("A", emptyList())
        // add rejecting workflow
        workflows["R"] = Workflow("R", emptyList())
        return workflows
    }

    fun computeAcceptReject(input: Map<String, Int>, workflows: Map<String, Workflow>): Boolean {
        // start with "in" workflow
        var currentWf = workflows["in"]
        while(currentWf?.rules?.isNotEmpty() == true) {
            currentWf = currentWf.nextWorkflow(input, workflows)
        }
        return if (currentWf?.name == "A") true else false
    }

    fun part1(input: List<List<String>>): Int {
        val workflows = initWorkflows(input[0])
        val items = input[1].map { parseItems(it) }
        val result = items.sumOf { if(computeAcceptReject(it, workflows)) it.values.sum() else 0 }
        return result
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    val testInput = parseChunks("Day19_test")
    check(part1(testInput) == 19114)
//    check(part2(testInput) == 51)


    val input = parseChunks("Day19")
    println(part1(input))
//    println(part2(input))
}
