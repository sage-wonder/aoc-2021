data class Board(
    val lines: List<MutableList<Int>>
) {
    fun getRow(i: Int) = lines[i]
    fun getCol(i: Int) = lines.map { it[i] }
    fun mark(num: Int) = lines.first { it.contains(num) }.let { it[it.indexOf(num)] = Int.MIN_VALUE }
    fun List<Int>.bingo() = this.count { it == Int.MIN_VALUE } == 5

    fun bingo(num: Int) = Result.runCatching {
        mark(num)
        (0..4).firstNotNullOf {
            val row = getRow(it)
            val col = getCol(it)
            if (row.bingo() || col.bingo()) {
                println("WINNING NUMBER: $num")
                println("WINNING BOARD: ${this@Board}")
                if (row.bingo()) println(row)
                if (col.bingo()) println(col)
                lines.flatMap { it.filter { it != Int.MIN_VALUE } }.sum() * num
            } else null
        }
    }.getOrNull()
}

fun createBoards(data: List<String>): List<Board> {
    val rows = data.subList(1, data.size).filterNot { it.isEmpty() }
    val boardCount = rows.size / 5
    return (0 until boardCount).map {
        val fromIndex = 5 * it
        val endIndex = fromIndex + 5
        Board(rows.subList(fromIndex, endIndex)
            .map {
                it.split(regex = "[ ]+".toRegex())
                    .filterNot { it.isEmpty() }
                    .map { it.toInt() }
                    .toMutableList()
            })
    }.onEach { println(it) }
}

fun main() {
    fun part1(data: List<String>): Int {
        val input = data[0].split(",").map { it.toInt() }
        val boards = createBoards(data)
        var score = 0
        for (num in input) {
            score = boards.mapNotNull { it.bingo(num) }.firstOrNull() ?: 0
            if (score > 0) break
        }
        return score
    }

    fun part2(data: List<String>): Int {
        val input = data[0].split(",").map { it.toInt() }
        val boards = createBoards(data)

        var score = 0
        input.fold(boards) { remainingBoards, num ->
            if (remainingBoards.isNotEmpty()) {
                remainingBoards.filterNot {
                    val winningScore = it.bingo(num)
                    if (winningScore != null) {
                        score = winningScore
                        true
                    } else false
                }
            } else listOf()
        }

        return score
    }

    val input = readInput("day4")
    println(part1(input))
    println(part2(input))
}
