package advent_of_code

import advent_of_code.domain.IntCode
import advent_of_code.domain.Io
import advent_of_code.domain.launchComputer
import advent_of_code.domain.memory
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

object Day07 {

    private val amplifierControllerSoftware = javaClass.classLoader
        .getResource("day07_thrusters.txt")!!
        .readText()

    fun partOne(): Signal = intArrayOf(0, 1, 2, 3, 4).permutations().bestSequentialOutput(amplifierControllerSoftware)

    suspend fun partTwo(): Signal = intArrayOf(5, 6, 7, 8, 9).permutations().bestParallelOutput(amplifierControllerSoftware)
}

typealias Signal = Int
typealias PhaseSetting = Int
typealias PhaseSettingSequence = IntArray

fun PhaseSettingSequence.sequentialAmplify(inputSignal: Signal, program: String): Signal {
    var previousOutputSignal = inputSignal
    forEach { phaseSetting ->
        val amplifier = Amplifier(program, phaseSetting)
        previousOutputSignal = amplifier.amplify(previousOutputSignal)
    }
    return previousOutputSignal
}

suspend fun PhaseSettingSequence.parallelAmplify(program: String) : Signal {
    var last: Int? = null
    coroutineScope {
        val broadcastChannel = BroadcastChannel<Int>(2)
        val ea = broadcastChannel.openSubscription()
        broadcastChannel.send(this@parallelAmplify[0]) // Initial phase
        broadcastChannel.send(0) // Primary input

        val (ab, bc, cd, de) = (1..4).map { i ->
            Channel<Int>(1).also { it.send(this@parallelAmplify[i]) }
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

fun List<PhaseSettingSequence>.bestSequentialOutput(program: String): Int = map { it.sequentialAmplify(0, program) }.max() ?: 0
suspend fun List<PhaseSettingSequence>.bestParallelOutput(program: String): Int = map { it.parallelAmplify(program) }.max() ?: 0


data class Amplifier(val program: String, val phaseSetting: PhaseSetting) {
    fun amplify(inputSignal: Signal): Signal {
        val amplifier = IntCode(memory(program), AmplifierIo(listOf(phaseSetting, inputSignal)))
        amplifier.run()
        return amplifier.io.outputs().first().toInt()
    }
}

class AmplifierIo(private val inputs: List<Int>) : Io {
    private var inputIndex = 0
    var outputs = mutableListOf<Int>()
    override fun input(): String? {
        return inputs[inputIndex++].toString()
    }

    override fun output(line: String) {
        outputs.add(line.toInt())
    }

    override fun outputs(): List<String> {
        return outputs.map { it.toString() }.toList()
    }
}

