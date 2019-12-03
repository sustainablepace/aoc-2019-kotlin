package advent_of_code

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class Day03Test {
    @Test
    fun `part one`() {
        val cable1 = cable("R75,D30,R83,U83,L12,D49,R71,U7,L72")
        val cable2 = cable("U62,R66,U55,R34,D71,R55,D58,R83")
        assertEquals(159, cable1.distanceToClosestIntersectionToPort(cable2))

        val cable3 = cable("R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51")
        val cable4 = cable("U98,R91,D20,R16,D67,R40,U7,R15,U6,R7")
        assertEquals(135, cable3.distanceToClosestIntersectionToPort(cable4))

        assertEquals(2193, Day03.cables[0].distanceToClosestIntersectionToPort(Day03.cables[1]))
    }

    @Test
    fun `part two`() {
        val cable1 = cable("R75,D30,R83,U83,L12,D49,R71,U7,L72")
        val cable2 = cable("U62,R66,U55,R34,D71,R55,D58,R83")
        assertEquals(610, cable1.stepsToClosestIntersection(cable2))

        val cable3 = cable("R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51")
        val cable4 = cable("U98,R91,D20,R16,D67,R40,U7,R15,U6,R7")
        assertEquals(410, cable3.stepsToClosestIntersection(cable4))

        assertNotEquals(63526, Day03.cables[0].stepsToClosestIntersection(Day03.cables[1]))
    }
}


