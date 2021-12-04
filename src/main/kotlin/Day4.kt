class Day4 : DayWithInputFile<Int, Day4.Bingo>() {

    override fun parseInput(input: String): Bingo {

        val numbers = input.lines().first().split(',').map { Integer.parseInt(it) }

        val boards = input.lines()
            .filter { it.isNotEmpty() }
            .drop(1)
            .chunked(5)
            .map { toBoard(it) }

        return Bingo(numbers, boards)

    }

    private fun toBoard(input: List<String>) : Board {

        var test =             input
            .joinToString(" ")
            .replace("  ", " ")

        return Board(
            input
                .joinToString(" ")
                .replace("  ", " ")
                .trim()
                .split(' ')
                .map { i -> Integer.parseInt(i) }
                .toIntArray())
    }

    override fun tests() {
        test(::part1Impl, 4512, testData)
        test(::part2Impl, 1924, testData)
    }

    override fun part1Impl(input: Bingo): Int {

        try {
            input.numbers.forEach {
                input.boards.forEach { b -> b.markNumber(it) }
            }
        }
        catch (e: CompletedBoard) {
            return e.board.value() * e.lastNumber
        }

        throw Exception("no winner")
    }

    override fun part2Impl(input: Bingo): Int {

        val boards = input.boards.toMutableList()


        input.numbers.forEach {
            boards.toMutableList().forEach { b ->
                try {
                    b.markNumber(it)
                }
                catch (e:CompletedBoard) {
                    if (boards.size > 1) {
                        boards.remove(b)
                    }
                    else {
                        return b.value() * it
                    }
                }
            }
        }

        throw Exception("no winner")
    }

    data class Board(val data:IntArray) {
        fun get(x:Int, y:Int): Int {
            return data[x + 5 * y]
        }

        fun markNumber(n:Int) {
            for (i in data.indices) {
                if (data[i] == n) {
                    data[i] = -1
                }
            }

            x@ for (x in 0..4) {
                for (y in 0..4) {
                    if (data[x + 5 * y] >= 0) {
                        continue@x
                    }

                }
                throw CompletedBoard(this, n)
            }

            y@ for (y in 0..4) {
                for (x in 0..4) {
                    if (data[x + 5 * y] >= 0) {
                        continue@y
                    }

                }
                throw CompletedBoard(this, n)
            }
        }

        fun value(): Int {
            return data.filter { it >= 0 }.sum()
        }

    }
    data class Bingo(val numbers:List<Int>, val boards:List<Board>)
    data class CompletedBoard(val board: Board, val lastNumber: Int) : Exception()

    private val testData = """7,4,9,5,11,17,23,2,0,14,21,24,10,16,13,6,15,25,12,22,18,20,8,19,3,26,1

22 13 17 11  0
 8  2 23  4 24
21  9 14 16  7
 6 10  3 18  5
 1 12 20 15 19

 3 15  0  2 22
 9 18 13 17  5
19  8  7 25 23
20 11 10 24  4
14 21 16 12  6

14 21 17 24  4
10 16 15  9 19
18  8 23 26 20
22 11 13  6  5
 2  0 12  3  7"""
}