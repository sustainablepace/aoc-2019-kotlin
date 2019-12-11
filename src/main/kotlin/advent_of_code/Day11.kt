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

        return Robot(emergencyHullPaintingRobotCode).compute(BLACK).distinctBy { it.coordinate }.size
    }

    fun partTwo(): Panels = Robot(emergencyHullPaintingRobotCode).compute(WHITE).filter { it.color == WHITE }
}

class Robot(code: Code) {
    private var position = Position(0, 0)
    private var orientation = NORTH
    private var panels = mutableListOf<Panel>()

    private val io = QueuedIo()

    private val program = Program(code.load(), io)

    fun compute(firstInput: Color): Panels {
        io.queueInput(firstInput.ordinal.toLong())
        while (!program.isTerminated()) {
            program.compute()
            if (io.outputQueue().size < 2) {
                break
            }
            val outputSize = io.outputQueue().size
            val paint = Color.from(io.outputQueue()[outputSize - 2])
            panels.add(Panel(position, paint))
            val turn = Turn.from(io.outputQueue()[outputSize - 1])
            orientation = orientation.turn(turn)
            position += orientation.getVector()
            io.queueInput(panels.color(position).ordinal.toLong())
        }
        return panels
    }
}

typealias Position = Coordinate

enum class Orientation {
    NORTH, EAST, SOUTH, WEST;

    fun getVector(): Vector {
        return when (this) {
            NORTH -> Vector(0, 1)
            EAST -> Vector(1, 0)
            SOUTH -> Vector(0, -1)
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

    companion object {
        fun from(value: Long) = if (value == WHITE.ordinal.toLong()) WHITE else BLACK
    }
}

enum class Turn {
    LEFT, RIGHT;

    companion object {
        fun from(value: Long) = if (value == LEFT.ordinal.toLong()) LEFT else RIGHT
    }
}

data class Panel(val coordinate: Coordinate, val color: Color)

typealias Panels = List<Panel>

fun Panels.color(position: Position): Color {
    return lastOrNull { it.coordinate == position }?.run { color } ?: BLACK
}

fun Panels.humanReadable(): String {
    val groups = groupBy { it.coordinate.y }.map { it.value.map { it.coordinate.x } }
    val lines = groups.map { line ->
        (0..line.max()!!).map { if (line.contains(it)) "#" else " " }.joinToString("")
    }
    return lines.toList().joinToString("\n")
}

