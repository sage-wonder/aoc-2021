import kotlin.math.ceil
import kotlin.math.floor

fun String.toPair() = substring(1, length - 1).split(",").map { it.toInt() }.zipWithNext().first()

fun String.pairCount() = fold(0) { bCount, it ->
    when (it) {
        '[' -> bCount + 1
        ']' -> bCount - 1
        else -> bCount
    }
}

fun String.lastNumberRange() = "\\d+".toRegex().findAll(this).lastOrNull()

fun String.firstNumberRange() = "\\d+".toRegex().findAll(this).firstOrNull()

fun Int.split() = (this / 2.0).let { floor(it).toInt() to ceil(it).toInt() }

fun Pair<Int, Int>.magnitude() = first * 3 + second * 2

fun String.findExplosionRange() =
    """\[\d+,\d+\]""".toRegex().findAll(this).filter {
        substring(0, it.range.first).pairCount() >= 4
    }.firstOrNull()

fun String.findSplitRange() = """\d\d""".toRegex().find(this)

fun String.findMagnitudeRange() = """\[\d+,\d+\]""".toRegex().find(this)

fun String.explode(match: MatchResult): String {
    println("Exploding ${match.value} found at ${match.range}")
    println("Before explosion: $this")
    val (left, right) = match.value.toPair()
    val leftS = substring(0, match.range.first)
    val rightS = substring(match.range.last + 1)
    val s1 = leftS.lastNumberRange()?.let { m ->
        leftS.replaceRange(m.range, (m.value.toInt() + left).toString())
    } ?: leftS
    val s2 = rightS.firstNumberRange()?.let { m ->
        rightS.replaceRange(m.range, (m.value.toInt() + right).toString())
    } ?: rightS
    return (s1 + '0' + s2).also { println("After explosion: $it") }
}

fun String.split(match: MatchResult): String {
    println("Splitting ${match.value} found at ${match.range}")
    println("Before split: $this")
    val replace = match.value.toInt().split().let { "[${it.first},${it.second}]" }
    return replaceRange(match.range, replace).also { println("After split: $it") }
}

fun String.explodeAndSplit(): String {
    return findExplosionRange()?.let { explode(it).explodeAndSplit() }
        ?: findSplitRange()?.let { split(it).explodeAndSplit() }
        ?: this
}

fun String.magnitude(): String {
    return findMagnitudeRange()?.let {
        val m = it.value.toPair().magnitude()
        val t = this.replaceRange(it.range, m.toString())
        return t.magnitude()
    } ?: this
}

fun part1(input: List<String>) {
    input.reduce { a, b -> "[$a,$b]".explodeAndSplit() }
        .also { println(it) }
        .magnitude()
        .also { println("Total magnitude: $it") }
}

fun part2(input: List<String>) {
    (input.indices).flatMap { i -> (input.indices).map { j -> i to j } }
        .filter { (i, j) -> i != j }
        .maxOf { (i, j) ->
            val sum = "[${input[i]},${input[j]}]".explodeAndSplit()
            sum.magnitude().toInt().also { println("[$i,$j] : $sum : $it") }
        }.also { println("Largest magnitude: $it") }
}

fun main() {
    val input = readInput("day18")
    part1(input)
    part2(input)
}
