fun main() {
    fun String.is1478(): Boolean {
        return when (this.length) {
            2, 3, 4, 7 -> true
            else -> false
        }
    }

    fun String.getDigit(): Int? {
        return when (this.length) {
            2 -> 1
            3 -> 7
            4 -> 4
            7 -> 8
            else -> null
        }
    }

    fun String.sortChars() = this.toSortedSet().joinToString("")

    fun Set<Char>.joinToString() = this.joinToString("")

    fun decipher(patterns: List<String>, output: List<String>): Int {
        val pattern1 = patterns.first { it.getDigit() == 1 }.toSet()
        val pattern3 = patterns.filter { it.length == 5 }.map { it.toSet() }.first { it.containsAll(pattern1) }
        val pattern4 = patterns.first { it.getDigit() == 4 }.toSet()
        val pattern6 = patterns.filter { it.length == 6 }.map { it.toSet() }.first { !it.containsAll(pattern1) }
        val pattern7 = patterns.first { it.getDigit() == 7 }.toSet()
        val pattern8 = patterns.first { it.getDigit() == 8 }.toSet()
        val pattern5 = patterns.filter { it.length == 5 && !pattern3.containsAll(it.toSet()) }
            .first { it.count { !pattern6.contains(it) } == 0 }.toSet()
        val pattern2 = patterns.filter { it.length == 5 }
            .first { !pattern3.containsAll(it.toSet()) && !pattern5.containsAll(it.toSet()) }
            .toSet()
        val e = pattern6.first { !pattern5.contains(it) }
        val pattern9 = pattern8.filter { it != e }.toSet()
        val pattern0 = patterns.filter { it.length == 6 }
            .first { !pattern6.containsAll(it.toSet()) && !pattern9.containsAll(it.toSet()) }
            .toSet()

        val val0 = pattern0.joinToString()
        val val1 = pattern1.joinToString()
        val val2 = pattern2.joinToString()
        val val3 = pattern3.joinToString()
        val val4 = pattern4.joinToString()
        val val5 = pattern5.joinToString()
        val val6 = pattern6.joinToString()
        val val7 = pattern7.joinToString()
        val val8 = pattern8.joinToString()
        val val9 = pattern9.joinToString()

        return output.joinToString("") {
            when (it) {
                val0 -> "0"
                val1 -> "1"
                val2 -> "2"
                val3 -> "3"
                val4 -> "4"
                val5 -> "5"
                val6 -> "6"
                val7 -> "7"
                val8 -> "8"
                val9 -> "9"
                else -> error("blip")
            }
        }.toInt()

    }

    fun parseLine(line: String): Pair<List<String>, List<String>> {
        return line.split("|").map { it.trim() }.let {
            val patterns = it[0].split(" ").map { it.sortChars() }
            val output = it[1].split(" ").map { it.sortChars() }
            patterns to output
        }
    }

    fun part1(input: List<String>): Int {
        return input.sumOf { parseLine(it).second.count { it.is1478() } }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf { parseLine(it).let { decipher(it.first, it.second) } }
    }

    val input = readInput("day8")
    println(part1(input))
    println(part2(input))
}
