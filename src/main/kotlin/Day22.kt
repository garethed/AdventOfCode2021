import java.lang.Integer.parseInt

data class Cube(val x1: Int, val x2:Int, val y1:Int, val y2:Int, val z1:Int, val z2:Int, val state:Boolean) {
    
    fun intersects(other:Cube): Boolean {
        return  (other.x2 >= x1 && other.x1 <= x2)
                && (other.y2 >= y1 && other.y1 <= y2)
                && (other.z2 >= z1 && other.z1 <= z2)
    }

    fun containedIn(other:Cube) : Boolean {
        return (other.x1 <= x1 && other.x2 >= x2)
                && (other.y1 <= y1 && other.y2 >= y2)
                && (other.z1 <= z1 && other.z2 >= z2)
    }

    fun splitX(x:Int): List<Cube> {
        assert(state)
        val next = mutableListOf<Cube>()
        if (x > x1) {
            next.add(Cube(x1, minOf(x2,x - 1), y1, y2, z1, z2, true))
        }
        if (x <= x2) {
            next.add(Cube(maxOf(x, x1), x2, y1, y2, z1, z2, true))
        }
        return next
    }

    fun splitY(y:Int): List<Cube> {
        assert(state)
        val next = mutableListOf<Cube>()
        if (y > y1) {
            next.add(Cube(x1, x2, y1,  minOf(y2,y - 1), z1, z2, true))
        }
        if (y <= y2) {
            next.add(Cube(x1, x2, maxOf(y, y1), y2, z1, z2, true))
        }
        return next
    }
    
    fun splitZ(z:Int): List<Cube> {
        assert(state)
        val next = mutableListOf<Cube>()
        if (z > z1) {
            next.add(Cube(x1, x2, y1, y2, z1, minOf(z2,z - 1), true))
        }
        if (z <= z2) {
            next.add(Cube(x1, x2, y1, y2, maxOf(z, z1), z2, true))
        }
        return next
    }

    fun split(other: Cube): List<Cube> {
        return splitX(other.x1)
            .flatMap { it.splitX(other.x2 + 1) }
            .flatMap { it.splitY(other.y1) }
            .flatMap { it.splitY(other.y2 + 1) }
            .flatMap { it.splitZ(other.z1) }
            .flatMap { it.splitZ(other.z2 + 1) }
            .filter { !it.containedIn(other) }
    }
    
    fun countOn(subsequentCubes:List<Cube>): Long {
        if (!state) return 0L
        
        val remaining = ArrayDeque(subsequentCubes)
        
        while (remaining.any()) {
            val next = remaining.removeFirst()
            
            if (intersects(next)) {
                
                val parts = split(next)
                
                return parts.sumOf { it.countOn(remaining)}
            }
        }

       // println(this)

        return (x2 - x1 + 1).toLong() * (y2 - y1 + 1).toLong() * (z2 - z1 + 1).toLong()
    }
}

class Day22 : DayWithInputFile<Long, List<Cube>>() {

    override fun tests() {
        test(::part1Impl, 39, """on x=10..12,y=10..12,z=10..12
on x=11..13,y=11..13,z=11..13
off x=9..11,y=9..11,z=9..11
on x=10..10,y=10..10,z=10..10""")
        test(::part1Impl, 590784, testInput)
    }

    override fun parseInput(input: String): List<Cube> {

        fun parseLine(line:String) : Cube {
            val parts = line.split(" ")
            val digits = parts[1]
                .split(Regex("[^0-9-]"))
                .filter { it != "" }
                .map { parseInt(it)}
            
            return Cube(minOf(digits[0], digits[1]),
                maxOf(digits[0], digits[1]),
                minOf(digits[2], digits[3]),
                maxOf(digits[2], digits[3]),
                minOf(digits[4], digits[5]),
                maxOf(digits[4], digits[5]),
                parts[0] == "on")
        }

        return input.lines().map {parseLine(it)}
    }

    override fun part1Impl(input: List<Cube>): Long {

        val remaining = ArrayDeque(input)

        remaining.add(Cube(51, 1000000, -1000000, 1000000, -1000000, 1000000, false))
        remaining.add(Cube(-1000000, -51, -1000000, 1000000, -1000000, 1000000, false))
        remaining.add(Cube(-1000000, 1000000,51, 1000000, -1000000, 1000000,  false))
        remaining.add(Cube(-1000000, 1000000,-1000000, -51, -1000000, 1000000,  false))
        remaining.add(Cube(-1000000, 1000000,-1000000, 1000000,51, 1000000, false))
        remaining.add(Cube(-1000000, 1000000,-1000000, 1000000,-1000000, -51,  false))


        var sum = 0L

        while (remaining.any()) {
            val next = remaining.removeFirst()
            sum += next.countOn(remaining)
            //println(sum)
        }

        return sum

    }

    override fun part2Impl(input: List<Cube>): Long {
        val remaining = ArrayDeque(input)

        var sum = 0L

        while (remaining.any()) {
            val next = remaining.removeFirst()
            sum += next.countOn(remaining)
            //println(sum)
        }

        return sum
    }

    val testInput = """on x=-20..26,y=-36..17,z=-47..7
on x=-20..33,y=-21..23,z=-26..28
on x=-22..28,y=-29..23,z=-38..16
on x=-46..7,y=-6..46,z=-50..-1
on x=-49..1,y=-3..46,z=-24..28
on x=2..47,y=-22..22,z=-23..27
on x=-27..23,y=-28..26,z=-21..29
on x=-39..5,y=-6..47,z=-3..44
on x=-30..21,y=-8..43,z=-13..34
on x=-22..26,y=-27..20,z=-29..19
off x=-48..-32,y=26..41,z=-47..-37
on x=-12..35,y=6..50,z=-50..-2
off x=-48..-32,y=-32..-16,z=-15..-5
on x=-18..26,y=-33..15,z=-7..46
off x=-40..-22,y=-38..-28,z=23..41
on x=-16..35,y=-41..10,z=-47..6
off x=-32..-23,y=11..30,z=-14..3
on x=-49..-5,y=-3..45,z=-29..18
off x=18..30,y=-20..-8,z=-3..13
on x=-41..9,y=-7..43,z=-33..15
on x=-54112..-39298,y=-85059..-49293,z=-27449..7877
on x=967..23432,y=45373..81175,z=27513..53682"""
}