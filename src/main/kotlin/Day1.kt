import java.io.File
import kotlin.math.abs


fun main() {
    val lines = FileReader.readAllLines("day1.txt")
    val leftList = mutableListOf<Int>()
    val rightList = mutableListOf<Int>()
    lines.forEach {
        val values = it.split(" ")
        leftList.add(values[0].toInt())
        rightList.add(values[3].toInt())
    }

    leftList.sort()
    rightList.sort()

    var distance = 0
    val copyLeftList = leftList.toMutableList()
    val copyRightList = rightList.toMutableList()
    while (copyLeftList.isNotEmpty() && copyRightList.isNotEmpty())
    {
        distance += abs(copyLeftList.removeFirst() - copyRightList.removeFirst())
    }

    println("Distance: $distance")

    //calculate similarity
    var similarity = 0
    leftList.forEach {
        similarity += it * rightList.count { el -> el == it }
    }

    println("Similarity score: $similarity")
}