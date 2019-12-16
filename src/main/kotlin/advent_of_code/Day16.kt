package advent_of_code

import kotlin.math.abs

fun main(args: Array<String>) {
    println(Day16.partOne())
    println(Day16.partTwo())
}

object Day16 {
    private val signal = javaClass.classLoader
        .getResource("day16_signal.txt")!!
        .readText()

    fun partOne(): String = signal.transform(100).substring(0, 8)

    fun partTwo(): String  = signal.transformWithRepetitionAndOffset(100)
}

typealias InputSignal = String

val pattern = listOf(0, 1, 0, -1)

fun patternForI(i: Int, l: Int): List<Int> {
    val p = mutableListOf<Int>()
    repeat(l) { j ->
        p.add(pattern[((j + 1) / (i + 1)) % pattern.size])
    }
    return p.toList()
}

fun patternAlternative(i: Int): Sequence<Int> = generateSequence(0) { it +1 }.map {
    pattern[((it + 1) / (i + 1)) % pattern.size]
}

fun InputSignal.transformWithRepetitionAndOffset(numPhases: Int) : InputSignal {
    val data = map { it.toString().toInt() }.toIntArray()


    val transformedSignal = transform(numPhases, data)
    val offset = transformedSignal.substring(0, 7).toInt()
    return transformedSignal.substring(offset+1, offset+1+8)
}

fun transform(numPhases: Int, data: IntArray): InputSignal {
    val transformedData = (0 until 10000 * data.size).map { i ->
        val seq = generateSequence(0) { it + 1 }.map { data[it%data.size] * pattern[((it + 1) / (i + 1)) % pattern.size]}
        abs(seq.take(10000 * data.size).sum() % 10)
    }.joinToString("")
    return if (numPhases == 1) {
        transformedData
    } else {
        transformedData.transform(numPhases - 1)
    }
}

fun InputSignal.transform(numPhases: Int): InputSignal {
    val data = map { it.toString().toInt() }
    val transformedData = data.mapIndexed { i, _ ->
        abs(patternAlternative(i).take(data.size).mapIndexed { index, digit ->
            digit * data[index]
        }.sum() % 10)
    }.joinToString("")
    return if (numPhases == 1) {
        transformedData
    } else {
        transformedData.transform(numPhases - 1)
    }
}