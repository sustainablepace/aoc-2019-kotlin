package advent_of_code

import advent_of_code.domain.Program
import advent_of_code.domain.load
import kotlin.test.Test
import kotlin.test.assertEquals

class Day02Test {
    @Test
    fun `part one`() {
        assertEquals(Program("99".load()), Program("99".load()).compute())
        assertEquals(Program("2,0,0,0,99".load()), Program("1,0,0,0,99".load()).compute())
        assertEquals(Program("2,3,0,6,99".load()), Program("2,3,0,3,99".load()).compute())
        assertEquals(Program("2,4,4,5,99,9801".load()), Program("2,4,4,5,99,0".load()).compute())
        assertEquals(Program("30,1,1,4,2,5,6,0,99".load()), Program("1,1,1,4,99,5,6,0,99".load()).compute())

        val memory = Day02.input.load()
        memory[1] = 12
        memory[2] = 2
        assertEquals(3716250, Program(memory).compute().memory[0])
    }

    @Test
    fun `part two`() {
        assertEquals(6472, Day02.partTwo())
    }
}


