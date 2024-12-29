fun main() {
    val input = FileReader.readContent("day11.txt")
    val line = parseLine(input)

    testOne()

    println("Part one: ${partOne(line)}")
    println("Part two: ${partTwo(line)}")
}

private fun parseLine(input: String): MutableList<Long> {
    val res = mutableListOf<Long>()
    val splitted = input.split(' ')
    for (part in splitted) {
        if (part.isNotEmpty()) {
            res.add(part.toLong())
        }
    }
    return res
}

//use memoization to improve performance
private fun partTwo(input: MutableList<Long>): Long {
    return partOne(input, false, 75)
}

private fun partOne(input: MutableList<Long>, print: Boolean = false, iterations: Int = 25): Long {
    var blinked = 0
    while (blinked < iterations) {
        var i = 0
        while (i < input.count()) {
            //rule one
            if (input[i] == 0L) {
                input[i] = 1L
                i++
                continue
            }

            //rule 2
            if (input[i].toString().count() % 2 == 0) {
                val length = input[i].toString().count()
                val first = input[i].toString().take(length / 2)
                val second = input[i].toString().drop(first.count()).take(length / 2)
                input[i] = first.toLong()
                input.add(i+1, second.toLong())
                i += 2
                continue
            }

            //rule 3
            input[i] = input[i] * 2024
            i++
        }
        if (print) println(input)
        blinked++
    }
    return input.size.toLong()
}

private fun testOne() {
    val input = mutableListOf(125L, 17L)
    println("Test: ${partOne(input, print = true, iterations = 6)}")
}