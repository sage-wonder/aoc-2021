import java.lang.Integer.min
import kotlin.math.abs

fun main() {
    fun part1(input: String): Int {
        val pos = input.split(",").map { it.toInt() }
        val max = pos.maxOrNull()!!
        return (0..max).fold(Int.MAX_VALUE) { minFuel, p ->
            min(minFuel, pos.sumOf { abs(it - p) })
        }
    }

    fun part2(input: String): Int {
        val pos = input.split(",").map { it.toInt() }
        val max = pos.maxOrNull()!!

//        val costMap: MutableMap<Int, Int> = mutableMapOf()
//        fun costStep(s: Int): Int {
//            if (s == 0) return 0
//            return costMap[s] ?: (s + costStep(s - 1)).also { costMap[s] = it }
//        }
//        return (0..max).fold(Int.MAX_VALUE) { minFuel, p ->
//            min(minFuel, pos.sumOf { costStep(abs(it - p)) })
//        }
        return (0..max).fold(Int.MAX_VALUE) { minFuel, p ->
            min(minFuel, pos.sumOf {
                val d = abs(it - p)
                d * (d + 1) / 2
            })
        }
    }

    val input = readInput("day7")[0]
    println(part1(input))
    println(part2(input))
}
