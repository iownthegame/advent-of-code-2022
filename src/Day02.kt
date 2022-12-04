enum class Shape {
    PAPER, SCISSORS, STONE, INVALID
}
enum class Outcome {
    WIN, DRAW, LOSE, INVALID
}

class Round(private val shapeOfOpponent: Shape, private var suggestedShape: Shape?, private var outcome: Outcome?) {
    private val scoreOfShape = mapOf(
        Shape.PAPER to 2,
        Shape.SCISSORS to 3,
        Shape.STONE to 1
    )

    private val scoreOfOutcome = mapOf(
        Outcome.WIN to 6,
        Outcome.DRAW to 3,
        Outcome.LOSE to 0
    )

    val score: Int

    init {
        outcome = calculateOutCome()
        suggestedShape = calculateSuggestedShape()
        score = calculateScore()
    }

    private fun calculateOutCome(): Outcome {
        if (outcome != null) return outcome as Outcome

        return when(shapeOfOpponent) {
            Shape.PAPER -> {
                when(suggestedShape) {
                    Shape.PAPER -> Outcome.DRAW
                    Shape.SCISSORS -> Outcome.WIN
                    Shape.STONE -> Outcome.LOSE
                    else -> Outcome.INVALID
                }
            }
            Shape.SCISSORS -> {
                when(suggestedShape) {
                    Shape.SCISSORS -> Outcome.DRAW
                    Shape.STONE -> Outcome.WIN
                    Shape.PAPER -> Outcome.LOSE
                    else -> Outcome.INVALID
                }
            }
            Shape.STONE -> {
                when(suggestedShape) {
                    Shape.STONE -> Outcome.DRAW
                    Shape.PAPER -> Outcome.WIN
                    Shape.SCISSORS -> Outcome.LOSE
                    else -> Outcome.INVALID
                }
            }
            else -> Outcome.INVALID
        }
    }

    private fun calculateSuggestedShape(): Shape {
        if (suggestedShape != null) return suggestedShape as Shape

        return when(shapeOfOpponent) {
            Shape.PAPER -> {
                when(outcome) {
                    Outcome.DRAW -> Shape.PAPER
                    Outcome.WIN -> Shape.SCISSORS
                    Outcome.LOSE -> Shape.STONE
                    else -> Shape.INVALID
                }
            }
            Shape.SCISSORS -> {
                when(outcome) {
                    Outcome.DRAW -> Shape.SCISSORS
                    Outcome.WIN -> Shape.STONE
                    Outcome.LOSE -> Shape.PAPER
                    else -> Shape.INVALID
                }
            }
            Shape.STONE -> {
                when(outcome) {
                    Outcome.DRAW -> Shape.STONE
                    Outcome.WIN -> Shape.PAPER
                    Outcome.LOSE -> Shape.SCISSORS
                    else -> Shape.INVALID
                }
            }
            else -> Shape.INVALID
        }
    }

    private fun calculateScore(): Int {
        return scoreOfShape[suggestedShape]!! + scoreOfOutcome[outcome]!!
    }
}

fun main() {
    val shapeOfStrategyInput = mapOf(
        "A" to Shape.STONE ,
        "B" to Shape.PAPER ,
        "C" to Shape.SCISSORS
    )

    fun calculateRoundsBySuggestedInput(input: List<String>): Array<Round> {
        val shapeOfStrategyOutput = mapOf(
            "X" to Shape.STONE ,
            "Y" to Shape.PAPER ,
            "Z" to Shape.SCISSORS
        )

        var rounds = arrayOf<Round>()
        for (line in input) {
            val splits = line.split(" ")
            val shapeOfOpponent = shapeOfStrategyInput[splits.first()]!!
            val suggestedShape = shapeOfStrategyOutput[splits.last()]!!
            val round = Round(shapeOfOpponent, suggestedShape, null)
            rounds += round
        }
        return rounds
    }

    fun calculateRoundsBySuggestedOutcome(input: List<String>): Array<Round> {
        val outcomeOfStrategyOutput = mapOf(
            "X" to Outcome.LOSE ,
            "Y" to Outcome.DRAW ,
            "Z" to Outcome.WIN
        )

        var rounds = arrayOf<Round>()
        for (line in input) {
            val splits = line.split(" ")
            val shapeOfOpponent = shapeOfStrategyInput[splits.first()]!!
            val suggestedOutCome = outcomeOfStrategyOutput[splits.last()]!!
            val round = Round(shapeOfOpponent, null, suggestedOutCome)
            rounds += round
        }
        return rounds
    }

    fun part1(input: List<String>): Int {
        val rounds = calculateRoundsBySuggestedInput(input)
        return rounds.sumOf { it.score }
    }

    fun part2(input: List<String>): Int {
        val rounds = calculateRoundsBySuggestedOutcome(input)
        return rounds.sumOf { it.score }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readTestInput("Day02_sample")
    check(part1(testInput) == 15)
    check(part2(testInput) == 12)

    val input = readTestInput("Day02")
    println("part 1 result: ${part1(input)}")
    println("part 2 result: ${part2(input)}")
}
