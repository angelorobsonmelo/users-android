package br.com.angelorobson.usermvi.utils

object FileUtils {

    @JvmStatic
    fun getJson(path: String): String {
        val url = this.javaClass.classLoader?.getResource(path)
        return String(url?.readBytes()!!)
    }
}