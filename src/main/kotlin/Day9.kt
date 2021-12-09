class Day9 : DayWithInputFile<Int, Array<IntArray>>() {

    override fun tests() {
        test(::part1Impl, 15, testInput)
        test(::part2Impl, 1134, testInput)
    }

    override fun parseInput(input: String): Array<IntArray> {
        return input
            .lines()
            .map { l -> l.toCharArray().map { it.digitToInt() }.toIntArray() }.toTypedArray()
    }

    override fun part1Impl(input: Array<IntArray>): Int {
        val grid = Grid(input[0].size, input.size)
        fun v(p:Point): Int { return input[p.y][p.x] }
        return grid.points()
            .filter { p -> grid.adjacent(p).all { v(it) >  v(p) }}
            .sumOf { v(it) + 1 }
    }

    override fun part2Impl(input: Array<IntArray>): Int {

        val grid = Grid(input[0].size, input.size)

        fun v(p:Point): Int { return input[p.y][p.x] }

        fun buildBasin(p:Point, found:MutableSet<Point>) {
            found.add(p)
            grid.adjacent(p)
                .toSet()
                .minus(found)
                .filter { v(it) > v(p) && v(it) < 9}
                .forEach { buildBasin(it, found) }
        }

        return grid.points()
            .filter { p -> grid.adjacent(p).all { v(it) >  v(p) }}
            .map { var basin = HashSet<Point>(); buildBasin(it, basin);  basin.size }
            .sortedDescending()
            .take(3)
            .reduce { acc, item -> acc * item }
    }

    val testInput = """2199943210
3987894921
9856789892
8767896789
9899965678"""
}