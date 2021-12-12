typealias Graph = MutableMap<String, MutableSet<String>>

fun main() {

    fun List<String>.buildGraph(): Graph {
        val nodeMap: Graph = mutableMapOf()
        this.forEach {
            val nodes = it.split("-")
            nodeMap.computeIfAbsent(nodes[0]) { mutableSetOf() }.add(nodes[1])
            nodeMap.computeIfAbsent(nodes[1]) { mutableSetOf() }.add(nodes[0])
        }
        return nodeMap
    }

    fun part1(graph: Graph): Int {
        fun Graph.buildPath(path: List<String>, start: String, end: String): List<List<String>> {
            if (start == end) return listOf(path + listOf(end))
            val newPath = path + listOf(start)
            fun canVisit(n: String) = n != n.lowercase() || !newPath.contains(n)
            return this[start]?.filter { canVisit(it) }?.flatMap { buildPath(newPath, it, end) } ?: emptyList()
        }

        return graph.buildPath(emptyList(), "start", "end")
            .onEach { println(it) }
            .size
    }

    fun part2(graph: Graph): Int {
        fun Graph.buildPath(path: List<String>, start: String, end: String): List<List<String>> {
            if (start == end) return listOf(path + listOf(end))
            val newPath = path + listOf(start)
            fun canVisit(n: String): Boolean {
                // Nope. you can't go back to start
                if (n == "start") return false

                // Must be a big cave or
                // never been here before or
                // no other small cave was visited more than once before
                return n != n.lowercase()
                        || !newPath.contains(n)
                        || newPath.filter { it == it.lowercase() }.groupBy { it }.filter { it.value.size > 1 }.isEmpty()
            }
            return this[start]?.filter { canVisit(it) }?.flatMap { buildPath(newPath, it, end) } ?: emptyList()
        }

        return graph.buildPath(emptyList(), "start", "end")
            .onEach { println(it) }
            .size
    }

    val graph = readInput("day12").buildGraph()
    println(part1(graph))
    println(part2(graph))
}
