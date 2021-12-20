import java.util.*

class Automata(val rules:BitSet, val grid: BitSet, val w:Int, val h:Int)  {

    fun expandGrid(padding:Int) : Automata {
        val newGrid = Automata(rules, BitSet((w + 2 * padding) * (h + 2 * padding)), w + 2 * padding, h + 2 * padding)

        for (y in 0 until h) {
            for (x in 0 until w) {
                newGrid[x + padding, y + padding] = this[x, y]
            }
        }

        return newGrid
    }

    operator fun get(x:Int, y:Int): Boolean {
        return grid[y * w + x]
    }

    operator fun set(x:Int, y:Int, v:Boolean) {
        grid[y * w + x] = v
    }

    fun next(): Automata {
        val next = Automata(rules, BitSet(w * h), w, h)

        for (y in listOf(0, h - 1)) {
            for (x in 0 until w) {
                next[x,y] = if (this[0,0]) rules[255] else rules[0]
            }
        }

        for (y in 0 until h) {
            for (x in listOf(0, w - 1)) {
                next[x,y] = if (this[0,0]) rules[255] else rules[0]
            }
        }


        for (y in 1 until h - 1) {
            for (x in 1 until w - 1) {
                val rule =
                            component(x - 1, y - 1, 256) +
                            component(x, y - 1, 128) +
                            component(x + 1, y - 1, 64) +
                            component(x - 1, y, 32) +
                            component(x, y, 16) +
                            component(x + 1, y, 8) +
                            component(x - 1, y + 1, 4) +
                            component(x, y + 1, 2) +
                            component(x + 1, y + 1, 1)

                next[x,y] = rules[rule]
            }
        }

        return next
    }

    fun component(x:Int, y:Int, v:Int): Int {
        return if (this[x,y]) v else 0
    }

    fun liveCount(): Int {
        return grid.cardinality()
    }

    fun print() {
        for (y in 1 until h - 1) {
            for (x in 1 until w - 1) {
                print(if (this[x, y]) '#' else '.')
            }
            println()
        }
        println()
    }
}

class Day20 : DayWithInputFile<Int,Automata>() {

    val testInput = """..#.#..#####.#.#.#.###.##.....###.##.#..###.####..#####..#....#..#..##..##
#..######.###...####..#..#####..##..#.#####...##.#.#..#.##..#.#......#.###
.######.###.####...#.##.##..#..#..#####.....#.#....###..#.##......#.....#.
.#..#..##..#...##.######.####.####.#.#...#.......#..#.#.#...####.##.#.....
.#..#...##.#.##..#...##.#.##..###.#......#.#.......#.#.#.####.###.##...#..
...####.#..#..#.##.#....##..#.####....##...##..#...#......#.#.......#.....
..##..####..#...#.#.#...##..#.#..###..#####........#..####......#..#

#..#.
#....
##..#
..#..
..###"""

    val testInput2 = """######.#..##..######.....#...#.#...#.######.#.#...#..#..#..###.#####.#.####...#.#.#...#.#.#...#####..###.....#.#.....#.#.#..###..#.#.#####..#.....####.##.##..#..##.#.##.##..##.#####.####.#.#....#...#...#...#.#########..#..#####..#.#.###....#.##.###...##.#..#....##.###...#..##.#..#...#.#.#####.####...#.##..##..#.#######...#..##.##.....#..#..#.###...###.######..##.#..##.######....#.##.##...###.##..#.#.#.#########....####.####.#.#...#.#.#..#.#.##.#...#.#..#######..###..##.#..####.###...#.#.#.##.#####.##.###.#.

#..#.
#....
##..#
..#..
..###""${'"'}    """

    override fun tests() {
        test(::part1Impl, 35, testInput)
        test(::part2Impl, 3351, testInput)
    }

    override fun parseInput(input: String): Automata {
        val parts = splitOnBlankLines(input)

        val rules = stringToBitSet(parts[0].lines().joinToString(("")))
        val lines = parts[1].lines()
        val grid = stringToBitSet(lines.joinToString(""))

        return Automata(rules, grid, lines[0].length, lines.size)
    }

    fun stringToBitSet(string: String): BitSet {
        val bitset = BitSet(string.length)
        string.forEachIndexed{ i, c -> if (c == '#') bitset.set(i, true) }
        return bitset
    }

    override fun part1Impl(input: Automata): Int {
        var grid = input.expandGrid(4)
        //grid.print()
        repeat(2) {
            grid = grid.next()
            //grid.print()
        }
        return grid.liveCount()
    }

    override fun part2Impl(input: Automata): Int {
        var grid = input.expandGrid(51)
        //grid.print()
        repeat(50) {
            grid = grid.next()
            //grid.print()
        }
        return grid.liveCount()
    }
}