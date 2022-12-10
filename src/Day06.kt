fun main() {
    fun findFirstMessage(n: Int, input: String): Int {
        val length = input.length
        for (i in 0 until length) {
            val string = input.substring(i, i + n)
            if (string.toSet().size == n) {
                return i + n
            }
        }
        return 0
    }

    fun part1(input: String): Int {
        return findFirstMessage(4, input)
    }

    fun part2(input: String): Int {
        return findFirstMessage(14, input)
    }

    // test if implementation meets criteria from the description, like:
    check(part1("mjqjpqmgbljsphdztnvjfqwrcgsmlb") == 7)
    check(part1("bvwbjplbgvbhsrlpgdmjqwftvncz") == 5)
    check(part1("nppdvjthqldpwncqszvftbrmjlhg") == 6)
    check(part1("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg") == 10)
    check(part1("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw") == 11)

    check(part2("mjqjpqmgbljsphdztnvjfqwrcgsmlb") == 19)
    check(part2("bvwbjplbgvbhsrlpgdmjqwftvncz") == 23)
    check(part2("nppdvjthqldpwncqszvftbrmjlhg") == 23)
    check(part2("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg") == 29)
    check(part2("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw") == 26)

    val input = readTestInput("Day06")[0]
    println("part 1 result: ${part1(input)}")
    println("part 2 result: ${part2(input)}")
}
