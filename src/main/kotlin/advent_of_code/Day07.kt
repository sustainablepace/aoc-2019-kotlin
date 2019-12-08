package advent_of_code

import advent_of_code.domain.*
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

suspend fun main(args: Array<String>) {
    println(Day07.partOne())
    println(Day07.partTwo())
}

object Day07 {

    private val amplifierControllerSoftware = javaClass.classLoader
        .getResource("day07_thrusters.txt")!!
        .readText()

    fun partOne(): Signal = intArrayOf(0, 1, 2, 3, 4).permutations().bestSequentialOutput(amplifierControllerSoftware)

    suspend fun partTwo(): Signal =
        intArrayOf(5, 6, 7, 8, 9).permutations().bestParallelOutput(amplifierControllerSoftware)
}

typealias Signal = Int
typealias PhaseSetting = Int
typealias PhaseSettingSequence = IntArray

fun PhaseSettingSequence.sequentialAmplify(inputSignal: Signal, code: Code): Signal {
    var previousOutputSignal = inputSignal
    forEach { phaseSetting ->
        val io = QueuedIo().queueInput(phaseSetting) as QueuedIo
        val amplifier = Amplifier(code, io)
        previousOutputSignal = amplifier.amplify(previousOutputSignal)!!
    }
    return previousOutputSignal
}

typealias AmplifierCluster = List<Amplifier>

fun AmplifierCluster.isRunning(): Boolean = any { !it.isTerminated() }

fun PhaseSettingSequence.parallelAmplify(inputSignal: Signal, code: Code): Signal {
    val ioList: List<SubscriptionIo> = this.map { phaseSetting ->
        SubscriptionIo().queueInput(phaseSetting) as SubscriptionIo
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

suspend fun PhaseSettingSequence.parallelAmplifyCoRoutine(inputSignal: Signal, program: String): Signal {
    var last: Int? = null
    coroutineScope {
        val broadcastChannel = BroadcastChannel<Int>(2)
        val ea = broadcastChannel.openSubscription()
        broadcastChannel.send(this@parallelAmplifyCoRoutine[0]) // Initial phase
        broadcastChannel.send(inputSignal) // Primary input

        val (ab, bc, cd, de) = (1..4).map { i ->
            Channel<Int>(1).also { it.send(this@parallelAmplifyCoRoutine[i]) }
        }
        val output = broadcastChannel.openSubscription()

        launch {
            try {
                while (true) {
                    last = output.receive()
                }
            } catch (e: ClosedReceiveChannelException) {
                // Calculation done
            }
        }
        launchComputer(program, ea, ab)
        launchComputer(program, ab, bc)
        launchComputer(program, bc, cd)
        launchComputer(program, cd, de)
        launchComputer(program, de, broadcastChannel)
    }
    return last!!
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
                                        permutations.add(intArrayOf(i, j, k, l, m))
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

fun List<PhaseSettingSequence>.bestSequentialOutput(program: String): Int =
    map { it.sequentialAmplify(0, program) }.max() ?: 0

fun List<PhaseSettingSequence>.bestParallelOutput(program: String): Int =
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

class SubscriptionIo() : QueuedIo() {
    var subscriber: SubscriptionIo? = null
    fun addSubscriber(subscriptionIo: SubscriptionIo) {
        subscriber = subscriptionIo
    }

    override fun output(line: Int) {
        if (subscriber != null) {
            subscriber!!.queueInput(line)
        }
        super.output(line)
    }
}

