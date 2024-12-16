import java.io.File

class FileReader {
    companion object{
        private const val folder = "inputs"

        fun readAllLines(fileName: String) : MutableList<String>{


            val inputStream = File("${folder}/${fileName}").inputStream()
            val lines = mutableListOf<String>()
            inputStream.bufferedReader().forEachLine { lines.add(it) }
            return lines
        }

        fun readContent(fileName: String): String {
            val inputStream = File("${folder}/${fileName}").inputStream()
            var content = ""
            inputStream.bufferedReader().readLines().forEach{ content += it }
            return content
        }
    }

}