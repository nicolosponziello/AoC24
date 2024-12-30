import kotlin.time.measureTime

private val zones = mutableListOf<List<Coordinate>>()

fun main() {
    val input = FileReader.readAllLines("day12.txt")
    val matrix = buildMatrix(input)

    var partOneResult = 0
    val timePartOne = measureTime {
        partOneResult = partOne(matrix)
    }
    println("Part one: $partOneResult (${timePartOne.inWholeMilliseconds}ms)")

    var partTwoResult = 0
    val timePartTwo = measureTime {
        partTwoResult = partTwo(matrix)
    }
    println("Part two: $partTwoResult (${timePartTwo.inWholeMilliseconds}ms)")

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
            zones.add(zone)

            alreadyAttributed.addAll(zone)
            val area = zone.count()
            val perimeter = calculatePerimeter(zone, matrix)
            value += area * perimeter
        }
    }
    return value
}

private fun partTwo(matrix: List<CharArray>): Int {
    var value = 0
    val directions = listOf(Coordinate(1, -1),
        Coordinate(1, 1),
        Coordinate(-1, 1),
        Coordinate(-1, -1))
    for (zone in zones) {
        var edges = 0
        for (node in zone) {
            directions.forEach {
                val rowNeighbour = Coordinate(node.row + it.row, node.col)
                val colNeighbour = Coordinate(node.row, node.col + it.col)
                val diagNeighbour = Coordinate(node.row + it.row, node.col + it.col)
                if (!zone.contains(rowNeighbour) && !zone.contains(colNeighbour)) {
                    edges++
                }

                if (zone.contains(rowNeighbour) && zone.contains(colNeighbour) && !zone.contains(diagNeighbour)) {
                    edges++
                }
            }
        }
        value += zone.count() * edges
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