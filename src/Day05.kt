import java.util.*

class Cargo(val moves: Array<Array<Int>>) {
    private var stacks = mutableMapOf<Int, Stack<String>>()

    fun putCrate(at: Int, label: String) {
        if (!stacks.containsKey(at)) {
            stacks[at] = Stack<String>()
        }
        stacks[at]!!.push(label)
    }

    fun moveCrate(amount: Int, from: Int, to: Int) {
        val fromStack = stacks[from]!!
        val toStack = stacks[to]!!
        for (i in 1..amount) {
            val topCrate = fromStack.pop()
            toStack.push(topCrate)
        }
    }

    fun moveCrateInTheSameOrder(amount: Int, from: Int, to: Int) {
        val fromStack = stacks[from]!!
        val toStack = stacks[to]!!
        val tmpStack = Stack<String>();
        for (i in 1..amount) {
            val topCrate = fromStack.pop()
            tmpStack.push(topCrate)
        }
        for (i in 1..amount) {
            val topCrate = tmpStack.pop()
            toStack.push(topCrate)
        }
    }

    fun topCrates(): List<String> {
        val keys = stacks.keys.sorted().toIntArray()
        return keys.map { stacks[it]!!.peek() }
    }
}

fun main() {
    fun parseInput(input: List<String>): Cargo {
        var stackLines = arrayOf<Array<String>>()
        var moves = arrayOf<Array<Int>>()

        for (line in input) {
            if (line.isEmpty()) {
                continue
            }
            if (line.substring(0, 4) == "move") {
                val splits = line.split(" ")
                moves += arrayOf(splits[1].toInt(), splits[3].toInt(), splits[5].toInt())
                continue
            }
            if (line.contains("[")) {
                val length = line.length
                var stackLine = arrayOf<String>()
                for (i in 1 until length step 4) {
                    stackLine += line[i].toString()
                }
                stackLines += stackLine
                continue
            }
        }

        val cargo = Cargo(moves)
        for (stackLine in stackLines.reversed()) {
            for (i in stackLine.indices) {
                val label = stackLine[i]
                if (label == " ") {
                    continue
                }
                cargo.putCrate(i + 1, label)
            }
        }
        return cargo
    }

    fun part1(input: List<String>): String {
        val cargo = parseInput(input)
        for (move in cargo.moves) {
            cargo.moveCrate(move[0], move[1], move[2])
        }
        return cargo.topCrates().joinToString("")
    }

     fun part2(input: List<String>): String {
         val cargo = parseInput(input)
         for (move in cargo.moves) {
             cargo.moveCrateInTheSameOrder(move[0], move[1], move[2])
         }
         return cargo.topCrates().joinToString("")
     }

    // test if implementation meets criteria from the description, like:
    val testInput = readTestInput("Day05_sample")
    check(part1(testInput) == "CMZ")
    check(part2(testInput) == "MCD")

    val input = readTestInput("Day05")
    println("part 1 result: ${part1(input)}")
    println("part 2 result: ${part2(input)}")
}
