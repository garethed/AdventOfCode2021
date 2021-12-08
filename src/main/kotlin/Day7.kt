import java.lang.Integer.parseInt
import java.lang.Math.abs

class Day7 : DayWithInputFile<Int, List<Int>>() {

    override fun tests() {
        test(::part1Impl, 37, "16,1,2,0,4,2,7,1,2,14")
        test(::part2Impl, 168, "16,1,2,0,4,2,7,1,2,14")
    }

    override fun parseInput(input: String): List<Int> {
        return input.split(",").map { parseInt(it) }
    }

    override fun part1Impl(input: List<Int>): Int {
        var min = Int.MAX_VALUE

        for (i in input.minOrNull()!!..input.maxOrNull()!!) {
            val f = input.sumOf { kotlin.math.abs(it - i) }
            min = if (f < min) f else min

        }

        return min
    }

    override fun part2Impl(input: List<Int>): Int {

        var min = Int.MAX_VALUE

        for (i in input.minOrNull()!!..input.maxOrNull()!!) {
            val f = input.sumOf { costOf(kotlin.math.abs(it - i)) }
            min = if (f < min) f else min

        }

        return min
    }

    private fun costOf(x: Int) : Int {
        return IntRange(1,x).sum()
    }
}