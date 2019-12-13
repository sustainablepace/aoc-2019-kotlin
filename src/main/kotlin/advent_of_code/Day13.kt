package advent_of_code

import advent_of_code.domain.Program
import advent_of_code.domain.QueuedIo
import advent_of_code.domain.load
import java.io.IOException
import kotlin.math.sign

suspend fun main(args: Array<String>) {
    println(Day13.partOne())
    println(Day13.partTwo())
}

object Day13 {
    private val gameCode = javaClass.classLoader
        .getResource("day13_game.txt")!!
        .readText()

    fun partOne(): Int =
        Program(gameCode.load(), QueuedIo()).compute().io.outputQueue().chunked(3).count { it[2] == 2L }

    fun partTwo(): Long {
        val memory = gameCode.load()
        memory[0] = 2 // Insert quarter

        val program = Program(memory, QueuedIo())

        var outputCounter = 0

        val breakout = Game(0L, mutableMapOf())

        while (!program.isTerminated()) {
            program.compute()
            val size = program.io.outputQueue().size
            program.io.outputQueue().subList(outputCounter, size).chunked(3).map { (x, y, tileId) ->
                val tile = tile(x, y, tileId)
                if (tile is SCORE) {
                    breakout.score = tileId
                } else {
                    breakout.tiles[Position(x.toInt(), y.toInt())] = tile
                }
            }
            val ball = breakout.tiles.filter { it.value is BALL }.keys.first()
            val paddle = breakout.tiles.filter { it.value is PADDLE }.keys.first()

            program.io.queueInput((ball.x - paddle.x).sign.toLong())
            outputCounter = size
        }
        return breakout.score
    }
}

data class Game(var score: Score, var tiles: MutableMap<Position, Tile>) {
    fun paint(): String = tiles.keys.groupBy { it.y }.map { line ->
        val maxX = line.value.map { it.x }.max()!!
        (0..maxX).joinToString("") { tiles[line.value.getOrNull(it)]?.character.toString() }
    }.toList().joinToString("\n")
}


typealias Score = Long

fun tile(x: Long, y: Long, tileId: Long): Tile = if (x == -1L) {
    SCORE
} else when (tileId) {
    0L -> EMPTY
    1L -> WALL
    2L -> BLOCK
    3L -> PADDLE
    4L -> BALL
    else -> throw IOException("Unknown tile")
}

sealed class Tile {
    abstract val character: Char
}

object SCORE : Tile() {
    override val character: Char = ' '
}

object EMPTY : Tile() {
    override val character: Char = ' '
}

object WALL : Tile() {
    override val character: Char = '|'
}

object BLOCK : Tile() {
    override val character: Char = '#'
}

object PADDLE : Tile() {
    override val character: Char = '='
}

object BALL : Tile() {
    override val character: Char = '*'
}