package advent_of_code

import advent_of_code.domain.Program
import advent_of_code.domain.Code
import advent_of_code.domain.load

fun main(args: Array<String>) {
    println(Day05.partOne())
    println(Day05.partTwo())
}

object Day05 {

    private val input: Code = javaClass.classLoader
        .getResource("day05_test.txt")!!
        .readText()

    fun partOne(): Unit {
        Program(input.load()).compute()
    }

    fun partTwo(): Unit {
        Program(input.load()).compute()
    }
}

