data class Point(val x:Int, val y:Int) {

    val adjacent:Sequence<Point> get()
        = sequenceOf(Point(x - 1, y), Point(x, y - 1), Point(x + 1, y), Point(x, y + 1))
}

data class Grid(val w:Int, val h:Int) {
    fun points(): Sequence<Point> {
        return sequence {
            for (y in 0 until h) {
                for (x in 0 until w) {
                    yield(Point(x, y))
                }
            }
        }
    }

    fun adjacent(p:Point): Sequence<Point> {
        return p.adjacent.filter { it.x >= 0 && it.y >= 0 && it.x < w && it.y < h }
    }
}

data class Line(val p1:Point, val p2:Point) {
}