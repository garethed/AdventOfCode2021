fun inputFromFile(day: String): String {
    return Day::class.java.getResource("${day.lowercase()}.txt").readText()
}

infix fun Int.toward(to: Int): IntProgression {
    val step = if (this > to) -1 else 1
    return IntProgression.fromClosedRange(this, to, step)
}

fun IntArray2D(input:String) : Array<IntArray> {
    return input.lines().map { l -> l.toCharArray().map { it.digitToInt() }.toIntArray() }.toTypedArray()
}

fun CopyArray(array: Array<IntArray>): Array<IntArray> {
    return array.map { it.clone() }.toTypedArray()
}

fun splitOnBlankLines(input:String): List<String> {
    return input.replace("\r\n", "\n").split("\n\n")
}