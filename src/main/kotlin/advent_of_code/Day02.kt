package advent_of_code

import advent_of_code.domain.IntCode
import advent_of_code.domain.memory


object Day02 {
    val input: String = javaClass.classLoader
        .getResource("day02_gravity_assist_program.txt")!!
        .readText()


    fun partOne(): IntCode {
        val memory = memory(input)
        memory[1] = 12
        memory[2] = 2
        return IntCode(memory).run()
    }

    fun partTwo(): Int? {
        (0..99).forEach { noun ->
            (0..99).forEach { verb ->
                val memory = memory(input)
                memory[1] = noun
                memory[2] = verb
                if (IntCode(memory).run().memory[0] == 19_690_720) {
                    return 100 * noun + verb
                }
            }
        }
        return null
    }
}

