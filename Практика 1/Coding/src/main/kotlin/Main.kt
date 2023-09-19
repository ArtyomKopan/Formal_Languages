import java.io.File
import java.nio.file.Files
import java.nio.file.Path

fun coding(grammarDescription: String) : Map<String, Int> {
    val codes = mutableMapOf<String, Int>(
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

    var termNumber = 51
    var nontermNumber = 11
    var semanticNumber = 101

    val grammar = grammarDescription.split('\n')

    // кодируем нетерминалы
    for (command in grammar) {
        if (command == "Eofgram")
            break
        val nonterm = command.split(" : ")[0]
        if (termNumber <= 50)
            codes[nonterm] = termNumber++
        else
            break
    }

    // кодируем терминалы и семантики
    for (command in grammar) {
        if (command == "Eofgram")
            break
        val rightPartCommand = command.split(" : ")[1]

        // кодируем терминалы
        var isTerminal = false
        var buffer = ""
        for (ch in rightPartCommand) {
            if (termNumber > 100)
                break

            // первый способ поиска терминалов
            if (ch == '\'' && !isTerminal)
                isTerminal = true
            else if (ch == '\'' && isTerminal) {
                if (!codes.contains(buffer))
                    codes[buffer] = termNumber++
                isTerminal = false
                buffer = ""
            }
            if (isTerminal)
                buffer += ch

            // второй способ поиска терминалов
            if (ch.isLowerCase() && !isTerminal) {
                isTerminal = true
                buffer += ch
            }
            else if (ch.isLowerCase() && isTerminal)
                buffer += ch
            else {
                if (!codes.contains(buffer))
                    codes[buffer] = termNumber++
                buffer = ""
                isTerminal = false
            }
        }

        // кодируем нетерминалы, обозначенные заглавными буквами
        var isNonterminal = false
        for (ch in rightPartCommand) {
            if (nontermNumber > 50)
                break
            if (ch.isUpperCase() && !isNonterminal) {
                isNonterminal = true
                buffer += ch
            }
            else if (ch.isUpperCase() && isNonterminal)
                buffer += ch
            else {
                isNonterminal = false
                if (!codes.contains(buffer))
                    codes[buffer] = nontermNumber++
                buffer = ""
            }
        }


        // кодируем семантики
        var isSemantics = false
        for (ch in rightPartCommand) {
            if (semanticNumber > 150)
                break

            if (ch == '$')
                isSemantics = true
            else if (ch == ' ') {
                isSemantics = false
                codes[buffer] = semanticNumber++
                buffer = ""
            }

            if (isSemantics)
                buffer += ch
        }
    }

    return codes
}

fun main() {
    val scan = java.util.Scanner(System.`in`)
    println("Введите путь к файлу с описанием грамматики: ")
    val pathToInputFile = scan.next() ?: "../expression.txt"
    val grammarDescription = Files.readAllLines(Path.of(pathToInputFile)).toString()
    val codes = coding(grammarDescription)

}