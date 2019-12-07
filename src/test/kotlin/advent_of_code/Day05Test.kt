package advent_of_code

import advent_of_code.domain.CommandLineIo
import advent_of_code.domain.Program
import advent_of_code.domain.load
import io.mockk.*
import kotlin.test.Test
import kotlin.test.assertEquals

class Day05Test {
    @Test
    fun `part one - mocking io`() {
        mockkObject(CommandLineIo)
        every { CommandLineIo.input() } returns "1"
        every { CommandLineIo.output("1") } just Runs

        assertEquals(Program("1,0,4,0,99".load()), Program("3,0,4,0,99".load()).compute())
        verify {
            CommandLineIo.input()
            CommandLineIo.output("1")
        }
        unmockkObject(CommandLineIo)
    }

    @Test
    fun `part one - immediate mode`() {
        assertEquals(Program("1002,4,3,4,99".load()), Program("1002,4,3,4,33".load()).compute())
    }

    @Test
    fun `part one`() {
        mockkObject(CommandLineIo)
        every { CommandLineIo.input() } returns "1"
        every { CommandLineIo.output(any()) } just Runs

        Day05.partOne()

        verifySequence {
            CommandLineIo.input()
            CommandLineIo.output("0")
            CommandLineIo.output("0")
            CommandLineIo.output("0")
            CommandLineIo.output("0")
            CommandLineIo.output("0")
            CommandLineIo.output("0")
            CommandLineIo.output("0")
            CommandLineIo.output("0")
            CommandLineIo.output("0")
            CommandLineIo.output("4887191")
        }
        unmockkObject(CommandLineIo)
    }

    @Test
    fun `part two - comparison 1`() {
        mockkObject(CommandLineIo)
        every { CommandLineIo.input() } returns "8"
        every { CommandLineIo.output(any()) } just Runs

        Program("3,9,8,9,10,9,4,9,99,-1,8".load()).compute()

        verifySequence {
            CommandLineIo.input()
            CommandLineIo.output("1")
        }
        unmockkObject(CommandLineIo)
    }

    @Test
    fun `part two - comparison 2`() {
        mockkObject(CommandLineIo)
        every { CommandLineIo.input() } returns "7" // not 8
        every { CommandLineIo.output(any()) } just Runs

        Program("3,9,8,9,10,9,4,9,99,-1,8".load()).compute()

        verifySequence {
            CommandLineIo.input()
            CommandLineIo.output("0")
        }
        unmockkObject(CommandLineIo)
    }

    @Test
    fun `part two - comparison 3`() {
        mockkObject(CommandLineIo)
        every { CommandLineIo.input() } returns "7" // < 8
        every { CommandLineIo.output(any()) } just Runs

        Program("3,9,7,9,10,9,4,9,99,-1,8".load()).compute()

        verifySequence {
            CommandLineIo.input()
            CommandLineIo.output("1")
        }
        unmockkObject(CommandLineIo)
    }

    @Test
    fun `part two - comparison 4`() {
        mockkObject(CommandLineIo)
        every { CommandLineIo.input() } returns "8" // ! < 8
        every { CommandLineIo.output(any()) } just Runs

        Program("3,9,7,9,10,9,4,9,99,-1,8".load()).compute()

        verifySequence {
            CommandLineIo.input()
            CommandLineIo.output("0")
        }
        unmockkObject(CommandLineIo)
    }

    @Test
    fun `part two - comparison 5`() {
        mockkObject(CommandLineIo)
        every { CommandLineIo.input() } returns "8" // == 8
        every { CommandLineIo.output(any()) } just Runs

        Program("3,3,1108,-1,8,3,4,3,99".load()).compute()

        verifySequence {
            CommandLineIo.input()
            CommandLineIo.output("1")
        }
        unmockkObject(CommandLineIo)
    }

    @Test
    fun `part two - comparison 6`() {
        mockkObject(CommandLineIo)
        every { CommandLineIo.input() } returns "7" // != 8
        every { CommandLineIo.output(any()) } just Runs

        Program("3,3,1108,-1,8,3,4,3,99".load()).compute()

        verifySequence {
            CommandLineIo.input()
            CommandLineIo.output("0")
        }
        unmockkObject(CommandLineIo)
    }

    @Test
    fun `part two - comparison 7`() {
        mockkObject(CommandLineIo)
        every { CommandLineIo.input() } returns "7" // < 8
        every { CommandLineIo.output(any()) } just Runs

        Program("3,3,1107,-1,8,3,4,3,99".load()).compute()

        verifySequence {
            CommandLineIo.input()
            CommandLineIo.output("1")
        }
        unmockkObject(CommandLineIo)
    }

    @Test
    fun `part two - comparison 8`() {
        mockkObject(CommandLineIo)
        every { CommandLineIo.input() } returns "8" // ! < 8
        every { CommandLineIo.output(any()) } just Runs

        Program("3,3,1107,-1,8,3,4,3,99".load()).compute()

        verifySequence {
            CommandLineIo.input()
            CommandLineIo.output("0")
        }
        unmockkObject(CommandLineIo)
    }

    @Test
    fun `part two - jump 1`() {
        mockkObject(CommandLineIo)
        every { CommandLineIo.input() } returns "0" // == 0
        every { CommandLineIo.output(any()) } just Runs

        Program("3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9".load()).compute()

        verifySequence {
            CommandLineIo.input()
            CommandLineIo.output("0")
        }
        unmockkObject(CommandLineIo)
    }

    @Test
    fun `part two - jump 2`() {
        mockkObject(CommandLineIo)
        every { CommandLineIo.input() } returns "9" // != 0
        every { CommandLineIo.output(any()) } just Runs

        Program("3,3,1105,-1,9,1101,0,0,12,4,12,99,1".load()).compute()

        verifySequence {
            CommandLineIo.input()
            CommandLineIo.output("1")
        }
        unmockkObject(CommandLineIo)
    }

    @Test
    fun `part two - larger example 1`() {
        mockkObject(CommandLineIo)
        every { CommandLineIo.input() } returns "7" // < 8
        every { CommandLineIo.output(any()) } just Runs

        Program("3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104,999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99".load()).compute()

        verifySequence {
            CommandLineIo.input()
            CommandLineIo.output("999")
        }
        unmockkObject(CommandLineIo)
    }

    @Test
    fun `part two - larger example 2`() {
        mockkObject(CommandLineIo)
        every { CommandLineIo.input() } returns "8" // == 8
        every { CommandLineIo.output(any()) } just Runs

        Program("3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104,999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99".load()).compute()

        verifySequence {
            CommandLineIo.input()
            CommandLineIo.output("1000")
        }
        unmockkObject(CommandLineIo)
    }

    @Test
    fun `part two - larger example 3`() {
        mockkObject(CommandLineIo)
        every { CommandLineIo.input() } returns "9" // < 8
        every { CommandLineIo.output(any()) } just Runs

        Program("3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104,999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99".load()).compute()

        verifySequence {
            CommandLineIo.input()
            CommandLineIo.output("1001")
        }
        unmockkObject(CommandLineIo)
    }

    @Test
    fun `part two`() {
        mockkObject(CommandLineIo)
        every { CommandLineIo.input() } returns "5"
        every { CommandLineIo.output(any()) } just Runs

        Day05.partTwo()

        verifySequence {
            CommandLineIo.input()
            CommandLineIo.output("3419022")
        }
        unmockkObject(CommandLineIo)
    }

}
