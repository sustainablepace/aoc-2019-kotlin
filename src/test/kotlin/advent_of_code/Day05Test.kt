package advent_of_code

import advent_of_code.domain.CommandLineIo
import advent_of_code.domain.Program
import advent_of_code.domain.QueuedIo
import advent_of_code.domain.load
import io.mockk.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class Day05Test {
    @Test
    fun `part one - mocking io`() {
        val io = QueuedIo().queueInput(1)
        val program = Program("3,0,4,0,99".load(), io)

        assertEquals(Program("1,0,4,0,99".load(), io), program.compute())
        assertEquals(1, io.outputQueue().first())
        assertTrue(program.isTerminated())
    }

    @Test
    fun `part one - immediate mode`() {
        assertEquals(Program("1002,4,3,4,99".load()), Program("1002,4,3,4,33".load()).compute())
    }

    @Test
    fun `part one`() {
        mockkObject(CommandLineIo)
        every { CommandLineIo.input() } returns 1
        every { CommandLineIo.output(any()) } just Runs

        Day05.partOne()

        verifySequence {
            CommandLineIo.input()
            CommandLineIo.output(0)
            CommandLineIo.output(0)
            CommandLineIo.output(0)
            CommandLineIo.output(0)
            CommandLineIo.output(0)
            CommandLineIo.output(0)
            CommandLineIo.output(0)
            CommandLineIo.output(0)
            CommandLineIo.output(0)
            CommandLineIo.output(4887191)
        }
        unmockkObject(CommandLineIo)
    }

    @Test
    fun `part two - equals true`() {
        val io = QueuedIo().queueInput(8) // == 8
        val program = Program("3,9,8,9,10,9,4,9,99,-1,8".load(), io)

        program.compute()

        assertEquals(1, io.outputQueue().first())
        assertTrue(program.isTerminated())
    }

    @Test
    fun `part two - equals false`() {
        val io = QueuedIo().queueInput(7) // != 8
        val program = Program("3,9,8,9,10,9,4,9,99,-1,8".load(), io)

        program.compute()

        assertEquals(0, io.outputQueue().first())
        assertTrue(program.isTerminated())
    }

    @Test
    fun `part two - less than true`() {
        val io = QueuedIo().queueInput(7) // < 8
        val program = Program("3,9,7,9,10,9,4,9,99,-1,8".load(), io)

        program.compute()

        assertEquals(1, io.outputQueue().first())
        assertTrue(program.isTerminated())
    }

    @Test
    fun `part two - less than false`() {
        val io = QueuedIo().queueInput(8) //! < 8
        val program = Program("3,9,7,9,10,9,4,9,99,-1,8".load(), io)

        program.compute()

        assertEquals(0, io.outputQueue().first())
        assertTrue(program.isTerminated())
    }

    @Test
    fun `part two - comparison 5`() {
        val io = QueuedIo().queueInput(8) // == 8
        val program = Program("3,3,1108,-1,8,3,4,3,99".load(), io)

        program.compute()

        assertEquals(1, io.outputQueue().first())
        assertTrue(program.isTerminated())
    }

    @Test
    fun `part two - comparison 6`() {
        val io = QueuedIo().queueInput(7) // != 8
        val program = Program("3,3,1108,-1,8,3,4,3,99".load(), io)

        program.compute()

        assertEquals(0, io.outputQueue().first())
        assertTrue(program.isTerminated())
    }

    @Test
    fun `part two - comparison 7`() {
        val io = QueuedIo().queueInput(7) // < 8
        val program = Program("3,3,1107,-1,8,3,4,3,99".load(), io)

        program.compute()

        assertEquals(1, io.outputQueue().first())
        assertTrue(program.isTerminated())
    }

    @Test
    fun `part two - comparison 8`() {
        val io = QueuedIo().queueInput(8) // ! < 8
        val program = Program("3,3,1107,-1,8,3,4,3,99".load(), io)

        program.compute()

        assertEquals(0, io.outputQueue().first())
        assertTrue(program.isTerminated())
    }

    @Test
    fun `part two - jump 1`() {
        val io = QueuedIo().queueInput(0) // == 0
        val program = Program("3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9".load(), io)

        program.compute()

        assertEquals(0, io.outputQueue().first())
        assertTrue(program.isTerminated())
    }

    @Test
    fun `part two - jump 2`() {
        val io = QueuedIo().queueInput(9) // != 0
        val program = Program("3,3,1105,-1,9,1101,0,0,12,4,12,99,1".load(), io)

        program.compute()

        assertEquals(1, io.outputQueue().first())
        assertTrue(program.isTerminated())
    }

    @Test
    fun `part two - larger example 1`() {
        val io = QueuedIo().queueInput(7) // < 8
        val program = Program(
            "3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104,999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99".load(),
            io
        )

        program.compute()

        assertEquals(999, io.outputQueue().first())
        assertTrue(program.isTerminated())
    }

    @Test
    fun `part two - larger example 2`() {
        val io = QueuedIo().queueInput(8) // == 8
        val program = Program(
            "3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104,999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99".load(),
            io
        )

        program.compute()

        assertEquals(1000, io.outputQueue().first())
        assertTrue(program.isTerminated())
    }

    @Test
    fun `part two - larger example 3`() {
        val io = QueuedIo().queueInput(9)
        val program = Program(
            "3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104,999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99".load(),
            io
        )

        program.compute()

        assertEquals(1001, io.outputQueue().first())
        assertTrue(program.isTerminated())
    }

    @Test
    fun `part two`() {
        mockkObject(CommandLineIo)
        every { CommandLineIo.input() } returns 5
        every { CommandLineIo.output(any()) } just Runs

        Day05.partTwo()

        verifySequence {
            CommandLineIo.input()
            CommandLineIo.output(3419022)
        }
        unmockkObject(CommandLineIo)
    }

}
