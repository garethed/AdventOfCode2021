fun inputFromFile(day: String): String {
    return Day::class.java.getResource("${day.lowercase()}.txt").readText()
}