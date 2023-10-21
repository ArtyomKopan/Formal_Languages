import java.nio.file.Files
import java.nio.file.Path

fun coding(grammarDescription: List<String>): Map<String, Int> {
    val codes = mutableMapOf(
            ":" to 1,
            "(" to 2,
            ")" to 3,
            "." to 4,
            "*" to 5,
            ";" to 6,
            "," to 7,
            "#" to 8,
            "[" to 9,
            "]" to 10,
            "Eofgram" to 1000
    )

    val grammar = grammarDescription.map { it.trim() }

    var terminalNumber = 51
    var nonterminalNumber = 11
    var semanticNumber = 101

    // кодируем нетерминалы

    for (command in grammar) {
        if (command == "Eofgram")
            break
        val nonterm = command.split(" : ")[0]
        if (nonterminalNumber <= 50)
            codes[nonterm] = nonterminalNumber++
        else
            break
    }

    // кодируем терминалы и семантики
    for (command in grammar) {
        if ("Eofgram" in command)
            break

        // кодируем семантики
        val semantics = command
                .split(" ")
                .filter { it.matches(Regex("\\$[a-z]+[0-9]*")) }
        for (semantic in semantics) {
            if (semanticNumber >= 1000) {
                break
            }
            if (semantic !in codes.keys) {
                codes[semantic] = semanticNumber++
            }
        }

        // кодируем нетерминалы, обозначенные заглавными буквами

        val nonterminals = command
                .split(" ")
                .filter { it.matches(Regex("[A-Z]+")) }
        for (nonterminal in nonterminals) {
            if (nonterminalNumber > 50) {
                break
            }
            if (nonterminal !in codes.keys) {
                codes[nonterminal] = nonterminalNumber++
            }
        }

        // кодируем терминалы
        val rightPartCommand = command.split(" : ")[1]
        val terminals = rightPartCommand
                .split(" ")
                .filter {
                    it.matches(Regex("'.+'")) ||
                            it.matches(Regex("[a-z]+"))
                }
        for (terminal in terminals) {
            if (terminalNumber > 100) {
                break
            }
            if (terminal !in codes.keys) {
                codes[terminal] = terminalNumber++
            }
        }
    }

    val trash = mutableListOf<String>()
    for (k in codes) {
        if ("Eofgram" in k.key && k.value != 1000)
            trash.add(k.key)
    }
    codes.filterKeys { it == "" || " " in it || it in trash }
    return codes
}

fun main() {
    println("Введите путь к файлу с описанием грамматики: ")
    val pathToInputFile = readlnOrNull() ?: "../expression.txt"
    val grammarDescription = Files
            .readString(Path.of(pathToInputFile))
            .split(" .")
            .map { it.trim() }
    val codes = coding(grammarDescription)
    codes.forEach { (t, u) -> println("$t $u") }

    for (line in grammarDescription.map { it.trim() }) {
        var buffer = ""
        val encodedLine = mutableListOf<Int>()
        for (ch in line) {
            if (buffer != "" && buffer in codes.keys) {
                encodedLine.add(codes[buffer] ?: -1)
                buffer = ""
            } else if (ch != ' ') {
                buffer += ch
            }
        }

        if (buffer != "" && buffer in codes.keys) {
            encodedLine.add(codes[buffer] ?: -1)
        }

        encodedLine.forEach { print("$it ") }
        println()
    }
}