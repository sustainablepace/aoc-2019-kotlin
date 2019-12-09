package advent_of_code

import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class Day07Test {
    @Test
    fun `part one - program 1`() {
        val program = "3,15,3,16,1002,16,10,16,1,16,15,15,4,15,99,0,0"
        assertEquals(43210, listOf(0,1,2,3,4).permutations().bestSequentialOutput(program))
        assertEquals(43210, listOf(4,3,2,1,0).sequentialAmplify(0, program))
    }

    @Test
    fun `part one - program 2`() {
        val program = "3,31,3,32,1002,32,10,32,1001,31,-2,31,1007,31,0,33,1002,33,7,33,1,33,31,31,1,32,31,31,4,31,99,0,0,0"
        assertEquals(65210, listOf(0,1,2,3,4).permutations().bestSequentialOutput(program))
        assertEquals(65210, listOf(1,0,4,3,2).sequentialAmplify(0, program))
    }

    @Test
    fun `part one - program 3`() {
        val program = "3,23,3,24,1002,24,10,24,1002,23,-1,23,101,5,23,23,1,24,23,23,4,23,99,0,0"
        assertEquals(54321, listOf(0,1,2,3,4).permutations().bestSequentialOutput(program))
        assertEquals(54321, listOf(0,1,2,3,4).sequentialAmplify(0, program))
    }

    @Test
    fun `part one`() {
        assertEquals(359142, Day07.partOne())
    }

    @Test
    fun `part two - program 1 best`() {
        val program = "3,26,1001,26,-4,26,3,27,1002,27,2,27,1,27,26,27,4,27,1001,28,-1,28,1005,28,6,99,0,0,5"
        assertEquals(139629729, listOf(9,8,7,6,5).parallelAmplify(0, program))
    }

    @Test
    fun `part two - program 1 permutations`() {
        val program = "3,26,1001,26,-4,26,3,27,1002,27,2,27,1,27,26,27,4,27,1001,28,-1,28,1005,28,6,99,0,0,5"
        assertEquals(139629729, listOf(5,6,7,8,9).permutations().bestParallelOutput(program))
    }

    @Test
    fun `part two - program 2 best`() {
        val program = "3,52,1001,52,-5,52,3,53,1,52,56,54,1007,54,5,55,1005,55,26,1001,54,-5,54,1105,1,12,1,53,54,53,1008,54,0,55,1001,55,1,55,2,53,55,53,4,53,1001,56,-1,56,1005,56,6,99,0,0,0,0,10"
        assertEquals(18216, listOf(9,7,8,5,6).parallelAmplify(0, program))
    }

    @Test
    fun `part two - program 2 permutations`() {
        val program = "3,52,1001,52,-5,52,3,53,1,52,56,54,1007,54,5,55,1005,55,26,1001,54,-5,54,1105,1,12,1,53,54,53,1008,54,0,55,1001,55,1,55,2,53,55,53,4,53,1001,56,-1,56,1005,56,6,99,0,0,0,0,10"
        assertEquals(18216, listOf(5,6,7,8,9).permutations().bestParallelOutput(program))
    }

    @Test
    fun `part two`() {
        runBlocking {
            assertEquals(4374895, Day07.partTwo())
        }
    }
}