fun main() {
    fun part1(input: List<String>): Int {
        val calculatedCycles = arrayOf<Int>(20, 60, 100, 140, 180, 220)
        var registerValuePerCycle = arrayOf<Int>()
        var currentRegisterValue = 1

        for (line in input) {
            val splits = line.split(" ")
            val op = splits[0]
            if (op == "noop") {
                registerValuePerCycle += currentRegisterValue
            } else if (op == "addx") {
                val addedValue = splits[1].toInt()
                registerValuePerCycle += currentRegisterValue
                currentRegisterValue += addedValue
                registerValuePerCycle += currentRegisterValue
            }
//            println("op: $line, cycle no: ${registerValuePerCycle.size}, value: $currentRegisterValue")
        }

        // We need to use the value from the previous cycle, because:
        // During the cycle, X is still unchanged. After the cycle, the addx instruction finishes execution, updating X.
        val signalStrengths = calculatedCycles.map { it * registerValuePerCycle[it - 2] }
        return signalStrengths.sum()
    }

    fun calculateCrt(cycleCount: Int, spritePosition: Int): Char {
        val newCycleNo = cycleCount % 40 + 1
        return if (spritePosition <= newCycleNo && newCycleNo <= spritePosition + 2) {
            "#".first()
        } else {
            ".".first()
        }
    }

    fun part2(input: List<String>) {
        var registerValuePerCycle = arrayOf<Int>()
        var currentRegisterValue = 1
        var spritePosition = 0 // occupy 3 pixels, can be 0-39
        var crtPerCycle = arrayOf<Char>()

        for (line in input) {
            val splits = line.split(" ")
            val op = splits[0]
            if (op == "noop") {
                // during the cycle
                crtPerCycle += calculateCrt(registerValuePerCycle.size, spritePosition)
                registerValuePerCycle += currentRegisterValue
            } else if (op == "addx") {
                // first cycle
                // during the cycle
                crtPerCycle += calculateCrt(registerValuePerCycle.size, spritePosition)
                // end of cycle
                registerValuePerCycle += currentRegisterValue

                // second cycle
                // during the cycle
                crtPerCycle += calculateCrt(registerValuePerCycle.size, spritePosition)

                // end of cycle
                val addedValue = splits[1].toInt()
                currentRegisterValue += addedValue
                registerValuePerCycle += currentRegisterValue
                spritePosition = currentRegisterValue
            }
//            println("op: $line, cycle no: ${registerValuePerCycle.size}, value: $currentRegisterValue")
        }

        for (i in crtPerCycle.indices step 40) {
            for (j in i..i + 39) {
                print(crtPerCycle[j])
            }
            println()
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readTestInput("Day10_sample")
    check(part1(testInput) == 13140)
    part2(testInput)
    /*
        ##..##..##..##..##..##..##..##..##..##..
        ###...###...###...###...###...###...###.
        ####....####....####....####....####....
        #####.....#####.....#####.....#####.....
        ######......######......######......####
        #######.......#######.......#######.....
     */

    val input = readTestInput("Day10")
    println("part 1 result: ${part1(input)}")
    part2(input)
    // get FBURHZCH
    /*
        ####.###..#..#.###..#..#.####..##..#..#.
        #....#..#.#..#.#..#.#..#....#.#..#.#..#.
        ###..###..#..#.#..#.####...#..#....####.
        #....#..#.#..#.###..#..#..#...#....#..#.
        #....#..#.#..#.#.#..#..#.#....#..#.#..#.
        #....###...##..#..#.#..#.####..##..#..#.
     */
}
