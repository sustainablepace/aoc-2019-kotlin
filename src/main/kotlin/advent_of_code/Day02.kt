package advent_of_code

import advent_of_code.Opcode.*

object Day02 {
    val input: String = javaClass.classLoader
        .getResource("day02_gravity_assist_program.txt")!!
        .readText()

    fun partOne(): IntCode {
        val memory = memory(input)
        memory[1] = 12
        memory[2] = 2
        return IntCode(memory).execute()
    }

    fun partTwo(): Int? {
        (0..99).forEach { noun ->
            (0..99).forEach { verb ->
                val memory = memory(input)
                memory[1] = noun
                memory[2] = verb
                if (IntCode(memory).execute().memory[0] == 19_690_720) {
                    return 100 * noun + verb
                }
            }
        }
        return null
    }
}

enum class Opcode(val value: Int) {
    ADDITION(1),
    MULTIPLICATION(2),
    TERMINATION(99);

    companion object {
        private val map = values().associateBy(Opcode::value)
        fun from(value: Int): Opcode = map.getOrDefault(value, TERMINATION)
    }
}

typealias Parameter = Int

sealed class Instruction {
    abstract val opcode: Opcode
}

class Addition(val p1: Parameter, val p2: Parameter, val p3: Parameter) : Instruction() {
    override val opcode = ADDITION
}

class Multiplication(val p1: Parameter, val p2: Parameter, val p3: Parameter) : Instruction() {
    override val opcode = MULTIPLICATION
}

class Termination : Instruction() {
    override val opcode = TERMINATION
}

typealias Address = Int
typealias InstructionPointer = Address

data class IntCode(val memory: Memory) {

    fun execute(): IntCode = execute(next()).also { i += 4 }?.execute() ?: this

    private var i: InstructionPointer = 0

    private fun memory(address: Address) = memory[address]!!

    private fun execute(instruction: Instruction) = instruction.run {
        when (this) {
            is Addition -> {
                memory[p3] = memory(p1).plus(memory(p2))
                this@IntCode
            }
            is Multiplication -> {
                memory[p3] = memory(p1).times(memory(p2))
                this@IntCode
            }
            is Termination -> null
        }
    }

    private fun next(): Instruction = when (Opcode.from(memory(i))) {
        ADDITION -> Addition(
            memory(i + 1),
            memory(i + 2),
            memory(i + 3)
        )
        MULTIPLICATION -> Multiplication(
            memory(i + 1),
            memory(i + 2),
            memory(i + 3)
        )
        TERMINATION -> Termination()
    }
}

typealias Memory = MutableMap<Int, Int>

fun memory(input: String): Memory {
    val addresses = input.split(",").mapIndexed { index, value -> index to value.toInt() }
    return mutableMapOf(*addresses.toTypedArray())
}