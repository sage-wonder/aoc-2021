fun main() {
    fun List<String>.isLowPoint(x: Int, y: Int): Boolean {
        val curr = this[x][y].digitToInt()
        return (x == 0 || this[x - 1][y].digitToInt() > curr) &&
                (x == this.size - 1 || this[x + 1][y].digitToInt() > curr) &&
                (y == 0 || this[x][y - 1].digitToInt() > curr) &&
                (y == this[0].length - 1 || this[x][y + 1].digitToInt() > curr)
    }

    fun List<String>.basin(basinArea: MutableSet<Pair<Int, Int>>, x: Int, y: Int): Set<Pair<Int, Int>> {
        if (x < 0 || x >= this.size || y < 0 || y >= this[0].length || this[x][y] == '9') return emptySet()
        basinArea.add(x to y)
        if (!basinArea.contains(x - 1 to y))
            basinArea.addAll(this.basin(basinArea, x - 1, y))
        if (!basinArea.contains(x + 1 to y))
            basinArea.addAll(this.basin(basinArea, x + 1, y))
        if (!basinArea.contains(x to y - 1))
            basinArea.addAll(this.basin(basinArea, x, y - 1))
        if (!basinArea.contains(x to y + 1))
            basinArea.addAll(this.basin(basinArea, x, y + 1))
        return basinArea
    }

    fun part1(input: List<String>): Int {
        return (input.indices).flatMap { x ->
            (0 until input[0].length).mapNotNull { y ->
                if (input.isLowPoint(x, y)) input[x][y]
                else null
            }
        }.sumOf { it.digitToInt() + 1 }
    }

    fun part2(input: List<String>): Int {
        return (input.indices).flatMap { x ->
            (0 until input[0].length).mapNotNull { y ->
                if (input.isLowPoint(x, y)) x to y
                else null
            }
        }.map { (x, y) -> input.basin(mutableSetOf(), x, y).size }
            .sortedDescending()
            .take(3)
            .fold(1) { acc, basinSize -> acc * basinSize }
    }

    val input = readInput("day9")
    println(part1(input))
    println(part2(input))
}
