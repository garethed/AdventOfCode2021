import java.util.*
import kotlin.collections.HashMap

data class Polymer(val pairs:Map<String,Long>, val rules : Map<String,Array<String>>, val end:Char) {


    fun grow() : Polymer {

        val newPairs = HashMap<String,Long>()

        fun add(pair:String, count:Long) {
            newPairs[pair] = newPairs.getOrDefault(pair, 0L) + count
        }

        pairs
            .entries
            .forEach {
                val rule = rules[it.key]
                if (rule != null) {
                    rule.forEach { newPair -> add(newPair, it.value) }
                }
                else {
                    add(it.key, it.value)
                }
            }

        return Polymer(newPairs, rules, end)
    }
}

class Day14 : DayWithInputFile<Long, Polymer>() {

    val testInput = """NNCB

CH -> B
HH -> N
CB -> H
NH -> C
HB -> C
HC -> B
HN -> C
NN -> C
BH -> H
NC -> B
NB -> B
BN -> B
BB -> N
BC -> B
CC -> N
CN -> C"""

    override fun tests() {
        test(::part1Impl, 1588, testInput)
        test(::part2Impl, 2188189693529, testInput)
    }

    override fun parseInput(input: String): Polymer {
        val lines = input.lines()
        val pairs =
            lines[0]
                .windowed(2)
                .groupingBy { it }
                .eachCount()
                .mapValues { it.value.toLong() }

        val rules =
            lines
                .drop(2)
                .map { it.split(" -> ")}
                .associateBy ( { it[0] }, { arrayOf(it[0][0] + it[1],it[1] + it[0][1] ) })

        return Polymer(pairs, rules, lines[0].last())
    }

    override fun part1Impl(input: Polymer): Long {
        return calc(input, 10)
    }

    fun calc(input: Polymer, generations: Int): Long {

        var growing = input
        repeat(generations) { growing = growing.grow() }

        val elements = growing
            .pairs
            .map { Pair(it.key[0], it.value)}
            .plus( Pair(input.end, 1L))
            .groupBy( {it.first }, {it.second })
            .mapValues { it.value.sum() }

        return elements.values.maxOrNull()!! - elements.values.minOrNull()!!
    }

    override fun part2Impl(input: Polymer): Long {
        return calc(input, 40)
    }
}

