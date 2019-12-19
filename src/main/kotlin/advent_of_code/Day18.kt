package advent_of_code

import java.io.IOException

fun main(args: Array<String>) {
    println(Day18.partOne())
    println(Day18.partTwo())
}

object Day18 {
    private val map = javaClass.classLoader
        .getResource("day18_vault.txt")!!
        .readText()

    fun partOne(): Int {
        val vault = map.toVault()
        vault.vaultMap.draw()
        return vault.minSteps()
    }

    fun partTwo(): Int {
        return 0
    }
}

typealias VaultFile = String

fun VaultFile.toVault(): Vault = lines().mapIndexed { y, line ->
    line.mapIndexed { x, c ->
        if (c in listOf('.', '@') || c.isLetter()) {
            VaultPosition(Position(x, y), c)
        } else null
    }
}.flatMap { it.filterNotNull() }.toMutableSet().run {
    val origin = this.first { it.char == '@' }
    this.remove(origin)
    this.add(origin.copy(char = '.'))
    Vault(this, origin, origin.copy(char = '?'))
}

class Vault(
    val vaultMap: VaultMap,
    private val origin: VaultPosition,
    private val current: VaultPosition,
    private val visited: Set<Position> = setOf()
) {
    fun minSteps(): Int {
        var stepsUntilKey = 0
        var vaults = setOf(this)

        while (vaults.isNotEmpty()) {
            vaults = vaults.flatMap { it.followPath() }.toSet()
            stepsUntilKey++
            println("\nsteps: $stepsUntilKey, breadth: ${vaults.size}")
            //vaults.forEach { v -> v.vaultMap.draw(v.current) }
            if (vaults.any { it.vaultMap.none { p -> p.isKey() } }) {
                return stepsUntilKey
            }
        }
        throw IOException("Keys unreachable.")
    }

    private fun followPath(): Set<Vault> {
        return vaultMap.neighbours(current).filter {
            (it.isKey() || it.isHallway()) && it.position !in visited
        }.map { pos ->

            if (pos.isKey()) {
                val newMap = vaultMap
                    .minus(pos)
                    .plus(pos.copy(char = '.')).toMutableSet()

                newMap.firstOrNull { it.isDoorForKey(pos) }?.run {
                    newMap.remove(this)
                    newMap.add(this.copy(char = '.'))
                }
                Vault(newMap, pos.copy(char = '@'), pos.copy(char = '?'))
            } else {
                Vault(vaultMap, origin.copy(char = '@'), pos.copy(char = '?'), setOf(current.position).plus(visited))
            }
        }.toSet()
    }
}

typealias VaultMap = MutableSet<VaultPosition>

fun VaultMap.neighbours(p: VaultPosition): List<VaultPosition> = filter { p.position isNeighbour it.position }

fun VaultMap.range() = SearchRange(
    Coordinate(
        map { it.position.x }.min()!!,
        map { it.position.y }.min()!!
    ), Coordinate(
        map { it.position.x }.max()!!,
        map { it.position.y }.max()!!
    )
)


fun VaultMap.draw(current: VaultPosition) {
    this.minus(this.first { it.position == current.position }).plus(current).toMutableSet().draw()
}

fun VaultMap.draw() {
    val range = range()
    ((range.min.y - 1)..(range.max.y + 1)).forEach { y ->
        val line = ((range.min.x - 1)..(range.max.x + 1)).map { x ->
            firstOrNull { it.position == Position(x, y) }?.char ?: '#'
        }.joinToString("") { it.toString() }
        println(line)
    }
}

data class VaultPosition(val position: Position, val char: Char) {
    fun isKey(): Boolean = char.isLetter() && char.isLowerCase()
    fun isDoorForKey(key: VaultPosition): Boolean = isDoor() && key.isKey() && char == key.char.toUpperCase()
    fun isHallway(): Boolean = char == '.'
    private fun isDoor(): Boolean = char.isLetter() && char.isUpperCase()
}