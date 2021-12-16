fun List<Long>.calculate(packetType: Int): Long {
    return when (packetType) {
        0 -> sum()
        1 -> fold(1L) { acc, it -> acc * it }
        2 -> minOrNull()!!
        3 -> maxOrNull()!!
        5 -> if (this[0] > this[1]) 1L else 0L
        6 -> if (this[0] < this[1]) 1L else 0L
        7 -> if (this[0] == this[1]) 1L else 0L
        else -> error("blip")
    }
}

data class PacketStream(
    val data: String
) {
    var currIndex = 0
    fun consume(length: Int) = data.substring(currIndex, currIndex + length).also { currIndex += length }
    fun consumeAsInt(length: Int) = consume(length).toInt(2)
    fun peekType() = data.substring(currIndex + 3, currIndex + 6).toInt(2)
    fun peekOperatorLengthType() = data.substring(currIndex + 6, currIndex + 7).toInt(2)
}

var versionSum = 0

fun PacketStream.parseLiteral(): Long {
    val startIndex = currIndex
    val version = consumeAsInt(3).also { versionSum += it }
    check(consumeAsInt(3) == 4)
    val literalValue = mutableListOf<String>().apply {
        while (true) {
            val block = consume(5)
            this.add(block)
            if (block.startsWith('0')) break
        }
    }.joinToString("") { it.takeLast(4) }.toLong(2)
    val endIndex = currIndex
    println("literal|version=$version|start=$startIndex|end=$endIndex|value=$literalValue")
    return literalValue
}

fun PacketStream.parseFixedLength(): Long {
    val startIndex = currIndex
    val version = consumeAsInt(3).also { versionSum += it }
    val type = consumeAsInt(3)
    check(consumeAsInt(1) == 0)
    val fixedLength = consumeAsInt(15)
    val subPackets = mutableListOf<Long>()
    val subPacketStartIndex = currIndex
    while (currIndex < subPacketStartIndex + fixedLength) {
        when (peekType()) {
            4 -> subPackets.add(parseLiteral())
            else -> when (peekOperatorLengthType()) {
                0 -> subPackets.add(parseFixedLength())
                1 -> subPackets.add(parseVariableLength())
                else -> error("blip")
            }
        }
    }
    val literalValue = subPackets.calculate(type)
    val endIndex = currIndex
    println("operatorType=$type|version=$version|start=$startIndex|end=$endIndex|value=$literalValue")
    return literalValue
}

fun PacketStream.parseVariableLength(): Long {
    val startIndex = currIndex
    val version = consumeAsInt(3).also { versionSum += it }
    val type = consumeAsInt(3)
    check(consumeAsInt(1) == 1)
    var packetCount = consumeAsInt(11)
    val subPackets = mutableListOf<Long>()
    while (--packetCount >= 0) {
        when (peekType()) {
            4 -> subPackets.add(parseLiteral())
            else -> when (peekOperatorLengthType()) {
                0 -> subPackets.add(parseFixedLength())
                1 -> subPackets.add(parseVariableLength())
                else -> error("blip")
            }
        }
    }
    val literalValue = subPackets.calculate(type)
    val endIndex = currIndex
    println("operatorType=$type|version=$version|start=$startIndex|end=$endIndex|value=$literalValue")
    return literalValue
}

fun decode(input: String) {
    val packet = PacketStream(input)
    when (packet.peekOperatorLengthType()) {
        0 -> packet.parseFixedLength()
        else -> packet.parseVariableLength()
    }.also {
        println("Version Sum: $versionSum")
        println("Packet Value: $it")
    }
}

fun main() {
    val input = readInput("day16").first().map { it.hexToBinary() }.joinToString("")
    println(input)
    decode(input)
}