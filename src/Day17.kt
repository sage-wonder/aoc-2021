import kotlin.math.abs
import kotlin.math.max

typealias Position = Pair<Int, Int>
typealias Velocity = Pair<Int, Int>

operator fun Position.plus(p: Velocity) = Position(this.first + p.first, this.second + p.second)

operator fun Velocity.dec(): Velocity {
    val x = if (first != 0) first - (first / abs(first)) else 0
    return Velocity(x, second - 1)
}

data class Container(
    val xRange: Pair<Int, Int>,
    val yRange: Pair<Int, Int>
) {
    fun inXRange(p: Position) = xRange.first <= p.first && p.first <= xRange.second
    fun inYRange(p: Position) = yRange.first <= p.second && p.second <= yRange.second
    fun inTheZone(p: Position) = inXRange(p) && inYRange(p)
}

fun main() {
    // Maybe this is an overkill.
    // Next time, I shall waste no time and just hardcode.
    val container = readInput("day17").first()
        .split(" ")
        .let {
            val xRange = it[2].substring(2, it[2].length - 1).split("..").map { it.toInt() }.zipWithNext().first()
            val yRange = it[3].substring(2).split("..").map { it.toInt() }.zipWithNext().first()
            Container(xRange, yRange)
        }.also { println(it) }

    val vxMax = container.xRange.second
    val vyMin = container.yRange.first
    // I feel like this could be calculated, but brute force for now
    val vyMax = 1000

    val velocityRange = (1..vxMax).flatMap { x -> (vyMin..vyMax).map { y -> Velocity(x, y) } }
    val possibleVelocity = velocityRange.mapNotNull { v ->
        var coord = Position(0, 0)
        var velocity = v
        var maxY = 0

        // Totally arbitrary..
        repeat(10000) {
            coord += velocity--
            maxY = max(maxY, coord.second)
            if (container.inTheZone(coord)) return@mapNotNull maxY

            // No chance of making it in the zone
            // Don't waste your time and short-circuit here
            if (velocity.first == 0 && !container.inXRange(coord)) return@mapNotNull null
        }
        null
    }

    println("Max height: ${possibleVelocity.maxOrNull()}")
    println("Possible velocity count: ${possibleVelocity.size}")
}