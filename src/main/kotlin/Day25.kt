
data class Cucumbers(val south: Set<Point>, val east: Set<Point>, val w:Int, val h:Int, val turn:Int, val moved:Boolean) {

    fun next() : Cucumbers {

        val nextSouth = mutableSetOf<Point>()
        val nextEast = mutableSetOf<Point>()

        var moved = false

        for (e in east) {
            val adjacent = eastOf(e)
            if (south.contains(adjacent) || east.contains(adjacent)) {
                nextEast.add(e)
            }
            else {
                nextEast.add(adjacent)
                moved = true
            }
        }

        for (s in south) {
            val adjacent = southOf(s)
            if (south.contains(adjacent) || nextEast.contains(adjacent)) {
                nextSouth.add(s)
            }
            else {
                nextSouth.add(adjacent)
                moved = true
            }
        }

        return Cucumbers(nextSouth, nextEast, w, h, turn + 1, moved)

    }

    fun eastOf(p:Point) : Point {
        if (p.x < w - 1) {
            return Point(p.x + 1, p.y)
        }
        return Point(0, p.y)
    }

    fun southOf(p:Point) : Point {
        if (p.y < h - 1) {
            return Point(p.x , p.y + 1)
        }
        return Point(p.x, 0)
    }

    fun print() {
        for (y in 0 until h) {
            for (x in 0 until w) {
                if (east.contains(Point(x,y))) {
                    print(">")
                }
                else if (south.contains(Point(x,y))) {
                    print("v")
                }
                else {
                    print(".")
                }
            }
            println()
        }
        println()
    }

}

class Day25 : DayWithInputFile<Int,Cucumbers>() {
    override fun tests() {
        test(::part1Impl, 58, testInput)
    }

    override fun parseInput(input: String): Cucumbers {
        val lines = input.lines()
        val w = lines[0].length
        val h = lines.count()
        val south = mutableSetOf<Point>()
        val east = mutableSetOf<Point>()

        for (y in lines.indices) {
            for (x in lines[0].indices) {
                when (lines[y][x]) {
                    'v' -> south.add(Point(x,y))
                    '>' -> east.add(Point(x,y))
                }
            }
        }

        return Cucumbers(south, east, w, h, 0, true)
    }

    override fun part1Impl(input: Cucumbers): Int {

        var state = input

        do {
            //state.print()
            state = state.next()

        } while (state.moved)

        return state.turn
    }

    override fun part2Impl(input: Cucumbers): Int {
        TODO("Not yet implemented")
    }


    val testInput = """v...>>.vv>
.vv>>.vv..
>>.>v>...v
>>v>>.>.v.
v>v.vv.v..
>.>>..v...
.vv..>.>v.
v.v..>>v.v
....v..v.>"""
}