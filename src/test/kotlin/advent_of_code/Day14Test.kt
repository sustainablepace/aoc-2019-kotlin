package advent_of_code

import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class Day14Test {

    @Test
    fun `part one - example 1`() {
        val part1example1 = javaClass.classLoader
            .getResource("day14_part1_example1.txt")!!
            .readText()

        val reactions = part1example1.parse()
        assertEquals(31, reactions.minOreForFuel(1))
    }

    @Test
    fun `part one - example 2`() {
        val part1example1 = javaClass.classLoader
            .getResource("day14_part1_example2.txt")!!
            .readText()

        val reactions = part1example1.parse()
        assertEquals(165, reactions.minOreForFuel(1))
    }

    @Test
    fun `part one - example 3`() {
        val part1example1 = javaClass.classLoader
            .getResource("day14_part1_example3.txt")!!
            .readText()

        val reactions = part1example1.parse()
        assertEquals(13312, reactions.minOreForFuel(1))
    }

    @Test
    fun `part one - example 4`() {
        val part1example1 = javaClass.classLoader
            .getResource("day14_part1_example4.txt")!!
            .readText()

        val reactions = part1example1.parse()
        assertEquals(180697, reactions.minOreForFuel(1))
    }

    @Test
    fun `part one - example 5`() {
        val part1example1 = javaClass.classLoader
            .getResource("day14_part1_example5.txt")!!
            .readText()

        val reactions = part1example1.parse()
        assertEquals(2210736, reactions.minOreForFuel(1))
    }

    @Test
    fun `part one`() {
        assertEquals(168046, Day14.partOne())
    }

    @Ignore
    @Test
    fun `part two - example 2`() {
        val part1example1 = javaClass.classLoader
            .getResource("day14_part1_example3.txt")!!
            .readText()

        val reactions = part1example1.parse()
        assertTrue(reactions.minOreForFuel(6323777403) <= 1_000_000_000_000)
        assertTrue(reactions.minOreForFuel(6323777403 + 1) > 1_000_000_000_000)

        assertEquals(6323777403, reactions.maxFuelForOre(1_000_000_000_000))
    }

    @Test
    @Ignore
    fun `part two - example 3`() {
        val part1example1 = javaClass.classLoader
            .getResource("day14_part1_example3.txt")!!
            .readText()

        val reactions = part1example1.parse()
        assertTrue(reactions.minOreForFuel(82892753) <= 1_000_000_000_000)
        assertTrue(reactions.minOreForFuel(82892753 + 1) > 1_000_000_000_000)

        assertEquals(82892753, reactions.maxFuelForOre(1_000_000_000_000))
    }

    @Test
    @Ignore
    fun `part two - example 4`() {
        val part1example1 = javaClass.classLoader
            .getResource("day14_part1_example4.txt")!!
            .readText()

        val reactions = part1example1.parse()
        assertTrue(reactions.minOreForFuel(5586022) <= 1_000_000_000_000)
        assertTrue(reactions.minOreForFuel(5586022 + 1) > 1_000_000_000_000)

        assertEquals(5586022, reactions.maxFuelForOre(1_000_000_000_000))
    }

    @Test
    @Ignore
    fun `part two - example 5`() {
        val part1example1 = javaClass.classLoader
            .getResource("day14_part1_example5.txt")!!
            .readText()

        val reactions = part1example1.parse()
        assertTrue(reactions.minOreForFuel(460664) <= 1_000_000_000_000)
        assertTrue(reactions.minOreForFuel(460664 + 1) > 1_000_000_000_000)

        assertEquals(460664, reactions.maxFuelForOre(1_000_000_000_000))
    }

    @Test
    @Ignore
    fun `part two`() {
        assertEquals(0, Day14.partTwo())
    }
}
