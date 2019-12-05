package advent_of_code

import advent_of_code.domain.IntCode
import advent_of_code.domain.memory

object Day05 {

    private val input: String = javaClass.classLoader
        .getResource("day05_test.txt")!!
        .readText()

    fun partOne(): Unit {
        IntCode(memory(input)).run()
    }

    fun partTwo(): Unit {
        IntCode(memory(input)).run()
    }
}

