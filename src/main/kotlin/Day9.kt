fun main() {
    val line = FileReader.readContent("day9.txt")

    //part one
    test()
    println("Part one: ${partOne(line)}")

    //part two
    testPartTwo()
    println("Part two: ${partTwo(line)}")
}

private fun partOne(input: String): Long {
    val newString = parseInput(input)
    val reordered = reorder(newString)
    return calculateHash(reordered)
}

//move file entirely without splitting
//from righ to left. if no space, do not move the file
private fun partTwo(input: String): Long {
    //create a list
    val blocks = parseInput(input)

    val reordered = reorderInBlocks(blocks)
    return calculateHash(reordered)
}

private fun reorderInBlocks(blocks: List<Int>): List<Int> {
    val input = blocks.toMutableList()
    //create a map of files (start index, how big) and empty spaces (startIndex and how big)
    var p1 = 0
    var p2 = input.lastIndex
    while (p2 > 0){
        //search for empty block on the left that can fit the file
        if (input[p2] == -1) {
            p2--
            continue
        }

        val fileSize = calculateSize(input, p2, backwards = true)
        p1 = 0
        //try to find an empty space
        while(p1 < p2) {
            if (input[p1] != -1){
                p1++
                continue
            }

            //calculate empty space
            val emptySize = calculateSize(input, p1)
            if (fileSize > emptySize) {
                p1 += emptySize
                continue
            }
            val valueToCopy = input[p2]
            copySize(input, p1, fileSize, valueToCopy)
            copySize(input, p2, fileSize, -1, true)
            p2 -= fileSize
            break
        }
        //if we end up here we could not find an empty block for the file
        if (p1 >= p2) {
            p2 -= fileSize
        }
    }

    return input
}

private fun calculateSize(input: List<Int>, startIdx: Int, backwards: Boolean = false): Int {
    val targetValue = input[startIdx]
    var idx = startIdx
    var size = 0
    while (idx >= 0 && input[idx] == targetValue) {
        idx = if (backwards) idx -1 else idx + 1
        size++
    }
    return size
}

private fun copySize(input: MutableList<Int>, startIdx: Int, size: Int, valueToCopy: Int, backwards: Boolean = false) {
    var copied = 0
    var idx = startIdx
    while (copied < size) {
        input[idx] = valueToCopy
        idx = if (backwards) idx -1 else idx + 1
        copied++
    }
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

private fun testPartTwo() {
    val input = "2333133121414131402"
    val parsed = parseInput(input)
    println("Parsed: $parsed")
    val reordered = reorderInBlocks(parsed)
    println("Reordered: $reordered")
    println("Hash: ${calculateHash(reordered)}")
}