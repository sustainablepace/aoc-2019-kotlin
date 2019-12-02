package advent_of_code

object Day02 {
    val input: String = javaClass.classLoader.getResource("day02_gravity_assist_program.txt")!!.readText()

    fun partOne(): IntCode {
        val memory = Memory(input)
        memory[1] = 12
        memory[2] = 2
        return IntCode(memory).execute()
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
    val parameter1: Parameter,
    val parameter2: Parameter,
    val parameter3: Parameter
) : Instruction {
    override val opcode: Opcode = Opcode.ADDITION
}

class Multiplication(
    val parameter1: Parameter,
    val parameter2: Parameter,
    val parameter3: Parameter
) : Instruction {
    override val opcode: Opcode = Opcode.MULTIPLICATION
}

class Termination : Instruction {
    override val opcode: Opcode = Opcode.TERMINATION
}

typealias InstructionPointer = Int


data class IntCode(val memory: Memory) {

    private var instructionPointer: InstructionPointer = 0

    fun put(address: Int, value: Int): IntCode {
        memory[address] = value
        return this
    }

    private fun execute(instruction: Instruction) {
        when (instruction) {
            is Addition -> {
                val sum = memory[instruction.parameter1]!! + memory[instruction.parameter2]!!
                memory[instruction.parameter3] = sum
                instructionPointer += 4
            }
            is Multiplication -> {
                val product = memory[instruction.parameter1]!! * memory[instruction.parameter2]!!
                memory[instruction.parameter3] = product
                instructionPointer += 4
            }
            is Termination -> {
            }
        }
    }

    fun execute(): IntCode {
        if (instructionPointer > memory.size) {
            return this
        }
        when (Opcode.fromInt(memory[instructionPointer]!!)!!) {
            Opcode.ADDITION -> {
                execute(
                    Addition(
                        memory[instructionPointer + 1]!!,
                        memory[instructionPointer + 2]!!,
                        memory[instructionPointer + 3]!!
                    )
                )
            }
            Opcode.MULTIPLICATION -> {
                execute(
                    Multiplication(
                        memory[instructionPointer + 1]!!,
                        memory[instructionPointer + 2]!!,
                        memory[instructionPointer + 3]!!
                    )
                )
            }
            Opcode.TERMINATION -> {
                execute(Termination())
                return this
            }
        }
        return this.execute()
    }

}

typealias Memory = MutableMap<Int, Int>

fun Memory(input: String): Memory {
    val steps = input.split(",").map { it.toInt() }.mapIndexed { index, i -> index to i }
    return mutableMapOf(*steps.toTypedArray())
}
