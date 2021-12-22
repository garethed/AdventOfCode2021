import java.lang.Integer.min
import java.lang.Integer.parseInt

class Day21 : Day<Long, Pair<Int,Int>>() {

    override fun getInput(): String {
        return "10,3"
    }

    override fun parseInput(input: String): Pair<Int, Int> {
        val parts = input.split(",").map { parseInt(it) }
        return Pair(parts[0], parts[1])
    }

    override fun part1Impl(input: Pair<Int, Int>): Long {

        val dieRolls = generateSequence(1) { it + 1 }.iterator()
        val positions = arrayOf(input.first, input.second)
        val scores = arrayOf(0,0)
        var currentPlayer = 0
        var moves = 0

        do {
            moves++
            val move = dieRolls.next() + dieRolls.next() + dieRolls.next()
            val newPosition = (positions[currentPlayer] + move - 1) % 10 + 1
            val newScore = scores[currentPlayer] + newPosition
            scores[currentPlayer] = newScore
            positions[currentPlayer] = newPosition

            //println("$currentPlayer  $move to $newPosition for $newScore")

            currentPlayer = 1 - currentPlayer


        } while (newScore < 1000)

        return (moves * 3 * scores[currentPlayer]).toLong()
    }

    override fun part2Impl(input: Pair<Int, Int>): Long {

        val p1 = getOutcomeFrequencies(input.first)
        val p2 = getOutcomeFrequencies(input.second)

        fun wins(outcomes:Array<Array<LongArray>>, step:Int): Long {
            return outcomes[step].sumOf { it[21] }
        }
        fun allOutcomes(outcomes:Array<Array<LongArray>>, step:Int): Long {
            return outcomes[step].sumOf { it.sum() }
        }
        fun nonWins(outcomes:Array<Array<LongArray>>, step:Int): Long {
            return allOutcomes(outcomes, step) - wins(outcomes, step)
        }

        var allWins = 0L
        var allLosses = 0L

        for (i in p1.indices) {
            val winsInThisStep = wins(p1, i) * (if (i > 0) nonWins(p2, i - 1) else 1)
            val lossesInThisStep = nonWins(p1, i) * wins(p2, i)

                allWins += winsInThisStep
                allLosses += lossesInThisStep
        }

        return maxOf(allWins, allLosses)
    }

    fun getOutcomeFrequencies(startingPos:Int): Array<Array<LongArray>> {

        val moves = Array(10) { Array(11) { LongArray(22) } }

        fun add (move:Int, pos:Int, score:Int, value:Long) {
            moves[move][pos][score] += value
        }

        val triples = mapOf(3 to 1, 4 to 3, 5 to 6, 6 to 7, 7 to 6, 8 to 3, 9 to 1)

        fun move(moveNumber:Int, prevPos:Int, prevScore:Int, pathsToPrevState:Long) {
            for ((distance, frequency) in triples) {
                val nextPos = (prevPos + distance - 1) % 10 + 1
                add(moveNumber, nextPos, min(21, prevScore + nextPos), pathsToPrevState * frequency)
            }
        }

        move(0, startingPos, 0, 1)

        for (prevMoveNumber in 0..8) {
            for (prevPos in 1..10) {
                for (prevScore in 1..20) {
                    val prevPaths = moves[prevMoveNumber][prevPos][prevScore]
                    if (prevPaths > 0) {
                        move(prevMoveNumber + 1, prevPos, prevScore, prevPaths)
                    }
                }
            }

        }

        return moves
    }

    override fun tests() {
        test(::part1Impl, 739785, "4,8")
        test(::part2Impl, 444356092776315L, "4,8")
    }
}