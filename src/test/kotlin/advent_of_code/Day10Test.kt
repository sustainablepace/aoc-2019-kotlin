package advent_of_code

import kotlin.math.PI
import kotlin.test.*

class Day10Test {
    val example1 = javaClass.classLoader
        .getResource("day10_example1.txt")!!
        .readText()

    @Test
    fun coordinates() {
        assertTrue(Coordinate(0,0) < Coordinate(1,1))
        assertTrue(Coordinate(0,0) <= Coordinate(1,1))
        assertFalse(Coordinate(0,0) > Coordinate(1,1))
        assertFalse(Coordinate(0,0) >= Coordinate(1,1))
        assertNotEquals(Coordinate(0,0), Coordinate(1,1))
    }

    @Test
    fun searchRange() {
        assertTrue(Coordinate(0,0).within(SearchRange(Coordinate(-1,-1), Coordinate(1, 1))))
        assertTrue(Coordinate(-1,-1).within(SearchRange(Coordinate(-1,-1), Coordinate(1, 1))))
        assertTrue(Coordinate(1,1).within(SearchRange(Coordinate(-1,-1), Coordinate(1, 1))))
        assertTrue(Coordinate(-1,1).within(SearchRange(Coordinate(-1,-1), Coordinate(1, 1))))
        assertTrue(Coordinate(1,-1).within(SearchRange(Coordinate(-1,-1), Coordinate(1, 1))))
        assertFalse(Coordinate(6,6).within(SearchRange(Coordinate(-1,-1), Coordinate(1, 1))))

    }

    @Test
    fun `part one - example 1`() {
        assertEquals(33, asteroidMap(example1).maxNumberOfVisibleAsteroids())
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
