import kotlin.time.Duration.Companion.seconds

fun main() {
    val input = FileReader.readAllLines("day10.txt")
    val matrix = buildMatrixAsIntArrays(input)

    testOne()
    println("Part one ${partOne(matrix).first}")
    println("Part two ${partOne(matrix).second}")
}

private fun partOne(matrix: List<List<Int>>): Pair<Int, Int> {
    var totalScore = 0
    var totalRating = 0
    val startingCoordinates = findZeroes(matrix)
    for (startingPos in startingCoordinates) {
        val result = calculateScoreStartingIn(startingPos, matrix)
        totalScore += result.first
        totalRating += result.second
    }
    return Pair(totalScore, totalRating)
}

private fun calculateScoreStartingIn(starting: Coordinate, matrix: List<List<Int>>): Pair<Int, Int> {
    val ends = HashSet<Coordinate>()
    var trailCount = 0

    fun dfs(node: Coordinate) {
        //if we ended up on a terminal node, keep track of it and increase
        //the trail count from the root
        if (matrix[node.row][node.col] == 9) {
            ends += node
            trailCount++
        } else {
            //otherwise, let's proceed the exploration
            getNeighbours(node, matrix).forEach {
                dfs(it)
            }
        }
    }

    dfs(starting)

    return Pair(ends.size, trailCount)
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
    var score = 0
    var rating = 0
    val matrix = buildMatrixAsIntArrays(input)
    val startingCoordinates = findZeroes(matrix)
    println("Starting pos: ${startingCoordinates.count()}")
    for (startingPos in startingCoordinates) {
        val res = calculateScoreStartingIn(startingPos, matrix)
        score += res.first
        rating += res.second
    }
    println("Test: $score (expected 36), $rating (expected 81)")
}