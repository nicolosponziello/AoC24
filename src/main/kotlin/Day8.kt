fun main() {
    val input = FileReader.readAllLines("day8.txt")
    val matrix = buildMatrix(input)

    testOne()

    val countPartOne = countAntinodes(matrix)
    println("Part one: $countPartOne")

    val countPartTwo = countAntinodesExtended(matrix)
    println("Part two: $countPartTwo")
}

// Count all the unique antinodes:
// an antinode occurs at any point that is perfectly in line with two antennas of the same frequency
// - but only when one of the antennas is twice as far away as the other.
// This means that for any pair of antennas with the same frequency, there are two antinodes, one on either side of them.
private fun countAntinodes(matrix: MutableList<CharArray>): Int {
    val antinodes = mutableSetOf<Coordinate>()
    val antennasMap = buildAntennasMap(matrix)

    //for each antenna type, calculate if there is a pair of coordinates that can generate an antidote
    for (entry in antennasMap) {
        if (entry.value.count() < 2) {
            //cannot generate antinodes with only one antenna
            continue
        }

        //generate all the combinations
        val towerCombinations = generateCombinations(entry.value)
        for (combination in towerCombinations) {
            //calculate vector between the two antennas
            val diff = combination.first - combination.second

            //create possible antinodes positions by from the two antenna points and filter the ones outside the map
            antinodes.addAll(listOf(combination.first + diff, combination.second - diff).filter { !isOutsideBounds(it, matrix) })
        }
    }
    return antinodes.count()
}

private fun countAntinodesExtended(matrix: MutableList<CharArray>): Int {
    val antinodes = mutableSetOf<Coordinate>()
    val antennasMap = buildAntennasMap(matrix)

    //for each antenna type, calculate if there is a pair of coordinates that can generate an antidote
    for (entry in antennasMap) {
        if (entry.value.count() < 2) {
            //cannot generate antinodes with only one antenna
            continue
        }

        //generate all the combinations
        val towerCombinations = generateCombinations(entry.value)

        for (combination in towerCombinations) {
            //calculate vector between the two antennas
            val vect = combination.second - combination.first

            //extend from first antenna and then from the second
            antinodes.addAll(extendFrom(combination.first, vect, matrix, true))
            antinodes.addAll(extendFrom(combination.second, vect, matrix, false))
        }
    }
    return antinodes.count()
}

//calculate antinodes in the dir direction while in the matrix
private fun extendFrom(pos: Coordinate, dir: Coordinate, matrix: MutableList<CharArray>, sum: Boolean): Set<Coordinate> {
    val coordinates = mutableSetOf<Coordinate>()
    var currentPos = pos
    while (true) {
        val nextAntinode = if (sum) currentPos + dir else currentPos - dir
        if (isOutsideBounds(nextAntinode, matrix)) {
            return coordinates
        }
        coordinates.add(nextAntinode)
        currentPos = nextAntinode
    }
}

private fun buildAntennasMap(matrix: MutableList<CharArray>): Map<Char, List<Coordinate>> {
    val map = mutableMapOf<Char, MutableList<Coordinate>>()
    for (rowIndex in 0..<matrix.count()) {
        val row = matrix[rowIndex]
        for (colIndex in row.indices) {
            val cell = row[colIndex]
            if (cell == '.') {
                continue
            }

            val coordinate = Coordinate(rowIndex, colIndex)
            if (map.containsKey(cell)) {
                map[cell]?.add(coordinate)
            } else {
                map[cell] = mutableListOf<Coordinate>()
                map[cell]?.add(coordinate)
            }
        }
    }
    return map
}


private fun testOne() {
    val input = listOf("..........",
                                    "..........",
                                    "..........",
                                    "....a.....",
                                    "..........",
                                    ".....a....",
                                    "..........",
                                    "..........",
                                    "..........",
                                    "..........")

    println("Test one: ${countAntinodes(buildMatrix(input))}, expected 2")
}