package advent_of_code

import advent_of_code.domain.Program
import advent_of_code.domain.Code
import advent_of_code.domain.load


fun main(args: Array<String>) {
    println(Day02.partOne())
    println(Day02.partTwo())
}

object Day02 {
    val input: Code = javaClass.classLoader
        .getResource("day02_gravity_assist_program.txt")!!
        .readText()

    fun partOne(): Program {
        val memory = input.load()
        memory[1] = 12
        memory[2] = 2
        return Program(memory).compute()
    }

    fun partTwo(): Int? {
        (0..99).forEach { noun ->
            (0..99).forEach { verb ->
                val memory = input.load()
                memory[1] = noun
                memory[2] = verb
                if (Program(memory).compute().memory[0] == 19_690_720) {
                    return 100 * noun + verb
                }
            }
        }
        return null
    }
}

