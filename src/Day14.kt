fun main() {
    fun part1(input: List<String>): Int {
        val start = input[0]
        val rules = (2 until input.size).associate {
            input[it].split(" -> ").let { it[0] to it[1] }
        }

        val result = (0 until 10).fold(start) { acc, _ ->
            acc.zipWithNext { a, b ->
                rules["$a$b"]?.let { "$a$it" } ?: "$a"
            }.joinToString("") + acc.last()
        }

        val pCount = result.groupBy { it }.mapValues { it.value.size }
        val maxCount = pCount.maxByOrNull { it.value }!!.value
        val minCount = pCount.minByOrNull { it.value }!!.value
        return maxCount - minCount
    }

    fun part2(input: List<String>): Long {
        val start = input[0]
        val rules = (2 until input.size).associate {
            input[it].split(" -> ").let { it[0] to it[1] }
        }

        val stems = start.zipWithNext().map { "${it.first}${it.second}" }
            .groupBy { it }
            .mapValues { it.value.size.toLong() }

        fun step(stem: String) = rules[stem]?.let { listOf("${stem[0]}$it", "$it${stem[1]}") } ?: listOf(stem)

        val result = (0 until 40).fold(stems) { acc, _ ->
            acc.flatMap { (stem, count) -> step(stem).map { it to count } }
                .groupBy { it.first }
                .mapValues { it.value.sumOf { it.second } }
        }

        val pCount = mutableMapOf<Char, Long>()
        result.forEach { (p, count) -> p.forEach { pCount[it] = count + (pCount[it] ?: 0L) } }
        val firstChar = start.first()
        val lastChar = start.last()
        pCount[firstChar] = pCount[firstChar]!! + 1L
        pCount[lastChar] = pCount[lastChar]!! + 1L

        val maxCount = (pCount.maxByOrNull { it.value }!!.value) / 2
        val minCount = (pCount.minByOrNull { it.value }!!.value) / 2
        return maxCount - minCount
    }

    val input = readInput("day14")
    println(part1(input))
    println(part2(input))
}
