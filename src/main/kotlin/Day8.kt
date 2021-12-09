
data class Digits(val allPatterns: List<String>, val output:List<String>)

class Day8 : DayWithInputFile<Int, List<Digits>>() {

    override fun tests() {
        test(::part1Impl, 26, testInput)
        test(::part2Impl, 61229, testInput)
    }

    override fun parseInput(input: String): List<Digits> {

        fun parseLine(line:String):Digits {
            val parts = line
                .split(" | ")
                .map { it.split(" ") }

            return Digits(parts[0].map { it.toCharArray().sorted().joinToString("") } , parts[1].map { it.toCharArray().sorted().joinToString("") })
        }

        return input.lines().map { parseLine(it) }
    }

    override fun part1Impl(input: List<Digits>): Int {
        return input
            .flatMap { it.output }
            .count { it.length == 2 || it.length == 4 || it.length == 3 || it.length == 7 }
    }

    override fun part2Impl(input: List<Digits>): Int {

        return input.map { decodeOutput(it) }.sum()
    }

    fun decodeOutput(digits: Digits) : Int {

        //acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab
        // dddd
        //e    a
        //e    a
        // ffff
        //g    b
        //g    b
        // cccc

        // 1 4 7 8
        // 6 - 0,6,9x
        // 5 - 2,3x,5x

        fun sameSegmentCount(first:String, second:String): Int {
            return first.toCharArray().intersect(second.asIterable()).count()
        }

        val digitMap = HashMap<String, Int>()
        digitMap[digits.allPatterns.first { it.length == 7 }] = 8
        val one = digits.allPatterns.first { it.length == 2 }
        digitMap[one] = 1
        digitMap[digits.allPatterns.first { it.length == 3 }] = 7
        val four = digits.allPatterns.first { it.length == 4 }
        digitMap[four] = 4
        val three = digits.allPatterns.first { it.length == 5 && sameSegmentCount(it, one) == 2 }
        digitMap[three] = 3
        val nine = digits.allPatterns.first { it.length == 6 && sameSegmentCount(it, four) == 4 }
        digitMap[nine] = 9
        val five = digits.allPatterns.first { it.length == 5 && sameSegmentCount(it, nine) == 5 && !digitMap.containsKey(it)}
        digitMap[five] = 5
        val six = digits.allPatterns.first { it.length == 6 && sameSegmentCount(it, five) == 5 && !digitMap.containsKey(it)}
        digitMap[six] = 6

        digitMap[digits.allPatterns.first { it.length == 6 && !digitMap.containsKey(it)}] = 0
        digitMap[digits.allPatterns.first { it.length == 5 && !digitMap.containsKey(it)}] = 2

        return 1000 * digitMap[digits.output[0]]!! + 100 * digitMap[digits.output[1]]!! + 10 * digitMap[digits.output[2]]!! + digitMap[digits.output[3]]!!

    }

    val testInput = """be cfbegad cbdgef fgaecd cgeb fdcge agebfd fecdb fabcd edb | fdgacbe cefdb cefbgd gcbe
edbfga begcd cbg gc gcadebf fbgde acbgfd abcde gfcbed gfec | fcgedb cgb dgebacf gc
fgaebd cg bdaec gdafb agbcfd gdcbef bgcad gfac gcb cdgabef | cg cg fdcagb cbg
fbegcd cbd adcefb dageb afcb bc aefdc ecdab fgdeca fcdbega | efabcd cedba gadfec cb
aecbfdg fbg gf bafeg dbefa fcge gcbea fcaegb dgceab fcbdga | gecf egdcabf bgf bfgea
fgeab ca afcebg bdacfeg cfaedg gcfdb baec bfadeg bafgc acf | gebdcfa ecba ca fadegcb
dbcfg fgd bdegcaf fgec aegbdf ecdfab fbedc dacgb gdcebf gf | cefg dcbef fcge gbcadfe
bdfegc cbegaf gecbf dfcage bdacg ed bedf ced adcbefg gebcd | ed bcgafe cdgba cbgef
egadfb cdbfeg cegd fecab cgb gbdefca cg fgcdab egfdb bfceg | gbdfcae bgc cg cgb
gcafb gcf dcaebfg ecagb gf abcdeg gaef cafbge fdbac fegbdc | fgae cfgab fg bagce"""
}