import java.util.Queue
import kotlin.Pair

class Point(val x: Int, val y: Int, val value: Char) {
    fun sameAs(anotherPoint: Point): Boolean {
        return anotherPoint.x == x && anotherPoint.y == y
    }
//    fun inRange(rows: Int, cols: Int): Boolean {
//        return (x in 0 until rows) && (y in 0 until cols)
//    }

    override fun toString(): String {
        return "(x: $x, y: $y)"
    }
}

fun main() {
//    fun dfs(grid: Array<Array<Char>>, rows: Int, cols: Int, currentPoint: Point, currentPointValue: Char, endPoint: Point, currentSteps: Int, totalSteps: MutableList<Int>) {
//        if (currentPoint.sameAs(endPoint)) {
//            totalSteps.add(currentSteps)
//            return
//        }
//
//        if (totalSteps.isNotEmpty() && currentSteps >= totalSteps.min()) {
//            return
//        }
//
//        val directions = arrayOf(arrayOf(0, -1), arrayOf(0, 1), arrayOf(-1, 0), arrayOf(1, 0))
//        for (direction in directions) {
//            val newPoint = Point(currentPoint.x + direction[0], currentPoint.y + direction[1])
//            if (!newPoint.inRange(rows, cols)) {
//                continue
//            }
//            if (grid[newPoint.x][newPoint.y] == "X".toCharArray().first()) { // visited
//                continue
//            }
//
//            val newPointValue = grid[newPoint.x][newPoint.y]
//            if (newPointValue.code - currentPointValue.code > 1) {
//                continue
//            }
//            grid[newPoint.x][newPoint.y] = "X".toCharArray().first()
//            dfs(grid, rows, cols, newPoint, newPointValue, endPoint, currentSteps + 1, totalSteps)
//            grid[newPoint.x][newPoint.y] = newPointValue
//        }
//    }

    fun bfs(grid: Array<Array<Point>>, rows: Int, cols: Int, startPoints: Array<Point>, endPoint: Point): Int {
        val visitedPoints = ArrayDeque<Point>()
        startPoints.forEach {  visitedPoints.add(it) }
        val directions = arrayOf(arrayOf(0, -1), arrayOf(0, 1), arrayOf(-1, 0), arrayOf(1, 0))

        var steps = 0

        while (true) {
            for (i in 1..visitedPoints.size) {
                val currentPoint = visitedPoints.removeFirst()

                for (direction in directions) {
                    val newX = currentPoint.x + direction[0]
                    val newY = currentPoint.y + direction[1]
                    if (!((newX in 0 until rows) && (newY in 0 until cols))) {
                        continue
                    }
                    val newPoint = grid[newX][newY]
                    if (visitedPoints.contains(newPoint)) {
                        continue
                    }
                    if (newPoint.value.code - currentPoint.value.code > 1) {
                        continue
                    }
                    visitedPoints.add(newPoint)
                }
            }
            steps += 1
            if (visitedPoints.contains(endPoint)) {
                return steps
            }
        }
    }

    fun part1(input: List<String>): Int {
        val rows = input.size
        val cols = input[0].length
        val grid = Array(rows) { Array(cols) { Point(-1, -1, '1') } }
        var startPoint = Point(-1, -1, '1')
        var endPoint = Point(-1, -1, '1')

        for ((i, line) in input.withIndex()) {
            for((j, c) in line.withIndex()) {
                if (c.toString() == "S") {
                    startPoint = Point(i, j, 'a')
                    grid[i][j] = startPoint
                } else if (c.toString() == "E") {
                    endPoint = Point(i, j, 'z')
                    grid[i][j] = endPoint
                } else {
                    grid[i][j] = Point(i, j, c)
                }
            }
        }

        // dfs works for sample data, not work for actual data
//        val totalSteps = mutableListOf<Int>()
//        val startPointValue = grid[startPoint.x][startPoint.y]
//        grid[startPoint.x][startPoint.y] = "X".toCharArray().first()
//        dfs(grid, rows, cols, startPoint, startPointValue, endPoint, 0, totalSteps)

        return bfs(grid, rows, cols, arrayOf(startPoint), endPoint)
    }

    fun part2(input: List<String>): Int {
        val rows = input.size
        val cols = input[0].length
        val grid = Array(rows) { Array(cols) { Point(-1, -1, '1') } }
        var startPoints = arrayOf<Point>()
        var endPoint = Point(-1, -1, '1')

        for ((i, line) in input.withIndex()) {
            for((j, c) in line.withIndex()) {
                if (c.toString() == "S" || c.toString() == "a") {
                    val startPoint = Point(i, j, 'a')
                    grid[i][j] = startPoint
                    startPoints += startPoint
                } else if (c.toString() == "E") {
                    endPoint = Point(i, j, 'z')
                    grid[i][j] = endPoint
                } else {
                    grid[i][j] = Point(i, j, c)
                }
            }
        }

        return bfs(grid, rows, cols, startPoints, endPoint)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readTestInput("Day12_sample")
    check(part1(testInput) == 31)
    check(part2(testInput) == 29)


    val input = readTestInput("Day12")
    println("part 1 result: ${part1(input)}")
    println("part 2 result: ${part2(input)}")
}
