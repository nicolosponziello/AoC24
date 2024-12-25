import java.math.BigInteger
import kotlin.contracts.contract

fun main() {
    var input = FileReader.readAllLines("day7.txt")
    val equations = parseEquations(input)

    var partOneSum = 0L
    for (equation in equations) {
        if (solve(equation)) {
            partOneSum += equation.testValue
        }
    }

    println("Part one sum: $partOneSum")
}


private fun solve(equation: Equation, current: Long = equation.inputs.first().toLong(), index: Int = 1): Boolean {
    if (index == equation.inputs.count()) return current == equation.testValue
    return solve(equation, current + equation.inputs[index], index + 1) || solve(equation, current * equation.inputs[index], index + 1)
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