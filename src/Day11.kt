typealias LightField = List<MutableList<Int>>

fun main() {
    fun LightField.increment(x: Int, y: Int) {
        getOrNull(x)?.getOrNull(y)?.let {
            if (this[x][y] != Int.MAX_VALUE) {
                this[x][y] += 1
                if (this[x][y] >= 10) {
                    this[x][y] = Int.MAX_VALUE
                    increment(x - 1, y - 1)
                    increment(x - 1, y)
                    increment(x - 1, y + 1)
                    increment(x, y - 1)
                    increment(x, y + 1)
                    increment(x + 1, y - 1)
                    increment(x + 1, y)
                    increment(x + 1, y + 1)
                }
            }
        }
    }

    fun LightField.step(): Int {
        this.indices.map { x ->
            this[0].indices.map { y ->
                increment(x, y)
            }
        }

        this.indices.map { x ->
            this[0].indices.map { y ->
                if (this[x][y] == Int.MAX_VALUE) this[x][y] = 0
            }
        }

        return this.sumOf { it.count { it == 0 } }
    }

    fun part1(input: LightField): Int {
        return (0 until 100).fold(0) { acc, _ ->
            acc + input.step()
        }
    }

    fun part2(input: LightField): Int {
        var stepCount = 0
        while (true) {
            input.step()
            stepCount++
            if (input.sumOf { it.count { it == 0 } } == 100) break
        }
        return stepCount
    }

    println(part1(readInput("day11").map { it.map { it.digitToInt() }.toMutableList() }))
    println(part2(readInput("day11").map { it.map { it.digitToInt() }.toMutableList() }))
}
