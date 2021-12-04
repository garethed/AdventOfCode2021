class Day1 : DayWithInputFile<Int, List<Int>>() {

    override fun tests() {
        test(::part1Impl, 7, testData)
        test(::part2Impl, 5, testData)
    }

    override fun parseInput(input: String): List<Int> {
        return input.lines().map { Integer.parseInt(it)}
    }

    override fun part1Impl(input: List<Int>): Int {
        return input
            .windowed(2)
            .filter { it[1] > it[0]}
            .count()

    }

    override fun part2Impl(input: List<Int>): Int {
        return part1Impl(
            input
            .windowed(3)
            .map { it.sum() })
    }

    var testData = """199
200
208
210
200
207
240
269
260
263"""
}