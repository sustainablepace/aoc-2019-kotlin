package advent_of_code

import kotlin.math.abs

object Day03 {
    val cablePair = javaClass.classLoader
        .getResource("day03_cables.txt")!!
        .readText().lines().map { cable(it) }.zipWithNext().first()

    fun partOne(): Int = cablePair.distanceToClosestIntersection()

    fun partTwo(): Int = cablePair.stepsToClosestIntersection()
}

enum class Direction(val input: Char) {
    RIGHT('R'),
    DOWN('D'),
    LEFT('L'),
    UP('U');

    companion object {
        private val directions = values().associateBy(Direction::input)
        fun from(input: Char): Direction? = directions[input]
    }
}

data class CableSegment(val direction: Direction, val distance: Int) {
    companion object {
        fun create(segment: String): CableSegment = CableSegment(
            Direction.from(segment[0])!!,
            segment.substring(1).toInt()
        )
    }
}


fun cable(line: String): Cable = line.split(",")
    .map { CableSegment.create(it) }
    .run { cable(this) }

fun cable(segments: List<CableSegment>): Cable {
    var point: Port = 0 to 0
    val cable = mutableListOf<Point>(point)
    segments.forEach { segment ->
        val step: Point = when (segment.direction) {
            Direction.RIGHT -> (1 to 0)
            Direction.DOWN -> (0 to -1)
            Direction.LEFT -> (-1 to 0)
            Direction.UP -> (0 to 1)
        }
        repeat(segment.distance) {
            point += step
            cable.add(point)
        }
    }
    return cable.toList()
}

typealias Point = Pair<Int, Int>

operator fun Point.plus(p: Point) = (first + p.first to second + p.second)

infix fun Point.distanceTo(p: Point) = abs(first - p.first) + abs(second - p.second)


typealias Cable = List<Point>
typealias Port = Point

fun Cable.port(): Port = this[0]


typealias CablePair = Pair<Cable, Cable>
typealias Intersection = Point


fun CablePair.distanceToClosestIntersection(): Int = first.port() distanceTo closestIntersection()

fun CablePair.closestIntersection(): Intersection = intersections().reduce { acc, intersection ->
    if (first.port() distanceTo intersection < first.port() distanceTo acc) intersection else acc
}

fun CablePair.intersections(): List<Intersection> = first.toSet().intersect(second.toSet()).minus(first.port()).toList()

fun CablePair.stepsTo(intersection: Intersection): Int = toList().sumBy { cable -> cable.indexOf(intersection) }

fun CablePair.stepsToClosestIntersection(): Int = intersections().map { stepsTo(it) }.min() ?: 0