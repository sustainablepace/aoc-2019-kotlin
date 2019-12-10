package advent_of_code

import kotlin.math.PI
import kotlin.test.*

class Day10Test {

    @Test
    fun `part one - example 1`() {
        val example1 = javaClass.classLoader
            .getResource("day10_example1.txt")!!
            .readText()

        assertEquals(Asteroid(5,8), example1.asteroids().monitoringSite())
        assertEquals(33, example1.asteroids().maxVisibleAsteroids())
    }

    @Test
    fun `part one - example 2`() {
        val example1 = javaClass.classLoader
            .getResource("day10_example2.txt")!!
            .readText()

        assertEquals(Asteroid(1,2), example1.asteroids().monitoringSite())
        assertEquals(35, example1.asteroids().maxVisibleAsteroids())
    }

    @Test
    fun `part one - example 3`() {
        val example1 = javaClass.classLoader
            .getResource("day10_example3.txt")!!
            .readText()

        assertEquals(Asteroid(6,3), example1.asteroids().monitoringSite())
        assertEquals(41, example1.asteroids().maxVisibleAsteroids())
    }

    @Test
    fun `part one - example 4`() {
        val example1 = javaClass.classLoader
            .getResource("day10_example4.txt")!!
            .readText()

        assertEquals(Asteroid(11,13), example1.asteroids().monitoringSite())
        assertEquals(210, example1.asteroids().maxVisibleAsteroids())
    }

    @Test
    fun `part one`() {
        assertEquals(278, Day10.partOne())
    }

    @Test
    fun `part two - angle`() {
        assertEquals(PI.toFloat(), Vector(0,-1).angle())
        assertEquals(PI.toFloat()/2, Vector(1,0).angle())
        assertEquals(0f, Vector(0,1).angle())
        assertEquals(-PI.toFloat()/2, Vector(-1,0).angle())
    }

    @Test
    fun `part two`() {
        assertEquals(1417, Day10.partTwo())
    }
}
