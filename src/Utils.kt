import java.math.BigInteger
import java.security.MessageDigest

typealias LavaFloor = List<List<Int>>
typealias Point = Pair<Int, Int>
typealias SeaFloor = List<MutableList<Int>>
typealias SeaLine = Pair<Point, Point>
typealias Cave = List<List<Int>>

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = ClassLoader.getSystemResourceAsStream("$name.txt")!!.bufferedReader().readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5(): String = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16)

/**
 * Caps a number. Wrap around if exceeds.
 */
fun Int.cap(cap: Int): Int {
    val r = this % cap
    return if (r == 0) cap else r
}

fun Char.hexToBinary() = digitToInt(16).toString(2).padStart(4, '0')