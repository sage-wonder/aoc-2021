import java.lang.Integer.max

typealias TransparentPaper = List<List<Boolean>>
typealias FoldingInstruction = Pair<String, Int>

fun main() {

    fun List<String>.convertInput(): Pair<TransparentPaper, List<FoldingInstruction>> {
        val (p1, p2) = this
            .filterNot { it.isEmpty() }
            .partition { !it.contains("fold") }

        val dots1 = p1.map { it.split(",").let { it[0].toInt() to it[1].toInt() } }
        val maxX = dots1.maxOf { it.first }
        val maxY = dots1.maxOf { it.second }
        val paper = List(maxY + 1) { MutableList(maxX + 1) { false } }
        dots1.forEach { (x, y) -> paper[y][x] = true }

        val instructions = p2.map {
            it.removePrefix("fold along ")
                .split("=")
                .let { it[0] to it[1].toInt() }
        }
        return paper to instructions
    }

    fun List<List<Boolean>>.foldX(x: Int): List<List<Boolean>> {
        return this.map { list ->
            val a = list.subList(0, x).reversed()
            val b = list.subList(x + 1, list.size)
            List(max(a.size, b.size)) {
                a.getOrElse(it) { false } || b.getOrElse(it) { false }
            }
        }
    }

    fun List<List<Boolean>>.foldY(y: Int): List<List<Boolean>> {
        val a = this.subList(0, y).reversed()
        val b = this.subList(y + 1, this.size)
        return List(max(a.size, b.size)) {
            val l1 = a.getOrElse(it) { List(this[0].size) { false } }
            val l2 = b.getOrElse(it) { List(this[0].size) { false } }
            l1.zip(l2).map { it.first || it.second }
        }.reversed()
    }

    fun part1(input: List<String>): Int {
        val (paper, instructions) = input.convertInput()
        val firstFold = if (instructions[0].first == "x")
            paper.foldX(instructions[0].second)
        else paper.foldY(instructions[0].second)
        return firstFold.sumOf { it.count { it } }
    }

    fun part2(input: List<String>) {
        val (paper, instructions) = input.convertInput()
        instructions.fold(paper) { foldedPaper, instruction ->
            if (instruction.first == "x") foldedPaper.foldX(instruction.second)
            else foldedPaper.foldY(instruction.second)
        }.onEach { it.map { if (it) "#" else "." }.also { println(it) } }
    }

    val input = readInput("day13")
    println(part1(input))
    part2(input)
}
