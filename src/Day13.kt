import java.util.Stack
import kotlin.math.max

class PacketElement() {
    private val elements = mutableListOf<PacketElement>()
    private var value: Int? = null

    fun isInteger(): Boolean {
        return value != null
    }

    fun getInteger(): Int {
        return value!!
    }

    fun getList(): List<PacketElement> {
        if (isInteger()) {
            val newPacketElement = PacketElement()
            newPacketElement.setInteger(getInteger())
            return listOf(newPacketElement)
        }
        return elements
    }

    fun addInteger(integer: Int) {
        val newPacketElement = PacketElement()
        newPacketElement.setInteger(integer)
        addElement(newPacketElement)
    }

    private fun setInteger(integer: Int) {
        value = integer
    }

    fun addElement(element: PacketElement) {
        elements.add(element)
    }

    override fun toString(): String {
        return if (isInteger()) {
            getInteger().toString()
        } else {
            "[" + elements.joinToString(", ") { it.toString() } + "]"
        }
    }
}

enum class Order {
    RIGHT,
    NOT_RIGHT,
    SAME
}

fun main() {
    fun parsePacketElement(line: String): PacketElement {
        /*
            example [1,[2,[3,[4,[5,6,7]]]],8,9]


            '[' -> new stack
                ',' -> push 1
                '[' -> new stack
                    ',' -> push 2
                    '[' -> new stack
                        ',' -> push 3
                        '[' -> new stack
                            ',' -> push 4
                            '[' -> new stack
                                ',' -> push 5
                                ',' -> push 6
                             ']' -> push 7, pop stack [5, 6, 7]
                        ']' -> push [5, 6, 7], pop stack [4, [5, 6, 7]]
                    ']' -> push [4, [5, 6, 7]], pop stack [3, [4, [5, 6, 7]]]
                ']' -> push [3, [4, [5, 6, 7]]], pop stack [2, [3, [4, [5, 6, 7]]]]
                ',' -> push [2, [3, [4, [5, 6, 7]]]],
                ',' -> push 8
            ']' -> push 9

            -> [1, big array, 8, 9]

        */

        val stack = Stack<PacketElement>()
        var parseIndex = -1
        val rootElement = PacketElement()
//        println("parse line $line")
        for ((index, c) in line.withIndex()) {
//            println("char: '$c', index: $index")
            if (c == '[') {
                // enter new stack
                stack.add(PacketElement())
                parseIndex = -1
//                println("add stack: ${stack.size}, pIndex: $parseIndex")
            } else if (c == ']') {
                val currentElement = stack.peek()
                // leave current stack
                if (parseIndex != -1) {
                    // next element
                    val value = line.substring(parseIndex, index).toInt()
//                    println("add value $value, fromIndex: $parseIndex, toIndex: $index")
                    currentElement.addInteger(value)
                    parseIndex = -1
//                    println("add $value to stack: ${stack.size} (length: ${currentElement.getList().size}), pIndex: $parseIndex")
                }
//                println("pop stack: ${stack.size}")
                stack.pop()
                if (stack.isNotEmpty()) {
                    val topElement = stack.peek()
                    topElement.addElement(currentElement)
//                    println("add popped element to stack: ${stack.size} (length: ${topElement.getList().size})")
                } else {
                    rootElement.addElement(currentElement)
//                    println("add popped element to rootElement: (length: ${rootElement.getList().size})")
                }
            } else if (c == ',') {
                if (parseIndex == -1) {
                    continue
                } else {
                    // next element
                    val value = line.substring(parseIndex, index).toInt()
//                    println("add value $value, fromIndex: $parseIndex, toIndex: $index")
                    if (stack.isNotEmpty()) {
                        val currentElement = stack.peek()
                        currentElement.addInteger(value)
                        parseIndex = -1
//                        println("add $value to stack: ${stack.size} (length: ${currentElement.getList().size}), pIndex: $parseIndex")
                    } else {
                        rootElement.addInteger(value)
                        parseIndex = -1
//                        println("add $value to rootElement (length: ${rootElement.getList().size}), pIndex: $parseIndex")
                    }
                }
            } else if (parseIndex == -1) {
                parseIndex = index
//                println("update pIndex: $parseIndex")
            }
        }
        return rootElement
    }

    fun comparePacketElement(firstPacketElement: PacketElement, secondPacketElement: PacketElement): Order {
        /*
                If both values are integers, the lower integer should come first.
                If the left integer is lower than the right integer, the inputs are in the right order.
                If the left integer is higher than the right integer, the inputs are not in the right order.
                Otherwise, the inputs are the same integer; continue checking the next part of the input.
         */

        if (firstPacketElement.isInteger() && secondPacketElement.isInteger()) {
            val firstPacketInteger = firstPacketElement.getInteger()
            val secondPacketInteger = secondPacketElement.getInteger()
            if (firstPacketInteger < secondPacketInteger) {
                return Order.RIGHT
            }
            if (firstPacketInteger > secondPacketInteger) {
                return Order.NOT_RIGHT
            }
            return Order.SAME
        }

        /*
            If exactly one value is an integer, convert the integer to a list which contains that integer as its only value, then retry the comparison.
                For example, if comparing [0,0,0] and 2, convert the right value to [2] (a list containing 2); the result is then found by instead comparing [0,0,0] and [2].

            If both values are lists, compare the first value of each list, then the second value, and so on.
                If the left list runs out of items first, the inputs are in the right order.
                If the right list runs out of items first, the inputs are not in the right order.
                If the lists are the same length and no comparison makes a decision about the order, continue checking the next part of the input.
         */
        val firstPacketElements = firstPacketElement.getList()
        val secondPacketElements = secondPacketElement.getList()

        if (firstPacketElements.isEmpty() && secondPacketElements.isEmpty()) {
            // does it ever happen?
            return Order.SAME
        }
        val maxLength = max(firstPacketElements.size, secondPacketElements.size)
        for (i in 0 until maxLength) {
            if (i >= firstPacketElements.size) {
                // left list runs out of items first
                return Order.RIGHT
            }
            if (i >= secondPacketElements.size) {
                //  right list runs out of items first
                return Order.NOT_RIGHT
            }
            val order = comparePacketElement(firstPacketElements[i], secondPacketElements[i])
            if (order == Order.SAME) {
                continue
            }
            return order
        }

        return Order.SAME
    }

    fun part1(input: List<String>): Int {
        var pairIndex: Int = 0;
        val packetElements = mutableListOf<PacketElement>()
        var validIndexOfPairs = arrayOf<Int>()

        for (line in input) {
            if (line.isEmpty()) {
                continue
            }
            val packetElement = parsePacketElement(line)
//            println("parsed: $packetElement")
            packetElements.add(packetElement)
            if (packetElements.size == 2) {
                pairIndex += 1
                val order = comparePacketElement(packetElements.first(), packetElements.last())
                if (order == Order.RIGHT) {
                    validIndexOfPairs += pairIndex
                }
                packetElements.clear()
            }
        }
//        println("validIndexOfPairs: ${validIndexOfPairs.joinToString(", ")}")
        return validIndexOfPairs.sum()
    }

    fun part2(input: List<String>): Int {
        val packetElements = mutableListOf<PacketElement>()

        for (line in input) {
            if (line.isEmpty()) {
                continue
            }
            val packetElement = parsePacketElement(line)
//            println("parsed: $packetElement")
            packetElements.add(packetElement)
        }

        val dividerPacketElements = listOf(parsePacketElement("[[2]]"), parsePacketElement("[[6]]"))
        packetElements.addAll(dividerPacketElements)

        // sort by the right order
        packetElements.sortWith(Comparator { a, b ->
            when (comparePacketElement(a, b)) {
                Order.RIGHT -> {
                    return@Comparator -1
                }
                Order.NOT_RIGHT -> {
                    return@Comparator 1
                }
                else -> {
                    return@Comparator 0
                }
            }
        })

        // determine the indices of the two divider packets and multiply them together
        var result = 1
        for ((index, element) in packetElements.withIndex()) {
            if (dividerPacketElements.contains(element)) {
                result *= (index + 1)
            }
        }
        return result
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readTestInput("Day13_sample")
    check(part1(testInput) == 13)
    check(part2(testInput) == 140)


    val input = readTestInput("Day13")
    println("part 1 result: ${part1(input)}")
    println("part 2 result: ${part2(input)}")
}
