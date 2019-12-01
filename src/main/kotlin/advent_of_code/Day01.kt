package advent_of_code

import kotlin.math.floor

class Day01 {
    fun partOne() : Fuel {
        return FuelUpper.calculateFuelNeeded(Spaceship)
    }
    fun partTwo() : Fuel {
        return RocketEquationDoubleChecker.calculateFuelNeeded(Spaceship)
    }
}

sealed class ModuleOrFuel(open val mass: Int)
data class Module(override val mass: Int) : ModuleOrFuel(mass)
data class Fuel(override val mass: Int) : ModuleOrFuel(mass)

infix operator fun Fuel.plus(fuel: Fuel) : Fuel = Fuel(mass + fuel.mass)
infix operator fun Fuel.compareTo(fuel: Fuel) : Int = mass - fuel.mass

object Spaceship {
    val modules: List<Module>
    init {
        val input = javaClass.classLoader.getResource("day01_spaceship_modules.txt")?.readText()!!
        modules = input.lines().map { line -> line.trim().toInt() }.map { mass -> Module(mass) }
    }
}

sealed class Elf

object RocketEquationDoubleChecker : Elf() {
    fun calculateFuelNeeded(spaceship: Spaceship) : Fuel = Fuel(spaceship.modules.sumBy { module ->
        calculateFuelNeeded(module).mass
    })

    fun calculateFuelNeeded(module: Module) : Fuel {
        val fuelNeeded = FuelUpper.calculateFuelNeeded(module)
        return fuelNeeded + calculateFuelNeeded(fuelNeeded)
    }

    private fun calculateFuelNeeded(fuel: Fuel) : Fuel {
        val fuelNeeded = FuelUpper.calculateFuelNeeded(fuel)
        if(fuelNeeded > Fuel(0)) {
            return fuelNeeded + calculateFuelNeeded(fuelNeeded)
        }
        return Fuel(0)
    }
}

object FuelUpper : Elf() {
    fun calculateFuelNeeded(spaceship: Spaceship) : Fuel = Fuel(spaceship.modules.sumBy { module ->
        calculateFuelNeeded(module).mass
    })

    fun calculateFuelNeeded(moduleOrFuel: ModuleOrFuel) : Fuel = Fuel(floor( moduleOrFuel.mass.toFloat().div(3)).toInt().minus(2))
}

