package advent_of_code

import advent_of_code.Opcode.*

object Day02 {
    val input: String = javaClass.classLoader.getResource("day02_gravity_assist_program.txt")!!.readText()

    fun partOne(): IntCode {
        val memory = memory(input)
        memory[1] = 12
        memory[2] = 2
        return IntCode(memory).execute()
    }

    fun partTwo(): Int? {
        for(noun in 0..99)  {
            for(verb in 0..99) {
                val memory = memory(input)
                memory[1] = noun
                memory[2] = verb
                val result = IntCode(memory).execute().memory[0]!!
                if(result == 19690720) {
                    return 100 * noun + verb
                }
            }
        }
        return null
    }
}

enum class Opcode(val number: Int) {
    ADDITION(1),
    MULTIPLICATION(2),
    TERMINATION(99);

    companion object {
        private val map = values().associateBy(Opcode::number)

        fun fromInt(number: Int) = map[number]
    }
}

interface Instruction {
    val opcode: Opcode
}

typealias Parameter = Int

class Addition(
    val param1: Parameter,
    val param2: Parameter,
    val param3: Parameter
) : Instruction {
    override val opcode: Opcode = ADDITION
}

class Multiplication(
    val param1: Parameter,
    val param2: Parameter,
    val param3: Parameter
) : Instruction {
    override val opcode: Opcode = MULTIPLICATION
}

class Termination : Instruction {
    override val opcode: Opcode = TERMINATION
}


typealias InstructionPointer = Int

data class IntCode(val memory: Memory) {

    private var i: InstructionPointer = 0

    private fun execute(instruction: Instruction) {
        instruction.run {
            when (this) {
                is Addition -> {
                    memory[param3] = memory.fetch(param1).plus(memory.fetch(param2))
                    i += 4
                }
                is Multiplication -> {
                    memory[param3] = memory.fetch(param1).times(memory.fetch(param2))
                    i += 4
                }
                is Termination -> {
                }
            }
        }
    }

    fun execute(): IntCode {
        when (Opcode.fromInt(memory.fetch(i))) {
            ADDITION -> {
                execute(
                    Addition(
                        memory.fetch(i + 1),
                        memory.fetch(i + 2),
                        memory.fetch(i + 3)
                    )
                )
            }
            MULTIPLICATION -> {
                execute(
                    Multiplication(
                        memory.fetch(i + 1),
                        memory.fetch(i + 2),
                        memory.fetch(i + 3)
                    )
                )
            }
            TERMINATION -> {
                execute(Termination())
                return this
            }
        }
        return this.execute()
    }

}

typealias Memory = MutableMap<Int, Int>

class InvalidMemoryException(msg: String) : RuntimeException(msg)

fun Memory.fetch(address: Int) = this[address] ?: throw InvalidMemoryException("Invalid memory.")

fun memory(input: String): Memory {
    val addresses = input.split(",").map { it.toInt() }.mapIndexed { index, i -> index to i }
    return mutableMapOf(*addresses.toTypedArray())
}