import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.HashSet

class Cave(val id:String) {
    val neighbours: MutableSet<Cave> = HashSet()
    val start:Boolean = id == "start"
    val end:Boolean = id == "end"
    val small:Boolean = id == id.lowercase()
}

class Day12 : DayWithInputFile<Int, Map<String, Cave>>() {

    override fun tests() {
        test(::part1Impl, 10, testInput)
        test(::part2Impl, 36, testInput)
    }

    override fun parseInput(input: String): Map<String, Cave> {

        val caves = HashMap<String,Cave>()

        fun getCave(id:String) : Cave {
            if (!caves.containsKey(id)) {
                caves[id] = Cave(id)
            }
            return caves[id]!!
        }

        input
            .lines()
            .forEach {
                val parts = it.split("-").map { getCave(it) }
                parts[0].neighbours.add(parts[1])
                parts[1].neighbours.add(parts[0])
            }

        return caves
    }

    override fun part1Impl(input: Map<String, Cave>): Int {
        return countRoutes(input, false)
    }

    fun countRoutes(input: Map<String, Cave>, allowRevisitSmall:Boolean) : Int {

        val start = input["start"]
        val route = Stack<Cave>()

        val routes = ArrayList<String>()

        fun findRoute(next:Cave, hasRevisited: Boolean) {

            var revisit = hasRevisited

            if (next.small && route.contains(next)) {
                if (next.start || !allowRevisitSmall || hasRevisited) {
                    return
                }
                else {
                    revisit = true
                }
            }

            route.push(next)

            if (input.size < 10) println( route.map {it.id}.joinToString("-"))

            if (next.end) {
                    routes.add(route.map { it.id }.joinToString("-"))
            }
            else {
                next.neighbours
                    .forEach { findRoute(it, revisit) }
            }

            route.pop()
        }

        findRoute(start!!, false)

        return routes.size
    }

    override fun part2Impl(input: Map<String, Cave>): Int {
        return countRoutes(input, true)
    }

    val testInput = """start-A
start-b
A-c
A-b
b-d
A-end
b-end"""
}