package advent_of_code

import advent_of_code.domain.Program
import advent_of_code.domain.QueuedIo
import advent_of_code.domain.load
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class Day09Test {
    @Test
    fun `part one - program 1`() {
        val io = QueuedIo()
        val code = "109,1,204,-1,1001,100,1,100,1008,100,16,101,1006,101,0,99"
        val program = Program(code.load(), io)

        program.compute()

        assertEquals(code.split(",").map { it.toLong() }, io.outputQueue())
        assertTrue(program.isTerminated())
    }

    @Test
    fun `part one - program 2`() {
        val io = QueuedIo()
        val code = "1102,34915192,34915192,7,4,7,99,0"
        val program = Program(code.load(), io)

        program.compute()

        assertEquals(1219070632396864, io.outputQueue().last())
        assertTrue(program.isTerminated())
    }

    @Test
    fun `part one - program 3`() {
        val io = QueuedIo()
        val code = "104,1125899906842624,99"
        val program = Program(code.load(), io)

        program.compute()

        assertEquals(1125899906842624, io.outputQueue().last())
        assertTrue(program.isTerminated())
    }

    @Test
    fun `part one`() {
        assertEquals(2941952859, Day09.partOne())
    }

    @Test
    fun `part two`() {
        assertEquals(0, Day09.partTwo())
    }
}
