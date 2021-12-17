import java.lang.Integer.parseInt

class Day17 : DayWithInputFile<Int,Rect>() {

    val testInput = "target area: x=20..30, y=-10..-5"

    override fun parseInput(input: String): Rect {

        val parts = input.substring(15).split(", y=").flatMap { it.split("..") }.map { parseInt(it) }
        return Rect(parts[0], parts[1], parts[2], parts[3])
    }

    override fun part1Impl(input: Rect): Int {

        var ymax = -1

        x@ for (startingDx in 1..input.xmin) {
            y@ for (startingDy in 0..1000) {


                var dx = startingDx
                var dy = startingDy
                var x = 0
                var y = 0

                var localymax = 0

                while (x <= input.xmax) {
                    x += dx
                    y += dy
                    localymax = maxOf(y, localymax)

                    if (input.contains(x,y)) {
                        ymax = maxOf(localymax, ymax)
                    }
                    if (localymax <= ymax && dy < 0) continue@y
                    if (y < input.ymin && dy < 0) continue@y
                    if (x > input.xmax && y > input.ymax) continue@x
                    if (x > input.xmax) continue@y
                    if (x < input.xmin && dx == 0) continue@x

                    dx = if (dx > 0) dx - 1 else 0
                    dy -= 1

                }

            }
        }

        return ymax
    }

    override fun part2Impl(input: Rect): Int {

        var hits = 0

        x@ for (startingDx in 1..input.xmax) {
            y@ for (startingDy in input.ymin..1000) {


                var dx = startingDx
                var dy = startingDy
                var x = 0
                var y = 0


                while (x <= input.xmax) {
                    x += dx
                    y += dy

                    if (input.contains(x,y)) {
                        hits++
                        println("$startingDx, $startingDy")
                        continue@y
                    }
                    if (y < input.ymin && dy < 0) continue@y
                    if (x > input.xmax && y > input.ymax) continue@x
                    if (x > input.xmax) continue@y
                    if (x < input.xmin && dx == 0) continue@x

                    dx = if (dx > 0) dx - 1 else 0
                    dy -= 1

                }

            }
        }
        return hits
    }

    override fun tests() {
        test(::part1Impl, 45, testInput)
        test(::part2Impl, 112, testInput)
    }
}