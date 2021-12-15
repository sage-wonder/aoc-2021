fun main() {
    fun LavaFloor.isLowPoint(p: Point): Boolean {
        val (x, y) = p
        val curr = this[x][y]
        return (x == 0 || this[x - 1][y] > curr) &&
                (x == this.size - 1 || this[x + 1][y] > curr) &&
                (y == 0 || this[x][y - 1] > curr) &&
                (y == this[0].size - 1 || this[x][y + 1] > curr)
    }

    fun LavaFloor.basin(basinArea: MutableSet<Point>, p: Point): Set<Point> {
        val (x, y) = p
        if (x < 0 || x >= this.size || y < 0 || y >= this[0].size || this[x][y] == 9) return emptySet()
        basinArea.add(p)
        if (!basinArea.contains(x - 1 to y))
            basinArea.addAll(this.basin(basinArea, x - 1 to y))
        if (!basinArea.contains(x + 1 to y))
            basinArea.addAll(this.basin(basinArea, x + 1 to y))
        if (!basinArea.contains(x to y - 1))
            basinArea.addAll(this.basin(basinArea, x to y - 1))
        if (!basinArea.contains(x to y + 1))
            basinArea.addAll(this.basin(basinArea, x to y + 1))
        return basinArea
    }

    fun part1(input: LavaFloor): Int {
        return (input.indices)
            .flatMap { x -> (input[0].indices).mapNotNull { y -> if (input.isLowPoint(x to y)) input[x][y] else null } }
            .sumOf { it + 1 }
    }

    fun part2(input: LavaFloor): Int {
        return (input.indices)
            .flatMap { x -> (input[0].indices).mapNotNull { y -> if (input.isLowPoint(x to y)) x to y else null } }
            .map { input.basin(mutableSetOf(), it).size }
            .sortedDescending()
            .take(3)
            .fold(1) { acc, basinSize -> acc * basinSize }
    }

    val input = readInput("day9").map { it.map { it.digitToInt() } }
    println(part1(input))
    println(part2(input))
}
