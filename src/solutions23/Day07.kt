package solutions23

import java.math.BigInteger
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * Map character to int value to make it easier to order them according
 * to description.
 * A, K, Q, J, T, 9, 8, 7, 6, 5, 4, 3, or 2.
 */
class CamelCard(val value: Int) : Comparable<CamelCard> {
    companion object {
        fun charToInt(c: Char): Int = when (c) {
            '2' -> 2
            '3' -> 3
            '4' -> 4
            '5' -> 5
            '6' -> 6
            '7' -> 7
            '8' -> 8
            '9' -> 9
            'T' -> 10
            'J' -> 11
            'Q' -> 12
            'K' -> 13
            'A' -> 14
            else -> 0
        }
    }

    constructor(c: Char) : this(charToInt(c))

    override fun compareTo(other: CamelCard): Int {
        return value.compareTo(other.value)
    }

    override fun equals(other: Any?)
            = (other is CamelCard)
            && value == other.value
}

class Hand(val cards: List<CamelCard>, val bid: Int) : Comparable<Hand> {
    companion object {
        fun parse(s: String): Hand {
            val cards = s.split(" ")[0].map { CamelCard(it) }
            val bid = s.split(" ")[1].toInt()
            return Hand(cards, bid)
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

    fun determineValue(): Int =
        if(isFiveOfAKind) 7
        else if(isFourOfAKind) 6
        else if(isFullHouse) 5
        else if(isThreeOfAKind) 4
        else if(isTwoPair) 3
        else if(isOnePair) 2
        else if(isHighCard) 1
        else 0


    val isFiveOfAKind: Boolean = cards.groupingBy { it.value }.eachCount().values.contains(5)

    val isFourOfAKind: Boolean = cards.groupingBy { it.value }.eachCount().values.contains(4)

    val isFullHouse: Boolean =
        cards.groupingBy { it.value }.eachCount().values.contains(3)
                && cards.groupingBy { it.value }.eachCount().values.contains(2)

    val isThreeOfAKind: Boolean = cards.groupingBy { it.value }.eachCount().values.contains(3)

    val isTwoPair: Boolean =
        cards.groupingBy { it.value }.eachCount().values.groupBy { it }.getOrDefault(2, emptyList()).size == 2

    val isOnePair: Boolean =
        cards.groupingBy { it.value }.eachCount().values.contains(2)

    val isHighCard: Boolean = cards.map { it.value }.distinct().size == 5
}



fun main() {

    fun part1(input: List<String>): Int {
        val hands = input.map { Hand.parse(it) }
        val handsSorted = hands.sortedBy { it }
        return handsSorted.withIndex().sumOf { it.value.bid * (it.index + 1) }
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    // test if implementation meets criteria from the description:
    val testInput = parseLines("Day07_test")
    check(part1(testInput) == 6440)
    check(part2(testInput) == 0)

    val input = parseLines("Day07")
    println(part1(input))
    println(part2(input))
}
