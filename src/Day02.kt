fun main() {
    fun part1(input: List<String>): Int {
        return input.map { it.split(" ").let { it[0] to it[1].toInt() } }
            .fold(0 to 0) { (h, d), (direction, movement) ->
                when (direction) {
                    "forward" -> h + movement to d
                    "down" -> h to d + movement
                    "up" -> h to d - movement
                    else -> error("blip")
                }
            }.let { it.first * it.second }
    }

    fun part2(input: List<String>): Int {
        return input.map { it.split(" ").let { it[0] to it[1].toInt() } }
            .fold(Triple(0, 0, 0)) { acc, (direction, movement) ->
                val h = acc.first
                val d = acc.second
                val aim = acc.third
                when (direction) {
                    "forward" -> Triple(h + movement, d + (movement * aim), aim)
                    "down" -> Triple(h, d, aim + movement)
                    "up" -> Triple(h, d, aim - movement)
                    else -> error("blip")
                }
            }.let { it.first * it.second }
    }

    val input = readInput("day2")
    println(part1(input))
    println(part2(input))
}
