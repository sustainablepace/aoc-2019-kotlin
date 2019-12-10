package advent_of_code

import kotlin.math.abs
import kotlin.math.atan2

fun main(args: Array<String>) {
    println(Day10.partOne())
    println(Day10.partTwo())
}

object Day10 {

    private val asteroidDiagram = javaClass.classLoader
        .getResource("day10_asteroids.txt")!!
        .readText()

    fun partOne(): Int = asteroidMap(asteroidDiagram).maxNumberOfVisibleAsteroids()

    fun partTwo(): Int {
        val asteroidMap = asteroidMap(asteroidDiagram)
        val laser = asteroidMap.asteroidWithMaxNumberOfVisibleAsteroids()!!
        val asteroidNumber200 = asteroidMap.shootingOrder(laser)?.get(199)
        return asteroidNumber200.x * 100 + asteroidNumber200.y
    }

}

fun AsteroidMap.shootingOrder(laser: AsteroidWithLaser): List<Asteroid> {
    val shootingOrder = mutableListOf<Asteroid>()
    val remainingAsteroids = minus(laser).toMutableList()

    val vectorList = dimensions().toRelativeSearchRange(laser).toVectorList()
    vectorList.sortByDescending { it.angle() }

    val groupedByAngle = vectorList.groupBy { it.angle() }

    while (remainingAsteroids.isNotEmpty()) {
        groupedByAngle.forEach { asteroidsWithSameAngle ->
            asteroidsWithSameAngle.value.firstOrNull { vector ->
                remainingAsteroids.contains(laser + vector)
            }?.also { vector ->
                shootingOrder.add(laser + vector)
                remainingAsteroids.remove(laser + vector)
            }
        }
    }
    return shootingOrder.toList()
}

fun AsteroidMap.asteroidWithMaxNumberOfVisibleAsteroids(): Asteroid? =
    maxBy { asteroid -> asteroid.numVisibleAsteroids(this) }

fun AsteroidMap.maxNumberOfVisibleAsteroids(): Int = map { asteroid -> asteroid.numVisibleAsteroids(this) }.max() ?: 0

typealias X = Int
typealias Y = Int

data class Coordinate(val x: X, val y: Y) {
    operator fun times(i: Int): Coordinate = Coordinate(x * i, y * i)
    operator fun plus(c: Coordinate): Coordinate = Coordinate(x + c.x, y + c.y)
    operator fun minus(c: Coordinate): Coordinate = Coordinate(x - c.x, y - c.y)
    operator fun compareTo(c: Coordinate): Int = (this - c).run {
        if (x < 0 || y < 0) return -1
        else if (x > 0 || y > 0) return 1 else 0
    }

    fun within(s: SearchRange) = this >= s.min && this <= s.max

}

data class SearchRange(val min: Coordinate, val max: Coordinate) {
    fun toRelativeSearchRange(c: Coordinate) = SearchRange(min - c, max - c)
    fun toVectorList(): MutableList<Vector> =
        (min.x..max.x).map { x ->
            (min.y..max.y).map { y -> Coordinate(x, y) }
        }.flatten().sortedBy { abs(it.x) + abs(it.y) }.toMutableList()

}

typealias Asteroid = Coordinate
typealias AsteroidWithLaser = Coordinate
typealias Vector = Coordinate

fun Vector.angle(): Float {
    return atan2(x.toFloat(), y.toFloat())
}

fun Asteroid.numVisibleAsteroids(asteroidMap: AsteroidMap): Int {
    val remainingAsteroids = asteroidMap.minus(this).toMutableList()
    val vectorList = asteroidMap.dimensions().toRelativeSearchRange(this).toVectorList()
    while (vectorList.isNotEmpty()) {
        val vector = vectorList.removeAt(0)
        var scale = 1
        var asteroidFound = false
        var scaledVector = vector * scale
        do {
            if (!asteroidFound && remainingAsteroids.contains(this + scaledVector)) {
                asteroidFound = true
            } else {
                remainingAsteroids.remove(this + scaledVector)
            }
            vectorList.remove(scaledVector)
            scaledVector = vector * (++scale)
        } while (vectorList.contains(scaledVector))

    }
    return remainingAsteroids.size
}
typealias AsteroidMap = List<Asteroid>

fun AsteroidMap.dimensions() = SearchRange(
    Coordinate(map { it.y }.min() ?: 0, map { it.x }.min() ?: 0),
    Coordinate(map { it.y }.max() ?: 0, map { it.x }.max() ?: 0)
)

fun asteroidMap(asteroidDiagram: String): AsteroidMap = asteroidDiagram.lines().mapIndexed { y, line ->
    line.mapIndexed { x, character ->
        if (character == '#') {
            Coordinate(x, y) as Asteroid
        } else null
    }
}.flatten().filterNotNull()