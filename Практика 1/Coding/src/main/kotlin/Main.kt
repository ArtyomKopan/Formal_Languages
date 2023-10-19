import java.nio.file.Files
import java.nio.file.Path

fun coding(grammarDescription: List<String>) : Map<String, Int> {
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

    val grammar = mutableListOf<String>()

    for (command in grammarDescription) {
        grammar.add(command.trim())
    }

    var termNumber = 51
    var nontermNumber = 11
    var semanticNumber = 101

    // кодируем нетерминалы
    for (command in grammar) {
        if (command == "Eofgram")
            break
        val nonterm = command.split(" : ")[0]
        if (nontermNumber <= 50)
            codes[nonterm] = nontermNumber++
        else
            break
    }

    // кодируем терминалы и семантики
    for (command in grammar) {
        if ("Eofgram" in command)
            break
        val rightPartCommand = command.split(" : ")[1]

        // кодируем терминалы
        var isTerminal = false
        var buffer = ""

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
                if (buffer !in codes.keys)
                    codes[buffer] = nontermNumber++
                buffer = ""
            }
        }


        // кодируем семантики
        var isSemantics = false
        for (ch in rightPartCommand) {
            if (semanticNumber > 150)
                break

            if (ch == '$') {
                isSemantics = true
//                buffer += ch
            }
            else if (isSemantics && (ch.isLetter() || ch.isDigit()))
                buffer += ch
            else if (isSemantics) {
                isSemantics = false
                if (buffer !in codes.keys)
                    codes[buffer] = semanticNumber++
                buffer = ""
            }
        }

        for (ch in rightPartCommand) {
            if (termNumber > 100)
                break

            // первый способ поиска терминалов
            if (ch == '\'' && !isTerminal) {
                isTerminal = true
            }
            else if (ch == '\'' && isTerminal) {
                if (buffer !in codes.keys)
                    codes[buffer] = termNumber++
                isTerminal = false
                buffer = ""
            } else if (isTerminal) {
                if (ch != '<' && ch != '>')
                    buffer += ch
            }
        }

        for (ch in rightPartCommand) {
            if (termNumber > 100)
                break
            // второй способ поиска терминалов
            if (ch.isLowerCase() && !isTerminal) {
                isTerminal = true
                buffer += ch
            }
            else if ((ch.isLowerCase() || ch.isDigit()) && isTerminal)
                buffer += ch
            else {
                if (buffer !in codes.keys)
                    codes[buffer] = termNumber++
                buffer = ""
                isTerminal = false
            }
        }
    }

    val trash = mutableListOf<String>()
    for (k in codes) {
        if ("Eofgram" in k.key && k.value != 1000)
            trash.add(k.key)
    }
    codes.filterKeys { it == "" || " " in it || it in trash}
    return codes
}

fun main() {
    val scan = java.util.Scanner(System.`in`)
    println("Введите путь к файлу с описанием грамматики: ")
    val pathToInputFile = scan.next() ?: "../expression.txt"
    val grammarDescription = Files.readString(Path.of(pathToInputFile)).split(" .")
    val codes = coding(grammarDescription)
    codes.forEach { (t, u) -> println("$t $u") }

    // таким образом, терминалы, нетерминалы и семантики в словаре кодов представлены одинаково:
    // как последовательности латинских букв и цифр
    // знаки '' < > $ не учитываются и в кодах, и учитывать их не нужно

    for (line_ in grammarDescription) {
        val line = line_.trim()
        val encodedElementsOfCommand = mutableListOf<Int>()


        if ("Eofgram" in line) {
            println(1000)
            break
        }

        // считаем, что строка имеет вид
        // левая_часть : правая_часть
        val leftPart = line.split(" : ")[0]
        val rightPart = line.split(" : ")[1]

        encodedElementsOfCommand.add(codes[leftPart] ?: -1)
        encodedElementsOfCommand.add(codes[":"] ?: 1)

        // разбираем правую часть
        for (ch in rightPart) {
            var isWord = false
            var buffer = ""
            if ((ch.isLetter() || ch.isDigit()) && !isWord) {
                isWord = true
                buffer += ch
            }
            else if ((ch.isDigit() || ch.isLetter()) && isWord) {
                buffer += ch
            }
            else if (!ch.isLetter() && !ch.isDigit() && isWord) {
                encodedElementsOfCommand.add(codes[buffer] ?: -1)
                isWord = false
                buffer = ""
            }
            else if (!ch.isLetter() && !ch.isDigit()) {
                if (ch in listOf(',', '.', '[', ']', '(', ')', '*', ';', ':', '#')) {
                    encodedElementsOfCommand.add(codes[ch.toString()] ?: -1)
                }
                // символы, не входящие в этот список, не учитываются
            }
        }


        encodedElementsOfCommand.add(codes["."] ?: 4)
        encodedElementsOfCommand.forEach { print("$it, ") }
        println()
    }
}