package advent_of_code

import advent_of_code.domain.Code
import advent_of_code.domain.Program
import advent_of_code.domain.QueuedIo
import advent_of_code.domain.load

fun main() {
    println(Day07.partOne())
    println(Day07.partTwo())
}

object Day07 {

    private val amplifierControllerSoftware = javaClass.classLoader
        .getResource("day07_thrusters.txt")!!
        .readText()

    fun partOne(): Signal = listOf(0, 1, 2, 3, 4).permutations().bestSequentialOutput(amplifierControllerSoftware)

    fun partTwo(): Signal = listOf(5, 6, 7, 8, 9).permutations().bestParallelOutput(amplifierControllerSoftware)
}

typealias Signal = Long
typealias PhaseSetting = Int
typealias PhaseSettingSequence = List<PhaseSetting>

fun PhaseSettingSequence.sequentialAmplify(inputSignal: Signal, code: Code): Signal {
    var previousOutputSignal = inputSignal
    forEach { phaseSetting ->
        val io = QueuedIo().queueInput(phaseSetting.toLong()) as QueuedIo
        val amplifier = Amplifier(code, io)
        previousOutputSignal = amplifier.amplify(previousOutputSignal)!!
    }
    return previousOutputSignal
}

typealias AmplifierCluster = List<Amplifier>

fun AmplifierCluster.isRunning(): Boolean = any { !it.isTerminated() }

fun PhaseSettingSequence.parallelAmplify(inputSignal: Signal, code: Code): Signal {
    val ioList: List<SubscriptionIo> = this.map { phaseSetting ->
        SubscriptionIo().queueInput(phaseSetting.toLong()) as SubscriptionIo
    }
    val pairs = ioList.toMutableList().also { it.add(ioList.first()) }.zipWithNext()
    pairs.forEach { pair ->
        pair.first.addSubscriber(pair.second)
    }
    ioList.first().queueInput(inputSignal)

    val cluster = listOf("A", "B", "C", "D", "E").zip(ioList).toMap().map {
        Amplifier(code, it.value, it.key)
    }
    while (cluster.isRunning()) {
        cluster.forEach { amplifier ->
            amplifier.amplify()
        }
    }
    return cluster.last().program.io.outputQueue().last()
}

fun PhaseSettingSequence.permutations(): List<PhaseSettingSequence> {
    val permutations = mutableListOf<PhaseSettingSequence>()
    forEach { i ->
        forEach { j ->
            if (j != i) {
                forEach { k ->
                    if (k != i && k != j) {
                        forEach { l ->
                            if (l != k && l != j && l != i) {
                                forEach { m ->
                                    if (m != l && m != k && m != j && m != i) {
                                        permutations.add(listOf(i, j, k, l, m))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    return permutations
}

fun List<PhaseSettingSequence>.bestSequentialOutput(program: String): Signal =
    map { it.sequentialAmplify(0, program) }.max() ?: 0

fun List<PhaseSettingSequence>.bestParallelOutput(program: String): Signal =
    map { it.parallelAmplify(0, program) }.max() ?: 0


data class Amplifier(val code: Code, val io: QueuedIo, val name: String? = null) {
    val program = Program(code.load(), io, name)

    fun isTerminated(): Boolean = program.isTerminated()

    fun amplify(inputSignal: Signal? = null): Signal? {
        if (inputSignal != null) {
            io.queueInput(inputSignal)
        }
        program.compute()
        return program.io.outputQueue().run {
            if (isNotEmpty()) first() else null
        }
    }
}

class SubscriptionIo : QueuedIo() {
    var subscriber: SubscriptionIo? = null
    fun addSubscriber(subscriptionIo: SubscriptionIo) {
        subscriber = subscriptionIo
    }

    override fun output(line: Long) {
        if (subscriber != null) {
            subscriber!!.queueInput(line)
        }
        super.output(line)
    }
}

