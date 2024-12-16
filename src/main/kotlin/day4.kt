fun main(){
    val allowedLetters = listOf("X", "M", "A", "S")
    var fileLines = FileReader.readContentAsListOfLines("day4.txt")

    //filter the letters not allowed
    val regex = Regex("[^xmas]")
    var countPartOne = partOne(fileLines)
    println("Part one: $countPartOne")

    println("Part two: ${partTwo(fileLines)}")
}

fun partOne(lines: List<String>): Int {
    val directions = listOf(
        Direction(1, 0),
        Direction(-1, 0),
        Direction(0, 1),
        Direction(0, -1),
        Direction(-1, 1),
        Direction(-1, -1),
        Direction(1, -1),
        Direction(1, 1))
    var count = 0
    for (row in 0..<lines.count()){
        val line = lines[row]
        for (col in line.indices){
            for (direction in directions) {
                if (checkXmas(lines, direction, row, col)){
                    count++
                }
            }
        }
    }
    return count
}

fun partTwo(lines: List<String>): Int {
    return 0
}

fun checkXmas(lines: List<String>, dir: Direction, row: Int, col: Int): Boolean {
    val limitX = row + 3 * dir.x
    val limitY = col + 3 * dir.y
    if (limitX >= lines[0].length || limitX < 0 ||
        limitY >= lines.count() || limitY < 0) {
        return false
    }

    var strInDirection = "${lines[row][col]}${lines[row + dir.x][col + dir.y]}${lines[row + 2 * dir.x][col + 2*dir.y]}${lines[row + 3 * dir.x][col + 3 * dir.y]}"
    if (strInDirection == "XMAS"){
        return true
    }
    return false
}

data class Direction(val x: Int, val y: Int)