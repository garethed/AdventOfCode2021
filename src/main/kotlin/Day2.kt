import java.lang.Integer.parseInt

class Day2 : DayWithInputFile<Int, List<Day2.Command>>() {

    data class Command(val direction:String, val distance:Int){}
    data class Position(val d:Int, val x:Int, val a:Int)

    override fun tests() {
        test(::part1Impl, 150, testData)
        test(::part2Impl, 900, testData)
    }

    override fun parseInput(input: String): List<Command> {
        return input.lines()
            .map { it.split(" ") }
            .map { Command(it[0], parseInt(it[1]))}
    }

    override fun part1Impl(input: List<Command>): Int {
        var finalPos = input
            .fold(Position(0,0, 0)) { p, c ->
                when (c.direction) {
                    "up" -> Position(p.d - c.distance, p.x, 0)
                    "down" -> Position(p.d + c.distance, p.x, 0)
                    "forward" -> Position(p.d, p.x + c.distance, 0)
                    else -> throw Exception()
                }
            }

        return finalPos.d * finalPos.x
    }

    override fun part2Impl(input: List<Command>): Int {
        var finalPos = input
            .fold(Position(0,0, 0)) { p, c ->
                when (c.direction) {
                    "up" -> Position(p.d, p.x, p.a - c.distance)
                    "down" -> Position(p.d, p.x, p.a + c.distance)
                    "forward" -> Position(p.d + p.a * c.distance, p.x + c.distance, p.a)
                    else -> throw Exception()
                }
            }

        return finalPos.d * finalPos.x    }

    val testData = """forward 5
down 5
forward 8
up 3
down 8
forward 2"""
}

