import java.lang.Integer.parseInt

class Day5 : DayWithInputFile<Int, List<Line>>() {

    override fun tests() {
        test(::part1Impl, 5, testData)
        test(::part2Impl, 12, testData)
    }

    override fun parseInput(input: String): List<Line> {

        fun parsePoint(input:String): Point {
            var coords = input
                .split(",")
                .map { parseInt(it) }

            return Point(coords[0], coords[1])
        }

        return input
            .lines()
            .map { it.split(" -> ")}
            .map { Line(parsePoint(it[0]), parsePoint(it[1]))}
    }

    override fun part1Impl(input: List<Line>): Int {
        return calc(input, false)
    }

    fun calc(input:List<Line>, includeDiagonals:Boolean) : Int {

        var grid = HashMap<Point,Int>()

        fun incr(p:Point) {
            if (!grid.containsKey(p)) {
                grid[p] = 1
            }
            else {
                grid[p] = grid[p]!! + 1
            }
        }

        fun addToGrid(line:Line) {
            if (line.p1.x == line.p2.x) {
                for (y in line.p1.y toward line.p2.y) {
                     incr(Point(line.p1.x, y))
                }
            }
            else if (line.p1.y == line.p2.y) {
                for (x in line.p1.x toward line.p2.x) {
                    incr(Point(x, line.p1.y))
                }
            }
            else if (includeDiagonals) {
                val dy = if (line.p2.y > line.p1.y) 1 else -1
                var y = line.p1.y
                for (x in line.p1.x toward line.p2.x) {
                    incr(Point(x, y))
                    y += dy
                }
            }
        }

        input
            .forEach { addToGrid(it) }

        return grid.filter { it.value > 1 }.count()
    }

    override fun part2Impl(input: List<Line>): Int {
        return calc(input, true)

    }

    val testData = """0,9 -> 5,9
8,0 -> 0,8
9,4 -> 3,4
2,2 -> 2,1
7,0 -> 7,4
6,4 -> 2,0
0,9 -> 2,9
3,4 -> 1,4
0,0 -> 8,8
5,5 -> 8,2"""
}