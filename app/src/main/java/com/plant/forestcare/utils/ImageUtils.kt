package com.plant.forestcare.utils

import android.util.Base64
import java.io.File

fun File.toBase64(): String {
    return Base64.encodeToString(this.readBytes(), Base64.NO_WRAP)
}
