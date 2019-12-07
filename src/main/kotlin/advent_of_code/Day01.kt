package advent_of_code

import kotlin.math.floor

object Day01 {
    fun partOne(): Fuel {
        return FuelUpper.fuelForSpaceship(Spaceship)
    }

    fun partTwo(): Fuel {
        return RocketEquationDoubleChecker.fuelForSpaceship(Spaceship)
    }
}

typealias Mass = Int
typealias Fuel = Mass
typealias Module = Mass

object Spaceship {
    val modules: List<Module> = javaClass.classLoader
        .getResource("day01_spaceship_modules.txt")!!
        .readText()
        .lines()
        .map { line -> line.trim().toInt() }
}

object RocketEquationDoubleChecker {
    fun fuelForSpaceship(spaceship: Spaceship): Fuel = spaceship.modules.sumBy(this::fuelForModule)

    fun fuelForModule(module: Module): Fuel = FuelUpper.fuelForMass(module).run { this + fuelForFuel(this) }

    private fun fuelForFuel(fuel: Fuel): Fuel = FuelUpper.fuelForMass(fuel).run {
        if (this > 0) {
            this + fuelForFuel(this)
        } else 0
    }
}

object FuelUpper {
    fun fuelForSpaceship(spaceship: Spaceship): Fuel = spaceship.modules.sumBy(this::fuelForMass)
    fun fuelForMass(mass: Mass): Fuel = mass / 3 - 2
}

