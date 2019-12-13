package advent_of_code

import advent_of_code.Color.BLACK
import advent_of_code.Color.WHITE
import advent_of_code.Orientation.NORTH
import advent_of_code.Turn.LEFT
import advent_of_code.Turn.RIGHT
import advent_of_code.domain.Code
import advent_of_code.domain.Program
import advent_of_code.domain.QueuedIo
import advent_of_code.domain.load

fun main(args: Array<String>) {
    println(Day11.partOne())
    println(Day11.partTwo().humanReadable())
}

object Day11 {
    private val emergencyHullPaintingRobotCode = javaClass.classLoader
        .getResource("day11_painting_robot.txt")!!
        .readText()

    fun partOne(): Int {

        return Robot(emergencyHullPaintingRobotCode).paint(Panel(Position(0,0),BLACK)).distinctBy { it.coordinate }.size
    }

    fun partTwo(): Panels = Robot(emergencyHullPaintingRobotCode).paint(Panel(Position(0,0),WHITE)).filter { it.color == WHITE }
}

class Robot(private val code: Code, private var orientation: Orientation? = NORTH) {

    fun paint(startPanel: Panel): Panels {
        var position = startPanel.coordinate

        val paintedPanels = mutableListOf<Panel>()
        val io = QueuedIo().queueInput(startPanel.color.toLong())
        val program = Program(code.load(), io)
        while (!program.isTerminated()) {
            program.compute()
            val (paint, turn) = io.outputQueue().takeLast(2)
            paintedPanels.add(Panel(position, Color.from(paint)))
            orientation = orientation!!.turn(Turn.from(turn))
            position += orientation!!.getVector()
            io.queueInput(paintedPanels.color(position).toLong())
        }
        return paintedPanels.toList()
    }
}

typealias Position = Coordinate

enum class Orientation {
    NORTH, EAST, SOUTH, WEST;

    fun getVector(): Vector {
        return when (this) {
            NORTH -> Vector(0, -1)
            EAST -> Vector(1, 0)
            SOUTH -> Vector(0, 1)
            WEST -> Vector(-1, 0)
        }
    }

    fun turn(turn: Turn): Orientation {
        return when (this) {
            NORTH -> when (turn) {
                LEFT -> WEST
                RIGHT -> EAST
            }
            EAST -> when (turn) {
                LEFT -> NORTH
                RIGHT -> SOUTH
            }
            SOUTH -> when (turn) {
                LEFT -> EAST
                RIGHT -> WEST
            }
            WEST -> when (turn) {
                LEFT -> SOUTH
                RIGHT -> NORTH
            }
        }
    }
}

enum class Color {
    BLACK, WHITE;

    fun toLong() = this.ordinal.toLong()

    companion object {
        fun from(value: Long) = if (value == WHITE.toLong()) WHITE else BLACK
    }
}

enum class Turn {
    LEFT, RIGHT;

    fun toLong() = this.ordinal.toLong()

    companion object {
        fun from(value: Long) = if (value == LEFT.toLong()) LEFT else RIGHT
    }
}

data class Panel(val coordinate: Coordinate, val color: Color)

typealias Panels = List<Panel>

fun Panels.color(position: Position): Color {
    return lastOrNull { it.coordinate == position }?.run { color } ?: BLACK
}

fun Panels.humanReadable(): String {
    val groups = groupBy { it.coordinate.y }.map { mapEntry -> mapEntry.value.map { it.coordinate.x } }
    val lines = groups.map { line ->
        (0..line.max()!!).joinToString("") { if (line.contains(it)) "#" else " " }
    }
    return lines.toList().joinToString("\n")
}

