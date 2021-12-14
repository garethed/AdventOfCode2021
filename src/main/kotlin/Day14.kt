import java.util.*

data class Polymer(val chain:String, val rules : Map<String,String>) {


    fun grow() : Polymer {

        val newChain = StringBuilder()
        var next:Char = chain[0]

        for ( i in 1 until chain.length) {

            val current = next
            next = chain[i]
            val rule = rules[current.toString() + next.toString()]

            newChain.append(current)

            if (rule != null) {
                newChain.append(rule)
            }
        }

        newChain.append(next)
        return Polymer(newChain.toString(), rules)
    }
}

class Day14 : DayWithInputFile<Int, Polymer>() {

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
    }

    override fun parseInput(input: String): Polymer {
        val lines = input.lines()
        val rules =
            lines
                .drop(2)
                .map { it.split(" -> ")}
                .associateBy ( { it[0] }, { it[1] })

        return Polymer(lines[0], rules)
    }

    override fun part1Impl(input: Polymer): Int {
        var growing = input
        repeat(10) { growing = growing.grow(); /*println(growing.chain)*/ println (growing.chain.groupingBy { it }.eachCount())  }
        val counts = growing
            .chain
            .groupingBy { it }
            .eachCount()

        return counts.values.maxOrNull()!! - counts.values.minOrNull()!!


    }

    override fun part2Impl(input: Polymer): Int {
        TODO("Not yet implemented")
    }
}