package advent_of_code

import advent_of_code.CameraOutput.Companion.from
import advent_of_code.CameraOutput.NEW_LINE
import advent_of_code.CameraOutput.SCAFFOLD
import advent_of_code.VacuumRobotCommand.*
import advent_of_code.domain.Program
import advent_of_code.domain.QueuedIo
import advent_of_code.domain.load

fun main(args: Array<String>) {
    println(Day17.partOne())
    println(Day17.partTwo())
}

object Day17 {
    private val code = javaClass.classLoader
        .getResource("day17_program.txt")!!
        .readText()

    fun partOne(): Int {
        val camera = Program(code.load(), QueuedIo()).compute()
        val image: CameraImage = camera.io.outputQueue()
            .map { ascii -> from(ascii.toChar())!! }
            .run {
                chunked(indexOf(NEW_LINE) + 1)
            }

        image.display()

        return image.alignmentParameterSum()
    }

    fun partTwo(): Long {
        val memory = code.load()
        memory[0] = 2L

        val commands = listOf(
            CALL_MOVEMENT_ROUTINE_A,            SEPARATOR,
            CALL_MOVEMENT_ROUTINE_A,            SEPARATOR,
            CALL_MOVEMENT_ROUTINE_B,            SEPARATOR,
            CALL_MOVEMENT_ROUTINE_C,            SEPARATOR,
            CALL_MOVEMENT_ROUTINE_B,            SEPARATOR,
            CALL_MOVEMENT_ROUTINE_C,            SEPARATOR,
            CALL_MOVEMENT_ROUTINE_B,            SEPARATOR,
            CALL_MOVEMENT_ROUTINE_C,            SEPARATOR,
            CALL_MOVEMENT_ROUTINE_B,            SEPARATOR,
            CALL_MOVEMENT_ROUTINE_A,            LINE_FEED,

            TURN_LEFT,                          SEPARATOR,
            ONE, ZERO,                          SEPARATOR,
            TURN_LEFT,                          SEPARATOR,
            EIGHT,                              SEPARATOR,
            TURN_RIGHT,                         SEPARATOR,
            EIGHT,                              SEPARATOR,
            TURN_LEFT,                          SEPARATOR,
            EIGHT,                              SEPARATOR,
            TURN_RIGHT,                         SEPARATOR,
            SIX,                                LINE_FEED,

            TURN_RIGHT,                         SEPARATOR,
            SIX,                                SEPARATOR,
            TURN_RIGHT,                         SEPARATOR,
            EIGHT,                              SEPARATOR,
            TURN_RIGHT,                         SEPARATOR,
            EIGHT,                              LINE_FEED,

            TURN_RIGHT,                         SEPARATOR,
            SIX,                                SEPARATOR,
            TURN_RIGHT,                         SEPARATOR,
            SIX,                                SEPARATOR,
            TURN_LEFT,                          SEPARATOR,
            EIGHT,                              SEPARATOR,
            TURN_LEFT,                          SEPARATOR,
            ONE, ZERO,                          LINE_FEED,

            WITHOUT_LIVE_FEED,                  LINE_FEED
        )

        val vacuumRobot = Program(memory, QueuedIo())

        commands.forEach {
            vacuumRobot.io.queueInput(it.character.toInt())
        }

        vacuumRobot.compute()

        return vacuumRobot.io.outputQueue().last()
    }
}

typealias CameraImage = List<CameraLine>

fun CameraImage.alignmentParameterSum(): Int = mapIndexed { y, line ->
    if (y == 0 || y == size - 1) {
        0 // skip first and last line
    } else {
        var x = 0
        line.windowed(3, 1) { window ->
            x++ // center of window
            if (window.all { it == SCAFFOLD } && this[y - 1][x] == SCAFFOLD && this[y + 1][x] == SCAFFOLD) {
                x * y
            } else 0
        }.sum()
    }
}.sum()

fun CameraImage.display() {
    val graphics = flatMap { lines ->
        lines.map { it.character }
    }.joinToString("").split("\n")
    graphics.forEach { println(it) }

}
typealias CameraLine = List<CameraOutput>

enum class CameraOutput(val character: Char) {
    SCAFFOLD('#'),
    OPEN_SPACE('.'),
    NEW_LINE(10.toChar()),
    ROBOT_LOST('X'),
    ROBOT_N('^'),
    ROBOT_E('>'),
    ROBOT_S('v'),
    ROBOT_W('<');

    companion object {
        private val chars = values().associateBy(CameraOutput::character)
        fun from(character: Char): CameraOutput? = chars[character]
    }
}

enum class VacuumRobotCommand(val character: Char) {
    CALL_MOVEMENT_ROUTINE_A('A'),
    CALL_MOVEMENT_ROUTINE_B('B'),
    CALL_MOVEMENT_ROUTINE_C('C'),
    ZERO('0'),
    ONE('1'),
    SIX('6'),
    EIGHT('8'),
    SEPARATOR(','),
    LINE_FEED('\n'),
    TURN_LEFT('L'),
    TURN_RIGHT('R'),
    WITH_LIVE_FEED('y'),
    WITHOUT_LIVE_FEED('n');

    companion object {
        private val chars = values().associateBy(VacuumRobotCommand::character)
        fun from(character: Char): VacuumRobotCommand? = chars[character]
    }
}
