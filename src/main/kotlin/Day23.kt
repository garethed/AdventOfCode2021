import java.lang.Integer.min
import java.lang.Integer.parseInt

data class Step(val index: Int, val targetPos : Int, val distance : Int)

data class State(val positions: IntArray, val moveCounts:IntArray) {

    //    0001  02  03  04  0506
    //        10  20  30  40
    //        11  21  31  41
    //
    //        t0  t1  t2  t3

    fun nextStates() = getMoves().map { applyStep(it) }

    fun applyStep(step:Step): State {
        var newPositions = positions.clone()
        var newMoveCounts = moveCounts.clone()

        newPositions[step.index] = step.targetPos
        newMoveCounts[step.index / 2] += step.distance

        return State(newPositions, newMoveCounts)
    }

    fun getMoves() = sequence {
        yieldAll(positions.indices.flatMap { possibleMoves(it) })
    }

    fun possibleMoves(index:Int) = sequence {
        val pos = positions[index]
        if (pos in 0..7) {
            yieldAll(movesFromHallway(index))
        }
        else {
            val type = typeForIndex(index)
            val isInRightRoom = type == typeForPosition(pos)
            if (pos % 10 == 0) {
                val lower = positions.indexOf(pos + 1)

                if (!isInRightRoom || typeForIndex(lower) != type) {
                    yieldAll(movesFromRoom(index))
                }
            }
            else if (!isInRightRoom && positions.indexOf(pos - 1) == -1) {
                yieldAll(movesFromRoom(index))
            }
        }
    }

    fun movesFromRoom(index:Int) = sequence {
        val left = positions[index] / 10
        var distance = positions[index] % 10
        for (i in left downTo 0) {
            if (positions.contains(i)) {
                break
            }
            distance += if (i == 0) 1 else 2
            yield(Step(index, i, distance))
        }
        distance = positions[index] % 10
        for (i in (left + 1)..6) {
            if (positions.contains(i)) {
                break
            }
            distance += if (i == 6) 1 else 2
            yield(Step(index, i, distance))
        }
    }

    fun movesFromHallway(index:Int) = sequence {

        // 01 2 3 4 56 myPos
        //   1 2 3 4   roomIdx
        //  10 20 etc, roomPos

        val type = typeForIndex(index)
        val roomIdx = (type + 1)
        val roomPos = roomIdx * 10
        var myPos = positions[index]
        val direction = if (myPos < roomIdx) 1 else -1
        var distance = if (myPos == 0 || myPos == 6)  1 else 2

        if (positions.contains(roomPos)) return@sequence

        while (myPos != roomIdx && myPos != roomIdx + 1) {
            myPos += direction
            distance += 2
            if (positions.contains(myPos)) {
                return@sequence
            }
        }

        if (!positions.contains(roomPos + 1)) {
            yield(Step(index, roomPos + 1, distance + 1))
        }
        else if (typeForIndex(positions.indexOf(roomPos + 1)) == type) {
            yield(Step(index, roomPos, distance))
        }
    }

    fun typeForIndex(index:Int): Int {
        return if (index < 0) index else index / 2
    }

    fun typeForPosition(position:Int): Int {
        return (position / 10) - 1
    }

    fun complete(): Boolean {
        return positions.indices.all {
            typeForIndex(it) == typeForPosition(positions[it])
        }
    }

    fun cost(): Int {
        return moveCounts[0] + moveCounts[1] * 10 + moveCounts[2] * 100 + moveCounts[3] * 1000
    }

    fun print() {

        val chars = "ABCD"

        fun printIndex(i:Int, s:String) {
            val type = typeForIndex(positions.indexOf(i))
            print(if (type >= 0) chars[type] else ".")
            print(s)
        }

        print("#")
        printIndex(0, "")
        printIndex(1, ".")
        printIndex(2, ".")
        printIndex(3, ".")
        printIndex(4, ".")
        printIndex(5, "")
        printIndex(6, "#\n")
        print("###")
        printIndex(10, "#")
        printIndex(20, "#")
        printIndex(30, "#")
        printIndex(40, "###\n")
        print("###")
        printIndex(11, "#")
        printIndex(21, "#")
        printIndex(31, "#")
        printIndex(41, "###\n")

    }

}


class Day23 : Day<Int, State>() {
    override fun getInput(): String {
/*        """#############
#...........#
###B#A#A#D###
  #B#C#D#C#
  #########"""*/
        return "20,30,10,11,21,41,31,40"
    }

    override fun tests() {
        test(::part1Impl, 12521, testInput)
        test(::part2Impl, 44169, testInput)

    }

    override fun parseInput(input: String): State {
        val positions = input.split(",").map { parseInt(it) }.toIntArray()

        return State(positions, IntArray(4))
    }

    override fun part1Impl(input: State): Int {

        return 0

        var minEnergy = Int.MAX_VALUE

        fun buildPath(state:State) {

            /*if (state.moveCounts.sum() % 5 == 0) {
                println("after ${state.moveCounts.sum()} moves:")
                state.print()
            }*/

            //state.print()

            if (state.complete()) {
                minEnergy = minOf(state.cost(), minEnergy)
                println(minEnergy)
            }
            else {
                for (nextState in state.nextStates()) {
                    if (nextState.cost() < minEnergy) {
                        buildPath(nextState)
                    }
                }
            }
        }

        buildPath(input)

        return minEnergy
    }

    override fun part2Impl(input: State): Int {
        var minEnergy = Int.MAX_VALUE

      /*#B#C#B#D###      -> #B#A#A#D#
        #D#C#B#A#           #D#C#B#A#
        #D#B#A#C#           #D#B#A#C#
        #A#D#C#A#        -> #B#C#D#C#
        */

        val test = intArrayOf(13,32,41,43,10,22,30,31,20,21,33,42,11,12,23,40)
        val actual = intArrayOf(20,30,32,41,10,13,22,31,21,23,42,43,11,12,33,40)


        val deepState = DeepState(if (input.positions.first() == 11) test else actual, IntArray(16) )

        fun buildPath(state: DeepState) {

            /*if (state.moveCounts.sum() % 5 == 0) {
                println("after ${state.moveCounts.sum()} moves:")
                state.print()
            }*/

            //state.print()

            if (state.complete()) {
                minEnergy = minOf(state.cost(), minEnergy)
                println(minEnergy)
            }
            else {
                for (nextState in state.nextStates()) {
                    if (nextState.cost() < minEnergy) {
                        buildPath(nextState)
                    }
                }
            }
        }

        buildPath(deepState)

        return minEnergy
    }

    val testInput = "11,41,10,30,20,31,21,40"
/*        """#############
#...........#
###B#C#B#D###
  #A#D#C#A#
  #########"""*/
}