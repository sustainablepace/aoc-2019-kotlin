package advent_of_code

import kotlin.test.Test
import kotlin.test.assertEquals

class Day12Test {
    var moonsExample1Input = javaClass.classLoader
        .getResource("day12_moons_example1.txt")!!
        .readText()

    @Test
    fun `part one - example 1 step 1`() {

        var moons = moonsExample1Input.load()

        assertEquals(
            listOf(
                Moon(Pos(-1, 0, 2), Velocity(0, 0, 0)),
                Moon(Pos(2, -10, -7), Velocity(0, 0, 0)),
                Moon(Pos(4, -8, 8), Velocity(0, 0, 0)),
                Moon(Pos(3, 5, -1), Velocity(0, 0, 0))
            ), moons
        )

        repeat(1) {
            moons = moons.simulateGravity()
        }

        assertEquals(
            listOf(
                Moon(Pos(2, -1, 1), Velocity(3, -1, -1)),
                Moon(Pos(3, -7, -4), Velocity(1, 3, 3)),
                Moon(Pos(1, -7, 5), Velocity(-3, 1, -3)),
                Moon(Pos(2, 2, 0), Velocity(-1, -3, 1))
            ), moons
        )
    }

    @Test
    fun `part one - example 1 step 2`() {

        var moons = moonsExample1Input.load()

        repeat(2) {
            moons = moons.simulateGravity()
        }

        assertEquals(
            listOf(
                Moon(Pos(5, -3, -1), Velocity(3, -2, -2)),
                Moon(Pos(1, -2, 2), Velocity(-2, 5, 6)),
                Moon(Pos(1, -4, -1), Velocity(0, 3, -6)),
                Moon(Pos(1, -4, 2), Velocity(-1, -6, 2))
            ), moons
        )

    }

    @Test
    fun `part one - example 1 step 10`() {

        var moons = moonsExample1Input.load()

        repeat(10) {
            moons = moons.simulateGravity()
        }

        assertEquals(
            listOf(
                Moon(Pos(2, 1, -3), Velocity(-3, -2, 1)),
                Moon(Pos(1, -8, 0), Velocity(-1, 1, 3)),
                Moon(Pos(3, -6, 1), Velocity(3, 2, -3)),
                Moon(Pos(2, 0, 4), Velocity(1, -1, -1))
            ), moons
        )

        assertEquals(179, moons.totalEnergy())
    }

    @Test
    fun `part one`() {
        assertEquals(6227, Day12.partOne())
    }

    @Test
    fun `part two`() {
        assertEquals(331346071640472, Day12.partTwo())
    }
}
