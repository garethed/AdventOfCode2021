

fun main(args: Array<String>) {

    for (i in 25 downTo 1) {
        try {
            val day = Class.forName("Day$i").getConstructor().newInstance() as AoC

            println("  ***** Day $i *****  ".bgBlue().black())

            val input  = day.getInput()
            day.tests()

            println("Part1:".bgBlue().black() + " " + day.part1(input).blue().bold())
            println("Part2:".bgBlue().black() + " " + day.part2(input).blue().bold())
            if (args.isEmpty()) {
                break
            } else {
                println()
            }
        }
        catch (e : ClassNotFoundException) {}
    }
}