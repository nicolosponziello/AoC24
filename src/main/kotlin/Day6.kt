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
    val matrix = mutableListOf<String>()
    matrix.addAll(input)

    val guard = findGuardStartPosition(matrix)
    if (guard == null) return

    visitedPositions.add(guard.position)
    var partOneCount = 0
    while (!isGuardOutsideBounds(guard, matrix)) {
        val moved = moveGuardInDirection(guard, matrix, visitedPositions)
        partOneCount += moved
        println("Moved $moved in direction ${guard.direction}, count is $partOneCount")
        guard.turnRight()
    }

    println("Part one: $partOneCount")
}

//returns the number of spots travelled
private fun moveGuardInDirection(guard: Guard, matrix: List<String>, visited: MutableList<Coordinate>): Int {
    var count = 0
    do {
        var nextPositionX = guard.position.x + guard.direction.x
        var nextPositionY = guard.position.y + guard.direction.y
        if (nextPositionY >= matrix.count() || nextPositionX >= matrix.first().length || nextPositionX == -1 || nextPositionY == -1) {
            guard.position.x += guard.direction.x
            guard.position.y += guard.direction.y
            break;
        }
        var cell = matrix[nextPositionY][nextPositionX]
        if (cell != '#'){
            if (!visited.contains(Coordinate(nextPositionY, nextPositionX))) {
                count++
                visited.add(Coordinate(nextPositionY, nextPositionX))
            }

            guard.position.x += guard.direction.x
            guard.position.y += guard.direction.y
        }

    } while (cell != '#' )
    return count
}

private fun isGuardOutsideBounds(guard: Guard, matrix: List<String>): Boolean {
    return guard.position.x >= matrix[0].length || guard.position.y >= matrix.count() || guard.position.x < 0 || guard.position.y < 0
}

private fun findGuardStartPosition(matrix: List<String>): Guard? {
    for (rowIndex in 0..< matrix.count()){
        val row = matrix[rowIndex]
        for (col in 0..<row.length) {
            if (matrix[rowIndex][col] == '^'){
                return Guard(Coordinate(col, rowIndex), Direction(0, -1))
            }
            if (matrix[rowIndex][col] == '<') {
                return Guard(Coordinate(col, rowIndex), Direction(-1, 0))
            }
            if (matrix[rowIndex][col] == '>') {
                return Guard(Coordinate(col, rowIndex), Direction(+1, 0))
            }
            if (matrix[rowIndex][col] == 'v') {
                return Guard(Coordinate(col, rowIndex), Direction(0, -1))
            }
        }
    }
    return null
}

//x -> column, y -> row in a matrix
data class Coordinate(var x: Int, var y: Int)

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
            println("Turning right")
            this.direction = Direction(1, 0) //turn right
        } else if (isRight()){
            println("Turning down")
            this.direction = Direction(0, 1) // turn down
        } else if (isDown()) {
            println("Turning left")
            this.direction = Direction(-1, 0) // turn left
        } else if (isLeft()) {
            println("Turning up")
            this.direction = Direction(0, -1) // turn up
        }
    }
}