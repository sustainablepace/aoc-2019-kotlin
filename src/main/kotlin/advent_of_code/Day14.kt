package advent_of_code

import kotlin.math.ceil

fun main(args: Array<String>) {
    println(Day14.partOne())
    println(Day14.partTwo())
}

object Day14 {
    private val reactionsList = javaClass.classLoader
        .getResource("day14_reaction.txt")!!
        .readText()

    private val reactions = reactionsList.parse()

    fun partOne(): Long = reactions.minOreForFuel(1)

    fun partTwo(): Long = reactions.maxFuelForOre(1_000_000_000_000)
}

typealias Reactions = Set<Reaction>
data class Reaction(val input: Ingredients, val output: Ingredient)
data class Ingredient(val amount: Amount, val chemical: Chemical)
typealias Ingredients = List<Ingredient>
typealias Chemical = String
typealias Amount = Long

fun Ingredients.containsOtherThanOre() = any { it.chemical != "ORE" }
fun Ingredients.aggregate() = groupBy {
    it.chemical
}.map { group ->
    Ingredient(group.value.map { it.amount }.sum(), group.key)
}

fun Reactions.isDeferredFor(chemical: Chemical) =
    any { reaction -> reaction.input.any { ingredient -> ingredient.chemical == chemical } }

fun Reactions.into(chemical: Chemical) = filter { it.output.chemical == chemical }

fun Reactions.substitute(ingredient: Ingredient): Ingredients =
    into(ingredient.chemical).flatMap { reaction ->
        reaction.input.map {
            Ingredient(
                ceil(ingredient.amount.toFloat() / reaction.output.amount).toInt() * it.amount,
                it.chemical
            )

        }
    }


fun Ingredients.substitute(reactions: Reactions) = flatMap { ingredient ->
    if (reactions.isDeferredFor(ingredient.chemical)) {
        listOf(ingredient)
    } else {
        reactions.substitute(ingredient)
    }
}

typealias FuelInterval = Pair<Amount, Amount>
fun Reactions.searchIntervalMaxFuelForOre(numOre: Amount): FuelInterval {
    var fuel = 1L
    var ore = minOreForFuel(fuel)
    println("$fuel fuel requires $ore ore")

    while(ore < numOre) {
        fuel *= 2
        ore = minOreForFuel(fuel)
        println("$fuel fuel requires $ore ore")
    }
    return fuel/2 to fuel
}
fun Reactions.maxFuelForOre(numOre: Amount): Amount {
    var interval = searchIntervalMaxFuelForOre(numOre)
    var abort = false
    var previousOre = 0L
    var maxFuel: Long
    do {
        println("Interval $interval")

        maxFuel = (interval.first + interval.second)/2.toLong()
        val newOre = minOreForFuel(maxFuel)
        println("$maxFuel fuel requires $newOre ore")
        if(newOre == numOre) {
            abort = true
        } else {
            interval = if(newOre > numOre) {
                interval.first to maxFuel
            } else {
                maxFuel to interval.second
            }
            if(newOre == previousOre) {
                abort = true
            }
            previousOre = newOre
        }
    } while (!abort)
    return maxFuel
}

fun Reactions.minOreForFuel(numFuel: Amount): Amount {
    var availableReactions = toMutableSet()
    var requiredIngredients = listOf(Ingredient(numFuel, "FUEL"))

    while (requiredIngredients.containsOtherThanOre()) {
        val requiredReactions = availableReactions.filter { reaction ->
            requiredIngredients.any { ingredient -> ingredient.chemical == reaction.output.chemical }
        }.toSet()
        val newRequiredIngredients = requiredIngredients.substitute(availableReactions).aggregate()

        val obsoleteReactions = requiredReactions.filter { reaction ->
            newRequiredIngredients.none { it.chemical == reaction.output.chemical } &&
                    availableReactions.none { it.input.any { it.chemical == reaction.output.chemical } }
        }.toSet()
        availableReactions = availableReactions.minus(obsoleteReactions).toMutableSet()

        requiredIngredients = newRequiredIngredients
    }
    return requiredIngredients.map { it.amount }.sum()
}


typealias ReactionsList = String

fun ReactionsList.parse(): Reactions = lines().map { line ->
    val (input, output) = line.split(" => ")
    val left = input.split(", ").map {
        val (amount, name) = it.split(" ")
        Ingredient(amount.toLong(), name)
    }
    val right = output.run {
        val (amount, name) = split(" ")
        Ingredient(amount.toLong(), name)
    }
    Reaction(left, right)
}.toSet()

