import java.util.PriorityQueue
import kotlin.math.min

// If out of bounds, returns null
fun Point.riskLevel(c: Cave) = c.getOrNull(first)?.getOrNull(second)

fun part1(cave: Cave): Int {
    val visited = mutableSetOf<Point>()
    val totalRisk = mutableMapOf<Point, Int>()
    (cave.indices).forEach { y ->
        (cave[0].indices).forEach { x ->
            totalRisk[Point(x, y)] = Int.MAX_VALUE
        }
    }

    val pq = PriorityQueue<Point>(compareBy { totalRisk[it] })

    fun Point.assessRisk(sumRisk: Int) {
        val riskValue = riskLevel(cave)
        if (visited.contains(this) || riskValue == null) return
        totalRisk[this] = min(sumRisk + riskValue, totalRisk[this] ?: 0)
        pq.add(this)
    }

    var curr = Point(0, 0).also { totalRisk[it] = 0 }
    val target = cave[0].size - 1 to cave.size - 1

    while (true) {
        val (x, y) = curr
        val sumRisk = totalRisk[curr]!!
        listOf(
            x - 1 to y,
            x + 1 to y,
            x to y - 1,
            x to y + 1
        ).forEach { it.assessRisk(sumRisk) }
        visited.add(curr)
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
    return (1 until scaleFactor).fold(this) { acc, add ->
        val newGrid = acc.takeLast(rowCount).map { l ->
            l.map { (it + 1).cap(9) }
        }
        acc + newGrid
    }
}

fun part2(cave: Cave): Int {
    return part1(cave.scaleX(5).scaleY(5))
}

fun main() {
    val input = readInput("test").map { it.map { it.digitToInt() } }
    println(part1(input))
    println(part2(input))
}