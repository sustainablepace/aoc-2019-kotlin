package advent_of_code


fun main(args: Array<String>) {
    println(Day04.partOne())
    println(Day04.partTwo())
}

object Day04 {

    private val passwordRange = (272091..815432).map { it.toString() }

    fun partOne(): Int = passwordRange.filter { it.matchesCriteria() }.count()

    fun partTwo(): Int = passwordRange.filter { it.matchesAdditionalCriteria() }.count()
}

typealias Password = String
fun Password.matchesCriteria() =
    hasExactlySixDigits() && twoAdjacentDigitsAreTheSame() && digitsNeverDecrease()

fun Password.matchesAdditionalCriteria() =
    hasExactlySixDigits() && twoAdjacentDigitsAreTheSameAndNotWithinLargerGroup() && digitsNeverDecrease()

fun Password.hasExactlySixDigits(): Boolean = count() == 6 && all { it.isDigit() }
fun Password.twoAdjacentDigitsAreTheSame(): Boolean = zipWithNext().any { it.first == it.second }
fun Password.digitsNeverDecrease(): Boolean = zipWithNext().all { it.first <= it.second }

fun Password.twoAdjacentDigitsAreTheSameAndNotWithinLargerGroup(): Boolean =
    zipWithNext().filter { it.first == it.second }.map { it.first }.any { characterOccursExactlyTwice(it) }

fun Password.characterOccursExactlyTwice(character: Char) = filter { it == character }.count() == 2
