package advent_of_code.domain

import java.io.IOException

typealias Address = Long
typealias Memory = MutableMap<Address, Long>
typealias Code = String

fun Code.load(): Memory {
    val values = split(",").mapIndexed { address, value -> address.toLong() to value.toLong() }
    return mutableMapOf(*values.toTypedArray())
}

fun Memory.read(address: Address): Long = this[address] ?: 0

fun Memory.read(parameter: Parameter): Long = parameter.run {
    when (mode) {
        PositionMode -> get(value) ?: 0
        ImmediateMode -> value
        RelativeMode -> get(value + relativeBase) ?: 0
    }
}

fun Memory.write(parameter: Parameter, v: Long) = parameter.run {
    when (mode) {
        PositionMode -> this@write[value] = v
        RelativeMode -> this@write[value + relativeBase] = v
        ImmediateMode -> throw IllegalArgumentException("No writes in immediate mode.")
    }
}

fun Long.toInstruction(): Instruction = FiveDigitOpcode(this).toInstruction()

data class FiveDigitOpcode(val opcode: Long) {
    val paddedOpcode = opcode.toString().padStart(5, '0')

    private val twoDigitOpcode: Int
        get() = paddedOpcode.substring(3).toInt(10)

    fun parameterMode(index: Int): ParameterMode {
        return if (index in (0..2)) {
            when (paddedOpcode[2 - index].toString().toInt()) {
                PositionMode.value -> PositionMode
                ImmediateMode.value -> ImmediateMode
                RelativeMode.value -> RelativeMode
                else -> throw IllegalArgumentException("Invalid mode.")
            }
        } else PositionMode
    }

    fun toInstruction() = when (twoDigitOpcode) {
        1 -> Addition(this)
        2 -> Multiplication(this)
        3 -> Input(this)
        4 -> Output(this)
        5 -> JumpIfTrue(this)
        6 -> JumpIfFalse(this)
        7 -> LessThan(this)
        8 -> Equals(this)
        9 -> AdjustRelativeBase(this)
        99 -> Termination(this)
        else -> Termination(this)
    }
}

data class Parameter(val value: Long, val mode: ParameterMode, val relativeBase: RelativeBase)

sealed class ParameterMode {
    abstract val value: Int
}

object PositionMode : ParameterMode() {
    override val value: Int = 0
}

object ImmediateMode : ParameterMode() {
    override val value: Int = 1
}

object RelativeMode : ParameterMode() {
    override val value: Int = 2
}

sealed class Instruction(val fiveDigitOpcode: FiveDigitOpcode) {
    protected abstract val numParams: Int
    abstract fun execute(memory: Memory, state: ProgramState, io: Io): ProgramState

    protected fun parameters(memory: Memory, state: ProgramState): List<Parameter> =
        generateSequence(0) { it + 1 }.take(numParams).map { index ->
            Parameter(
                memory[index + state.instructionPointer + 1]!!,
                fiveDigitOpcode.parameterMode(index),
                state.relativeBase
            )
        }.toList()
}

class Addition(fiveDigitOpcode: FiveDigitOpcode) : Instruction(fiveDigitOpcode) {
    override val numParams = 3
    override fun execute(memory: Memory, state: ProgramState, io: Io): ProgramState {
        val p = parameters(memory, state)
        memory.write(p[2], memory.read(p[0]) + memory.read(p[1]))
        return ProgramState(state.instructionPointer + numParams + 1, state.relativeBase)
    }
}

class Multiplication(fiveDigitOpcode: FiveDigitOpcode) : Instruction(fiveDigitOpcode) {
    override val numParams = 3
    override fun execute(memory: Memory, state: ProgramState, io: Io): ProgramState {
        val p = parameters(memory, state)
        memory.write(p[2], memory.read(p[0]) * memory.read(p[1]))
        return ProgramState(state.instructionPointer + numParams + 1, state.relativeBase)
    }
}

class Input(fiveDigitOpcode: FiveDigitOpcode) : Instruction(fiveDigitOpcode) {
    override val numParams = 1
    override fun execute(memory: Memory, state: ProgramState, io: Io): ProgramState {
        val p = parameters(memory, state)
        val input = io.input()!!.toLong()
        memory.write(p[0], input)
        return ProgramState(state.instructionPointer + numParams + 1, state.relativeBase)
    }
}

class Output(fiveDigitOpcode: FiveDigitOpcode) : Instruction(fiveDigitOpcode) {
    override val numParams = 1
    override fun execute(memory: Memory, state: ProgramState, io: Io): ProgramState {
        val p = parameters(memory, state)
        io.output(memory.read(p[0]))
        return ProgramState(state.instructionPointer + numParams + 1, state.relativeBase)
    }
}

class LessThan(fiveDigitOpcode: FiveDigitOpcode) : Instruction(fiveDigitOpcode) {
    override val numParams = 3
    override fun execute(memory: Memory, state: ProgramState, io: Io): ProgramState {
        val p = parameters(memory, state)
        memory.write(p[2], if (memory.read(p[0]) < memory.read(p[1])) 1 else 0)
        return ProgramState(state.instructionPointer + numParams + 1, state.relativeBase)
    }
}

class Equals(fiveDigitOpcode: FiveDigitOpcode) : Instruction(fiveDigitOpcode) {
    override val numParams = 3
    override fun execute(memory: Memory, state: ProgramState, io: Io): ProgramState {
        val p = parameters(memory, state)
        memory.write(p[2], if (memory.read(p[0]) == memory.read(p[1])) 1 else 0)
        return ProgramState(state.instructionPointer + numParams + 1, state.relativeBase)
    }
}

class JumpIfTrue(fiveDigitOpcode: FiveDigitOpcode) : Instruction(fiveDigitOpcode) {
    override val numParams = 2
    override fun execute(memory: Memory, state: ProgramState, io: Io): ProgramState {
        val p = parameters(memory, state)
        return ProgramState(
            if (memory.read(p[0]) != 0L) memory.read(p[1]) else state.instructionPointer + numParams + 1,
            state.relativeBase
        )
    }
}

class JumpIfFalse(fiveDigitOpcode: FiveDigitOpcode) : Instruction(fiveDigitOpcode) {
    override val numParams = 2
    override fun execute(memory: Memory, state: ProgramState, io: Io): ProgramState {
        val p = parameters(memory, state)
        return ProgramState(
            if (memory.read(p[0]) == 0L) memory.read(p[1]) else state.instructionPointer + numParams + 1,
            state.relativeBase
        )
    }
}

class AdjustRelativeBase(fiveDigitOpcode: FiveDigitOpcode) : Instruction(fiveDigitOpcode) {
    override val numParams = 1
    override fun execute(memory: Memory, state: ProgramState, io: Io): ProgramState {
        val p = parameters(memory, state)
        return ProgramState(state.instructionPointer + numParams + 1, state.relativeBase + memory.read(p[0]))
    }
}

class Termination(fiveDigitOpcode: FiveDigitOpcode) : Instruction(fiveDigitOpcode) {
    override val numParams = 0
    override fun execute(memory: Memory, state: ProgramState, io: Io): ProgramState {
        return state
    }
}


typealias InstructionPointer = Address
typealias RelativeBase = Address

interface Io {
    fun queueInput(line: Long): Io
    fun input(): Long?
    fun output(line: Long)
    fun outputQueue(): List<Long>
}

object CommandLineIo : Io {
    override fun queueInput(line: Long): Io = this
    override fun input(): Long? = readLine()?.toLong()
    override fun output(line: Long) = println(line)
    override fun outputQueue(): List<Long> = listOf<Long>()
}

open class QueuedIo : Io {
    private val inputQueue = mutableListOf<Long>()
    private val outputQueue = mutableListOf<Long>()
    override fun queueInput(line: Long): Io {
        inputQueue.add(line)
        return this
    }

    override fun input(): Long? {
        if (inputQueue.size > 0) {
            return inputQueue.removeAt(0)
        } else throw IOException("Insufficient input.")
    }

    override fun output(line: Long) {
        outputQueue.add(line)
    }

    override fun outputQueue(): List<Long> {
        return outputQueue
    }

}

data class ProgramState(val instructionPointer: InstructionPointer, val relativeBase: RelativeBase)

data class Program(val memory: Memory, val io: Io = CommandLineIo, val name: String? = null) {

    private var state: ProgramState = ProgramState(0, 0)

    fun isTerminated(): Boolean = memory.read(state.instructionPointer).toInstruction() is Termination

    fun compute(): Program {
        while (memory.read(state.instructionPointer).toInstruction().let { instruction ->
            if (instruction is Termination) false else {
                try {
                    state = instruction.execute(memory, state, io)
                    true
                } catch (e: IOException) {
                    false
                }
            }
        }) {}
        return this
    }
}