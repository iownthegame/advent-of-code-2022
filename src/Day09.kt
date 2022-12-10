import kotlin.math.abs
import kotlin.math.max

fun main() {
    fun moveHeadPos(direction: String, knotPos: Array<Int>): Array<Int> {
//        println("move knot ${knotPos.joinToString(", ")} with direction $direction")
        when (direction) {
            "U" -> knotPos[0] += 1
            "D" -> knotPos[0] -= 1
            "R" -> knotPos[1] += 1
            "L" -> knotPos[1] -= 1
        }
        return knotPos
    }

    fun updateNextKnot(headPos: Array<Int>, tailPos: Array<Int>): Array<Int> {
        // Don't move if touching
        if (headPos[0] == tailPos[0] && headPos[1] == tailPos[1]) {
            return tailPos
        }
        if (headPos[0] == tailPos[0] && abs(headPos[1] - tailPos[1]) == 1) {
            return tailPos
        }
        if (headPos[1] == tailPos[1] && abs(headPos[0] - tailPos[0]) == 1) {
            return tailPos
        }
        if (abs(headPos[0] - tailPos[0]) == 1 && abs(headPos[1] - tailPos[1]) == 1) {
            return tailPos
        }

        // move T close to H
        if (headPos[0] == tailPos[0]) {
            if (headPos[1] > tailPos[1]) {
                tailPos[1] += 1
            } else {
                tailPos[1] -= 1
            }
            return tailPos
        }

        if (headPos[1] == tailPos[1]) {
            if (headPos[0] > tailPos[0]) {
                tailPos[0] += 1
            } else {
                tailPos[0] -= 1
            }
            return tailPos
        }

        if (headPos[0] > tailPos[0]) {
            tailPos[0] += 1
        } else {
            tailPos[0] -= 1
        }
        if (headPos[1] > tailPos[1]) {
            tailPos[1] += 1
        } else {
            tailPos[1] -= 1
        }
        return tailPos
    }

    fun part1(input: List<String>): Int {
        var headPos = Array(2) { 0 }
        var tailPos = Array(2) { 0 }
        val tailVisitedPoses = mutableSetOf<String>()
        tailVisitedPoses.add(tailPos.joinToString(", "))

        for (line in input) {
            val splits = line.split(" ")
            val stepDirection = splits[0]
            val steps = splits[1].toInt()

            for (i in 1..steps) {
                headPos = moveHeadPos(stepDirection, headPos)
//                println("head pos updated: ${headPos.joinToString(", ")}")
                tailPos = updateNextKnot(headPos, tailPos)
//                println("tail pos updated: ${tailPos.joinToString(", ")}")
                tailVisitedPoses.add(tailPos.joinToString(", "))
            }
        }

        return tailVisitedPoses.size
    }

    fun part2(input: List<String>): Int {
        // 10 knots, first one is head, last one is tail
        val knotPoses = Array(10) { Array(2) { 0 } }
        val tailVisitedPoses = mutableSetOf<String>()
        var tailPos = knotPoses.last()
        tailVisitedPoses.add(tailPos.joinToString(", "))

        for (line in input) {
            val splits = line.split(" ")
            val stepDirection = splits[0]
            val steps = splits[1].toInt()

            for (i in 1..steps) {
                var headPos = knotPoses.first()
                headPos = moveHeadPos(stepDirection, headPos)
                knotPoses[0] = headPos

                for (k in 0..8) {
                    val currentKnotPos = knotPoses[k]
                    var currentNextKnotPos = knotPoses[k + 1]
                    currentNextKnotPos = updateNextKnot(currentKnotPos, currentNextKnotPos)
                    knotPoses[k + 1] = currentNextKnotPos
//                    println("next knot pos updated: ${currentNextKnotPos.joinToString(", ")}")
                }
                tailPos = knotPoses.last()
                tailVisitedPoses.add(tailPos.joinToString(", "))
            }
        }
        return tailVisitedPoses.size
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readTestInput("Day09_sample")
    check(part1(testInput) == 13)
    check(part2(testInput) == 1)

    val testInput2 = readTestInput("Day09_sample2")
    check(part2(testInput2) == 36)


    val input = readTestInput("Day09")
    println("part 1 result: ${part1(input)}")
    println("part 2 result: ${part2(input)}")
}
