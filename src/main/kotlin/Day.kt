import kotlin.system.measureTimeMillis

interface AoC {
    fun part1(input:String) : String
    fun part2(input:String) : String
    fun getInput():String
    fun tests()
}


abstract class Day<ReturnType,InputType> : AoC {

    override fun part1(input: String): String {
        return time { part1Impl(parseInput(input)).toString() }
    }

    override fun part2(input: String): String {
        return time { part2Impl(parseInput(input)).toString() }
    }

    abstract fun parseInput(input:String) : InputType
    abstract fun part1Impl(input:InputType) : ReturnType
    abstract fun part2Impl(input:InputType) : ReturnType

    fun test(func: (InputType) -> ReturnType, expected: ReturnType, input: InputType) {
        val actual = time { func(input) }
        if (actual == expected) {
            println("Test: ".green() + "${input.toString().take(40)}".white() + " -> $expected".green())
        }
        else {
            println("Test: ".red() + "${input.toString().take(40)}".white() + "-> $actual but should be $expected".red());
        }
    }
    fun test(func: (InputType) -> ReturnType, expected: ReturnType, input: String) {
        test(func, expected, parseInput(input))
    }

    private fun <T>time(func:() -> (T) ) : T {
        var result:T
        val time = measureTimeMillis { result = func() }
        print("($time ms) ")
        return result
    }
}

abstract class DayWithInputFile<ReturnType,InputType> : Day<ReturnType,InputType>() {

    override fun getInput(): String {
        return inputFromFile(javaClass.simpleName)
    }

}