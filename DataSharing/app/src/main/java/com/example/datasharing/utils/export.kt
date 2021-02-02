package com.example.datasharing.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import java.io.File

object Exporter {

    const val EXPORT_DIR = "csv"

    fun getTemporaryFile(
        context: Context,
        filename: String
    ): File {
        val path = File(context.cacheDir, EXPORT_DIR)
        path.mkdirs()
        val file = File(path, "$filename.csv")
        return file
    }

    fun writeSampleCsv(file: File) {
        val rows = listOf(listOf("a", "b", "c"), listOf("d", "e", "f"))
        csvWriter().open(file) {
            rows.forEach {
                writeRow(it)
            }
        }
    }
}

fun File.getUriForFile(context: Context): Uri = FileProvider
    .getUriForFile(context, "com.example.fileprovider", this)

fun Intent.shareCsv(uri: Uri): Intent {
    return this.apply {
        action = Intent.ACTION_SEND
        type = "text/comma-separated-values"
        flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        putExtra(Intent.EXTRA_STREAM, uri)
    }
}