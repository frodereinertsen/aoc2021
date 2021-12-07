object InputUtils {
    fun readFile(filePath: String): String = javaClass.getResource(filePath).readText()
}