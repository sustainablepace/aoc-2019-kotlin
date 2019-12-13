package advent_of_code

import advent_of_code.domain.Program
import advent_of_code.domain.QueuedIo
import advent_of_code.domain.load
import java.io.IOException
import kotlin.math.sign

fun main(args: Array<String>) {
    println(Day13.partOne())
    println(Day13.partTwo())
}

object Day13 {
    fun partOne(): Int = Breakout.getNumberOfBlocks()
    fun partTwo(): Long = Breakout.insertQuarter().play()
}

object Breakout {
    private val code = javaClass.classLoader
        .getResource("day13_game.txt")!!
        .readText()

    private var program = Program(code.load(), BreakoutIo)
    private var tiles: Tiles = mutableMapOf()
    private var score = 0L

    init {
        program.compute().also { updateScoreAndTiles() }
    }

    fun getNumberOfBlocks(): Int {
        return tiles.filter { it.value is Block }.size
    }

    fun insertQuarter(): Breakout {
        program = Program(code.load().also { it[0] = 2 }, BreakoutIo)
        tiles = mutableMapOf()
        score = 0L
        return this
    }

    private object BreakoutIo : QueuedIo() {
        override fun outputQueue(): List<Long> = outputQueue.toList().also { outputQueue.clear() }
    }

    private fun updateScoreAndTiles() = program.io.outputQueue().chunked(3).map { (x, y, tileId) ->
        tile(x, y, tileId).also { tile ->
            if (tile is Score) {
                score = tileId
            } else {
                tiles[Position(x.toInt(), y.toInt())] = tile
            }
        }
    }

    fun play(withGraphics: Boolean = false): BreakoutScore = program.run {
        if (isTerminated()) {
            println("Insert quarter...")
            return score
        }
        while (!isTerminated()) {
            compute().also { updateScoreAndTiles() }

            if (withGraphics) {
                println(score)
                println(graphics())
            }

            io.queueInput(
                (tiles.find(Ball).x - tiles.find(Paddle).x).sign
            )
        }
        return score
    }

    private fun graphics(): String = tiles.keys.groupBy { it.y }.map { line ->
        val maxX = line.value.map { it.x }.max()!!
        (0..maxX).joinToString("") {
            when (tiles[line.value.getOrNull(it)]) {
                Score -> " "
                Empty -> " "
                Wall -> "|"
                Block -> "#"
                Paddle -> "="
                Ball -> "*"
                null -> " "
            }
        }
    }.toList().joinToString("\n")
}

sealed class Tile
object Score : Tile()
object Empty : Tile()
object Wall : Tile()
object Block : Tile()
object Paddle : Tile()
object Ball : Tile()

fun tile(x: Long, y: Long, tileId: Long): Tile = if (x == -1L) {
    Score
} else when (tileId) {
    0L -> Empty
    1L -> Wall
    2L -> Block
    3L -> Paddle
    4L -> Ball
    else -> throw IOException("Unknown tile")
}

typealias Tiles = MutableMap<Position, Tile>

fun Tiles.find(tile: Tile) = filter { it.value == tile }.keys.first()

typealias BreakoutScore = Long
