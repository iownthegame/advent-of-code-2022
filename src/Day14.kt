import kotlin.math.max
import kotlin.math.min

class RockLine(val fromPoint: Position, val toPoint: Position)

fun main() {
    fun parseMap(input: List<String>, extendsFloor: Boolean): Array<Array<Char>> {
        val rockLines = mutableListOf<RockLine>()
        var maxX: Int = 0
        var maxY: Int = 0

        for (line in input) {
            val splits = line.split(" -> ")
            var previousRockPoint: Position? = null
            for (rock in splits) {
                val splits2 = rock.split(",")
                val x = splits2.last().toInt()
                val y = splits2.first().toInt()
                maxX = max(maxX, x)
                maxY = max(maxY, y)
                val point = Position(x, y)
                if (previousRockPoint != null) {
                    rockLines.add(RockLine(previousRockPoint, point))
                }
                previousRockPoint = point
            }
        }

        val extendedX = if (extendsFloor) 2 else 0
        val extendedY = if (extendsFloor) maxY else 0
        val map = Array(maxX + 1 + extendedX) { Array(maxY + 1 + extendedY) { '.' } }
        for (rockLine in rockLines) {
            val fromPoint = rockLine.fromPoint
            val toPoint = rockLine.toPoint

            if (fromPoint.x == toPoint.x) {
                val i = fromPoint.x
                if (fromPoint.y > toPoint.y) {
                    for (j in toPoint.y..fromPoint.y) {
                        map[i][j] = '#'
                    }
                } else {
                    for (j in fromPoint.y..toPoint.y) {
                        map[i][j] = '#'
                    }
                }
            }  else if (fromPoint.y == toPoint.y) {
                val j = fromPoint.y
                if (fromPoint.x > toPoint.x) {
                    for (i in toPoint.x..fromPoint.x) {
                        map[i][j] = '#'
                    }
                } else {
                    for (i in fromPoint.x..toPoint.x) {
                        map[i][j] = '#'
                    }
                }
            } else {
                throw Exception(" error")
            }
        }
        return map
    }

    fun moveSand(map: Array<Array<Char>>, sandPosition: Position, width: Int, height: Int): Position {
        fun canMove(position: Position): Boolean {
            if ((position.x in 0 until width) && (position.y in 0 until height)) {
                return map[position.x][position.y] == '.'
            }
            return false
        }

        val currentSandPosition = Position(sandPosition.x, sandPosition.y)

        while(true) {
            val stepDownPosition = Position(currentSandPosition.x + 1, currentSandPosition.y)
            val stepDownLeftPosition = Position(currentSandPosition.x + 1, currentSandPosition.y - 1)
            val stepDownRightPosition = Position(currentSandPosition.x + 1, currentSandPosition.y + 1)

            val tryPositions = arrayOf(stepDownPosition, stepDownLeftPosition, stepDownRightPosition)
            var moved = false
            for (position in tryPositions) {
                if (canMove(position)) {
                    currentSandPosition.x = position.x
                    currentSandPosition.y = position.y
                    moved = true
//                    println("move sand to $currentSandPosition")
                    break
                }
            }

            if (!moved) {
                // time to rest
//                println("rest at $currentSandPosition")
                return currentSandPosition
            }
        }
    }

    fun sandReachesLowestRock(map: Array<Array<Char>>, width: Int, height: Int, position: Position): Boolean {
        var maxX: Int = 0

        for (j in 0 until height) {
            for (i in width-1 downTo 0 ) {
                if (map[i][j] == '#') {
                    maxX = max(maxX, i)
                    break
                }
            }
        }

        return position.x == maxX
    }

    fun getFloorX(map: Array<Array<Char>>, width: Int, height: Int): Int {
        var maxX: Int = 0

        for (j in 0 until height) {
            for (i in width-1 downTo 0 ) {
                if (map[i][j] == '#') {
                    maxX = max(maxX, i)
                    break
                }
            }
        }

        return maxX + 2
    }

    fun printMap(map: Array<Array<Char>>, width: Int, height: Int) {
        // print
        var minX: Int = 10000
        var minY: Int = 10000
        var maxX: Int = 0
        var maxY: Int = 0

        for (i in 0 until width) {
            for (j in 0 until height) {
                if (map[i][j] == 'O' || map[i][j] == '+' ) {
                    minY = min(minY, j)
                    break
                }
            }
            for (j in (height - 1) downTo 0) {
                if (map[i][j] == 'O' || map[i][j] == '+' ) {
                    maxY = max(maxY, j)
                    break
                }
            }
        }

        for (j in 0 until height) {
            for (i in 0 until width) {
                if (map[i][j] == 'O' || map[i][j] == '+' ) {
                    minX = min(minX, i)
                    break
                }
            }
            for (i in width-1 downTo 0 ) {
                if (map[i][j] == 'O' || map[i][j] == '+' ) {
                    maxX = max(maxX, i)
                    break
                }
            }
        }

//        println("minX: $minX, maxX: $maxX, minY: $minY, maxY: $maxY")

        for (i in minX..maxX) {
            for (j in minY..maxY) {
                print(map[i][j])
            }
            println()
        }
    }

    fun part1(input: List<String>): Int {
        val map = parseMap(input, false)
        val width = map.size
        val height = map[0].size
        var steps = 0
        val sandPosition = Position(0, 500)
        map[sandPosition.x][sandPosition.y] = '+'

       while(true) {
            val restPosition = moveSand(map, sandPosition, width, height)
            map[restPosition.x][restPosition.y] = 'O'

            if (sandReachesLowestRock(map, width, height, restPosition)) {
                break
            }
           steps += 1
       }

//        printMap(map, width, height)
        return steps
    }

    fun part2(input: List<String>): Int {
        val map = parseMap(input, true)
        val width = map.size
        val height = map[0].size
        var steps = 0
        val sandPosition = Position(0, 500)
        map[sandPosition.x][sandPosition.y] = '+'

        val floorX = getFloorX(map, width, height)
        for (j in 0 until height) {
            map[floorX][j] = '#'
        }

        while(true) {
            val restPosition = moveSand(map, sandPosition, width, height)
            map[restPosition.x][restPosition.y] = 'O'
            steps += 1

            if (restPosition.x == sandPosition.x && restPosition.y == sandPosition.y) {
                break
            }
        }

//        printMap(map, width, height)
        return steps
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readTestInput("Day14_sample")
    check(part1(testInput) == 24)
    check(part2(testInput) == 93)


    val input = readTestInput("Day14")
    println("part 1 result: ${part1(input)}")
    println("part 2 result: ${part2(input)}")
}
