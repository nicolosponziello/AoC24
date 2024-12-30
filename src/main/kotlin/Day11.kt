

fun main() {
    val input = FileReader.readContent("day11.txt")
    val line = parseLine(input)

    //testOne()

    println("Part one: ${partOne(line)}")
    println("Part two: ${partTwo(line)}")
}

// usign a map of occurrencies for each stone, as order is not important
// stone -> occurrencies
private fun parseLine(input: String): MutableMap<Long, Long> {
    val res = mutableMapOf<Long, Long>()
    val splitted = input.split(' ')
    for (part in splitted) {
        if (part.isNotEmpty()) {
            if (res.containsKey(part.toLong())){
                res[part.toLong()] = res[part.toLong()]!! + 1
            } else {
                res[part.toLong()] = 1
            }
        }
    }
    return res
}

private fun partTwo(input: MutableMap<Long, Long>): Long {
    return partOne(input, 75)
}

//refactored to use a hash map to store the stones value and the occurrencies
private fun partOne(input: MutableMap<Long, Long>, iterations: Int = 25): Long {
    var blinked = 0
    var current = input.toMutableMap()
    while (blinked < iterations) {
        val mutated = mutableMapOf<Long, Long>()

        for (pair in current) {
            val stone = pair.key
            val occurrencies = pair.value
            for (new in mutateStone(stone)) {
                if (mutated.containsKey(new)) {
                    mutated[new] = mutated[new]!! + occurrencies
                } else {
                    mutated[new] = occurrencies
                }
            }
        }
        current = mutated
        blinked++
    }

    return current.values.reduce {acc, value -> acc + value}
}

private fun mutateStone(stone: Long) = sequence<Long> {
    if (stone == 0L){
        yield(1L)
    }
    else if (stone.toString().count() % 2 == 0) {
        val length = stone.toString().count()
        val first = stone.toString().take(length / 2)
        val second = stone.toString().drop(first.count()).take(length / 2)
        yield(first.toLong())
        yield(second.toLong())
    } else {
        yield(stone * 2024L)
    }
}

private fun testOne() {
    val input = mutableMapOf(Pair(125L, 1L), Pair(17L, 1L))
    println("Test: ${partOne(input, iterations = 6)} (expected: 22)")
}