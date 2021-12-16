import java.util.PriorityQueue
import kotlin.math.min

// If out of bounds, returns null
fun Point.riskLevel(c: Cave) = c.getOrNull(second)?.getOrNull(first)

fun part1(cave: Cave): Int {

    val totalRisk = mutableMapOf<Point, Int>().withDefault { Int.MAX_VALUE }
    val pq = PriorityQueue<Point>(compareBy { totalRisk[it] })
    val visited = mutableSetOf<Point>()

    var curr = Point(0, 0).also { totalRisk[it] = 0 }
    val target = cave[0].size - 1 to cave.size - 1

    while (true) {
        val (x, y) = curr
        val currRisk = totalRisk.getValue(curr)

        listOf(
            Point(x - 1, y),
            Point(x + 1, y),
            Point(x, y - 1),
            Point(x, y + 1)
        ).forEach { neighbor ->
            // We only care about neighbors that
            // - we haven't visited yet
            // - isn't out of bounds
            val riskLevel = neighbor.riskLevel(cave)
            if (!visited.contains(neighbor) && riskLevel != null) {
                totalRisk[neighbor] = min(currRisk + riskLevel, totalRisk.getValue(neighbor))
                pq.add(neighbor)
            }
        }
        visited.add(curr)

        // Move onto next smallest risk point that we haven't visited yet
        while (visited.contains(curr)) {
            curr = pq.remove()
        }
        if (curr == target) break
    }

    return totalRisk[target]!!
}

fun Cave.scaleX(scaleFactor: Int): Cave {
    return this.map { l ->
        (0 until scaleFactor).flatMap { add ->
            l.map { (it + add).cap(9) }
        }
    }
}

fun Cave.scaleY(scaleFactor: Int): Cave {
    val rowCount = this.size
    return (1 until scaleFactor).fold(this) { acc, _ ->
        acc + acc.takeLast(rowCount).map { l ->
            l.map { (it + 1).cap(9) }
        }
    }
}

fun part2(cave: Cave): Int {
    return part1(cave.scaleX(5).scaleY(5))
}

fun main() {
    val input = readInput("day15").map { it.map { it.digitToInt() } }
    println(part1(input))
    println(part2(input))
}