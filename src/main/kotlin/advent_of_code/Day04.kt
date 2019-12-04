package advent_of_code

object Day04 {

    private val passwordRange = (272091..815432).map { it.toString().toCharArray().toList() }

    fun partOne(): Int = passwordRange.filter { it.matchesCriteria() }.count()

    fun partTwo(): Int = passwordRange.filter { it.matchesAdditionalCriteria() }.count()
}

typealias Password = List<Char>

fun Password.matchesCriteria() =
    hasOnlySixDigits() && twoAdjacentDigitsAreTheSame() && digitsNeverDecrease()

fun Password.matchesAdditionalCriteria() =
    hasOnlySixDigits() && twoAdjacentDigitsAreTheSameAndNotWithinLargerGroup() && digitsNeverDecrease()

fun Password.hasOnlySixDigits(): Boolean = count() == 6 && all { it.isDigit() }
fun Password.twoAdjacentDigitsAreTheSame(): Boolean = zipWithNext().any { it.first == it.second }
fun Password.digitsNeverDecrease(): Boolean = zipWithNext().all { it.first <= it.second }

fun Password.twoAdjacentDigitsAreTheSameAndNotWithinLargerGroup(): Boolean =
    zipWithNext().filter { it.first == it.second }.map { it.first }.any { characterOccursExactlyTwice(it) }

fun Password.characterOccursExactlyTwice(character: Char) = filter { it == character }.count() == 2
