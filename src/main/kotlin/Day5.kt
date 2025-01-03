fun main(){
    val lines = FileReader.readAllLines("day5.txt")

    val testInput = mutableListOf<String>("47|53",
    "97|13",
    "97|61",
    "97|47",
    "75|29",
    "61|13",
    "75|53",
    "29|13",
    "97|29",
    "53|29",
    "61|53",
    "97|53",
    "61|29",
    "47|13",
    "75|47",
    "97|75",
    "47|61",
    "75|61",
    "47|29",
    "75|13",
    "53|13",
    "\n",
    "75,47,61,53,29",
    "97,61,53,29,13",
    "75,29,13",
    "75,97,47,61,53",
    "61,13,29",
    "97,13,75,29,47",)
    println("Test partOne: ${partOne(testInput)}, expected: 143")
    println("Valid updates: ${partOne(lines)}")

    //part 2
    println("Test partTwo: ${partTwo(testInput)}, expected: 123")
    println("Incorrect updates corrected: ${partTwo(lines)}")
}

private fun partOne(input: List<String>): Int {
    val rules = mutableListOf<Rule>()
    val updates = mutableListOf<List<Int>>()
    for (line in input){
        if (line.contains("|")){
            rules.add(parseRule(line))
        }else {
            if (line.isBlank()) {
                continue
            }
            updates.add(parseUpdate(line))
        }
    }

    return checkUpdates(updates, rules)
}

private fun partTwo(input: List<String>): Int {
    val rules = mutableListOf<Rule>()
    val updates = mutableListOf<List<Int>>()

    for (line in input){
        if (line.contains("|")){
            rules.add(parseRule(line))
        }else {
            if (line.isBlank()) {
                continue
            }
            updates.add(parseUpdate(line))
        }
    }

    var partTwoSum = 0
    for (update in updates) {
        if (!isValidUpdate(update, rules)){
            val reorderedUpdate = reorderUpdate(update, rules)
            partTwoSum += reorderedUpdate.elementAt(reorderedUpdate.count() / 2)
        }
    }

    return partTwoSum
}

private fun reorderUpdate(update: List<Int>, rules: List<Rule>): List<Int> {
    val mutableUpdate = mutableListOf<Int>()
    mutableUpdate.addAll(update)

    for (rule in rules) {
        if (isRuleCorrect(mutableUpdate, rule)) {
            continue
        }

        //swap two elements
        val indexBefore = mutableUpdate.indexOf(rule.before)
        val indexAfter = mutableUpdate.indexOf(rule.after)
        if (indexBefore == -1 || indexAfter == -1) {
            continue
        }

        val temp = mutableUpdate[indexBefore]
        mutableUpdate[indexBefore] = mutableUpdate[indexAfter]
        mutableUpdate[indexAfter] = temp
    }

    //recursive call to ensure the update is reordered correctly
    if (!isValidUpdate(mutableUpdate, rules)){
        return reorderUpdate(mutableUpdate, rules)
    }

    return mutableUpdate.toList()
}

private fun checkUpdates(updates: List<List<Int>>, rules: List<Rule>): Int{
    var validUpdates = mutableListOf<List<Int>>()
    for (update in updates) {
        if (isValidUpdate(update, rules)){
            validUpdates.add(update)
        }
    }

    var sum = 0
    for (validUpdate in validUpdates) {
        var middleElement = validUpdate.elementAt(validUpdate.count() / 2)
        sum += middleElement
    }

    return sum
}


private fun isRuleCorrect(update: List<Int>, rule: Rule): Boolean {
    //check if index of before is less than index after
    var indexBefore = update.indexOf(rule.before)
    var indexAfter = update.indexOf(rule.after)
    if (indexBefore == -1 || indexAfter == -1) {
        //we return true, so we skip this rule
        return true
    }

    return indexBefore < indexAfter;
}

private fun isValidUpdate(update: List<Int>, rules: List<Rule>): Boolean {
    for (rule in rules) {
       if (isRuleCorrect(update, rule)){
           continue
       } else {
           return false
       }
    }
    return true
}

private fun parseRule(value: String): Rule{
    val numbers = value.split("|")
    return Rule(numbers.first().toInt(), numbers[1].toInt())
}

private fun parseUpdate(value: String): List<Int> {
    val numbers = value.split(",")
    val filtered = numbers.filter { it != ""}
    val update = mutableListOf<Int>()
    for (number in filtered){
        update.add(number.toInt())
    }

    return update
}


data class Rule(val before: Int, val after: Int)