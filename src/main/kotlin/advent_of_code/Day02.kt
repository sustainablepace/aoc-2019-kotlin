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

typealias Address = Int
typealias Memory = MutableMap<Address, Int>

fun memory(input: String): Memory {
    val values = input.split(",").mapIndexed { address, value -> address to value.toInt() }
    return mutableMapOf(*values.toTypedArray())
}


enum class Opcode(val value: Int) {
    ADDITION(1),
    MULTIPLICATION(2),
    TERMINATION(99);

    companion object {
        private val opcodes = values().associateBy(Opcode::value)
        fun from(value: Int): Opcode = opcodes.getOrDefault(value, TERMINATION)
    }
}


sealed class Instruction {
    abstract val opcode: Opcode
}
typealias Parameter = Int

class Addition(val p1: Parameter, val p2: Parameter, val p3: Parameter) : Instruction() {
    override val opcode = ADDITION
}

class Multiplication(val p1: Parameter, val p2: Parameter, val p3: Parameter) : Instruction() {
    override val opcode = MULTIPLICATION
}

class Termination : Instruction() {
    override val opcode = TERMINATION
}


typealias InstructionPointer = Address

data class IntCode(val memory: Memory) {

    fun run(): IntCode = process(next())
        .also { i += 4 }
        ?.run() ?: this

    private var i: InstructionPointer = 0

    private fun memory(address: Address) = memory[address]!!

    private fun process(instruction: Instruction) = instruction.run {
        when (this) {
            is Addition -> {
                memory[p3] = memory(p1) + memory(p2)
                this@IntCode
            }
            is Multiplication -> {
                memory[p3] = memory(p1) * memory(p2)
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

