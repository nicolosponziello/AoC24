fun main(){
    val allowedLetters = listOf("X", "M", "A", "S")
    val fileLines = FileReader.readContentAsListOfLines("day4.txt")

    //filter the letters not allowed
    val regex = Regex("[^xmas]")
    println("Part one: ${partOne(fileLines)}")
    
    println("Part two: ${partTwo(fileLines)}")
}

private fun partOne(lines: List<String>): Int {
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

private fun partTwo(lines: List<String>): Int {
    var count = 0
    for (row in 1..<lines.count()-1){
        val line = lines[row]
        for (col in 1..<line.length-1){
            if (!checkMas(lines, row, col)){
                continue
            }
            count++
        }
    }

    return count
}

fun checkMas(lines: List<String>, row: Int, col: Int): Boolean {
    //check if the diagonal centered in [row, col] is MAS or SAM
    if (lines[row][col] != 'A'){
        return false
    }

    var diag1 = "${lines[row-1][col-1]}${lines[row][col]}${lines[row+1][col+1]}"
    if (diag1 != "SAM" && diag1 != "MAS"){
        return false
    }
    var diag2 = "${lines[row-1][col+1]}${lines[row][col]}${lines[row+1][col-1]}"
    if (diag2 != "SAM" && diag2 != "MAS"){
        return false
    }
    return true
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