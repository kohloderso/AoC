package solutions23


fun main() {

    fun hashAlgorithm(s: String): Int {
        var result = 0
        for(c in s) {
            result += c.code
            result *= 17
            result %= 256
        }
        return result
    }

    fun performStep(s: String, boxes: Array<MutableMap<String, Int>>) {
        val split = s.split("-", "=")
        val id = split[0]
        if(s.contains("-")) {
            boxes[hashAlgorithm(id)].remove(id)
        } else {
            val focalLength = split[1].toInt()
            boxes[hashAlgorithm(id)][id] = focalLength
        }
    }

    fun computeFocusingPower(boxes: Array<MutableMap<String, Int>>): Int {
        var result = 0
        for(i in boxes.indices) {
            val lenses = boxes[i].values.toList()
            for(j in lenses.indices) {
                result += (i+1) * (j+1) * lenses[j]
            }
        }
        return result
    }

    fun parseSequence(s: String): List<String> = s.split(",").map { it.trim() }

    fun part1(input: List<String>): Int {
        val strings = parseSequence(input.first())
        return strings.sumOf { hashAlgorithm(it) }
    }

    fun part2(input: List<String>): Int {
        val strings = parseSequence(input.first())
        val boxes = Array(256) { mutableMapOf<String, Int>() }
        strings.forEach { performStep(it, boxes) }
        return computeFocusingPower(boxes)
    }

    val testInput = parseLines("Day15_test")
    check(part1(testInput) == 1320)
    check(part2(testInput) == 145)


    val input = parseLines("Day15")
    println(part1(input))
    println(part2(input))
}

