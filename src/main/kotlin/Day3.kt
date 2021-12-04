class Day3 : DayWithInputFile<Int, List<Int>>() {

    private var digits:Int = 0

    override fun tests() {
        test(::part1Impl, 198, testInput)
        test(::part2Impl, 230, testInput)
    }

    override fun parseInput(input: String): List<Int> {
        digits = input.lines()[0].length
        return input.lines().map { Integer.parseInt(it, 2)}
    }

    override fun part1Impl(input:List<Int>): Int {
        var gamma = 0
        var epsilon = 0

        for (i in 0 until digits) {
            val bit = getMostCommonBit(i, input)
            gamma += bit shl i
            epsilon += (1 - bit) shl i
        }

        return gamma * epsilon
    }

    fun getMostCommonBit(offset:Int, data:List<Int>): Int {
        val threshold = (data.count() + 1) / 2
        val sum = data.sumOf { it shr offset and 1 }
        return if ( sum >= threshold) 1 else 0
    }

    override fun part2Impl(input:List<Int>): Int {

        val gamma = part2filter(input, false)
        val epsilon = part2filter(input, true)

        return gamma * epsilon
    }

    fun part2filter(data:List<Int>, invert:Boolean): Int {
        var gammaData = data
        var offset = digits - 1
        var gamma = 0
        while (gammaData.count() > 1) {
            var bit = getMostCommonBit(offset, gammaData)
            if (invert) {
                bit = 1 - bit;
            }
            gamma += bit shl offset
            gammaData = gammaData.filter { (it xor gamma) shr offset == 0 }
            offset--
        }
        return gammaData[0];
    }

    val testInput = """00100
11110
10110
10111
10101
01111
00111
11100
10000
11001
00010
01010"""
}


