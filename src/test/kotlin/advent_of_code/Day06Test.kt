package advent_of_code

import kotlin.test.Test
import kotlin.test.assertEquals

class Day06Test {
    @Test
    fun `part one`() {

        val input = javaClass.classLoader
            .getResource("day06_part1_test.txt")!!
            .readText()
        val orbitData = OrbitData(input)
        assertEquals(42, orbitData.numOrbits)
        assertEquals(140608, Day06.partOne())

    }

    @Test
    fun `part two`() {
        val input = javaClass.classLoader
            .getResource("day06_part2_test.txt")!!
            .readText()
        val orbitData = OrbitData(input)

        assertEquals(7, orbitData.parentsOf("YOU").size)
        assertEquals(5, orbitData.parentsOf("SAN").size)
        assertEquals(4, orbitData.numTransfersBetween("YOU", "SAN"))

        assertEquals(337, Day06.partTwo())
    }
}