package advent_of_code

object Day02 {
    private val input: String = javaClass.classLoader.getResource("day02_gravity_assist_program.txt")!!.readText()

    fun partOne() : Intcode {
        return Intcode(input).set(1, 12).set(2, 2).execute()
    }
}

enum class Opcode(val number: Int) {
    ADDITION(1),
    MULTIPLICATION(2),
    TERMINATION(99);

    companion object {
        private val map = Opcode.values().associateBy(Opcode::number)

        fun fromInt(number: Int) = map[number]
    }
}

data class Intcode(val input: String, val counter: Int = 0) {

    private val code: MutableMap<Int, Int>

    init {
        val steps = input.split(",").map { it.toInt() }.mapIndexed { index, i -> index to i }
        code = mutableMapOf(*steps.toTypedArray())
    }

    fun set(index: Int, value: Int) : Intcode {
        code[index] = value
        return this
    }

    fun execute(): Intcode {
        if(counter > code.size) {
            return this
        }
        val opcode = Opcode.fromInt(code[counter]!!)
        when(opcode) {
            Opcode.ADDITION -> {
                val index1 = code[counter+1]!!
                val index2 = code[counter+2]!!
                val sum = code[index1]!! + code[index2]!!
                val index3 = code[counter+3]!!
                code[index3] = sum
            }
            Opcode.MULTIPLICATION -> {
                val index1 = code[counter+1]!!
                val index2 = code[counter+2]!!
                val product = code[index1]!! * code[index2]!!
                val index3 = code[counter+3]!!
                code[index3] = product
            }
            Opcode.TERMINATION -> {
                return this
            }
        }
        return Intcode(code.map { it.value }.joinToString(","), counter + 4).execute()
    }
}
