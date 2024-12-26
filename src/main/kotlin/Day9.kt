fun main() {
    val line = FileReader.readContent("day9.txt")

    test()

    println("Part one: ${partOne(line)}")
}

private fun partOne(input: String): Int {
    val newString = parseInput(input)
    println(newString)
    val reordered = reorder(newString)
    return calculateHash(reordered)
}

private fun calculateHash(input: List<String>): Int {
    var count = 0
    for (i in input.indices) {
        if (input[i] == ".") {
            return count
        }

        count += input[i].toInt() * i
    }

    return count
}

private fun reorder(input: String): List<String> {
    val mutableInput = input.toMutableList()
    for (i in 0..<input.length) {
        if (input[i] != '.') {
            mutableInput.set(i, mutableInput[i])
        } else {
            //find the left most value that can be moved to the empty block in pos i
            val j = findRightMostIndex(mutableInput.joinToString(separator = "."))
            //set char at j in i and . in j
            if (j <= i){
                break
            }
            mutableInput.set(i, input[j])
            mutableInput.set(j, '.')
        }
    }
    return mutableInput.toList()
}

private fun findRightMostIndex(input: String): Int {
    var index = input.length - 1
    while (index >= 0 && input[index] == '.'){
        index--
    }
    return index
}

private fun parseInput(input: String): String {
    //0 - 2 ... => files
    //1 - 3 ... => empty blocks
    val parsed = mutableListOf<String>()
    var fileId = 0
    for (idx in 0..<input.length) {
        if (idx % 2 == 0) {
            //is file
            repeat(input[idx].digitToInt()) {
                parsed.add(fileId.toString())
            }
            fileId++
        } else {
            //is empty
            repeat(input[idx].digitToInt()) {
                parsed.add(".")
            }
        }
    }
    return parsed.joinToString(separator = "")
}

private fun test() {
    val input = "2333133121414131402"
    val parsed = parseInput(input)
    println("Parsed: $parsed")
    val reordered = reorder(parsed)
    println("Reordered: $reordered")
    println("Hash: ${calculateHash(reordered)}")
}