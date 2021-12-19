import java.lang.Integer.parseInt
import java.lang.Math.abs
import kotlin.math.min

data class Point3(val x:Int, val y:Int, val z:Int) {

    fun distance(other:Point3): String {
        val parts = listOf(abs(x - other.x), abs(y - other.y), abs(z - other.z)).sorted()
        return parts.joinToString("." ) { it.toString() } + if (parts.distinct().count() == 3 && parts.none { it == 0 }) "d" else ""
    }

    fun transform(m: List<Int>): Point3 {
        return Point3(x * m[0] + y * m[1] + z * m[2], x * m[3] + y * m[4] + z * m[5], x * m[6] + y * m[7] + z * m[8] )
    }

    override fun toString(): String {
        return "($x, $y, $z)"
    }

    fun distanceInt(other: Point3) : Int {
        return abs(x - other.x) + abs(y - other.y) + abs(z - other.z)
    }

    fun plus(other:Point3): Point3 {
        return Point3(x + other.x, y + other.y, z + other.z)
    }
}

class Pair3(val p1:Point3, val p2:Point3) {
    val distance = p1.distance(p2)
    val components = listOf(p2.x - p1.x, p2.y - p1.y, p2.z - p1.z)
    val magnitudes = listOf(abs(p2.x - p1.x), abs(p2.y - p1.y), abs(p2.z - p1.z))

    fun matrix(other:Pair3) : List<Int> {

        fun compare(i:Int, j:Int): Int {
            return if (magnitudes[i] == other.magnitudes[j]) components[i] / other.components[j] else 0
        }

        return listOf(
            compare(0,0), compare(0,1), compare(0,2),
            compare(1,0), compare(1,1), compare(1,2),
            compare(2,0), compare(2,1), compare(2,2)
        )
    }

    fun transform(other: Pair3) : Point3 {
        val matrix = matrix(other)
        val p3 = other.p1.transform(matrix)
        val p4 = other.p2.transform(matrix)

        return Point3(min(p1.x, p2.x) - min(p3.x, p4.x), min(p1.y, p2.y) - min(p3.y, p4.y), min(p1.z, p2.z) - min(p3.z, p4.z))
    }

    override fun toString(): String {
        return distance
    }

}

class Scanner(val points : Set<Point3>, val offset:Point3) {
    val distances = points.associateWith { p1 -> points.filter { it != p1 }.map { p2 -> Pair3(p1,p2) }.toSet() }.toMutableMap()

    fun alignOther(other:Scanner): Scanner {
        val edgePair = edgePair(other)
        val matrix = edgePair.first.matrix(edgePair.second)
        val transform = edgePair.first.transform(edgePair.second)

        val rotated = other.points.map { it.transform(matrix) }

        val transformed = rotated.map { it.plus(transform) }

        return Scanner(transformed.toSet(), transform )
    }

    private fun edgePair(other:Scanner) : Pair<Pair3, Pair3> {
        val match = findMatchingPair(other)

        for (edge in match.first.value) {
            val edgeMatches = match.second.value.filter { it.distance == edge.distance && it.distance.endsWith("d")}
            if (edgeMatches.size == 1) {
                val edgeMatch = edgeMatches.first()
                return Pair(edge, edgeMatch)
            }
        }

        throw Exception()
    }

    private fun findMatchingPair(other: Scanner): Pair<Map.Entry<Point3, Set<Pair3>>, Map.Entry<Point3, Set<Pair3>>> {

        for (point in distances) {
            for (other in other.distances) {
                if (overlap(point.value, other.value) > 3) {
                    return Pair(point, other)
                }
            }
        }

        throw Exception("failed")
    }

    fun print() {
        println (points.sortedBy { 1000000 * it.x + 1000 * it.y + it.z }.joinToString(" "));
    }

    override fun toString(): String {
        return offset.toString()
    }
}

fun overlap(set1:Set<Pair3>, set2:Set<Pair3>): Int {
    return set1.map { it.distance }.toSet().intersect(set2.map { it.distance }.toSet()).count()
}


class Day19 : DayWithInputFile<Int, List<Set<Point3>>>() {

    val testInput = inputFromFile("day19-test")

    override fun tests() {
        test(::part1Impl, 6, testInput2)
        test(::part1Impl, 6, testInput3)
        test(::part1Impl, 79, testInput)
        test(::part2Impl, 3621, testInput)

    }

    override fun parseInput(input: String): List<Set<Point3>> {
        return splitOnBlankLines(input)
            .map { scanner ->
                scanner
                    .lines()
                    .drop(1)
                    .map {
                        val parts = it.split(",")
                        Point3(parseInt(parts[0]), parseInt(parts[1]), parseInt(parts[2]))
                    }.toSet()
            }
    }

     fun alignScanners(input: List<Set<Point3>>): List<Scanner> {

        val scanners = input.map { Scanner(it, Point3(0,0,0)) }

        val alignedScanners = mutableListOf(scanners.first())
        val remainingScanners = scanners.drop(1).toMutableList()
        scanners.first().print()

        start@ while (!remainingScanners.isEmpty()) {
            for (s1 in remainingScanners) {
                for (s2 in alignedScanners) {
                    try {
                        //s1.print()
                        var aligned = s2.alignOther(s1)
                        alignedScanners.add(aligned)
                        remainingScanners.remove(s1)
                        //aligned.print()
                        continue@start
                    } catch (e: Exception) {
                    }
                }
            }

            throw Exception("couldn't align")
        }

        return alignedScanners
    }

    override fun part1Impl(input: List<Set<Point3>>): Int {
        return alignScanners(input).flatMap { it.points }.distinct().count()
    }

    override fun part2Impl(input: List<Set<Point3>>): Int {

        var points = alignScanners(input).map { it.offset}

        return points.flatMap { i1 ->
            points.map { i2 ->
                i1.distanceInt(i2)
            }
        }
            .maxOrNull()!!
    }


    val testInput3 = """--- scanner 0 ---
-1,-1,1
-2,-2,2
-3,-3,3
-2,-3,1
5,6,-4
8,0,7

--- scanner 0 ---
2,-3,11
3,-4,12
4,-5,13
3,-3,13
-4,2,4
-7,-9,10"""

    val testInput2 = """--- scanner 0 ---
-1,-1,1
-2,-2,2
-3,-3,3
-2,-3,1
5,6,-4
8,0,7

--- scanner 0 ---
1,-1,1
2,-2,2
3,-3,3
2,-1,3
-5,4,-6
-8,-7,0

--- scanner 0 ---
-1,-1,-1
-2,-2,-2
-3,-3,-3
-1,-3,-2
4,6,5
-7,0,8

--- scanner 0 ---
1,1,-1
2,2,-2
3,3,-3
1,3,-2
-4,-6,5
7,0,8

--- scanner 0 ---
1,1,1
2,2,2
3,3,3
3,1,2
-6,-4,-5
0,7,-8"""
}

