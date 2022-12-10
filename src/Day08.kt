import kotlin.math.max

fun main() {
    fun parseTrees(input: List<String>): Array<List<Int>> {
        var treeHeights = arrayOf<List<Int>>()

        for (line in input) {
            val heights = line.map { it.toString().toInt() }
            treeHeights += heights
        }
        return treeHeights
    }

    fun calculateMaxTreePerRow(treeHeights: Array<List<Int>>, startCol: Int, direction: Int): Array<Array<Int>> {
        val rows = treeHeights.size
        val cols = treeHeights[0].size
        val endCol = startCol + direction * cols
        val maxHeights = Array(rows) {Array(cols) {0} }

        for (i in 0 until rows) {
            var j = startCol
            var currentMax = 0
            while (j != endCol) {
                currentMax = if (j == startCol) {
                    treeHeights[i][j]
                } else {
                    maxOf(currentMax, treeHeights[i][j])
                }
                maxHeights[i][j] = currentMax
                j += direction
            }
        }
        return maxHeights
    }

    fun calculateMaxTreePerCol(treeHeights: Array<List<Int>>, startRow: Int, direction: Int): Array<Array<Int>> {
        val rows = treeHeights.size
        val cols = treeHeights[0].size
        val endRow = startRow + direction * rows

        val maxHeights = Array(rows) {Array(cols) {0} }

        for (j in 0 until cols) {
            var i = startRow
            var currentMax = 0
            while (i != endRow) {
                currentMax = if (i == startRow) {
                    treeHeights[i][j]
                } else {
                    maxOf(currentMax, treeHeights[i][j])
                }
                maxHeights[i][j] = currentMax
                i += direction
            }
        }
        return maxHeights
    }

    fun part1(input: List<String>): Int {
        val treeHeights = parseTrees(input)
        val rows = treeHeights.size
        val cols = treeHeights[0].size

        val maxTreeHeightsFromLeft = calculateMaxTreePerRow(treeHeights, startCol = 0, direction = 1)
        val maxTreeHeightFromRight = calculateMaxTreePerRow(treeHeights, startCol = cols -1, direction = -1)
        val maxTreeHeightFromTop = calculateMaxTreePerCol(treeHeights, startRow = 0, direction = 1)
        val maxTreeHeightFromBottom = calculateMaxTreePerCol(treeHeights, startRow = rows - 1, direction = -1)

        var visibleCount = rows * 2 + cols * 2 - 4 // edges of trees
        for (i in 1 until rows - 1) {
            for (j in 1 until cols - 1) {
                val treeHeight = treeHeights[i][j]
                if (treeHeight > maxTreeHeightsFromLeft[i][j-1] ||
                    treeHeight > maxTreeHeightFromRight[i][j+1] ||
                    treeHeight > maxTreeHeightFromTop[i-1][j] ||
                    treeHeight > maxTreeHeightFromBottom[i+1][j]
                ) {
                    visibleCount += 1
                }
            }
        }
        return visibleCount
    }

    fun part2(input: List<String>): Int {
        val treeHeights = parseTrees(input)
        val rows = treeHeights.size
        val cols = treeHeights[0].size

        var highestScore =0
        for (i in 1 until rows - 1) {
            for (j in 1 until cols - 1) {
                var viewDistances = arrayOf<Int>()
                val treeHeight = treeHeights[i][j]

                // left
                var distance = 0
                var jj = j - 1
                while (jj >= 0) {
                    distance += 1
                    if (treeHeights[i][jj] >= treeHeight) {
                        // time to block the view
                        break
                    }
                    jj -= 1
                }
                viewDistances += distance

                // right
                distance = 0
                jj = j + 1
                while (jj <= cols - 1) {
                    distance += 1
                    if (treeHeights[i][jj] >= treeHeight) {
                        // time to block the view
                        break
                    }
                    jj += 1
                }
                viewDistances += distance

                // top
                distance = 0
                var ii = i - 1
                while (ii >= 0) {
                    distance += 1
                    if (treeHeights[ii][j] >= treeHeight) {
                        // time to block the view
                        break
                    }
                    ii -= 1
                }
                viewDistances += distance

                // bottom
                distance = 0
                ii = i + 1
                while (ii <= rows - 1) {
                    distance += 1
                    if (treeHeights[ii][j] >= treeHeight) {
                        // time to block the view
                        break
                    }
                    ii += 1
                }
                viewDistances += distance

                val currentScore = viewDistances.reduce { acc, it ->  acc * it }
                highestScore = max(highestScore, currentScore)
            }
        }
        return highestScore
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readTestInput("Day08_sample")
    check(part1(testInput) == 21)
    check(part2(testInput) == 8)

    val input = readTestInput("Day08")
    println("part 1 result: ${part1(input)}")
    println("part 2 result: ${part2(input)}")
}
