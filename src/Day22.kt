import kotlin.math.max
import kotlin.math.min

fun IntRange.overlaps(r: IntRange) = contains(r.first) || contains(r.last)
fun IntRange.contains(r: IntRange) = contains(r.first) && contains(r.last)

typealias Cube = List<IntRange>

fun Cube.volume(): Long = this.fold(1L) { acc, r -> acc * (r.last - r.first + 1) }

fun Cube.overlaps(c: Cube): Cube? {
    return this.zip(c).map { (r1, r2) ->
        if (!r1.overlaps(r2) && !r2.contains(r1)) return null
        listOf(r1.first, r1.last, r2.first, r2.last).sorted().let { it[1]..it[2] }
    }
}

fun Cube.contains(c: Cube): Cube? {
    return this.zip(c).map { (r1, r2) ->
        if (!r1.contains(r2)) return null
        r2
    }
}

data class Instruction(
    val on: Boolean,
    val cube: Cube
)

fun String.parse(): Instruction {
    val t = this.split(" ")
    return t[1].split(",").let {
        Instruction(
            on = t[0] == "on",
            cube = it.map {
                it.substring(2)
                    .split("..")
                    .map { it.toInt() }
                    .let { it[0]..it[1] }
            }
        )
    }
}

fun part1(input: List<Instruction>) {
    val cap = 50
    val area = List(cap * 2 + 1) { List(cap * 2 + 1) { MutableList(cap * 2 + 1) { false } } }
    input.forEach {
        val x1 = max(it.cube[0].first, -cap)
        val x2 = min(it.cube[0].last, cap)
        val y1 = max(it.cube[1].first, -cap)
        val y2 = min(it.cube[1].last, cap)
        val z1 = max(it.cube[2].first, -cap)
        val z2 = min(it.cube[2].last, cap)
        (x1..x2).forEach { x ->
            (y1..y2).forEach { y ->
                (z1..z2).forEach { z ->
                    area[x + cap][y + cap][z + cap] = it.on
                }
            }
        }
    }

    val onCount = area.sumOf { x -> x.sumOf { y -> y.count { it } } }
    println(onCount)
}

fun part2(input: List<Instruction>) {
    val cubeCount = mutableMapOf<Cube, Int>().withDefault { 0 }
    input.forEach { s ->
        val c1 = s.cube
        val cubes = cubeCount.keys.toList()
        cubes.forEach { c2 ->
            // If c1 contains c2,
            // nullify all counts
            c1.contains(c2)?.let { cubeCount[c2] = 0 }
            // Otherwise, check for overlaps.
            // Overlapping cube should not be double counted.
                ?: c1.overlaps(c2)?.let { cubeCount[it] = cubeCount.getValue(it) - cubeCount[c2]!! }
        }
        if (s.on) cubeCount[c1] = cubeCount.getValue(c1) + 1
    }
    val volume = cubeCount.map { (cube, ct) -> cube.volume() * ct }.sum()
    println(volume)
}

fun main() {
    val input = readInput("day22").map { it.parse() }
    part1(input)
    part2(input)
}