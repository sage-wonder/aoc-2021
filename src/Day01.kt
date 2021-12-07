fun main() {
    fun part1(input: List<String>): Int {
        return input.map { it.toInt() }
            .zipWithNext { a, b -> if (b > a) 1 else 0 }
            .sum()
    }

    fun part2(input: List<String>): Int {
        return input.map { it.toInt() }
            .let { depths ->
                (2 until depths.size)
                    .map { depths[it - 2] + depths[it - 1] + depths[it] }
                    .zipWithNext { a, b -> if (b > a) 1 else 0 }
                    .sum()
            }
    }

    val input = readInput("day1")
    println(part1(input))
    println(part2(input))
}
