var defaultIsLit = false

// THIS WAS A TRICK QUESTION
fun List<String>.getPixel(x: Int, y: Int) = this.getOrNull(y)?.getOrNull(x) ?: (if (defaultIsLit) '#' else '.')

fun List<String>.getBit(x: Int, y: Int) = getPixel(x, y).let {
    when (it) {
        '.' -> '0'
        else -> '1'
    }
}

fun List<String>.enhanceImage(algo: String, x: Int, y: Int): Char {
    val index = listOf(
        getBit(x - 1, y - 1),
        getBit(x, y - 1),
        getBit(x + 1, y - 1),
        getBit(x - 1, y),
        getBit(x, y),
        getBit(x + 1, y),
        getBit(x - 1, y + 1),
        getBit(x, y + 1),
        getBit(x + 1, y + 1),
    ).joinToString("").toInt(2)
    return algo[index]
}

fun List<String>.enhanceImage(algorithm: String): List<String> {
    val yMax = this.size
    val xMax = this[0].length
    return (-1 .. yMax).map { y ->
        (-1 .. xMax).map { x -> this.enhanceImage(algorithm, x, y) }.joinToString("")
    }
}

fun part1() {
    val input = readInput("day20")
    val algorithm = input[0]
    val inputImage = input.drop(2)

    val litCount = (0 until 2)
        .fold(inputImage) {acc, _ -> acc.enhanceImage(algorithm).also { defaultIsLit = !defaultIsLit } }
        .onEach { println(it) }
        .sumOf { it.count { it == '#' } }

    println(litCount)
}

fun part2() {
    val input = readInput("day20")
    val algorithm = input[0]
    val inputImage = input.drop(2)

    val litCount = (0 until 50)
        .fold(inputImage) {acc, _ -> acc.enhanceImage(algorithm).also { defaultIsLit = !defaultIsLit } }
        .onEach { println(it) }
        .sumOf { it.count { it == '#' } }

    println(litCount)
}

fun main() {
    part1()
    part2()
}