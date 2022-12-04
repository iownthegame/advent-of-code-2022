class Pair(private val line: String) {
    private val assignmentOfFirstElf: Assignment
    private val assignmentOfSecondElf: Assignment

    init {
        val parts = line.split(",")
        assignmentOfFirstElf = Assignment(parts.first().split("-").map { it.toInt() })
        assignmentOfSecondElf = Assignment(parts.last().split("-").map { it.toInt() })
    }

    fun hasExclusiveAssignment(): Boolean {
        return assignmentOfFirstElf.containsAssignment(assignmentOfSecondElf)
            || assignmentOfSecondElf.containsAssignment(assignmentOfFirstElf)
    }

    fun hasOverlappedAssignment(): Boolean {
        return assignmentOfFirstElf.overlapsAssignment(assignmentOfSecondElf)
    }

    class Assignment(private val array: List<Int>) {
        private var min: Int = 0
        private var max: Int = 0

        init {
            min = array.first()
            max = array.last()
        }

        fun containsAssignment(otherAssignment: Assignment): Boolean {
            return min <= otherAssignment.min && max >= otherAssignment.max
        }

        fun overlapsAssignment(otherAssignment: Assignment): Boolean {
            return (max >= otherAssignment.min && max <= otherAssignment.max) || (otherAssignment.max in min..max)
        }
    }
}

fun main() {
    fun getPairs(input: List<String>): Array<Pair> {
        var pairs = arrayOf<Pair>()
        for (line in input) {
            pairs += Pair(line)
        }
        return pairs
    }

    fun part1(input: List<String>): Int {
        val pairs = getPairs(input)
        return pairs.count { it.hasExclusiveAssignment() }
    }

    fun part2(input: List<String>): Int {
        val pairs = getPairs(input)
        return pairs.count { it.hasOverlappedAssignment() }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readTestInput("Day04_sample")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    val input = readTestInput("Day04")
    println("part 1 result: ${part1(input)}")
    println("part 2 result: ${part2(input)}")
}
