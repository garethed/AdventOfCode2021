class Day18 : DayWithInputFile<Int, List<SnailPair>>() {

    override fun tests() {
        test(::part1Impl, 445, "[1,1]\n[2,2]\n[3,3]\n[4,4]")
        test(::part1Impl, 445, "[[[[1,1],[2,2]],[3,3]],[4,4]]")
        test(::part1Impl, 791, "[1,1]\n[2,2]\n[3,3]\n[4,4]\n[5,5]")
        test(::part1Impl, 1137, "[1,1]\n[2,2]\n[3,3]\n[4,4]\n[5,5]\n[6,6]")
        test(::part1Impl, 4140, testInput)
        test(::part2Impl, 3993, testInput)
    }

    override fun parseInput(input: String): List<SnailPair> {

        fun parseLine(line: String) : SnailPair {

            var i = 0;

            fun parseNext(): SnailPair {
                if (line[i] == '[') {
                    i++  // [
                    var left = parseNext()
                    i++ // ,
                    var right = parseNext()
                    i++ // ]

                    return SnailPair.Pair(left, right)
                } else {
                    return SnailPair.Value(line[i++].digitToInt());
                }
            }

            return parseNext();
        }

        return input.lines().map { parseLine(it) }
    }

    override fun part1Impl(input: List<SnailPair>): Int {

        var output = input.reduce { l,r -> l.plus(r) }
        output.reduce()
        return output.magnitude()
    }

    override fun part2Impl(input: List<SnailPair>): Int {

        fun sum(x: SnailPair, y:SnailPair): Int {
            var s = x.plus(y);
            var m = s.magnitude()
            //println("$m: $x + $y -> $s ")
            return m
        }

        return input.flatMap { x ->
            input.filter { it != x }
                .flatMap { y-> sequenceOf( sum(x,y), sum(y,x)) }

        }.maxOrNull()!!
    }

    val testInput = """[[[0,[5,8]],[[1,7],[9,6]]],[[4,[1,2]],[[1,4],2]]]
[[[5,[2,8]],4],[5,[[9,9],0]]]
[6,[[[6,2],[5,6]],[[7,6],[4,7]]]]
[[[6,[0,7]],[0,9]],[4,[9,[9,0]]]]
[[[7,[6,4]],[3,[1,3]]],[[[5,5],1],9]]
[[6,[[7,3],[3,2]]],[[[3,8],[5,7]],4]]
[[[[5,4],[7,7]],8],[[8,3],8]]
[[9,3],[[9,9],[6,[4,9]]]]
[[2,[[7,7],7]],[[5,8],[[9,3],[0,2]]]]
[[[[5,2],5],[8,[3,7]]],[[5,[7,5]],[4,4]]]"""
}

sealed class SnailPair {

    var isLeft = true
    var parent:SnailPair.Pair? = null;

    abstract fun explode() : Boolean
    abstract fun split() : Boolean
    abstract fun items() : Sequence<SnailPair>
    abstract fun leftValue() : Value
    abstract fun rightValue() : Value
    abstract fun magnitude() : Int
    abstract fun clone() : SnailPair

    fun plus(other:SnailPair) : SnailPair {
        val new = SnailPair.Pair(this.clone(), other.clone())
        new.reduce()
        //println(new)
        return new
    }

    fun reduce() {
        do {
            //println(this.toString())
        } while (explode() || split())
    }

    fun depth(): Int {
        return if (parent == null) 0 else parent!!.depth() + 1
    }

    class Pair(left: SnailPair, right: SnailPair) : SnailPair() {

        var left:SnailPair = left
            get() = field
            set(value) {
                field = value
                value.parent = this
                value.isLeft = true
            }

        var right:SnailPair = right
            get() = field
            set(value) {
                field = value
                value.parent = this
                value.isLeft = false
            }

        init // grr. why can't we call setter from constructor
        {
            this.left = left
            this.right = right
        }

        fun prevValue() : Value? {
            var pair:SnailPair.Pair? = this
            while (pair != null && pair.isLeft) pair = pair.parent
            return pair?.parent?.left?.rightValue()
        }

        fun nextValue() : Value? {
            var pair:SnailPair.Pair? = this
            while (pair != null && !pair.isLeft) pair = pair.parent
            return pair?.parent?.right?.leftValue()
        }


        override fun explode() : Boolean {
            if (depth() == 4) {
                var prev = prevValue()
                if (prev != null) {
                    prev.value += leftValue().value
                }
                var next = nextValue()
                if (next != null) {
                    next.value += rightValue().value
                }

                if (isLeft) {
                    parent!!.left = Value(0)
                }
                else {
                    parent!!.right = Value(0)
                }
                return true
            }
            else {
                 if (left.explode()) return true
                return right.explode()
            }
        }

        override fun split() : Boolean {
            if (left.split()) return true
            return right.split()
        }

        override fun items(): Sequence<SnailPair> {
            return sequenceOf(left, right)
        }

        override fun leftValue(): Value {
            return left.leftValue()
        }
        override fun rightValue() : Value {
            return right.rightValue()
        }


        override fun magnitude(): Int {
            return 3 * left.magnitude() + 2 * right.magnitude()
        }

        override fun toString(): String {
            return "[$left,$right]"
        }

        override fun clone(): SnailPair {
            return Pair(left.clone(), right.clone())
        }
    }

    class Value(var value:Int) : SnailPair() {

        override fun explode() : Boolean {
            return false
        }

        override fun split(): Boolean {
            if (value >= 10) {

                var new = Pair( Value(value / 2), Value((value + 1) / 2))

                if (isLeft) {
                    parent!!.left = new
                }
                else {
                    parent!!.right = new
                }

                return true
            }
            return false
        }

        override fun items(): Sequence<SnailPair> {
            return emptySequence()
        }

        override fun leftValue(): Value {
            return this
        }

        override fun rightValue(): Value {
            return this
        }

        override fun magnitude(): Int {
            return value
        }

        override fun toString(): String {
            return value.toString()
        }

        override fun clone(): SnailPair {
            return Value(value)
        }
    }

    class DoneException : Exception()
}
