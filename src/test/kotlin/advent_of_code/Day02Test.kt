package advent_of_code

import kotlin.test.Test
import kotlin.test.assertEquals

class Day02Test {
    @Test
    fun `intcode examples`() {
        assertEquals(Intcode("99"), Intcode("99").execute())
        assertEquals(Intcode("2,0,0,0,99", 4), Intcode("1,0,0,0,99").execute())
        assertEquals(Intcode("2,3,0,6,99", 4), Intcode("2,3,0,3,99").execute())
        assertEquals(Intcode("2,4,4,5,99,9801", 4), Intcode("2,4,4,5,99,0").execute())
        assertEquals(Intcode("30,1,1,4,2,5,6,0,99", 8), Intcode("1,1,1,4,99,5,6,0,99").execute())

    }
}


