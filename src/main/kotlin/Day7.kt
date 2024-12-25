import java.math.BigInteger
import kotlin.contracts.contract

fun main() {
    var input = FileReader.readAllLines("day7.txt")
    val equations = parseEquations(input)

    var partOneSum = 0L
    for (equation in equations) {
        if (solve(equation, equation.inputs.first().toLong(), 1, false)) {
            partOneSum += equation.testValue
        }
    }

    println("Part one sum: $partOneSum")

    var partTwoSum = 0L
    for (equation in equations) {
        if (solve(equation, equation.inputs.first().toLong(), 1, true)) {
            partTwoSum += equation.testValue
        }
    }

    println("Part two sum: $partTwoSum")

}


private fun solve(equation: Equation, current: Long = equation.inputs.first().toLong(), index: Int = 1, partTwo: Boolean): Boolean {
    if (index == equation.inputs.count()) return current == equation.testValue
    return solve(equation, current + equation.inputs[index], index + 1, partTwo) ||
           solve(equation, current * equation.inputs[index], index + 1, partTwo) ||
           (partTwo && solve(equation, "${current.toString()}${equation.inputs[index]}".toLong(), index + 1, partTwo))
}

// an equation is a list of int
private fun parseEquations(input : List<String>) : MutableList<Equation> {
    val equations = mutableListOf<Equation>()
    for (line in input) {
        val split = line.split(':')
        var testValue = split.first().toLong()
        var values = mutableListOf<Int>()
        var valuesSplit = split[1].split(' ')
        valuesSplit.forEach {
            if (it.isNotEmpty()){
                values.add(it.toInt())
            }
        }

        equations.add(Equation(testValue, values))
    }

    return equations
}

private data class Equation (val testValue: Long, val inputs: List<Int>)