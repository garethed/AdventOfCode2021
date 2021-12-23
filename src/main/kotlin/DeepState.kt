data class DeepState(val positions: IntArray, val moveCounts:IntArray) {

    //    0001  02  03  04  0506
    //        10  20  30  40
    //        11  21  31  41
    //        12
    //        13
    //
    //        t0  t1  t2  t3

    fun nextStates() = getMoves().map { applyStep(it) }

    fun applyStep(step:Step): DeepState {
        var newPositions = positions.clone()
        var newMoveCounts = moveCounts.clone()

        newPositions[step.index] = step.targetPos
        newMoveCounts[step.index / 4] += step.distance

        return DeepState(newPositions, newMoveCounts)
    }

    fun getMoves() = sequence {
        yieldAll(positions.indices.reversed().flatMap { possibleMoves(it) })
    }

    fun possibleMoves(index:Int) = sequence {
        val pos = positions[index]
        if (pos in 0..7) {
                yieldAll(movesFromHallway(index))
        }
        else {
            val type = typeForIndex(index)
            val typeForPosition = typeForPosition(pos)
            val clearAbove = (pos - 1 downTo (pos - (pos % 10))).none { positions.contains(it) }
            val needsToMove = (pos..( pos - (pos % 10) + 3)).any { typeForIndex(positions.indexOf(it)) != typeForPosition }

            if (needsToMove && clearAbove) {
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
        var depth = -1


        for (i in 3 downTo 0) {
            if (!positions.contains(roomPos + i)) {
                depth = i
                break
            }
            if (typeForIndex(positions.indexOf(roomPos + i)) != type) {
                return@sequence
            }
        }

        if (depth >= 0) {
            while (myPos != roomIdx && myPos != roomIdx + 1) {
                myPos += direction
                distance += 2
                if (positions.contains(myPos)) {
                    return@sequence
                }
            }

            yield(Step(index, roomPos + depth, distance + depth))
        }
    }

    fun typeForIndex(index:Int): Int {
        return if (index < 0) index else index / 4
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
        print("###")
        printIndex(12, "#")
        printIndex(22, "#")
        printIndex(32, "#")
        printIndex(42, "###\n")
        print("###")
        printIndex(13, "#")
        printIndex(23, "#")
        printIndex(33, "#")
        printIndex(43, "###\n")
    }

}
