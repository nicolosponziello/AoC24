import javax.swing.text.Position

fun main(){
    val input = FileReader.readAllLines("day6.txt")

    val testInput = listOf(
        "....#.....",
        ".........#",
        "..........",
        "..#.......",
        ".......#..",
        "..........",
        ".#..^.....",
        "........#.",
        "#.........",
        "......#...")

    val visitedPositions = mutableSetOf<Coordinate>()
    var matrix = buildMatrix(input)
    printMatrix(matrix)

    var guard = findGuardStartPosition(matrix) ?: return

    visitedPositions.add(guard.position)
    var partOneCount = 0
    while (!isOutsideBounds(guard.position, matrix)) {
        val moved = moveGuardInDirection(guard, matrix, visitedPositions)
        partOneCount += moved
        println("Moved $moved in direction ${guard.direction}, count is $partOneCount")
        guard.turnRight()
    }

    println("Part one: $partOneCount")

    // PART 2
    // reset the matrix and the guard position
    // put obstacle in a position that was visited before => this is the new matrix to search for loops
    // check for loops
    //  - there is a loop if the guard comes back to the same position with the same direction
    //  - if there is a loop then stop the search with the current configuration and increase the loop counter
    // restart again until all the visited positions have been tested as a candidate for an obstacle

    var loopsFound = 0
    visitedPositions.remove(visitedPositions.last())
    for (visitedPos in visitedPositions) {

        // reset the matrix and guard
        matrix = buildMatrix(input)
        guard = findGuardStartPosition(matrix) ?: return

        println("Testing position: ${visitedPositions.indexOf(visitedPos)} / ${visitedPositions.count()} ${visitedPos}")

        // place the obstacle
        if (!placeObstacleSafe(visitedPos, matrix, guard.position)){
            continue
        }

        if (checkLoop(guard, matrix)){
            loopsFound++
        }
    }

    println("Part 2: $loopsFound")
}

private fun placeObstacleSafe(obstaclePos: Coordinate , matrix: List<CharArray>, guardPosition: Coordinate): Boolean{
    if (obstaclePos.col == guardPosition.col && obstaclePos.row == guardPosition.row) {
        println("Cannot put obstacle in guard position")
        return false // cannot place obstacle in guard position
    }

    if (isOutsideBounds(obstaclePos, matrix)) {
        println("Obstacle: $obstaclePos is out of bounds (max row index: ${matrix.count()} max col index. ${matrix.first().count()}")
        return false // out of bounds
    }

    matrix[obstaclePos.row][obstaclePos.col] = '#'
    return true
}

private fun buildMatrix(input: List<String>): MutableList<CharArray> {
    val result = mutableListOf<CharArray>()
    for (line in input) {
        result.add(line.toCharArray())
    }
    return result
}

private fun printMatrix(input: MutableList<CharArray>) {
    println("**------**\n")
    for (line in input) {
        println(line)
    }
    println("\n**------**\n")
}

//check for loops
// This should check the whole guard path until stuck or outside bounds
private fun checkLoop(guard: Guard, matrix: List<CharArray>): Boolean {
    val visitedAndDirection = mutableListOf<CoordinateWithDirection>()

    //move the guard in the path
    while (!isOutsideBounds(guard.position, matrix)) {
        do {
            val nextPositionCol = guard.position.col + guard.direction.x
            val nextPositionRow = guard.position.row + guard.direction.y
            val nextGuardPosition = Coordinate(nextPositionRow, nextPositionCol)
            if (isOutsideBounds(nextGuardPosition, matrix)) {
                guard.position = nextGuardPosition

                //guard escaped, no loop
                return false
            }

            val cell = matrix[nextPositionRow][nextPositionCol]
            if (cell != '#'){
                if (!isOutsideBounds(nextGuardPosition, matrix)) {
                    val coordAndDir = CoordinateWithDirection(nextGuardPosition, guard.direction)
                    if (visitedAndDirection.contains(coordAndDir)) {
                        //we alredy been here with the current direction, we looped!
                        return true
                    }

                    matrix[nextPositionRow][nextPositionCol] = 'X'
                    visitedAndDirection.add(coordAndDir)
                }
                guard.position.col += guard.direction.x
                guard.position.row += guard.direction.y
            }
        } while (cell != '#' )

        guard.turnRight()
    }

    return false
}


//returns the number of spots travelled
private fun moveGuardInDirection(guard: Guard, matrix: MutableList<CharArray>, visited: MutableSet<Coordinate>): Int {
    var count = 0
    do {
        val nextPositionCol = guard.position.col + guard.direction.x
        val nextPositionRow = guard.position.row + guard.direction.y
        val nextGuardPosition = Coordinate(nextPositionRow, nextPositionCol)
        if (isOutsideBounds(nextGuardPosition, matrix)) {
            guard.position = nextGuardPosition
            break;
        }
        val cell = matrix[nextPositionRow][nextPositionCol]
        if (cell != '#'){
            if (!isOutsideBounds(nextGuardPosition, matrix) && !visited.contains(nextGuardPosition)) {
                count++
                matrix[nextPositionRow][nextPositionCol] = 'X'
                visited.add(nextGuardPosition)
            }
            guard.position.col += guard.direction.x
            guard.position.row += guard.direction.y
        }

    } while (cell != '#' )
    printMatrix(matrix)
    return count
}

private fun isOutsideBounds(position: Coordinate, matrix: List<CharArray>): Boolean {
    return position.col >= matrix[0].size || position.row >= matrix.count() || position.col < 0 || position.row < 0
}

private fun findGuardStartPosition(matrix: List<CharArray>): Guard? {
    for (rowIndex in 0..< matrix.count()){
        val row = matrix[rowIndex]
        for (col in 0..<row.size) {
            if (matrix[rowIndex][col] == '^'){
                return Guard(Coordinate(rowIndex, col), Direction(0, -1))
            }
            if (matrix[rowIndex][col] == '<') {
                return Guard(Coordinate(rowIndex, col), Direction(-1, 0))
            }
            if (matrix[rowIndex][col] == '>') {
                return Guard(Coordinate(rowIndex, col), Direction(+1, 0))
            }
            if (matrix[rowIndex][col] == 'v') {
                return Guard(Coordinate(rowIndex, col), Direction(0, -1))
            }
        }
    }
    return null
}

data class CoordinateWithDirection(val coordinate: Coordinate, val direction: Direction)

//x -> column, y -> row in a matrix
data class Coordinate(var row: Int, var col: Int) {
    override fun equals(other: Any?): Boolean {
        if (other !is Coordinate) {
            return false
        }

        return row == other.row && col == other.col
    }
}

data class Guard(var position: Coordinate, var direction: Direction){
    fun isRight(): Boolean {
        return direction.x == 1 && direction.y == 0
    }
    fun isLeft(): Boolean {
        return direction.x == -1 && direction.y == 0
    }
    fun isUp(): Boolean {
        return direction.x == 0 && direction.y == -1
    }
    fun isDown(): Boolean {
        return direction.x == 0 && direction.y == 1
    }

    fun turnRight() {
        if (isUp()){
            this.direction = Direction(1, 0) //turn right
        } else if (isRight()){
            this.direction = Direction(0, 1) // turn down
        } else if (isDown()) {
            this.direction = Direction(-1, 0) // turn left
        } else if (isLeft()) {
            this.direction = Direction(0, -1) // turn up
        }
    }
}