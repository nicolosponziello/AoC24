import kotlin.math.abs

fun main(){
    var lines = FileReader.readAllLines("day2.txt")

    var safeReports = 0
    lines.forEach {
        var levels = it.split(" ").map { it.toInt()}.toTypedArray()
        if (isLevelSafe(levels) || canLevelBeDampened(levels.toMutableList())) {
            safeReports++
        }
    }

    println("Safe reports: $safeReports")
}

fun canLevelBeDampened(level: MutableList<Int>): Boolean {
    //very inefficient but it does work
    if (isLevelSafe(level.toTypedArray())) return true

    //else, we can remove each item and if it is safe then it can be dampened
    for (i in 0..level.count()-1)
    {
        var dampedArray = level.toMutableList()
        dampedArray.removeAt(i)
        if (isLevelSafe(dampedArray.toTypedArray())) return true
    }

    return false
}

fun isLevelSafe(level: Array<Int>): Boolean{
    return isLevelAllDecreasingOrIncreasing(level) && isDistanceSafe(level)
}

fun isLevelAllDecreasingOrIncreasing(level: Array<Int>): Boolean {
    var isDecreasing = false; //decreasing true, increasing false

    //test the first two
    isDecreasing = if (level.first() > level.elementAt(1)) true else false

    var value = level.zip(level.drop(1)).all { (a, b) -> if (isDecreasing) b <= a else a <= b }

    return value
}

fun isDistanceSafe(level: Array<Int>): Boolean {
    val value = level.zip(level.drop(1)).all { (a, b) -> abs(a-b) in 1..3 }
    return value
}