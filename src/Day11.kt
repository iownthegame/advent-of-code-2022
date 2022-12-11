import kotlin.math.floor

class Inspection(val monkeyIndex: Int, private val operation: String, val divisibleByValue: Int, private val throwToMonkeys: Array<Int>) {
    var inspectedItemTimes = 0

    fun operateItem(worryLevel: Long): Long {
        // do operation, for example: old * 19, old + 3, old * old
        var newWorryLevel = doOperation(worryLevel)

//        print("monkey $monkeyIndex operates $worryLevel to become $newWorryLevel ")

//        println("after divided by 3 and round down: $newWorryLevel")
        return newWorryLevel
    }

    fun inspectItemAndThrow(worryLevel: Long): Int {
        inspectedItemTimes += 1
        // do test operation and throw, for example: divisible by 13
        //        println("monkey $monkeyIndex throw $worryLevel to $throwToMonkey")
        return testAndThrow(worryLevel)
    }

    private fun doOperation(worryLevel: Long): Long {
        val splits = operation.split(" ")
        val op = splits[1]
        val value = if (splits[2] == "old") worryLevel else splits[2].toLong()
        val newWorryLevel =
            when(op) {
                "*" -> worryLevel * value
                "+" -> worryLevel + value
                else -> { 0 }
            }
        return newWorryLevel
    }

    private fun testAndThrow(worryLevel: Long): Int {
        return if (worryLevel % divisibleByValue.toLong() == 0.toLong()) {
            throwToMonkeys.first()
        } else {
            throwToMonkeys.last()
        }
    }
}

class MonkeyInTheMiddle(val itemsOfMonkeys: MutableMap<Int, MutableList<Long>>, val inspections: Array<Inspection>) {
    val productOfDivisibleByValue: Int?

    init {
        productOfDivisibleByValue = inspections.map { it.divisibleByValue }.reduce{ acc, it -> acc * it }
    }
}

fun main() {
    fun parseInput(input: List<String>): MonkeyInTheMiddle {
        val itemsOfMonkeys = mutableMapOf<Int, MutableList<Long>>()
        var inspections = arrayOf<Inspection>()

        /*
          Monkey 0:
            Starting items: 54, 89, 94
            Operation: new = old * 7
            Test: divisible by 17
              If true: throw to monkey 5
              If false: throw to monkey 3
         */

        var lineIndex = 0
        while (lineIndex < input.size) {
            val line = input[lineIndex]

            if (line.startsWith("Monkey")) {
                // parse monkey index
                val monkeyIndex = line.dropLast(1).split(" ")[1].toInt()

                // parse starting items
                var nextLine = input[++lineIndex]
                val items = nextLine.substring("  Starting items: ".length).split(", ").map { it.toLong() }

                // parse operation
                nextLine = input[++lineIndex]
                val operation = nextLine.substring("  Operation: new = ".length)

                // parse Test operation and throw to monkeys
                nextLine = input[++lineIndex]
                val testOperation = nextLine.substring("  Test: ".length)
                val splits = testOperation.split(" ")
                val divisibleByValue = splits[2].toInt()
                var throwToMonkeys = arrayOf<Int>()
                nextLine = input[++lineIndex]
                throwToMonkeys += nextLine.split(" ").last().toInt()
                nextLine = input[++lineIndex]
                throwToMonkeys += nextLine.split(" ").last().toInt()

                // add inspection and starting items
                val inspection = Inspection(monkeyIndex, operation, divisibleByValue, throwToMonkeys)
                inspections += inspection
                itemsOfMonkeys[monkeyIndex] = items.toMutableList()
            }

            lineIndex++
        }

        return MonkeyInTheMiddle(itemsOfMonkeys, inspections)
    }

    fun doRound(itemsOfMonkeys: MutableMap<Int, MutableList<Long>>, inspections: Array<Inspection>, productOfDivisibleByValue: Int?) {
        for (inspection in inspections) {
            val itemsOfMonkey = itemsOfMonkeys[inspection.monkeyIndex]!!
            while(itemsOfMonkey.isNotEmpty()) {
                val item = itemsOfMonkey.removeFirst()
                var newWorryValueOfItem = inspection.operateItem(item)

                if(productOfDivisibleByValue != null) {
                    newWorryValueOfItem %= productOfDivisibleByValue
                } else {
                    newWorryValueOfItem = floor((newWorryValueOfItem / 3).toDouble()).toLong() // part 1
                }

                val throwToMonkey = inspection.inspectItemAndThrow(newWorryValueOfItem)
                if (itemsOfMonkeys.containsKey(throwToMonkey)) {
                    itemsOfMonkeys[throwToMonkey]!!.add(newWorryValueOfItem)
                } else {
                    itemsOfMonkeys[throwToMonkey] = arrayOf(newWorryValueOfItem).toMutableList()
                }
            }
        }
    }

    fun part1(input: List<String>): Int {
        val monkeyInTheMiddle = parseInput(input)
        val itemsOfMonkeys = monkeyInTheMiddle.itemsOfMonkeys
        val inspections = monkeyInTheMiddle.inspections

        val totalRounds = 20

        for (i in 1..totalRounds) {
            doRound(itemsOfMonkeys, inspections, null)
        }

        // get the top 2 inspectedItemTimes
        val inspectedItemTimes = inspections.map { it.inspectedItemTimes }.sortedDescending()
//        println("inspectedItemTimes: ${inspectedItemTimes.map { it }}")

        return inspectedItemTimes[0] * inspectedItemTimes[1]
    }

    fun part2(input: List<String>): Long {
        val monkeyInTheMiddle = parseInput(input)
        val inspections = monkeyInTheMiddle.inspections

        val totalRounds = 10000

        for (i in 1..totalRounds) {
            doRound(monkeyInTheMiddle.itemsOfMonkeys, inspections, monkeyInTheMiddle.productOfDivisibleByValue)
        }

        // get the top 2 inspectedItemTimes
        val inspectedItemTimes = inspections.map { it.inspectedItemTimes }.sortedDescending()
//        println("inspectedItemTimes: ${inspections.map { it.inspectedItemTimes }}")

        return inspectedItemTimes[0].toLong() * inspectedItemTimes[1].toLong()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readTestInput("Day11_sample")
    check(part1(testInput) == 10605)
    check(part2(testInput) == 2713310158)


    val input = readTestInput("Day11")
    println("part 1 result: ${part1(input)}")
    println("part 2 result: ${part2(input)}")
}
