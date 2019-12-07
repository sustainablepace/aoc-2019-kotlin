package advent_of_code.domain

typealias Address = Int
typealias Memory = MutableMap<Address, Int>
typealias Code = String

fun Code.load(): Memory {
    val values = split(",").mapIndexed { address, value -> address to value.toInt() }
    return mutableMapOf(*values.toTypedArray())
}

fun Memory.read(address: Address): Int = this[address]!!

fun Memory.read(parameter: Parameter): Int = parameter.run {
    when (mode) {
        PositionMode -> get(value)!!
        ImmediateMode -> value
    }
}

fun Memory.write(parameter: Parameter, value: Int) {
    when (parameter.mode) {
        PositionMode -> this[parameter.value] = value
        ImmediateMode -> throw IllegalArgumentException("No writes in immediate mode.")
    }
}

fun Int.toInstruction(): Instruction = FiveDigitOpcode(this).toInstruction()

data class FiveDigitOpcode(val opcode: Int) {
    private val paddedOpcode = opcode.toString().padStart(5, '0')

    private val twoDigitOpcode: Int
        get() = paddedOpcode.substring(4).toInt(10)

    fun parameterMode(index: Int): ParameterMode {
        return if (index in (0..2)) {
            when (paddedOpcode[2 - index].toString().toInt()) {
                PositionMode.value -> PositionMode
                ImmediateMode.value -> ImmediateMode
                else -> PositionMode
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
        99 -> Termination(this)
        else -> Termination(this)
    }
}

data class Parameter(val value: Int, val mode: ParameterMode)

sealed class ParameterMode {
    abstract val value: Int
}

object PositionMode : ParameterMode() {
    override val value: Int = 0
}

object ImmediateMode : ParameterMode() {
    override val value: Int = 1
}

sealed class Instruction(private val fiveDigitOpcode: FiveDigitOpcode) {
    protected abstract val numParams: Int
    abstract fun execute(memory: Memory, instructionPointer: InstructionPointer, io: Io): InstructionPointer

    protected fun parameters(memory: Memory, i: InstructionPointer): List<Parameter> =
        generateSequence(0) { it + 1 }.take(numParams).map { index ->
            Parameter(memory[index + i + 1]!!, fiveDigitOpcode.parameterMode(index))
        }.toList()
}

class Addition(fiveDigitOpcode: FiveDigitOpcode) : Instruction(fiveDigitOpcode) {
    override val numParams = 3
    override fun execute(memory: Memory, instructionPointer: InstructionPointer, io: Io): InstructionPointer {
        val p = parameters(memory, instructionPointer)
        memory.write(p[2], memory.read(p[0]) + memory.read(p[1]))
        return instructionPointer + numParams + 1
    }
}

class Multiplication(fiveDigitOpcode: FiveDigitOpcode) : Instruction(fiveDigitOpcode) {
    override val numParams = 3
    override fun execute(memory: Memory, instructionPointer: InstructionPointer, io: Io): InstructionPointer {
        val p = parameters(memory, instructionPointer)
        memory.write(p[2], memory.read(p[0]) * memory.read(p[1]))
        return instructionPointer + numParams + 1
    }
}

class Input(fiveDigitOpcode: FiveDigitOpcode) : Instruction(fiveDigitOpcode) {
    override val numParams = 1
    override fun execute(memory: Memory, instructionPointer: InstructionPointer, io: Io): InstructionPointer {
        val p = parameters(memory, instructionPointer)
        val input = io.input()!!.toInt()
        memory.write(p[0], input)
        return instructionPointer + numParams + 1
    }
}

class Output(fiveDigitOpcode: FiveDigitOpcode) : Instruction(fiveDigitOpcode) {
    override val numParams = 1
    override fun execute(memory: Memory, instructionPointer: InstructionPointer, io: Io): InstructionPointer {
        val p = parameters(memory, instructionPointer)
        io.output(memory.read(p[0]).toString())
        return instructionPointer + numParams + 1
    }
}

class LessThan(fiveDigitOpcode: FiveDigitOpcode) : Instruction(fiveDigitOpcode) {
    override val numParams = 3
    override fun execute(memory: Memory, instructionPointer: InstructionPointer, io: Io): InstructionPointer {
        val p = parameters(memory, instructionPointer)
        memory.write(p[2], if (memory.read(p[0]) < memory.read(p[1])) 1 else 0)
        return instructionPointer + numParams + 1
    }
}

class Equals(fiveDigitOpcode: FiveDigitOpcode) : Instruction(fiveDigitOpcode) {
    override val numParams = 3
    override fun execute(memory: Memory, instructionPointer: InstructionPointer, io: Io): InstructionPointer {
        val p = parameters(memory, instructionPointer)
        memory.write(p[2], if (memory.read(p[0]) == memory.read(p[1])) 1 else 0)
        return instructionPointer + numParams + 1
    }
}

class JumpIfTrue(fiveDigitOpcode: FiveDigitOpcode) : Instruction(fiveDigitOpcode) {
    override val numParams = 2
    override fun execute(memory: Memory, instructionPointer: InstructionPointer, io: Io): InstructionPointer {
        val p = parameters(memory, instructionPointer)
        return if (memory.read(p[0]) != 0) memory.read(p[1]) else instructionPointer + numParams + 1
    }
}

class JumpIfFalse(fiveDigitOpcode: FiveDigitOpcode) : Instruction(fiveDigitOpcode) {
    override val numParams = 2
    override fun execute(memory: Memory, instructionPointer: InstructionPointer, io: Io): InstructionPointer {
        val p = parameters(memory, instructionPointer)
        return if (memory.read(p[0]) == 0) memory.read(p[1]) else instructionPointer + numParams + 1
    }
}

class Termination(fiveDigitOpcode: FiveDigitOpcode) : Instruction(fiveDigitOpcode) {
    override val numParams = 0
    override fun execute(memory: Memory, instructionPointer: InstructionPointer, io: Io): InstructionPointer {
        return instructionPointer
    }
}


typealias InstructionPointer = Address

interface Io {
    fun input(): String?
    fun output(line: String)
    fun outputs(): List<String>
}

object CommandLineIo : Io {
    override fun input(): String? = readLine()
    override fun output(line: String) = println(line)
    override fun outputs() = listOf("")
}

data class Program(val memory: Memory, val io: Io = CommandLineIo) {

    private var instructionPointer: InstructionPointer = 0

    fun compute(): Program = memory.read(instructionPointer).toInstruction().let { instruction ->
        if (instruction is Termination) null else {
            instructionPointer = instruction.execute(memory, instructionPointer, io)
            this
        }
    }?.compute() ?: this

}