object InputUtils {
    fun readFile(filePath: String): String = javaClass.getResource(filePath)?.readText() ?: throw RuntimeException("No file here: $filePath")
}