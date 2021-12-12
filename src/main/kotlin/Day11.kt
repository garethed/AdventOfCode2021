import java.util.*
import kotlin.collections.ArrayDeque

class Day11 : DayWithInputFile<Int, OctoGrid>() {

    override fun tests() {
        test(::part1Impl, 1656, testInput)
        test(::part2Impl, 195, testInput)
    }

    override fun parseInput(input: String): OctoGrid {
        return OctoGrid(input)
    }

    override fun part1Impl(input: OctoGrid): Int {
        repeat(100) { input.step(); /*input.print()*/ }
        return input.flashCount
    }

    override fun part2Impl(input: OctoGrid): Int {
        var step = 1
        while (!input.step()) {
            step++
        }

        return step

    }

    val testInput = """5483143223
2745854711
5264556173
6141336146
6357385478
4167524645
2176841721
6882881134
4846848554
5283751526"""
}

class OctoGrid(val data : Array<IntArray>) {
    val grid = Grid(data[0].size, data.size, data)
    val pendingFlashes = ArrayDeque<Point>()
    var flashCount: Int = 0

    constructor(input : String) : this(IntArray2D(input))
    constructor(grid: OctoGrid) : this(CopyArray(grid.data))

    fun step() : Boolean {

        grid.points().forEach { increment(it) }

        while (pendingFlashes.any()) {
            val p = pendingFlashes.removeFirst();
            grid.adjacentWithDiagonals(p).forEach { increment(it) }
        }

        var allFlashed = true

        grid.points().forEach {
            if (grid.v(it) > 9) {
                grid.v(it, 0)
            }
            else {
                allFlashed = false
            }
        }

        return allFlashed
    }

    fun print() {
        data.forEach {
            it.forEach { i -> print(i) }
            println()
        }
        println()
    }

    private fun increment(point: Point) {
        val i = grid.v(point) + 1
        if (i == 10) {
            pendingFlashes.add(point)
            flashCount++
        }
        grid.v(point, i)
    }


}