package advent_of_code

import kotlin.math.abs
import kotlin.math.sign

fun main(args: Array<String>) {
    println(Day12.partOne())
    println(Day12.partTwo())
}

object Day12 {
    private val jupiterMoons = javaClass.classLoader
        .getResource("day12_moons.txt")!!
        .readText()

    fun partOne(): Int {
        var moons = jupiterMoons.load()
        repeat(1000) {
            moons = moons.simulateGravity()
        }
        return moons.totalEnergy()
    }

    fun partTwo(): Any {
        var xPeriod = 0L
        var yPeriod = 0L
        var zPeriod = 0L
        var counter = 0L
        val moons = jupiterMoons.load()
        var step = jupiterMoons.load()
        while (listOf(xPeriod, yPeriod, zPeriod).any { it == 0L }) {
            step = step.step()
            counter++
            if (xPeriod == 0L && step.check(Vector3::x, moons)) xPeriod = counter
            if (yPeriod == 0L && step.check(Vector3::y, moons)) yPeriod = counter
            if (zPeriod == 0L && step.check(Vector3::z, moons)) zPeriod = counter
        }
        return lcm(xPeriod, yPeriod, zPeriod)

    }
}

fun lcm(a: Long, b: Long, vararg numbers: Long): Long = when {
    numbers.isEmpty() -> abs(a * b) / gcd(a, b)
    else -> lcm(lcm(a, b), numbers.first(), *numbers.drop(1).toLongArray())
}

fun gcd(a: Long, b: Long, vararg numbers: Long): Long = when {
    numbers.isEmpty() -> if (b == 0L) a else gcd(b, a % b)
    else -> gcd(gcd(a, b), numbers.first(), *numbers.drop(1).toLongArray())
}

typealias MoonsInput = String

fun MoonsInput.load() = lines().map {
    it
        .replace("<", "")
        .replace(">", "")
        .replace("x=", "")
        .replace("y=", "")
        .replace("z=", "")
        .split(", ").map { it.toInt() }
}.map { (x, y, z) -> Moon(Pos(x, y, z), Velocity(0, 0, 0)) }

typealias AxisVelocity = Int
typealias Energy = Int
typealias Moons = List<Moon>
typealias Pos = Vector3
typealias Velocity = Vector3
typealias Z = Int

data class Vector3(val x: X, val y: Y, val z: Z) {
    operator fun plus(vector3: Vector3): Vector3 = Vector3(x + vector3.x, y + vector3.y, z + vector3.z)
}



data class Moon(val pos: Pos, val velocity: Velocity) {
    operator fun plus(v: Velocity): Moon = (velocity + v).run {
        Moon(pos + this, this)
    }

    fun gravitate(other: Moon): Moon = copy(
        velocity = velocity.copy(
            x = gravitateAxis(other, Vector3::x),
            y = gravitateAxis(other, Vector3::y),
            z = gravitateAxis(other, Vector3::z)
        )
    )

    fun applyVelocity(): Moon = copy(pos = pos + velocity)

    private inline fun gravitateAxis(other: Moon, axis: Vector3.() -> Int): Int = velocity.axis() + (other.pos.axis() - pos.axis()).sign

    fun totalEnergy(): Energy = potentialEnergy() * kineticEnergy()
    private fun potentialEnergy(): Energy = pos.energy()
    private fun kineticEnergy(): Energy = velocity.energy()
}



fun Moons.simulateGravity(): Moons {
    val pairs = (0 until size).map { x -> (0 until size).map { y -> get(x) to get(y) } }
    return pairs.map { pairsForMoon ->
        pairsForMoon.map { pair ->
            pair.simulateGravity()
        }.reduce { acc, vector3 ->
            acc + vector3
        }.run {
            pairsForMoon.first().first + this
        }
    }
}

fun Moons.totalEnergy() = map { it.totalEnergy() }.sum()

typealias MoonPair = Pair<Moon, Moon>

fun MoonPair.simulateGravity(): Velocity {
    return (first.pos to second.pos).gravity()
}

typealias VelocityPair = Pair<Velocity, Velocity>
typealias AxisVelocityPair = Pair<AxisVelocity, AxisVelocity>

fun VelocityPair.gravity(): Velocity = Velocity(
    (first.x to second.x).gravity(),
    (first.y to second.y).gravity(),
    (first.z to second.z).gravity()
)

fun AxisVelocityPair.gravity(): AxisVelocity = (second - first).sign

fun Vector3.energy() = abs(x) + abs(y) + abs(z)

private fun List<Moon>.step(): List<Moon> = mapIndexed { index, moon ->
    indices.filterNot { it == index }
        .map { get(it) }
        .fold(moon) { current, other -> current.gravitate(other) }
        .applyVelocity()
}

private inline fun List<Moon>.check(axis: Vector3.() -> Int, moons: Moons): Boolean =
    indices.all { idx -> matches(idx, axis, moons) }

private inline fun List<Moon>.matches(index: Int, axis: Vector3.() -> Int, moons: Moons): Boolean =
    this[index].pos.axis() == moons[index].pos.axis() && this[index].velocity.axis() == moons[index].velocity.axis()

