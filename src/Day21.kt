import kotlin.math.max

var die = 0
var rollCount = 0

fun rollDie(): Int {
    rollCount++
    die++
    return die.cap(100)
}

fun day21part1() {
    var p1Pos = 5
    var p2Pos = 9
    var p1Score = 0
    var p2Score = 0
    while (true) {
        p1Pos = (p1Pos + rollDie() + rollDie() + rollDie()).cap(10)
        p1Score += p1Pos
        if (p1Score >= 1000) break
        p2Pos = (p2Pos + rollDie() + rollDie() + rollDie()).cap(10)
        p2Score += p2Pos
        if (p2Score >= 1000) break
    }

    println(
        if (p1Score > p2Score) p2Score * rollCount
        else p1Score * rollCount
    )
}

fun Pair<Int, Int>.reverse() = second to first

data class GameState(
    val positions: Pair<Int, Int>,
    val scores: Pair<Int, Int> = 0 to 0,
)

data class Score(
    var p1: Long,
    var p2: Long
)

val stateMap = mutableMapOf<GameState, Score>()

fun play(state: GameState): Score {
    return stateMap[state] ?: Score(0, 0).apply {
        (1..3).forEach { x ->
            (1..3).forEach { y ->
                (1..3).forEach { z ->
                    println("state=$state")
                    val newPos = (state.positions.first + x + y + z).cap(10)
                    val newScore = state.scores.first + newPos
                    println("newPos=$newPos|newScore=$newScore")
                    if (newScore >= 21) {
                        p1++
                    } else {
                        val positions = state.positions.second to newPos
                        val scores = state.scores.second to newScore
                        play(GameState(positions, scores)).let {
                            p1 += it.p2
                            p2 += it.p1
                        }
                    }
                }
            }
        }
    }.also { stateMap[state] = it }
}

fun day21part2() {
    val state = GameState(5 to 9)
    play(state).run { println("Winning score: ${max(p1, p2)}") }
}

fun main() {
    day21part1()
    day21part2()
}