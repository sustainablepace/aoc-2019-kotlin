package advent_of_code

import advent_of_code.Pixel.*
import advent_of_code.domain.Program
import advent_of_code.domain.QueuedIo
import advent_of_code.domain.load

fun main(args: Array<String>) {
    println(Day09.partOne())
    println(Day09.partTwo())
}

object Day09 {

    private val boostSoftware = javaClass.classLoader
        .getResource("day09_boost.txt")!!
        .readText()

    fun partOne(): Long {
        val program = Program(boostSoftware.load(), QueuedIo().queueInput(1))
        program.compute()
        return program.io.outputQueue().last()
    }

    fun partTwo(): Long {
        val program = Program(boostSoftware.load(), QueuedIo().queueInput(2))
        program.compute()
        return program.io.outputQueue().last()
    }
}
