package solutions23


/**
 * Map character to int value to make it easier to order them according
 * to description.
 * A, K, Q, J, T, 9, 8, 7, 6, 5, 4, 3, or 2.
 */
class CamelCard(val value: Int, val jokerRule: Boolean = false) : Comparable<CamelCard> {
    companion object {
        fun charToInt(c: Char, jokerRule: Boolean): Int = when (c) {
            '2' -> 2
            '3' -> 3
            '4' -> 4
            '5' -> 5
            '6' -> 6
            '7' -> 7
            '8' -> 8
            '9' -> 9
            'T' -> 10
            'J' -> if(jokerRule) 1 else 11
            'Q' -> 12
            'K' -> 13
            'A' -> 14
            else -> 0
        }
    }

    constructor(c: Char, jokerRule: Boolean = false) : this(charToInt(c, jokerRule))

    override fun compareTo(other: CamelCard): Int {
        return value.compareTo(other.value)
    }

    override fun equals(other: Any?)
            = (other is CamelCard)
            && value == other.value

    override fun toString(): String = when (value) {
        1 -> "J"
        2 -> "2"
        3 -> "3"
        4 -> "4"
        5 -> "5"
        6 -> "6"
        7 -> "7"
        8 -> "8"
        9 -> "9"
        10 -> "T"
        11 -> "J"
        12 -> "Q"
        13 -> "K"
        14 -> "A"
        else -> ""
    }

    // auto-generated
    override fun hashCode(): Int {
        var result = value
        result = 31 * result + jokerRule.hashCode()
        return result
    }
}

class Hand(val cards: List<CamelCard>, val bid: Int, val jokerRule: Boolean = false) : Comparable<Hand> {
    companion object {
        fun parse(s: String, jokerRule: Boolean = false): Hand {
            val cards = s.split(" ")[0].map { CamelCard(it, jokerRule) }
            val bid = s.split(" ")[1].toInt()
            return Hand(cards, bid, jokerRule)
        }
    }


    override fun compareTo(other: Hand): Int {
        if (determineValue() > other.determineValue()) return 1
        if (determineValue() < other.determineValue()) return -1
        if (determineValue() == other.determineValue()) {
            for (i in cards.indices) {
                if(cards[i]!= other.cards[i]) return cards[i].compareTo(other.cards[i])
            }
        }
        return 0
    }

    val jokers = cards.filter { it.value == 1 }.size

    val occurrenceCount = cards.groupingBy { it.value }.eachCount().values
    val highestCount = occurrenceCount.maxOrNull()?: 0
    val highestWithoutJokers = cards.filter { it.value!= 1 }.groupingBy { it.value }.eachCount().values.maxOrNull()?: 0

    fun determineValue(): Int =
        if(isFiveOfAKind) 7
        else if(isFourOfAKind) 6
        else if(isFullHouse) 5
        else if(isThreeOfAKind) 4
        else if(isTwoPair) 3
        else if(isOnePair) 2
        else if(isHighCard) 1
        else 0


    val isFiveOfAKind: Boolean = highestWithoutJokers + jokers >= 5

    val isFourOfAKind: Boolean = highestWithoutJokers + jokers >= 4

    val isTwoPair: Boolean = occurrenceCount.groupBy { it }.getOrDefault(2, emptyList()).size == 2

    val isFullHouse: Boolean =
        (occurrenceCount.contains(3) && occurrenceCount.contains(2))
                || (isTwoPair && jokers == 1)

    val isThreeOfAKind: Boolean = highestWithoutJokers + jokers >= 3

    val isOnePair: Boolean = highestWithoutJokers + jokers >= 2

    val isHighCard: Boolean = cards.map { it.value }.distinct().size == 5

    override fun toString(): String {
        return cards.joinToString("") { it.toString() }
    }
}



fun main() {

    fun part1(input: List<String>): Int {
        val hands = input.map { Hand.parse(it) }
        val handsSorted = hands.sortedBy { it }
        return handsSorted.withIndex().sumOf { it.value.bid * (it.index + 1) }
    }

    fun part2(input: List<String>): Int {
        val hands = input.map { Hand.parse(it, true) }
        val handsSorted = hands.sortedBy { it }
        return handsSorted.withIndex().sumOf { it.value.bid * (it.index + 1) }
    }

    // test if implementation meets criteria from the description:
    val testInput = parseLines("Day07_test")
    check(part1(testInput) == 6440)
    check(part2(testInput) == 5905)

    val input = parseLines("Day07")
    println(part1(input))
    println(part2(input))
}
