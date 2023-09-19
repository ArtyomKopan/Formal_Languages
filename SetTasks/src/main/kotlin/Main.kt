fun task1() {
    val scan = java.util.Scanner(System.`in`)
    println("Введите первый текст: ")
    val text1 = scan.nextLine().split(" ", ".", ",", "!", "?").toSet()
    println("Введите второй текст: ")
    val text2 = scan.nextLine().split(" ", ".", ",", "!", "?").toSet()
    val commonWords = text1.intersect(text2)
    println("Список общих слов в этих текстах: ")
    commonWords.forEach { println(it) }
}

fun task2() {
    val scan = java.util.Scanner(System.`in`)
    println("Введите первый текст: ")
    val symbolsInText1 = scan.nextLine().split("").toSet()
    println("Введите второй текст: ")
    val symbolsInText2 = scan.nextLine().split("").toSet()
    val symbolsOnlyInText1 = symbolsInText1.subtract(symbolsInText2)
    println("Символы, которые встречаются только в первом тексте: ")
    symbolsOnlyInText1.forEach { print("$it ") }
}

fun task3() {
    val scan = java.util.Scanner(System.`in`)
    println("Введите текст: ")
    val text = scan.nextLine()
    println("В тексте ${text.split("").toSet().size} различных символов")
}

fun main(args: Array<String>) {
//    task1()
//    task2()
    task3()
}