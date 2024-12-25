fun <T> generateCombinations(source: List<T>): List<Pair<T, T>> {
    val combinations = mutableListOf<Pair<T, T>>()
    for (i in 0..<source.count()){
        for (j in i+1..<source.count()){
            combinations.add(Pair(source[i], source[j]))
        }
    }

    return combinations
}