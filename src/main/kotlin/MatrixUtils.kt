fun buildMatrix(input: List<String>): MutableList<CharArray> {
    val result = mutableListOf<CharArray>()
    for (line in input) {
        result.add(line.toCharArray())
    }
    return result
}

fun buildMatrixAsIntArrays(input: List<String>): MutableList<List<Int>> {
    val result = mutableListOf<List<Int>>()
    for (line in input) {
        val lineInt = mutableListOf<Int>()
        for (char in line){
            lineInt.add(char.digitToInt())
        }
        result.add(lineInt)
    }
    return result
}

fun printMatrix(input: MutableList<CharArray>) {
    println("**------**\n")
    for (line in input) {
        println(line)
    }
    println("\n**------**\n")
}

fun isOutsideBounds(position: Coordinate, matrix: List<CharArray>): Boolean {
    return position.col >= matrix[0].size || position.row >= matrix.count() || position.col < 0 || position.row < 0
}

fun <T> isOutsideBoundsGeneric(position: Coordinate, matrix: List<List<T>>): Boolean {
    return position.col >= matrix[0].size || position.row >= matrix.count() || position.col < 0 || position.row < 0
}

operator fun Coordinate.minus(other: Coordinate) = Coordinate(row - other.row, col - other.col)

operator fun Coordinate.plus(other: Coordinate) = Coordinate(row + other.row, col + other.col)