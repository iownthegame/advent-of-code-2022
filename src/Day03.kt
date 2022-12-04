class Rucksack(private val input: String) {
    private val itemsInFirstCompartment: CharArray
    private val itemsInSecondCompartment: CharArray
    private val sharedItems: Set<Char>

    init {
        val halfInputLength = input.length / 2
        itemsInFirstCompartment = input.substring(0, halfInputLength).toCharArray()
        itemsInSecondCompartment = input.substring(halfInputLength).toCharArray()
        sharedItems = itemsInFirstCompartment.toSet().intersect(itemsInSecondCompartment.toSet())
    }

    fun prioritiesOfSharedItems(): Int {
        var priorities = 0
        for (char in sharedItems) {
            priorities += if (char.isLowerCase()) {
                char.code - 97 + 1
            } else {
                char.code - 65 + 26 + 1
            }
        }
        return priorities
    }
}

class RucksackGroup(private val groupLines: List<String>) {
    private val badge: Char

    init {
        var items = setOf<Char>()
        for (line in groupLines) {
            val currentItems = line.toCharArray()
            items = if (items.isEmpty()) {
                currentItems.toSet()
            } else {
                items.intersect(currentItems.toSet())
            }
        }
        badge = items.first() // Assume we only have one badge
    }

    fun priorityOfBadge(): Int {
         return if (badge.isLowerCase()) {
             badge.code - 97 + 1
        } else {
             badge.code - 65 + 26 + 1
        }
    }
}

fun main() {
    fun getRucksacks(input: List<String>): Array<Rucksack> {
        var rucksacks = arrayOf<Rucksack>()
        for (line: String in input) {
            val rucksack = Rucksack(line)
            rucksacks += rucksack
        }
        return rucksacks
    }

    fun getRucksackGroups(input: List<String>): Array<RucksackGroup> {
        var rucksackGroups = arrayOf<RucksackGroup>()
        val length = input.size
        var index = 0
        while (index < length) {
            val lines = input.subList(index, index+3)
            val rucksackGroup = RucksackGroup(lines)
            rucksackGroups += rucksackGroup
            index += 3
        }
        return rucksackGroups
    }

    fun part1(input: List<String>): Int {
        val rucksacks = getRucksacks(input)
        return rucksacks.sumOf { it.prioritiesOfSharedItems() }
    }

    fun part2(input: List<String>): Int {
        val rucksackGroups = getRucksackGroups(input)
        return rucksackGroups.sumOf { it.priorityOfBadge() }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readTestInput("Day03_sample")
    check(part1(testInput) == 157)
    check(part2(testInput) == 70)

    val input = readTestInput("Day03")
    println("part 1 result: ${part1(input)}")
    println("part 2 result: ${part2(input)}")
}
