package advent_of_code

fun main(args: Array<String>) {
    println(Day06.partOne())
    println(Day06.partTwo())
}

object Day06 {

    private val input = javaClass.classLoader
        .getResource("day06_orbit_data.txt")!!
        .readText()

    fun partOne(): Int {
        return OrbitData(input).numOrbits
    }

    fun partTwo(): Int {
        return OrbitData(input).numTransfersBetween("YOU", "SAN")
    }
}

class OrbitData(input: String) {

    val numOrbits: Int
        get() = next(1, "COM")

    fun next(depth: Int, parent: String): Int {
        val children = getChildren(parent)
        var newNum = depth * children.size
        children.forEach {
            newNum += next(depth + 1, it)
        }
        return newNum
    }

    private fun getChildren(parent: String) = orbitList.filter { it.first == parent }.map { it.second }

    fun numTransfersBetween(from: String, to: String): Int {
        val parentsFrom = parentsOf(from)
        val parentsTo = parentsOf(to)
        return parentsFrom.toSet().intersect(parentsTo.toSet()).map {
            parentsFrom.indexOf(it) + parentsTo.indexOf(it)
        }.min()!!
    }

    fun parentsOf(from: String): Array<out String> {
        val me = orbitList.filter { it.second == from }
        if (me.isEmpty()) {
            return emptyArray()
        }
        val parent = me.first().first
        val itsParents = parentsOf(parent)
        return mutableListOf(parent, *itsParents).toTypedArray()
    }

    private val orbitList = input.lines().map {
        val l = it.split(")")
        l[0] to l[1]
    }.toList()

}