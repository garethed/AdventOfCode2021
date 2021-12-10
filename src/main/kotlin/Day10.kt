import java.util.*

class Day10 : DayWithInputFile<Long, List<String>>() {

    override fun tests() {
        test(::part1Impl, 26397, testInput)
        test(::part2Impl, 288957, testInput)
    }

    override fun parseInput(input: String): List<String> {
        return input.lines()
    }

    override fun part1Impl(input: List<String>): Long {
        return input
            .map { illegalChar(it) }
            .sumOf {
                when (it) {
                    ')' -> 3L
                    ']' -> 57L
                    '}' -> 1197L
                    '>' -> 25137L
                    else -> 0L
                } as Long
            } 
    }

    private fun illegalChar(input: String) : Char? {
        val stack = Stack<Char>()
        input.forEach {
            when {
                isOpen(it) -> stack.push(it)
                !matches(it, stack.pop()) -> return it
            }
        }
        return null
    }

    private fun missingChars(input: String) : Stack<Char> {
        val stack = Stack<Char>()
        input.forEach {
            when {
                isOpen(it) -> stack.push(it)
                !matches(it, stack.pop()) -> return Stack<Char>()
            }
        }
        return stack
    }

    fun isOpen(char: Char) : Boolean {
        return char == '(' || char == '[' || char == '{' || char == '<'
    }

    fun matches (c1: Char, c2: Char) : Boolean {
        if (!isOpen(c1) && isOpen(c2)) {
            return matches(c2, c1)
        }
        return when (c1) {
            '(' -> c2 == ')'
            '[' -> c2 == ']'
            '{' -> c2 == '}'
            '<' -> c2 == '>'
            else -> throw Exception("invalid")
        }
    }



    override fun part2Impl(input: List<String>): Long {
        val scores = input
            .map { missingChars(it) }
            .filter { it.size > 0}
            .map { scoreRemainder(it) }
            .toList().sorted()

        return scores[scores.size / 2]
    }

    private fun scoreRemainder(stack: Stack<Char>) : Long {
        return stack
            .reversed()
            .fold(0) { sum, next ->
                sum * 5 + when(next) {
                    '(' -> 1L
                    '[' -> 2L
                    '{' -> 3L
                    '<' -> 4L
                    else -> throw Exception("invalid $next")
                }
            }
    }

    val testInput = """[({(<(())[]>[[{[]{<()<>>
[(()[<>])]({[<{<<[]>>(
{([(<{}[<>[]}>{[]{[(<()>
(((({<>}<{<{<>}{[]{[]{}
[[<[([]))<([[{}[[()]]]
[{[{({}]{}}([{[{{{}}([]
{<[[]]>}<{[{[{[]{()[[[]
[<(<(<(<{}))><([]([]()
<{([([[(<>()){}]>(<<{{
<{([{{}}[<[[[<>{}]]]>[]]"""
}