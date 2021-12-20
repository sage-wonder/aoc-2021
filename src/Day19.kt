import kotlin.math.abs
import kotlin.math.sqrt

data class Scanner(
    val id: Int,
    var origin: List<Int> = listOf(0, 0, 0),
    val beacons: List<List<Int>>
) {
    val distanceMap = beacons.indices.associateWith { x ->
        beacons.indices
            .map { y -> (x to y) to distance(beacons[x], beacons[y]) }
            .sortedBy { it.second }
    }

    override fun hashCode() = id
}

fun distance(x: List<Int>, y: List<Int>) =
    x.zip(y).sumOf { (x1, x2) -> (x2 - x1) * (x2 - x1) }.let { sqrt(it.toDouble()) }

fun manhattanDistance(x: List<Int>, y: List<Int>) =
    x.zip(y).sumOf { (x1, x2) -> abs(x1 - x2) }

fun delta(x: List<Int>, y: List<Int>) =
    x.zip(y).map { it.first - it.second }

fun initScanners(input: List<String>): List<Scanner> {
    val scannerMap = mutableMapOf<Int, MutableList<List<Int>>>()
    var currIndex = 0
    input.forEach {
        if (it.startsWith("---"))
            currIndex = it.split(" ")[2].toInt()
        else {
            scannerMap.getOrPut(currIndex) { mutableListOf() }.apply {
                add(it.split(",").map { it.toInt() })
            }
        }
    }
    return scannerMap.map { Scanner(it.key, beacons = it.value) }
}

fun canMap(s1: Scanner, s2: Scanner): List<Pair<Int, Int>> {
    val dMap1 = s1.distanceMap
    val dMap2 = s2.distanceMap
    dMap1.forEach { (_, iDist) ->
        dMap2.forEach { (_, jDist) ->
            val dist1 = iDist.map { it.second }.toSet()
            val dist2 = jDist.map { it.second }.toSet()
            val distIntersect = dist1.intersect(dist2)
            if (distIntersect.size >= 12) {
                return distIntersect.map { dist ->
                    val p1 = iDist.find { it.second == dist }!!.first.second
                    val p2 = jDist.find { it.second == dist }!!.first.second
                    p1 to p2
                }
            }
        }
    }
    return emptyList()
}

// 24 possible rotations
fun List<Int>.rotate(d: Int): List<Int> {
    val (x, y, z) = this
    return when (d) {
        0 -> listOf(x, y, z)
        1 -> listOf(x, z, -y)
        2 -> listOf(x, -y, -z)
        3 -> listOf(x, -z, y)
        4 -> listOf(-x, -y, z)
        5 -> listOf(-x, -z, -y)
        6 -> listOf(-x, y, -z)
        7 -> listOf(-x, z, y)
        8 -> listOf(y, -x, z)
        9 -> listOf(y, z, x)
        10 -> listOf(y, x, -z)
        11 -> listOf(y, -z, -x)
        12 -> listOf(-y, x, z)
        13 -> listOf(-y, -z, x)
        14 -> listOf(-y, -x, -z)
        15 -> listOf(-y, z, -x)
        16 -> listOf(z, y, -x)
        17 -> listOf(z, x, y)
        18 -> listOf(z, -y, x)
        19 -> listOf(z, -x, -y)
        20 -> listOf(-z, -y, -x)
        21 -> listOf(-z, -x, y)
        22 -> listOf(-z, y, x)
        23 -> listOf(-z, x, -y)
        else -> error("blip")
    }
}

fun reduce(src: Scanner, tgt: Scanner): Scanner? {
    val mappedBeacons = canMap(src, tgt)
    if (mappedBeacons.isNotEmpty()) {
        (0 until 24).forEach { r ->
            // I just need 2 mapped beacons to verify rotation
            val (s1b, s2b) = mappedBeacons.take(2)
                .map { (i, j) -> src.beacons[i] to tgt.beacons[j].rotate(r) }
                .unzip()
            val d1 = delta(s1b[0], s1b[1])
            val d2 = delta(s2b[0], s2b[1])
            if (d1 == d2) {
                println("rotation: $r")
                val originDelta = delta(s1b[0], s2b[0])
                tgt.origin = originDelta
                val calibratedBeacons = tgt.beacons.map { it.rotate(r).zip(originDelta).map { it.first + it.second } }
                return src.copy(beacons = (calibratedBeacons + src.beacons).distinctBy { it.toString() })
            }
        }
    }
    return null
}

fun main() {
    val input = readInput("test").filter { it.isNotEmpty() }
    val scanners = initScanners(input)
    var merged: Scanner = scanners[0]
    val remaining = scanners.toMutableSet()
    while (remaining.isNotEmpty()) {
        val tgt = remaining.first { canMap(merged, it).isNotEmpty() }
        merged = reduce(merged, tgt)!!
        remaining.remove(tgt)
    }

    println("Number of distinct beacons: ${merged.beacons.size}")

    val maxOrigDistance = scanners.indices
        .flatMap { i -> scanners.indices.map { j -> i to j } }
        .maxOf { (i, j) -> manhattanDistance(scanners[i].origin, scanners[j].origin) }

    println("Max manhattan distance: $maxOrigDistance")
}
