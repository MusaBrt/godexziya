package org.karpuzdev.codexiabot.util

import java.awt.Color
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*

object Util {

    private val random = Random()

    fun randomColor(): Color = Color(random.nextInt(255), random.nextInt(255), random.nextInt(255))

    fun getArrayAsString(arr: Array<String>, slicer: String): String {
        val sbb = StringBuilder()
        for (i in arr.indices) {
            sbb.append(arr[i])
            if (arr.size-1!=i) sbb.append(slicer)
        }
        return sbb.toString()
    }

    fun readFile(file: File): String? {
        if (!file.exists()) {
            println("Dosya yok?")
            return null
        }
        val sb = StringBuilder()
        try {
            BufferedReader(InputStreamReader(FileInputStream(file), StandardCharsets.UTF_8)).use { br ->
                var line: String? = null
                while (br.readLine().also { line = it } != null) {
                    sb.append(line).append("\n")
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            return sb.toString()
        }
    }

    fun writeFile(file: File, str: String): Boolean {
        return try {
            BufferedWriter(OutputStreamWriter(
                    FileOutputStream(file), StandardCharsets.UTF_8)).use { writer -> writer.write(str) }
            true
        } catch (ex: Exception) {
            ex.printStackTrace()
            false
        }
    }

}