package advent_of_code.domain

import advent_of_code.domain.ParameterMode.IMMEDIATE
import advent_of_code.domain.ParameterMode.POSITION

typealias Address = Int
typealias Memory = MutableMap<Address, Int>

fun memory(input: String): Memory {
    val values = input.split(",").mapIndexed { address, value -> address to value.toInt() }
    return mutableMapOf(*values.toTypedArray())
}

data class FiveDigitOpcode(val opcode: Int) {
    private val paddedOpcode = opcode.toString().padStart(5, '0')

    val twoDigitOpcode: Int
        get() = paddedOpcode.substring(4).toInt(10)

    fun parameterMode(index: Int): ParameterMode {
        return if (index in (0..2)) ParameterMode.from(paddedOpcode[2 - index].toString().toInt()) else POSITION
    }
}

sealed class Instruction(val p: List<Parameter>)

data class Parameter(val value: Int, val mode: ParameterMode)

enum class ParameterMode(val mode: Int) {
    POSITION(0),
    IMMEDIATE(1);

    companion object {
        private val modes = values().associateBy(ParameterMode::mode)
        fun from(mode: Int): ParameterMode = modes.getOrDefault(mode, POSITION)
    }
}

class Addition(p: List<Parameter>) : Instruction(p) {
    companion object {
        const val numParams = 3
    }
}

class Multiplication(p: List<Parameter>) : Instruction(p) {
    companion object {
        const val numParams = 3
    }
}

class Input(p: List<Parameter>) : Instruction(p) {
    companion object {
        const val numParams = 1
    }
}

class Output(p: List<Parameter>) : Instruction(p) {
    companion object {
        const val numParams = 1
    }
}

class LessThan(p: List<Parameter>) : Instruction(p) {
    companion object {
        const val numParams = 3
    }
}

class Equals(p: List<Parameter>) : Instruction(p) {
    companion object {
        const val numParams = 3
    }
}

class JumpIfTrue(p: List<Parameter>) : Instruction(p) {
    companion object {
        const val numParams = 2
    }
}

class JumpIfFalse(p: List<Parameter>) : Instruction(p) {
    companion object {
        const val numParams = 2
    }
}

class Termination(p: List<Parameter>) : Instruction(p) {
    companion object {
        const val numParams = 0
    }
}


typealias InstructionPointer = Address

object InputOutput {
    fun input(): String? = readLine()
    fun output(line: String) = println(line)
}

data class IntCode(val memory: Memory) {

    fun run(): IntCode = next(FiveDigitOpcode(memory(i))).run {
        process(this, memory)
    }?.run() ?: this

    private var i: InstructionPointer = 0

    private fun memory(address: Address) = memory[address]!!

    private fun modalValue(parameter: Parameter): Int = parameter.run {
        when (mode) {
            POSITION -> memory(value)
            IMMEDIATE -> value
        }
    }

    private fun process(instruction: Instruction, memory: Memory) = instruction.run {
        when (this) {
            is Addition -> {
                memory[p[2].value] = modalValue(p[0]) + modalValue(p[1])
                i += Addition.numParams + 1
                this@IntCode
            }
            is Multiplication -> {
                memory[p[2].value] = modalValue(p[0]) * modalValue(p[1])
                i += Multiplication.numParams + 1
                this@IntCode
            }
            is Input -> {
                val input = InputOutput.input()!!.toInt()
                memory[p[0].value] = input
                i += Input.numParams + 1
                this@IntCode
            }
            is Output -> {
                InputOutput.output(modalValue(p[0]).toString())
                i += Output.numParams + 1
                this@IntCode
            }
            is Termination -> null
            is LessThan -> {
                memory[p[2].value] = if (modalValue(p[0]) < modalValue(p[1])) 1 else 0
                i += LessThan.numParams + 1
                this@IntCode
            }
            is Equals -> {
                memory[p[2].value] = if (modalValue(p[0]) == modalValue(p[1])) 1 else 0
                i += Equals.numParams + 1
                this@IntCode
            }
            is JumpIfTrue -> {
                i = if (modalValue(p[0]) != 0) modalValue(p[1]) else i + JumpIfTrue.numParams + 1
                this@IntCode
            }
            is JumpIfFalse -> {
                i = if (modalValue(p[0]) == 0) modalValue(p[1]) else i + JumpIfFalse.numParams + 1
                this@IntCode
            }
        }
    }

    private fun parameters(num: Int, fiveDigitOpcode: FiveDigitOpcode): List<Parameter> =
        generateSequence(0) { it + 1 }.take(num).map {
            Parameter(memory(i + it + 1), fiveDigitOpcode.parameterMode(it))
        }.toList()


    private fun next(fiveDigitOpcode: FiveDigitOpcode): Instruction = when (fiveDigitOpcode.twoDigitOpcode) {
        1 -> Addition(parameters(Addition.numParams, fiveDigitOpcode))
        2 -> Multiplication(parameters(Multiplication.numParams, fiveDigitOpcode))
        3 -> Input(parameters(Input.numParams, fiveDigitOpcode))
        4 -> Output(parameters(Output.numParams, fiveDigitOpcode))
        5 -> JumpIfTrue(parameters(JumpIfTrue.numParams, fiveDigitOpcode))
        6 -> JumpIfFalse(parameters(JumpIfFalse.numParams, fiveDigitOpcode))
        7 -> LessThan(parameters(LessThan.numParams, fiveDigitOpcode))
        8 -> Equals(parameters(Equals.numParams, fiveDigitOpcode))
        else -> Termination(parameters(Termination.numParams, fiveDigitOpcode))
    }
}