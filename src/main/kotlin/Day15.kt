
class Day15 : DayWithInputFile<Int,Array<IntArray>>() {

    override fun tests() {
        test(::part1Impl, 40, testInput)
        test(::part2Impl, 315, testInput)
    }

    override fun parseInput(input: String): Array<IntArray> {
        return IntArray2D(input)
    }

    override fun part1Impl(input: Array<IntArray>): Int {

        val risks = Grid(input)
        val pathRisks = HashMap<Point,Int>()

        val pending = ArrayDeque<Point>()
        pending.add(Point(0,0))
        pathRisks[Point(0,0)] = 0

        while (pending.any()) {
            val next = pending.removeFirst()
            val risk = pathRisks[next]!!

            risks.adjacent(next).forEach {
                val pathRisk = risk + risks.v(it)
                if (pathRisks.getOrDefault(it, Int.MAX_VALUE) > pathRisk) {
                    pathRisks[it] = pathRisk
                    pending.add(it)
                }
            }
        }

        return pathRisks[Point(risks.w - 1, risks.h - 1)]!!

    }

    override fun part2Impl(input: Array<IntArray>): Int {
        val smallGrid = Grid(input)
        val grid = Grid(input[0].size * 5, input.size * 5)
        val pathRisks = HashMap<Point,Int>()

        val pending = ArrayDeque<Point>()
        pending.add(Point(0,0))
        pathRisks[Point(0,0)] = 0

        fun getRisk(next:Point): Int {
            return ((input[next.y % smallGrid.h][next.x % smallGrid.w] + (next.y / smallGrid.h) + (next.x / smallGrid.w) - 1) % 9) + 1
        }

        /*for (y in 0 until smallGrid.h * 5) {
            for (x in 0 until smallGrid.w * 5)
            {
                print(getRisk(Point(x,y)))
            }
            println()
        }*/


        while (pending.any()) {
            val next = pending.removeFirst()
            val risk = pathRisks[next]!!

            grid.adjacent(next).forEach {
                val addedRisk = getRisk(it)
                val pathRisk = risk + addedRisk
                if (pathRisks.getOrDefault(it, Int.MAX_VALUE) > pathRisk) {
                    pathRisks[it] = pathRisk
                    pending.add(it)
                }
            }
        }

        /*var point = Point(grid.w - 1, grid.h - 1)

        while (point != Point(0,0)) {
            println("$point ${getRisk(point)} ${pathRisks[point]}")
            point = grid.adjacent(point).minByOrNull { pathRisks[it]!! }!!
        }*/


        return pathRisks[Point(grid.w - 1, grid.h - 1)]!!
        
    }

    val testInput = """1163751742
1381373672
2136511328
3694931569
7463417111
1319128137
1359912421
3125421639
1293138521
2311944581"""
}