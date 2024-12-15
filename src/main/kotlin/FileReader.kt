import java.io.File

class FileReader {
    companion object{
        fun readAllLines(fileName: String) : MutableList<String>{
            val folder = "inputs"

            val inputStream = File("${folder}/${fileName}").inputStream()
            val lines = mutableListOf<String>()
            inputStream.bufferedReader().forEachLine { lines.add(it) }
            return lines
        }
    }

}