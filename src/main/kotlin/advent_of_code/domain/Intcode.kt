package advent_of_code.domain

import advent_of_code.domain.ParameterMode.IMMEDIATE
import advent_of_code.domain.ParameterMode.POSITION
import advent_of_code.domain.TwoDigitOpcode.*

typealias Address = Int
typealias Memory = MutableMap<Address, Int>

fun memory(input: String): Memory {
    val values = input.split(",").mapIndexed { address, value -> address to value.toInt() }
    return mutableMapOf(*values.toTypedArray())
}


enum class TwoDigitOpcode(val value: Int) {
    ADDITION(1),
    MULTIPLICATION(2),
    INPUT(3),
    OUTPUT(4),
    JUMP_IF_TRUE(5),
    JUMP_IF_FALSE(6),
    LESS_THAN(7),
    EQUALS(8),
    TERMINATION(99);

    companion object {
        private val opcodes = values().associateBy(TwoDigitOpcode::value)
        fun from(value: Int): TwoDigitOpcode = opcodes.getOrDefault(value, TERMINATION)
    }
}

data class FiveDigitOpcode(val opcode: Int) {
    private val paddedOpcode = opcode.toString().padStart(5, '0')

    val twoDigitOpcode: TwoDigitOpcode
        get() = Companion.from(paddedOpcode.substring(4).toInt(10))

    fun parameterMode(index: Int): ParameterMode {
        return if (index in (0..2)) ParameterMode.from(paddedOpcode[2 - index].toString().toInt()) else POSITION
    }
}

sealed class Instruction {
    abstract val twoDigitOpcode: TwoDigitOpcode
}

enum class ParameterMode(val mode: Int) {
    POSITION(0),
    IMMEDIATE(1);

    companion object {
        private val modes = values().associateBy(ParameterMode::mode)
        fun from(mode: Int): ParameterMode = modes.getOrDefault(mode, POSITION)
    }
}

data class Parameter(val value: Int, val mode: ParameterMode)

class Addition(val p1: Parameter, val p2: Parameter, val p3: Parameter) : Instruction() {
    override val twoDigitOpcode = ADDITION
}

class Multiplication(val p1: Parameter, val p2: Parameter, val p3: Parameter) : Instruction() {
    override val twoDigitOpcode = MULTIPLICATION
}

class Input(val p1: Parameter) : Instruction() {
    override val twoDigitOpcode = INPUT
}

class Output(val p1: Parameter) : Instruction() {
    override val twoDigitOpcode = OUTPUT
}

class LessThan(val p1: Parameter, val p2: Parameter, val p3: Parameter) : Instruction() {
    override val twoDigitOpcode = LESS_THAN
}

class Equals(val p1: Parameter, val p2: Parameter, val p3: Parameter) : Instruction() {
    override val twoDigitOpcode = EQUALS
}

class JumpIfTrue(val p1: Parameter, val p2: Parameter) : Instruction() {
    override val twoDigitOpcode = JUMP_IF_TRUE
}

class JumpIfFalse(val p1: Parameter, val p2: Parameter) : Instruction() {
    override val twoDigitOpcode = JUMP_IF_FALSE
}

class Termination : Instruction() {
    override val twoDigitOpcode = TERMINATION
}


typealias InstructionPointer = Address

object InputOutput {
    fun input(): String? = readLine()
    fun output(line: String) = println(line)
}

data class IntCode(val memory: Memory) {

    fun run(): IntCode = next(memory).run {
        process(this, memory)
    }?.run() ?: this

    private var i: InstructionPointer = 0

    private fun memory(address: Address) = memory[address]!!

    private fun getValueFromParameter(memory: Memory, parameter: Parameter): Int = when (parameter.mode) {
        POSITION -> memory(parameter.value)
        IMMEDIATE -> parameter.value
    }

    private fun process(instruction: Instruction, memory: Memory) = instruction.run {
        when (this) {
            is Addition -> {
                memory[p3.value] = getValueFromParameter(memory, p1) + getValueFromParameter(memory, p2)
                i += 4
                this@IntCode
            }
            is Multiplication -> {
                memory[p3.value] = getValueFromParameter(memory, p1) * getValueFromParameter(memory, p2)
                i += 4
                this@IntCode
            }
            is Input -> {
                val input = InputOutput.input()!!.toInt()
                memory[p1.value] = input
                i += 2
                this@IntCode
            }
            is Output -> {
                InputOutput.output(getValueFromParameter(memory, p1).toString())
                i += 2
                this@IntCode
            }
            is Termination -> null
            is LessThan -> {
                memory[p3.value] = if (getValueFromParameter(memory, p1) < getValueFromParameter(memory, p2)) 1 else 0
                i += 4
                this@IntCode
            }
            is Equals -> {
                memory[p3.value] = if (getValueFromParameter(memory, p1) == getValueFromParameter(memory, p2)) 1 else 0
                i += 4
                this@IntCode
            }
            is JumpIfTrue -> {
                i = if (getValueFromParameter(memory, p1) != 0) getValueFromParameter(memory, p2) else i+3
                this@IntCode
            }
            is JumpIfFalse -> {
                i = if (getValueFromParameter(memory, p1) == 0) getValueFromParameter(memory, p2) else i+3
                this@IntCode
            }
        }
    }

    private fun next(memory: Memory): Instruction {
        val fiveDigitOpcode = FiveDigitOpcode(memory(i))
        return when (fiveDigitOpcode.twoDigitOpcode) {
            ADDITION -> Addition(
                Parameter(memory(i + 1), fiveDigitOpcode.parameterMode(0)),
                Parameter(memory(i + 2), fiveDigitOpcode.parameterMode(1)),
                Parameter(memory(i + 3), fiveDigitOpcode.parameterMode(2))
            )
            MULTIPLICATION -> Multiplication(
                Parameter(
                    memory(i + 1), fiveDigitOpcode.parameterMode(0)
                ),
                Parameter(
                    memory(i + 2), fiveDigitOpcode.parameterMode(1)
                ),
                Parameter(
                    memory(i + 3), fiveDigitOpcode.parameterMode(2)
                )
            )
            INPUT -> Input(Parameter(memory(i + 1), fiveDigitOpcode.parameterMode(0)))
            OUTPUT -> Output(Parameter(memory(i + 1), fiveDigitOpcode.parameterMode(0)))
            JUMP_IF_TRUE -> JumpIfTrue(
                Parameter(
                    memory(i + 1), fiveDigitOpcode.parameterMode(0)
                ),
                Parameter(
                    memory(i + 2), fiveDigitOpcode.parameterMode(1)
                )
            )
            JUMP_IF_FALSE -> JumpIfFalse(
                Parameter(
                    memory(i + 1), fiveDigitOpcode.parameterMode(0)
                ),
                Parameter(
                    memory(i + 2), fiveDigitOpcode.parameterMode(1)
                )
            )
            LESS_THAN -> LessThan(
                Parameter(
                    memory(i + 1), fiveDigitOpcode.parameterMode(0)
                ),
                Parameter(
                    memory(i + 2), fiveDigitOpcode.parameterMode(1)
                ),
                Parameter(
                    memory(i + 3), fiveDigitOpcode.parameterMode(2)
                )
            )
            EQUALS -> Equals(
                Parameter(
                    memory(i + 1), fiveDigitOpcode.parameterMode(0)
                ),
                Parameter(
                    memory(i + 2), fiveDigitOpcode.parameterMode(1)
                ),
                Parameter(
                    memory(i + 3), fiveDigitOpcode.parameterMode(2)
                )
            )
            TERMINATION -> Termination()
        }
    }
}

