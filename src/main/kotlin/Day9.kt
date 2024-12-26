fun main() {
    val line = FileReader.readContent("day9.txt")

    test()

    println("Part one: ${partOne(line)}")
}

private fun partOne(input: String): Long {
    val newString = parseInput(input)
    val reordered = reorder(newString)
    return calculateHash(reordered)
}

private fun calculateHash(input: List<Int>): Long {
    var count = 0L
    for (i in input.indices) {
        if (input[i] == -1) {
            continue
        }

        count += input[i] * i
    }

    return count
}

private fun reorder(input: MutableList<Int>): List<Int> {
    val mutableInput = input.toMutableList()
    var p1 = 0
    var p2 = input.lastIndex
    while (p1 < p2) {
        if (input[p1] != -1) {
            p1++
            continue
        }
        if (input[p2] == -1) {
            p2--
            continue
        }
        mutableInput[p1] = input[p2]
        mutableInput[p2] = -1
        p1++
        p2--
    }
    return mutableInput.toList()
}

private fun parseInput(input: String): MutableList<Int> {
    //0 - 2 ... => files
    //1 - 3 ... => empty blocks
    val parsed = mutableListOf<Int>()
    var fileId = 0
    for (idx in 0..<input.length) {
        if (idx % 2 == 0) {
            //is file
            repeat(input[idx].digitToInt()) {
                parsed.add(fileId)
            }
            fileId++
        } else {
            //is empty
            repeat(input[idx].digitToInt()) {
                parsed.add(-1)
            }
        }
    }
    return parsed
}

private fun test() {
    val input = "2333133121414131402"
    val parsed = parseInput(input)
    println("Parsed: $parsed")
    val reordered = reorder(parsed)
    println("Reordered: $reordered")
    println("Hash: ${calculateHash(reordered)}")
}