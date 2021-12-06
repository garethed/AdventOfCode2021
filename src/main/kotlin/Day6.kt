import java.lang.Integer.parseInt
import java.lang.Long.parseLong

class Day6 : DayWithInputFile<Long, List<Long>>() {

    override fun tests() {
        test(::part1Impl, 5934, "3,4,3,1,2")
        test(::part2Impl, 26984457539, "3,4,3,1,2")
    }

    override fun parseInput(input: String): List<Long> {
        return input.split(",").map { parseLong(it) }
    }

    override fun part1Impl(input: List<Long>): Long {
        return calc(input, 80)
    }

    fun calc(input: List<Long>, generations:Int): Long {
        var counts = input.groupingBy { it }.eachCount().map { it.key to it.value.toLong()}.toMap().toMutableMap()
        for (count in 0 until generations) {
            counts = counts.map { (it.key - 1) to it.value }.toMap().toMutableMap()
            counts[8] = counts[-1] ?: 0
            counts[6] = (counts[6] ?: 0) +  (counts[-1] ?: 0)
            counts[-1] = 0
        }

        return counts.values.sum()
    }

    override fun part2Impl(input: List<Long>): Long {
        return calc(input, 256)
    }
}