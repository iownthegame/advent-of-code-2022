import java.math.BigDecimal
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class Sensor(val position: Position, val beaconPosition: Position)

fun main() {
    fun parseMap(input: List<String>): MutableList<Sensor> {
        // Sensor at x=20, y=1: closest beacon is at x=15, y=3

        val sensors = mutableListOf<Sensor>()

        for (line in input) {
            val splits = line.split(": ")
            val sensorSplits = splits.first().split(", ")
            val beaconSplits = splits.last().split(", ")
            val sensorX = sensorSplits.last().split("=").last().toInt()
            val sensorY = sensorSplits.first().split("=").last().toInt()
            val beaconX = beaconSplits.last().split("=").last().toInt()
            val beaconY = beaconSplits.first().split("=").last().toInt()

            val sensor = Sensor(Position(sensorX, sensorY), Position(beaconX, beaconY))
            sensors.add(sensor)
        }

        return sensors
    }

    fun distance(positionA: Position, positionB: Position): Int {
        return abs(positionA.x - positionB.x) + abs(positionA.y - positionB.y)
    }

    fun mergeRanges(ranges: Array<Array<Int>>): Array<Array<Int>>{
        var newRanges = arrayOf<Array<Int>>()
        var previousRange: Array<Int>? = null

        for ((index, range) in ranges.withIndex()) {
            if (index == 0) {
                previousRange = range
                continue
            }
            if (range.first() - previousRange!!.last() > 1) {
                // no intersection
                newRanges += previousRange
                previousRange = range
                continue
            }
            previousRange = arrayOf(previousRange.first(), max(previousRange.last(), range.last()))
        }
        newRanges += previousRange!! // last one
        return newRanges
    }

    fun getCoverRanges(sensors: MutableList<Sensor>, targetX: Int, excludeBeacon: Boolean, minX: Int?, maxX: Int?): Array<Array<Int>>{
        var coverRanges = arrayOf<Array<Int>>()

        for (sensor in sensors) {
            val sensorPoint = sensor.position
            val beaconPoint = sensor.beaconPosition
            val distance = distance(sensorPoint, beaconPoint)
            val targetPoint = Position(targetX, sensorPoint.y)
            val targetDistance = distance(targetPoint, sensorPoint)
            val coverageDistance = distance - targetDistance
            if (coverageDistance > 0) {
                var left = sensorPoint.y - coverageDistance
                var right = sensorPoint.y + coverageDistance
                if (excludeBeacon && left == beaconPoint.y) {
                    left += 1
                }
                if (excludeBeacon && right == beaconPoint.y) {
                    right -= 1
                }
                if (minX != null) {
                    left = max(minX, left)
                }
                if (maxX != null) {
                    right = min(maxX, right)
                }
                coverRanges += arrayOf(left, right)
            }
        }

        return coverRanges
    }

    fun part1(input: List<String>, targetX: Int): Int {
        val sensors = parseMap(input)

        var coverRanges = getCoverRanges(sensors, targetX, true, null, null)
        coverRanges.sortBy { it.first() }
//        println("ranges (before merge): ${coverRanges.map { "[" + it.joinToString(", ") + "]" }}")
        coverRanges = mergeRanges(coverRanges)
//        println("ranges: ${coverRanges.map { "[" + it.joinToString(", ") + "]" }}")

        return coverRanges.sumOf { it.last() - it.first() + 1 }
    }

    fun part2(input: List<String>, fromX: Int, toX: Int): BigDecimal {
        val sensors = parseMap(input)

        var distressBeaconPoint: Position? = null
        for (x in fromX..toX) {
            var coverRanges = getCoverRanges(sensors, x, false, 0, 4000000)
            coverRanges.sortBy { it.first() }
//            println("ranges (before merge): ${coverRanges.map { "[" + it.joinToString(", ") + "]" }}")
            coverRanges = mergeRanges(coverRanges)
//            println("ranges for x: $x: ${coverRanges.map { "[" + it.joinToString(", ") + "]" }}")
            if (coverRanges.size == 2) {
                distressBeaconPoint = Position(x, coverRanges.first().last() + 1)
            }
        }
//        println("distressBeaconPoint: $distressBeaconPoint")
        return BigDecimal(distressBeaconPoint!!.y).multiply(BigDecimal(4000000)).plus(BigDecimal(distressBeaconPoint.x))
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readTestInput("Day15_sample")
    check(part1(testInput, 10) == 26)
    check(part2(testInput, 0, 20) == BigDecimal(56000011))


    val input = readTestInput("Day15")
    println("part 1 result: ${part1(input, 2000000)}")
    println("part 2 result: ${part2(input, 0, 4000000).toPlainString()}")
}
