import java.lang.Integer.parseInt

data class Paper(val points: Set<Point>, val folds: List<Point>) {

    fun fold(): Paper {
        val fold = folds[0]
        return Paper(
            points.map { fold(it, fold) }.toSet(),
            folds.subList(1, folds.size)

        )
    }

    fun fold(point: Point, fold:Point):Point {

        return if (fold.x > 0 && point.x > fold.x) {
            Point( fold.x - (point.x - fold.x), point.y )
        } else if (fold.y > 0 && point.y > fold.y) {
            Point(point.x,  fold.y - (point.y - fold.y))
        } else point
    }

    fun print() {
        println()

        for (y in 0..points.maxOf { it.y }) {
            for (x in 0..points.maxOf { it.x }) {
                print(if (points.contains(Point(x,y))) "#" else ".")
            }
            println()
        }

    }


}

class Day13 : DayWithInputFile<Int, Paper>() {

    override fun tests() {
        test(::part1Impl, 17, testInput)
    }

    override fun parseInput(input: String): Paper {
        val parts = input.replace("\r\n", "\n").split("\n\n")
        val points = parts[0]
            .lines()
            .map { Point.fromString(it) }
            .toSet()

        val folds = parts[1]
            .lines()
            .map { it.split("=") }
            .map {
                val i = parseInt(it[1])
                if (it[0].endsWith("x")) Point(i,0) else Point(0, i)
            }

        return Paper(points, folds)
    }

    override fun part1Impl(input: Paper): Int {
        return input.fold().points.count()

    }

    override fun part2Impl(input: Paper): Int {
        var paper = input
        while (paper.folds.any()) {
            paper = paper.fold()
        }

        paper.print()

        return 0


    }

    val testInput = """6,10
0,14
9,10
0,3
10,4
4,11
6,0
6,12
4,1
0,13
10,12
3,4
3,0
8,4
1,10
2,14
8,10
9,0

fold along y=7
fold along x=5"""
}