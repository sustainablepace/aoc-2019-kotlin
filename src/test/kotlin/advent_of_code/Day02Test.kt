package advent_of_code

import kotlin.test.Test
import kotlin.test.assertEquals

class Day02Test {
    @Test
    fun `part one`() {
        assertEquals(IntCode(memory("99")), IntCode(memory("99")).run())
        assertEquals(IntCode(memory("2,0,0,0,99")), IntCode(memory("1,0,0,0,99")).run())
        assertEquals(IntCode(memory("2,3,0,6,99")), IntCode(memory("2,3,0,3,99")).run())
        assertEquals(IntCode(memory("2,4,4,5,99,9801")), IntCode(memory("2,4,4,5,99,0")).run())
        assertEquals(IntCode(memory("30,1,1,4,2,5,6,0,99")), IntCode(memory("1,1,1,4,99,5,6,0,99")).run())

        val memory = memory(Day02.input)
        memory[1] = 12
        memory[2] = 2
        assertEquals(3716250, IntCode(memory).run().memory[0])
    }

    @Test
    fun `part two`() {
        assertEquals(6472, Day02.partTwo())
    }
}


