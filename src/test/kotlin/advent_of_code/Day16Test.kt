package advent_of_code

import kotlin.test.Test
import kotlin.test.assertEquals

class Day16Test {
    @Test
    fun `part one - pattern`() {
        assertEquals(listOf(1, 0, -1, 0, 1, 0, -1, 0), patternForI(0, 8))
        assertEquals(listOf(0, 1, 1, 0, 0, -1, -1, 0), patternForI(1, 8))
        assertEquals(listOf(0, 0, 1, 1, 1, 0, 0, 0), patternForI(2, 8))
    }

    @Test
    fun `part one - example 1`() {
        val input = "12345678"
        assertEquals("48226158", input.transform(1))
        assertEquals("34040438", input.transform(2))
        assertEquals("03415518", input.transform(3))
        assertEquals("01029498", input.transform(4))
    }

    @Test
    fun `part one - example 2`() {
        val input = "80871224585914546619083218645595"
        assertEquals("24176176", input.transform(100).substring(0, 8))
    }

    @Test
    fun `part one - example 3`() {
        val input = "19617804207202209144916044189917"
        assertEquals("73745418", input.transform(100).substring(0, 8))
    }

    @Test
    fun `part one - example 4`() {
        val input = "69317163492948606335995924319873"
        assertEquals("52432133", input.transform(100).substring(0, 8))
    }

    @Test
    fun `part one`() {
        assertEquals("82435530", Day16.partOne())
    }
}
