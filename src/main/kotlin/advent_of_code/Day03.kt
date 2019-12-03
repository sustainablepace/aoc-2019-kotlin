package advent_of_code

import kotlin.math.abs

object Day03 {
    val input: String = javaClass.classLoader
        .getResource("day03_cables.txt")!!
        .readText()

    val cables = input.lines().map { cable(it) }

    fun partOne(): Int = cables[0].distanceToClosestIntersectionToPort(cables[1])

    fun partTwo(): Int = cables[0].stepsToClosestIntersection(cables[1])

}

enum class Direction(val input: Char) {
    RIGHT('R'),
    DOWN('D'),
    LEFT('L'),
    UP('U');

    companion object {
        private val opcodes = Direction.values().associateBy(Direction::input)
        fun from(value: Char): Direction? = opcodes.get(value)
    }
}

data class CableSegment(val direction: Direction, val distance: Int) {

}
typealias Cable = List<Pair<Int, Int>>

fun cable(input: String): Cable = input.split(",").map { segment ->
    val direction = Direction.from(segment[0])!!
    val distance = segment.substring(1).toInt()
    CableSegment(direction, distance)
}.run {
    cable(this)
}


fun cable(cablesSegments: List<CableSegment>): Cable {
    val cable = mutableListOf(0 to 0) // port
    var pos = cable[0]
    cablesSegments.forEach { segment ->
        val dir = when (segment.direction) {
            Direction.RIGHT -> (1 to 0)
            Direction.DOWN -> (0 to -1)
            Direction.LEFT -> (-1 to 0)
            Direction.UP -> (0 to 1)
        }
        repeat(segment.distance) {
            pos = (pos.first + dir.first to pos.second + dir.second)
            val next = pos
            cable.add(next)
        }
    }
    return cable.toList()
}

fun Cable.distanceToPort(point: Pair<Int, Int>): Int =
    abs(point.first - this[0].first) + abs(point.second - this[0].second)

fun Cable.distanceToClosestIntersectionToPort(cable: Cable): Int = distanceToPort(closestIntersectionToPort(cable))

fun Cable.closestIntersectionToPort(cable: Cable): Pair<Int, Int> =
    intersections(cable).reduce { acc, pair ->
        if (this@closestIntersectionToPort.distanceToPort(pair) < this@closestIntersectionToPort.distanceToPort(acc)) {
            pair
        } else acc
    }

fun Cable.intersections(cable: Cable): List<Pair<Int, Int>> = toSet().intersect(cable.toSet()).minus(this[0]).toList()

fun List<Cable>.stepsToIntersection(intersection: Pair<Int, Int>): Int = sumBy { it.indexOf(intersection) }

fun Cable.stepsToClosestIntersection2(cable: Cable): Int = intersections(cable).reduce { acc, intersection ->
    if (listOf(this, cable).stepsToIntersection(intersection) < listOf(this, cable).stepsToIntersection(intersection)) intersection else acc
}.run { listOf(this@stepsToClosestIntersection2, cable).stepsToIntersection(this) }

fun Cable.stepsToClosestIntersection(cable: Cable): Int = intersections(cable).map { listOf(this, cable).stepsToIntersection(it)}.min() ?: 0

