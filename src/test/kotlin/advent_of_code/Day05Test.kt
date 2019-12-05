package advent_of_code

import advent_of_code.domain.InputOutput
import advent_of_code.domain.IntCode
import advent_of_code.domain.memory
import io.mockk.*
import kotlin.test.Test
import kotlin.test.assertEquals

class Day05Test {
    @Test
    fun `part one - mocking io`() {
        mockkObject(InputOutput)
        every { InputOutput.input() } returns "1"
        every { InputOutput.output("1") } just Runs

        assertEquals(IntCode(memory("1,0,4,0,99")), IntCode(memory("3,0,4,0,99")).run())
        verify {
            InputOutput.input()
            InputOutput.output("1")
        }
        unmockkObject(InputOutput)
    }

    @Test
    fun `part one - immediate mode`() {
        assertEquals(IntCode(memory("1002,4,3,4,99")), IntCode(memory("1002,4,3,4,33")).run())
    }

    @Test
    fun `part one`() {
        mockkObject(InputOutput)
        every { InputOutput.input() } returns "1"
        every { InputOutput.output(any()) } just Runs

        Day05.partOne()

        verifySequence {
            InputOutput.input()
            InputOutput.output("0")
            InputOutput.output("0")
            InputOutput.output("0")
            InputOutput.output("0")
            InputOutput.output("0")
            InputOutput.output("0")
            InputOutput.output("0")
            InputOutput.output("0")
            InputOutput.output("0")
            InputOutput.output("4887191")
        }
        unmockkObject(InputOutput)
    }

    @Test
    fun `part two - comparison 1`() {
        mockkObject(InputOutput)
        every { InputOutput.input() } returns "8"
        every { InputOutput.output(any()) } just Runs

        IntCode(memory("3,9,8,9,10,9,4,9,99,-1,8")).run()

        verifySequence {
            InputOutput.input()
            InputOutput.output("1")
        }
        unmockkObject(InputOutput)
    }

    @Test
    fun `part two - comparison 2`() {
        mockkObject(InputOutput)
        every { InputOutput.input() } returns "7" // not 8
        every { InputOutput.output(any()) } just Runs

        IntCode(memory("3,9,8,9,10,9,4,9,99,-1,8")).run()

        verifySequence {
            InputOutput.input()
            InputOutput.output("0")
        }
        unmockkObject(InputOutput)
    }

    @Test
    fun `part two - comparison 3`() {
        mockkObject(InputOutput)
        every { InputOutput.input() } returns "7" // < 8
        every { InputOutput.output(any()) } just Runs

        IntCode(memory("3,9,7,9,10,9,4,9,99,-1,8")).run()

        verifySequence {
            InputOutput.input()
            InputOutput.output("1")
        }
        unmockkObject(InputOutput)
    }

    @Test
    fun `part two - comparison 4`() {
        mockkObject(InputOutput)
        every { InputOutput.input() } returns "8" // ! < 8
        every { InputOutput.output(any()) } just Runs

        IntCode(memory("3,9,7,9,10,9,4,9,99,-1,8")).run()

        verifySequence {
            InputOutput.input()
            InputOutput.output("0")
        }
        unmockkObject(InputOutput)
    }

    @Test
    fun `part two - comparison 5`() {
        mockkObject(InputOutput)
        every { InputOutput.input() } returns "8" // == 8
        every { InputOutput.output(any()) } just Runs

        IntCode(memory("3,3,1108,-1,8,3,4,3,99")).run()

        verifySequence {
            InputOutput.input()
            InputOutput.output("1")
        }
        unmockkObject(InputOutput)
    }

    @Test
    fun `part two - comparison 6`() {
        mockkObject(InputOutput)
        every { InputOutput.input() } returns "7" // != 8
        every { InputOutput.output(any()) } just Runs

        IntCode(memory("3,3,1108,-1,8,3,4,3,99")).run()

        verifySequence {
            InputOutput.input()
            InputOutput.output("0")
        }
        unmockkObject(InputOutput)
    }

    @Test
    fun `part two - comparison 7`() {
        mockkObject(InputOutput)
        every { InputOutput.input() } returns "7" // < 8
        every { InputOutput.output(any()) } just Runs

        IntCode(memory("3,3,1107,-1,8,3,4,3,99")).run()

        verifySequence {
            InputOutput.input()
            InputOutput.output("1")
        }
        unmockkObject(InputOutput)
    }

    @Test
    fun `part two - comparison 8`() {
        mockkObject(InputOutput)
        every { InputOutput.input() } returns "8" // ! < 8
        every { InputOutput.output(any()) } just Runs

        IntCode(memory("3,3,1107,-1,8,3,4,3,99")).run()

        verifySequence {
            InputOutput.input()
            InputOutput.output("0")
        }
        unmockkObject(InputOutput)
    }

    @Test
    fun `part two - jump 1`() {
        mockkObject(InputOutput)
        every { InputOutput.input() } returns "0" // == 0
        every { InputOutput.output(any()) } just Runs

        IntCode(memory("3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9")).run()

        verifySequence {
            InputOutput.input()
            InputOutput.output("0")
        }
        unmockkObject(InputOutput)
    }

    @Test
    fun `part two - jump 2`() {
        mockkObject(InputOutput)
        every { InputOutput.input() } returns "9" // != 0
        every { InputOutput.output(any()) } just Runs

        IntCode(memory("3,3,1105,-1,9,1101,0,0,12,4,12,99,1")).run()

        verifySequence {
            InputOutput.input()
            InputOutput.output("1")
        }
        unmockkObject(InputOutput)
    }

    @Test
    fun `part two - larger example 1`() {
        mockkObject(InputOutput)
        every { InputOutput.input() } returns "7" // < 8
        every { InputOutput.output(any()) } just Runs

        IntCode(memory("3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104,999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99")).run()

        verifySequence {
            InputOutput.input()
            InputOutput.output("999")
        }
        unmockkObject(InputOutput)
    }

    @Test
    fun `part two - larger example 2`() {
        mockkObject(InputOutput)
        every { InputOutput.input() } returns "8" // == 8
        every { InputOutput.output(any()) } just Runs

        IntCode(memory("3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104,999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99")).run()

        verifySequence {
            InputOutput.input()
            InputOutput.output("1000")
        }
        unmockkObject(InputOutput)
    }

    @Test
    fun `part two - larger example 3`() {
        mockkObject(InputOutput)
        every { InputOutput.input() } returns "9" // < 8
        every { InputOutput.output(any()) } just Runs

        IntCode(memory("3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104,999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99")).run()

        verifySequence {
            InputOutput.input()
            InputOutput.output("1001")
        }
        unmockkObject(InputOutput)
    }

    @Test
    fun `part two`() {
        mockkObject(InputOutput)
        every { InputOutput.input() } returns "5"
        every { InputOutput.output(any()) } just Runs

        Day05.partTwo()

        verifySequence {
            InputOutput.input()
            InputOutput.output("3419022")
        }
        unmockkObject(InputOutput)
    }

}
