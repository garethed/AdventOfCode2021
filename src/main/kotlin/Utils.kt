fun inputFromFile(day: String): String {
    return Day::class.java.getResource("${day.lowercase()}.txt").readText()
}

infix fun Int.toward(to: Int): IntProgression {
    val step = if (this > to) -1 else 1
    return IntProgression.fromClosedRange(this, to, step)
}