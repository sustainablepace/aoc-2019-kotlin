package advent_of_code

import advent_of_code.domain.IntCode
import advent_of_code.domain.memory
import kotlin.test.Test
import kotlin.test.assertEquals

class Day07Test {
    @Test
    fun `part one - program 1`() {
        val program = "3,15,3,16,1002,16,10,16,1,16,15,15,4,15,99,0,0"
        assertEquals(43210, intArrayOf(0,1,2,3,4).permutations().bestOutput(program))
        assertEquals(43210, intArrayOf(4,3,2,1,0).amplify(0, program))
    }

    @Test
    fun `part one - program 2`() {
        val program = "3,31,3,32,1002,32,10,32,1001,31,-2,31,1007,31,0,33,1002,33,7,33,1,33,31,31,1,32,31,31,4,31,99,0,0,0"
        assertEquals(65210, intArrayOf(0,1,2,3,4).permutations().bestOutput(program))
        assertEquals(65210, intArrayOf(1,0,4,3,2).amplify(0, program))
    }

    @Test
    fun `part one - program 3`() {
        val program = "3,23,3,24,1002,24,10,24,1002,23,-1,23,101,5,23,23,1,24,23,23,4,23,99,0,0"
        assertEquals(54321, intArrayOf(0,1,2,3,4).permutations().bestOutput(program))
        assertEquals(54321, intArrayOf(0,1,2,3,4).amplify(0, program))
    }

    @Test
    fun `part one`() {
        assertEquals(359142, Day07.partOne())
    }

    @Test
    fun `part two`() {
    }
}