package advent_of_code

import advent_of_code.Pixel.*

fun main(args: Array<String>) {
    println(Day08.partOne())
    println(Day08.partTwo())
}

object Day08 {

    private val rawImage = javaClass.classLoader
        .getResource("day08_image.txt")!!
        .readText()
    val image = Image(rawImage, 25 to 6)

    fun partOne(): Int {
        val layerWithFewestZero = image.layers.minBy { it.count { it == BLACK.ordinal } }
        val numOnes = layerWithFewestZero?.count { it == WHITE.ordinal }
        val numTwos = layerWithFewestZero?.count { it == TRANSPARENT.ordinal }
        return numOnes!! * numTwos!!
    }

    fun partTwo(): String = image.visibleLayer.joinToString("")
}

data class Image(val rawData: RawData, val dimensions: Dimensions) {
    val layers
        get() = rawData.mapIndexed { index, c ->
            index / (dimensions.first * dimensions.second) to c
        }.groupBy { it.first }.values.map { it.map { it.second.toString().toInt() } }

    val visibleLayer
        get() = (0 until dimensions.size()).map { index ->
            layers.map { layer ->
                layer[index]
            }.visiblePixel()
        }
}

typealias PixelLayer = List<Int>

fun PixelLayer.visiblePixel() = first { it != TRANSPARENT.ordinal }

enum class Pixel {
    BLACK,
    WHITE,
    TRANSPARENT
}
typealias RawData = String
typealias Dimensions = Pair<Int, Int>

fun Dimensions.size() = first * second
