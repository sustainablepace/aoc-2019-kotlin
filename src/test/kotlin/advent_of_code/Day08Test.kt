package advent_of_code

import kotlin.test.Test
import kotlin.test.assertEquals

class Day08Test {
    @Test
    fun `part one`() {
        assertEquals(2250, Day08.partOne())
    }

    @Test
    fun `part two`() {
        assertEquals(
            "111101001000110100101000010000100100001010010100001110011110000101001010000100001001000010100101000010000100101001010010100001000010010011000110011110",
            Day08.partTwo()
        )
    }
}

/*
#### #  #   ## #  # #
#    #  #    # #  # #
###  ####    # #  # #
#    #  #    # #  # #
#    #  # #  # #  # #
#    #  #  ##   ##  ####
*/