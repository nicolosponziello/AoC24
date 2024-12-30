import kotlin.time.measureTime

fun main() {
    val input = FileReader.readAllLines("day12.txt")
    val matrix = buildMatrix(input)

    testOne()

    var partOneResult = 0
    val timePartOne = measureTime {
        partOneResult = partOne(matrix)
    }
    println("Part one: $partOneResult (${timePartOne.inWholeMilliseconds}ms)")
}

private fun testOne() {
    val alreadyAttributed = mutableListOf<Coordinate>()

    val input = listOf("AAAA",
                      "BBCD",
                      "BBCC",
                      "EEEC")
    val matrix = buildMatrix(input)
    var value = 0
    for (row in 0..<matrix.count()) {
        for (col in 0..<matrix.first().count()) {
            if (alreadyAttributed.contains(Coordinate(row, col))) {
                continue
            }
            val zone = discoverZoneFrom(Coordinate(row, col), matrix)
            alreadyAttributed.addAll(zone)
            val area = zone.count()
            val perimeter = calculatePerimeter(zone, matrix)
            println("Zone: ${matrix[row][col]} - perimeter $perimeter area $area")
            value += area * perimeter
        }
    }
}

private fun partOne(matrix: MutableList<CharArray>) : Int {
    // list of nodes already assigned to a specific zone
    val alreadyAttributed = mutableListOf<Coordinate>()

    var value = 0
    for (row in 0..<matrix.count()) {
        for (col in 0..<matrix.first().count()) {
            if (alreadyAttributed.contains(Coordinate(row, col))) {
                continue
            }
            val zone = discoverZoneFrom(Coordinate(row, col), matrix)
            alreadyAttributed.addAll(zone)
            val area = zone.count()
            val perimeter = calculatePerimeter(zone, matrix)
            value += area * perimeter
        }
    }
    return value
}

private fun calculatePerimeter(zone: List<Coordinate>, matrix: List<CharArray>): Int {
    //idea: each node contributes to the perimeter as 4 - num of its neighbours
    var perimeter = 0
    for (node in zone) {
        perimeter += 4 - getNeighbours(node, matrix).count()
    }
    return perimeter
}
private fun discoverZoneFrom(starting: Coordinate, matrix: List<CharArray>): List<Coordinate> {
    val nodes = mutableListOf<Coordinate>()
    fun dfs(node: Coordinate, matrix: List<CharArray>) {
        if (nodes.contains(node)) {
            return
        }
        nodes.add(node)
        //let's proceed the exploration
        val neighbours = getNeighbours(node, matrix)
        neighbours.forEach {
            dfs(it, matrix)
        }
    }
    dfs(starting, matrix)
    return nodes
}

private fun getNeighbours(node: Coordinate, matrix: List<CharArray>): List<Coordinate> {
    val nodeType = matrix[node.row][node.col]
    val neighbours = mutableListOf<Coordinate>()
    val directions = listOf(Coordinate(1, 0),
                            Coordinate(0, 1),
                            Coordinate(-1, 0),
                            Coordinate(0, -1))
    directions.forEach {
        val potentialNeighbour = node + it
        if (!isOutsideBounds(potentialNeighbour, matrix)) {
            if (matrix[potentialNeighbour.row][potentialNeighbour.col] == nodeType) {
                neighbours.add(potentialNeighbour)
            }
        }
    }

    return neighbours
}