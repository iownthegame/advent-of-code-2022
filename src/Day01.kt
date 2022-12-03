fun main() {
    fun getCaloriesOfElves(input: List<String>): Array<Int> {
        var caloriesOfElves = arrayOf<Int>()
        var currentCalories = 0
        for (line: String in input) {
            if (line.isEmpty()) {
                caloriesOfElves += currentCalories
                currentCalories = 0
            } else {
                currentCalories += line.toInt()
            }
        }
        if (currentCalories != 0) {
            caloriesOfElves += currentCalories
        }
        return caloriesOfElves
    }

    fun part1(input: List<String>): Int {
        val caloriesOfElves = getCaloriesOfElves(input)
        return caloriesOfElves.max()
    }

     fun part2(input: List<String>): Int {
         val caloriesOfElves = getCaloriesOfElves(input)
         return caloriesOfElves.sortedArrayDescending().take(3).sum()
     }

    // test if implementation meets criteria from the description, like:
    val testInput = readTestInput("Day01_sample")
    check(part1(testInput) == 24000)
    check(part2(testInput) == 45000)

    val input = readTestInput("Day01")
    println("part 1 result: ${part1(input)}")
    println("part 2 result: ${part2(input)}")
}
