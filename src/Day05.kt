fun initSeaLines(data: List<String>) =
    data.map {
        it.split(" -> ")
            .map {
                it.split(",")
                    .map { it.toInt() }
                    .let { it[0] to it[1] }
            }.let { it[0] to it[1] }
    }

fun initSeaFloor(data: List<SeaLine>): SeaFloor {
    val (maxX, maxY) = data.fold(0 to 0) { dim, line ->
        val maxX = Integer.max(dim.first, Integer.max(line.first.first, line.second.first))
        val maxY = Integer.max(dim.second, Integer.max(line.first.second, line.second.second))
        maxX to maxY
    }
    return List(maxY + 1) { MutableList(maxX + 1) { 0 } }
}

fun SeaFloor.mark(line: SeaLine) {
    val isHorizontal = line.first.second == line.second.second
    val isVertical = line.first.first == line.second.first

    if (isHorizontal || isVertical) {
        val fixed = if (isHorizontal) line.first.second else line.first.first
        val range = if (isHorizontal) listOf(line.first.first, line.second.first).sorted() else
            listOf(line.first.second, line.second.second).sorted()
        if (isHorizontal)
            this[fixed].let { (range[0]..range[1]).forEach { index -> it[index] += 1 } }
        else (range[0]..range[1]).forEach { this[it][fixed] += 1 }
    } else {
        val yRange = if (line.first.second < line.second.second)
            (line.first.second..line.second.second)
        else
            (line.first.second downTo line.second.second)

        var startX = line.first.first
        val delta = if (line.first.first < line.second.first) 1 else -1
        yRange.forEach { indexY ->
            this[indexY][startX] += 1
            startX += delta
        }
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        val seaLines = initSeaLines(input)
            .filter { it.first.first == it.second.first || it.first.second == it.second.second }
        val seaFloor = initSeaFloor(seaLines)
        seaLines.forEach { seaFloor.mark(it) }
        return seaFloor.sumOf { it.count { it > 1 } }
    }

    fun part2(input: List<String>): Int {
        val seaLines = initSeaLines(input)
        val seaFloor = initSeaFloor(seaLines)
        seaLines.forEach { seaFloor.mark(it) }
        return seaFloor.sumOf { it.count { it > 1 } }
    }

    val input = readInput("day5")
    println(part1(input))
    println(part2(input))
}
