package advent_of_code

import advent_of_code.CompassDirection.*
import advent_of_code.DroidResponse.*
import advent_of_code.domain.Code
import advent_of_code.domain.Program
import advent_of_code.domain.QueuedIo
import advent_of_code.domain.load
import java.io.IOException
import kotlin.random.Random

fun main(args: Array<String>) {
    println(Day15.partOne())
    println(Day15.partTwo())
}

object Day15 {
    private val code = javaClass.classLoader
        .getResource("day15_program.txt")!!
        .readText()

    fun partOne(): Int {
        val explorationResult = findOxygen(code)
        return explorationResult.instructions.size
    }

    fun partTwo(): Int {
        val oxygen = findOxygen(code)
        val explorationResult = ExplorationResult( oxygen.position, MOVED, oxygen.instructions)
        val maze = mutableListOf(explorationResult)

        var droids = spawnDroids(code, explorationResult, maze)
        var steps = 0
        while (droids.isNotEmpty()) {
            droids = droids.flatMap { droid ->
                val result = droid.explore()
                maze.add(result)
                when (result.droidResponse) {
                    OXYGEN -> listOf()
                    WALL -> listOf()
                    MOVED -> spawnDroids(code, result, maze)
                }
            }
            steps++
        }
        return steps
    }
}

data class Droid(val code: Code, val explorationResult: ExplorationResult, val direction: CompassDirection) {
    fun explore(): ExplorationResult {
        val program = Program(code.load(), QueuedIo())
        explorationResult.instructions.forEach { program.io.queueInput(it) }
        program.io.queueInput(direction.input)
        program.compute()
        val droidResponse = when (program.io.outputQueue().last()) {
            0L -> WALL
            1L -> MOVED
            2L -> OXYGEN
            else -> throw IOException("Invalid droid response.")
        }
        return ExplorationResult(explorationResult.position + direction, droidResponse, program.io.inputQueue())
    }
}

typealias Maze = MutableList<ExplorationResult>

data class ExplorationResult(val position: Position, val droidResponse: DroidResponse, val instructions: List<Long>)

fun Maze.get(position: Position, compassDirection: CompassDirection): ExplorationResult? = firstOrNull {
    it.position == position + compassDirection
}

enum class DroidResponse {
    WALL, MOVED, OXYGEN
}

val compassDirections = listOf<CompassDirection>(NORTH, SOUTH, WEST, EAST)

enum class CompassDirection(val input: Int) {
    NORTH(1),
    SOUTH(2),
    WEST(3),
    EAST(4);

    companion object {
        private val directions = values().associateBy(CompassDirection::input)
        fun from(input: Int): CompassDirection? = directions[input]

        fun random(): CompassDirection = from(Random.nextInt(1, 4))!!

    }

}
fun findOxygen(code: Code) : ExplorationResult {
    val explorationResult = ExplorationResult( Position(0, 0), MOVED, listOf())
    val maze = mutableListOf(explorationResult)

    var droids = spawnDroids(code, explorationResult, maze)

    while (droids.isNotEmpty()) {
        droids = droids.flatMap { droid ->
            val result = droid.explore()
            maze.add(result)
            when (result.droidResponse) {
                OXYGEN -> {
                    return result
                }
                WALL -> listOf()
                MOVED -> spawnDroids(code, result, maze)
            }
        }
    }
    throw IOException("Droid lost in maze.")

}

private fun spawnDroids(code: Code, result: ExplorationResult, maze: Maze): List<Droid> {
    val options = options(result.position, maze)
    return options.map { direction ->
        Droid(code, result, direction)
    }
}


private fun options(currentPosition: Position, maze: Maze): List<CompassDirection> {
    return compassDirections.map { direction ->
        if (maze.get(currentPosition, direction) == null) direction else null
    }.filterNotNull()
}

infix operator fun Position.plus(compassDirection: CompassDirection): Position = when (compassDirection) {
    NORTH -> this + Vector(0, -1)
    SOUTH -> this + Vector(0, 1)
    WEST -> this + Vector(-1, 0)
    EAST -> this + Vector(1, 0)
}