fun main() {
    var content = FileReader.readContent("day3.txt")

    //part 1
    var regex = Regex("mul\\(([0-9]{1,3}),([0-9]{1,3})\\)")
    val matches = regex.findAll(content)
    var sum = 0
    matches.forEach {
        sum += multiply(it.value)
    }

    println("Mul sum: $sum")

    var part2Regex = Regex("mul\\(([0-9]{1,3}),([0-9]{1,3})\\)|(do\\(\\))|(don't\\(\\))")
    var part2Matches = part2Regex.findAll(content)
    var part2Answer = 0
    var enabled = true
    part2Matches.forEach {
        if (it.value == "do()"){
            enabled = true
        } else if (it.value == "don't()")
        {
            enabled = false
        } else {
            //it is a mul instruction
            if (enabled){
                part2Answer += multiply(it.value)
            }
        }
    }

    println("Part 2 answer: $part2Answer")

}

fun multiply(value: String): Int {
    val parts = value.split(",")
    var regex = "[^0-9]".toRegex()
    var firstNumber = regex.replace(parts[0], "").toInt()
    var secondNumber = regex.replace(parts[1], "").toInt()
    return firstNumber * secondNumber
}