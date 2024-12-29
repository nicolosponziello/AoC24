fun main() {
    val input = FileReader.readAllLines("day10.txt")
    val matrix = buildMatrixAsIntArrays(input)

    testOne()
    println("Part one ${partOne(matrix)}")
}

private fun partOne(matrix: List<List<Int>>): Int {
    var totalScore = 0
    val startingCoordinates = findZeroes(matrix)
    for (startingPos in startingCoordinates) {
        val score = calculateScoreStartingIn(startingPos, matrix)
        totalScore += score
    }
    return totalScore
}

private fun calculateScoreStartingIn(starting: Coordinate, matrix: List<List<Int>>): Int {
    var score = 0
    val visited = mutableListOf<Coordinate>()
    val stack = java.util.ArrayDeque<Coordinate>()
    //push starting and its neighbours
    stack.push(starting)
    while (!stack.isEmpty()) {
        val current = stack.pop()
        // if the node from the stack is the end of the trail, increment the score
        if (matrix[current.row][current.col] == 9) {
            score++
            visited.add(current)
            continue
        }

        //otherwise, get its neighbours in the stack and continue
        getNeighbours(current, matrix).forEach {
            if (visited.contains(it) == false) {
                stack.push(it)
            }
        }
        visited.add(current)
    }


    return score
}

private fun getNeighbours(pos: Coordinate, matrix: List<List<Int>>): List<Coordinate> {
    val directions = listOf(Coordinate(1, 0),
                            Coordinate(0, 1),
                            Coordinate(-1, 0),
                            Coordinate(0, -1))
    val neighbours = mutableListOf<Coordinate>()
    val currentCellValue = matrix[pos.row][pos.col]
    directions.forEach {
        val neighbour = pos + it
        if (isOutsideBoundsGeneric(neighbour, matrix) == false) {
            if (matrix[neighbour.row][neighbour.col] == currentCellValue + 1) {
                neighbours.add(neighbour)
            }
        }
    }
    return neighbours
}

private fun findZeroes(matrix: List<List<Int>>): List<Coordinate> {
    val coordinates = mutableListOf<Coordinate>()
    for (row in 0..<matrix.count()) {
        for (col in 0..<matrix[row].count()) {
            if (matrix[row][col] == 0) {
                coordinates.add(Coordinate(row, col))
            }
        }
    }
    return coordinates
}

private fun testOne() {
    val input = listOf("89010123",
                       "78121874",
                       "87430965",
                       "965498742",
                       "45678903",
                       "32019012",
                       "01329801",
                       "10456732")
    var count = 0
    val matrix = buildMatrixAsIntArrays(input)
    val startingCoordinates = findZeroes(matrix)
    println("Starting pos: ${startingCoordinates.count()}")
    for (startingPos in startingCoordinates) {
        count += calculateScoreStartingIn(startingPos, matrix)
    }
    println("Test: $count (expected 36)")
}