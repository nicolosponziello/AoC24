fun main(){
    val lines = FileReader.readAllLines("day5.txt")
    val rules = mutableListOf<Rule>()
    val updates = mutableListOf<List<Int>>()
    for (line in lines){
        if (line.contains("|")){
            rules.add(parseRule(line))
        }else {
            if (line.isBlank()) {
                continue
            }
            val numbers = line.split(",")
            var filtered = numbers.filter { it != ""}
            val update = mutableListOf<Int>()
            for (number in filtered){
                update.add(number.toInt())
            }
            updates.add(update)
        }
    }

    println("Valid updates: ${checkUpdates(updates, rules)}")
}

fun checkUpdates(updates: List<List<Int>>, rules: List<Rule>): Int{
    var validUpdates = mutableListOf<List<Int>>()
    for (update in updates) {
        if (isValidUpdate(update, rules)){
            validUpdates.add(update)
        }
    }

    var sum = 0
    for (validUpdate in validUpdates) {
        var middleElement = validUpdate.elementAt(validUpdate.count() / 2 + 1)
        sum += middleElement
    }

    return sum
}

fun isValidUpdate(update: List<Int>, rules: List<Rule>): Boolean {
    for (rule in rules) {
        //check if index of before is less than index after
        var indexBefore = update.indexOf(rule.before)
        var indexAfter = update.indexOf(rule.after)
        if (indexBefore == -1 || indexAfter == -1) {
            continue
        }

        if (indexBefore > indexAfter) {
            return false
        }
    }
    return true
}

fun parseRule(value: String): Rule{
    val numbers = value.split("|")
    return Rule(numbers.first().toInt(), numbers[1].toInt())
}
data class Rule(val before: Int, val after: Int)