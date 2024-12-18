import javax.swing.text.Position

fun main(){
    val input = FileReader.readAllLines("day6.txt")

    val testInput = listOf<String>(
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


    val visitedPositions = mutableListOf<Coordinate>()
    val visitedPositionsAndDirection = mutableListOf<CoordinateWithDirection>()
    var matrix = buildMatrix(testInput)
    printMatrix(matrix)

    var guard = findGuardStartPosition(matrix) ?: return

    visitedPositions.add(guard.position)
    var partOneCount = 0
    while (!isOutsideBounds(guard.position, matrix)) {
        val moved = moveGuardInDirection(guard, matrix, visitedPositions, visitedPositionsAndDirection)
        partOneCount += moved
        println("Moved $moved in direction ${guard.direction}, count is $partOneCount")
        guard.turnRight()
    }

    println("Part one: $partOneCount")

    //for part 2, I assume that the obstacles must be placed in a place where the guard will pass/has passed
    var possibleObstaclesPositions = 0
    matrix = buildMatrix(testInput)
    guard = findGuardStartPosition(matrix)!!
    for (visitedPos in visitedPositions) {
        if (visitedPos == guard.position) {
            continue
        }

        //place an obstacle in the visited pos
        matrix[visitedPos.row][visitedPos.col] = 'X'

        //run the walk, but check if it loops => it ends up in a visited position with the same direction
        if (checkLoop(guard, matrix, visitedPositionsAndDirection)) {
            possibleObstaclesPositions++
        }

        guard = findGuardStartPosition(matrix)!!
        matrix[visitedPos.row][visitedPos.col] = '.'
    }

    println("Part 2: $possibleObstaclesPositions")
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
private fun checkLoop(guard: Guard, matrix: List<CharArray>, visitedAndDirection: MutableList<CoordinateWithDirection>): Boolean {
    do {
        val nextPositionX = guard.position.col + guard.direction.x
        val nextPositionY = guard.position.row + guard.direction.y
        if (nextPositionY >= matrix.count() || nextPositionX >= matrix.first().size || nextPositionX == -1 || nextPositionY == -1) {
            guard.position.col += guard.direction.x
            guard.position.row += guard.direction.y
            return false
        }

        val cell = matrix[nextPositionY][nextPositionX]
        if (cell != '#'){
            var element = CoordinateWithDirection(Coordinate(nextPositionY, nextPositionX), guard.direction)
            if (visitedAndDirection.contains(element)) {
                //looped
                return true
            }
            visitedAndDirection.add(element)

            guard.position.col += guard.direction.x
            guard.position.row += guard.direction.y
        }

    } while (cell != '#' )
    guard.turnRight()
    return checkLoop(guard, matrix, visitedAndDirection)
}


//returns the number of spots travelled
private fun moveGuardInDirection(guard: Guard, matrix: MutableList<CharArray>, visited: MutableList<Coordinate>, visitedAndDirection: MutableList<CoordinateWithDirection>): Int {
    var count = 0
    do {
        val nextPositionX = guard.position.col + guard.direction.x
        val nextPositionY = guard.position.row + guard.direction.y
        if (isOutsideBounds(Coordinate(nextPositionY, nextPositionX), matrix)) {
            guard.position.col += guard.direction.x
            guard.position.row += guard.direction.y
            break;
        }
        val cell = matrix[nextPositionY][nextPositionX]
        if (cell != '#'){
            if (!visited.contains(Coordinate(nextPositionY, nextPositionX)) && !isOutsideBounds(Coordinate(nextPositionY, nextPositionX), matrix)) {
                count++
                matrix[nextPositionY][nextPositionX] = 'X'
                visited.add(Coordinate(nextPositionY, nextPositionX))
                visitedAndDirection.add(CoordinateWithDirection(Coordinate(nextPositionY, nextPositionX), guard.direction))
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
data class Coordinate(var row: Int, var col: Int)

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