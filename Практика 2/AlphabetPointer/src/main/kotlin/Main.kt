import java.nio.file.Files
import java.nio.file.Path

fun main(args: Array<String>) {
    val pathToInputFile = readlnOrNull() ?: ""
    val text = Files.readAllLines(Path.of(pathToInputFile))

    val cleanText = text.map {
        it.filter { ch -> ch.isLetter() || ch == ' ' || ch == '-' }.trim()
    }

    val uniqueWords = mutableSetOf<String>()
    cleanText.forEach { line -> uniqueWords.addAll(line.split(' '))}
    uniqueWords.removeIf { it == "-" || it == "" }

    val dictionary = sortedMapOf<String, MutableList<Int>>()
    for (word in uniqueWords) {
        dictionary[word] = mutableListOf()
        for (i in 1..cleanText.size) {
            if (word in cleanText[i - 1]) {
                dictionary[word]?.add(i)
            }
        }
    }

    for (item in dictionary) {
        print("${item.key}: ")
        item.value.forEach { print("$it ") }
        println()
    }
}