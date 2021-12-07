fun main() {
    fun part1(input: List<String>): Long {
        val aggData = input.fold(List(input[0].length) { mutableListOf<Char>() }) { acc, row ->
            row.forEachIndexed { index, c -> acc[index].add(c) }
            acc
        }
        val gamma = aggData.joinToString("") { if (it.count { it == '1' } > it.size / 2) "1" else "0" }
        val epsilon = gamma.toList().joinToString("") { if (it == '1') "0" else "1" }
        return gamma.toLong(2) * epsilon.toLong(2)
    }

    fun part2(input: List<String>): Long {
        fun List<String>.getOxygen(pos: Int = 0): String {
            if (this.size == 1) return this[0]
            if (this.size == 2 && this[0][pos] != this[1][pos]) return this.first { it[pos] == '1' }
            val dominant = this.groupBy { it[pos] }.mapValues { (_, value) -> value.size }.let {
                if (it['1']!! >= it['0']!!) '1' else '0'
            }
            return this.filter { it[pos] == dominant }.getOxygen(pos + 1)
        }

        fun List<String>.getCo2(pos: Int = 0): String {
            if (this.size == 1) return this[0]
            if (this.size == 2 && this[0][pos] != this[1][pos]) return this.first { it[pos] == '0' }
            val minor = this.groupBy { it[pos] }.mapValues { (_, value) -> value.size }.let {
                if (it['1']!! >= it['0']!!) '0' else '1'
            }
            return this.filter { it[pos] == minor }.getCo2(pos + 1)
        }

        return input.getOxygen().toLong(2) * input.getCo2().toLong(2)
    }

    val input = readInput("day3")
    println(part1(input))
    println(part2(input))
}
