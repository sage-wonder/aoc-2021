import java.util.LinkedList

fun main() {

    fun Char.isMatching(c: Char): Boolean {
        return when {
            this == '[' && c == ']' ||
                    this == '{' && c == '}' ||
                    this == '(' && c == ')' ||
                    this == '<' && c == '>' -> true
            else -> false
        }
    }

    fun Char.toScore(): Long {
        return when (this) {
            ')' -> 3L
            ']' -> 57L
            '}' -> 1197L
            '>' -> 25137L
            else -> 0L
        }
    }

    fun List<Char>.toScore(): Long {
        return this.reversed().fold(0L) { score, c ->
            score * 5 + when (c) {
                '(' -> 1L
                '[' -> 2L
                '{' -> 3L
                '<' -> 4L
                else -> 0L
            }
        }
    }

    fun String.getIllegalChar(): Char? {
        val queue = LinkedList<Char>()
        this.forEach {
            try {
                when (it) {
                    '[', '{', '<', '(' -> queue.add(it)
                    else -> if (queue.peekLast().isMatching(it)) queue.pollLast() else error("blip")
                }
            } catch (ex: Exception) {
                return it
            }
        }
        return null
    }

    fun String.remaining(): List<Char> {
        val queue = LinkedList<Char>()
        this.forEach {
            when (it) {
                '[', '{', '<', '(' -> queue.add(it)
                else -> queue.pollLast()
            }
        }
        return queue
    }

    fun part1(input: List<String>): Long {
        return input.mapNotNull { it.getIllegalChar() }.sumOf { it.toScore() }
    }

    fun part2(input: List<String>): Long {
        val scores = input.filter { it.getIllegalChar() == null }
            .map { it.remaining().toScore() }
            .sorted()
        return scores[scores.size / 2]
    }

    val input = readInput("day10")
    println(part1(input))
    println(part2(input))
}
