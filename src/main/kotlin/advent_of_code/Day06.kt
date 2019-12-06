package advent_of_code

object Day06 {

    val input = javaClass.classLoader
        .getResource("day06_orbit_data.txt")!!
        .readText()

    fun partOne(): Int {
        return OrbitData(input).numOrbits
    }

    fun partTwo(): Int {
        return OrbitData(input).numTransfersBetween("YOU", "SAN")
    }
}

class OrbitData(val input: String) {

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

    fun getChildren(parent: String) = orbitList.filter { it.first == parent }.map { it.second }

    fun numTransfersBetween(from: String, to: String): Int {
        val parentsFrom = parentsOf(from)
        val parentsTo = parentsOf(to)
        val distance = parentsFrom.toSet().intersect(parentsTo.toSet()).map {
            parentsFrom.indexOf(it) + parentsTo.indexOf(it)
        }.min()!!
        return distance
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

    val orbitList = input.lines().map {
        val l = it.split(")")
        l[0] to l[1]
    }.toList()

}