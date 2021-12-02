object InputUtils {
    fun readFile(filePath: String): String = javaClass.getResource(filePath).readText()

    fun String.toIntList(): List<Int> = this.split("\n").map { it.toInt() }
}