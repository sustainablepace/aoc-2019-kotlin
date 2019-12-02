package advent_of_code

import kotlin.test.Test
import kotlin.test.assertEquals

class Day02Test {
    @Test
    fun `part one`() {
        assertEquals(IntCode(Memory("99")), IntCode(Memory("99")).execute())
        assertEquals(IntCode(Memory("2,0,0,0,99")), IntCode(Memory("1,0,0,0,99")).execute())
        assertEquals(IntCode(Memory("2,3,0,6,99")), IntCode(Memory("2,3,0,3,99")).execute())
        assertEquals(IntCode(Memory("2,4,4,5,99,9801")), IntCode(Memory("2,4,4,5,99,0")).execute())
        assertEquals(IntCode(Memory("30,1,1,4,2,5,6,0,99")), IntCode(Memory("1,1,1,4,99,5,6,0,99")).execute())

        val memory = Memory(Day02.input)
        memory[1] = 12
        memory[2] = 2
        assertEquals(3716250, IntCode(memory).execute().memory[0])
    }

    @Test
    fun `part two`() {
    }
}


