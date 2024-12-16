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

}

fun multiply(value: String): Int {
    val parts = value.split(",")
    var regex = "[^0-9]".toRegex()
    var firstNumber = regex.replace(parts[0], "").toInt()
    var secondNumber = regex.replace(parts[1], "").toInt()
    return firstNumber * secondNumber
}