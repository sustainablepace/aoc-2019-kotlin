package advent_of_code

import kotlin.test.Test
import kotlin.test.assertEquals

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

    @Test
    fun `part two - example 3`() {
        val part1example1 = javaClass.classLoader
            .getResource("day14_part1_example3.txt")!!
            .readText()

        val reactions = part1example1.parse()
        assertEquals(82892753, reactions.maxFuelForOre(1_000_000_000_000))
    }

    @Test
    fun `part two - example 4`() {
        val part1example1 = javaClass.classLoader
            .getResource("day14_part1_example4.txt")!!
            .readText()

        val reactions = part1example1.parse()
        assertEquals(5586022, reactions.maxFuelForOre(1_000_000_000_000))
    }

    @Test
    fun `part two - example 5`() {
        val part1example1 = javaClass.classLoader
            .getResource("day14_part1_example5.txt")!!
            .readText()

        val reactions = part1example1.parse()
        assertEquals(460664, reactions.maxFuelForOre(1_000_000_000_000))
    }

    @Test
    fun `part two`() {
        assertEquals(0, Day14.partTwo())
    }
}
