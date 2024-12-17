import javax.swing.text.Position

fun main(){
    val input = FileReader.readAllLines("day6.txt")

    val matrix = mutableListOf<String>()
    matrix.addAll(input)

    var guard = findGuardStartPosition(matrix)
    println(guard)
}

private fun findGuardStartPosition(matrix: List<String>): Guard? {
    for (rowIndex in 0..< matrix.count()){
        val row = matrix[rowIndex]
        for (col in 0..<row.length) {
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

//x -> column, y -> row in a matrix
data class Coordinate(val x: Int, val y: Int)

data class Guard(val position: Coordinate, val direction: Direction)