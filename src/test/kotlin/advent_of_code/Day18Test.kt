package advent_of_code

import kotlin.test.Test
import kotlin.test.assertEquals

class Day18Test {

    @Test
    fun `part one - example 1`() {
        val vaultFile = javaClass.classLoader
            .getResource("day18_example1.txt")!!
            .readText()

        assertEquals(132, vaultFile.toVault().minSteps())
    }

    @Test
    fun `part one - example 2`() {
        val vaultFile = javaClass.classLoader
            .getResource("day18_example2.txt")!!
            .readText()

 //       assertEquals(136, vaultFile.toVault().minSteps())
    }

    @Test
    fun `part one - example 3`() {
        val vaultFile = javaClass.classLoader
            .getResource("day18_example3.txt")!!
            .readText()

        assertEquals(81, vaultFile.toVault().minSteps())
    }

    @Test
    fun `part one`() {
//        assertEquals(0, Day18.partOne())
    }

    @Test
    fun `part two`() {
        assertEquals(0, Day18.partTwo())
    }
}