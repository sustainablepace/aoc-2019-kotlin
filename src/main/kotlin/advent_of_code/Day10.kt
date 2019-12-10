package advent_of_code

import kotlin.math.abs
import kotlin.math.atan2

fun main(args: Array<String>) {
    println(Day10.partOne())
    println(Day10.partTwo())
}

object Day10 {
    private val diagram = javaClass.classLoader
        .getResource("day10_asteroids.txt")!!
        .readText()

    fun partOne(): Int = diagram.asteroids().maxVisibleAsteroids()

    fun partTwo(): Int? = diagram.asteroids().run {
        monitoringSite()?.shootingOrder(this)?.get(199)?.run {
            x * 100 + y
        }
    }
}

typealias AsteroidDiagram = String
fun AsteroidDiagram.asteroids(): Asteroids = lines().mapIndexed { y, line ->
    line.mapIndexed { x, character ->
        if (character == '#') Asteroid(x, y) else null
    }
}.flatten().filterNotNull()

typealias Asteroids = List<Asteroid>
fun Asteroids.maxVisibleAsteroids(): Int = monitoringSite()?.visibleAsteroids(this)?.size ?: 0
fun Asteroids.monitoringSite(): MonitoringSite? = maxBy { it.visibleAsteroids(this).size }
fun Asteroids.searchRange() = SearchRange(
    Coordinate(map { it.y }.min() ?: 0, map { it.x }.min() ?: 0),
    Coordinate(map { it.y }.max() ?: 0, map { it.x }.max() ?: 0)
)

typealias Asteroid = Coordinate
fun Asteroid.visibleAsteroids(asteroids: Asteroids): Asteroids {
    val remainingAsteroids = asteroids.minus(this).toMutableList()
    val vectors = asteroids.searchRange().relativeTo(this).vectors()
    while (vectors.isNotEmpty()) {
        val vector = vectors.removeAt(0)
        var scale = 1
        var asteroidFound = false
        var scaledVector = vector * scale
        do {
            if (!asteroidFound && remainingAsteroids.contains(this + scaledVector)) {
                asteroidFound = true
            } else {
                remainingAsteroids.remove(this + scaledVector)
            }
            vectors.remove(scaledVector)
            scaledVector = vector * (++scale)
        } while (vectors.contains(scaledVector))
    }
    return remainingAsteroids.toList()
}

typealias Vector = Coordinate
fun Vector.angle(): Float = atan2(x.toFloat(), y.toFloat())
operator fun Vector.times(i: Int): Vector = Vector(x * i, y * i)

data class SearchRange(val min: Coordinate, val max: Coordinate) {
    fun relativeTo(c: Coordinate): RelativeSearchRange = SearchRange(min - c, max - c)
}

typealias RelativeSearchRange = SearchRange
fun RelativeSearchRange.vectors(): MutableList<Vector> =
    (min.x..max.x).map { x ->
        (min.y..max.y).map { y -> Coordinate(x, y) }
    }.flatten().sortedBy { abs(it.x) + abs(it.y) }.toMutableList()

typealias MonitoringSite = Asteroid
fun MonitoringSite.shootingOrder(asteroids: Asteroids): List<Asteroid> {
    val shootingOrder = mutableListOf<Asteroid>()
    val remainingAsteroids = asteroids.minus(this).toMutableList()

    val vectors = asteroids.searchRange().relativeTo(this).vectors()
    vectors.sortByDescending { it.angle() }

    val groupedByAngle = vectors.groupBy { it.angle() }

    while (remainingAsteroids.isNotEmpty()) {
        groupedByAngle.forEach { alignedAsteroids ->
            alignedAsteroids.value.firstOrNull { vector ->
                remainingAsteroids.contains(this + vector)
            }?.also { vector ->
                shootingOrder.add(this + vector)
                remainingAsteroids.remove(this + vector)
            }
        }
    }
    return shootingOrder.toList()
}

data class Coordinate(val x: X, val y: Y) {
    operator fun plus(c: Coordinate): Coordinate = Coordinate(x + c.x, y + c.y)
    operator fun minus(c: Coordinate): Coordinate = Coordinate(x - c.x, y - c.y)
}

typealias X = Int
typealias Y = Int