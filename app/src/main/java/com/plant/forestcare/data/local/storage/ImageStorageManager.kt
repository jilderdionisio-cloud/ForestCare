package com.plant.forestcare.data.local.storage

import android.content.Context
import android.net.Uri
import java.io.File
import java.util.UUID

interface ImageStorage {
    fun saveImage(uri: Uri): String?
    fun deleteImage(path: String): Boolean
    fun getFileFromPath(path: String): File?
}

class ImageStorageManager(private val context: Context) : ImageStorage {

    private val folderName = "plants_media"

    override fun saveImage(uri: Uri): String? {
        return try {
            val directory = File(context.filesDir, folderName).apply {
                if (!exists()) mkdirs()
            }

            val fileName = "${UUID.randomUUID()}.jpg"
            val targetFile = File(directory, fileName)

            context.contentResolver.openInputStream(uri)?.use { input ->
                targetFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            targetFile.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override fun deleteImage(path: String): Boolean {
        return try {
            val file = File(path)
            if (file.exists()) file.delete() else false
        } catch (e: Exception) {
            false
        }
    }

    override fun getFileFromPath(path: String): File? {
        val file = File(path)
        return if (file.exists()) file else null
    }
}
