import java.lang.Integer.parseInt
import java.lang.Math.abs

class Point3(val x:Int, val y:Int, val z:Int) {

    fun distance(other:Point3): String {
        val parts = listOf(abs(x - other.x), abs(y - other.y), abs(z - other.z)).sorted()
        return parts.joinToString("." ) { it.toString() }
    }
}

class Scanner(val points : Set<Point3>) {
    val distances = points.associateWith { points.map { p -> p.distance(it) }.toSet() }
}

class Day19 : DayWithInputFile<Int, List<Set<Point3>>>() {

    val testInput = inputFromFile("day19-test")

    override fun tests() {
        test(::part1Impl, 79, testInput)
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

    override fun part1Impl(input: List<Set<Point3>>): Int {

        val scanners = input.map { Scanner(it) }
        val allPoints = HashSet<HashSet<String>>()

        fun overlap(set1:Set<String>, set2:Set<String>): Int {
            return set1.intersect(set2).count()
        }

        allPoints.addAll(scanners[0].distances.values.map { it.toHashSet() })

        var unmatched = ArrayDeque<HashSet<String>>()
        unmatched.addAll(scanners.drop(1).flatMap { it.distances.values.map { v -> v.toHashSet()} })

        while (unmatched.any()) {

            var matched = mutableListOf<HashSet<String>>()

            unmatched.forEach { toMatch ->
                val match = allPoints.maxByOrNull { overlap(toMatch, it) }
                if (match != null && overlap(toMatch, match) >= 3) {
                    match.addAll(toMatch)
                    matched.add(toMatch)
                }
            }

            unmatched.removeAll(matched)

            if (unmatched.any()) {
                allPoints.add(unmatched.removeFirst())
            }
        }


        return allPoints.count()

    }

    override fun part2Impl(input: List<Set<Point3>>): Int {
        TODO("Not yet implemented")
    }
}

