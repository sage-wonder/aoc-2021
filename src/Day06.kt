fun main() {
    fun part1(input: String, days: Int): Long {
        var fish = input.split(",")
            .map { it.toInt() }
            .groupBy { it }
            .mapValues { it.value.size.toLong() }
            .toMutableMap()

        repeat(days) {
            val newFish = fish[0] ?: 0L
            fish = fish.filterKeys { it != 0 }
                .mapKeys { it.key - 1 }
                .toMutableMap()
            fish[6] = (fish[6] ?: 0L) + newFish
            fish[8] = newFish
//            println(fish)
        }
        return fish.values.sum()
    }


    val input = readInput("day6")[0]
    println(part1(input, 80))
    println(part1(input, 256))
}
