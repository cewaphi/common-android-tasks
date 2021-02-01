package com.example.profilepicture.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.example.profilepicture.R
import com.example.profilepicture.ui.home.HomeFragment
import com.google.android.material.navigation.NavigationView
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*


/**
 * According to
 * https://stackoverflow.com/questions/37192611/navigation-drawer-header-textview-nullpointerexception
 * https://stackoverflow.com/questions/43284754/android-how-to-access-the-navigation-drawer-header-items
 * views from the header view needs to be obtained as following
 */
fun getHeaderView(navigationView: NavigationView): View {
    return navigationView.getHeaderView(0)
}

/**
 * Extension function to get an Intent for opening a picture from the gallery
 */
fun Intent.openImage(): Intent {
    return this.apply {
        this.type = "image/*"
        this.action = Intent.ACTION_OPEN_DOCUMENT
        addCategory(Intent.CATEGORY_OPENABLE)
    }
}

fun setImageFromUri(uri: Uri, imageView: ImageView) {
    Picasso.get().load(uri).error(R.drawable.ic_baseline_error_24).into(imageView)
}

/**
 * Saving and opening a file as described in the official documentation:
 * https://developer.android.com/training/data-storage/app-specific#kotlin
 */
object InternalMemory {

    fun savePickedImageFromStream(uri: Uri, filename: String, context: Context) {
        Date().apply { }
        val fos = context.openFileOutput(filename, Context.MODE_PRIVATE)
        val ips = context.contentResolver.openInputStream(uri)
        ips?.use {
            it.copyTo(fos)
            Toast.makeText(context, "Saved picture via copy from 'is' to 'fos'", Toast.LENGTH_LONG)
                .show()
        }
    }


    fun saveCompressBitmapFromUri(uri: Uri, filename: String, context: Context) {
        Date().apply { }
        val fos = context.openFileOutput(filename, Context.MODE_PRIVATE)
        val ips = context.contentResolver.openInputStream(uri)
        ips?.use {
            it.copyTo(fos)
            Toast.makeText(context, "Saved picture via copy from 'is' to 'fos'", Toast.LENGTH_LONG)
                .show()
        }
    }

    fun setImageByStream(context: Context, filename: String) {
        context.openFileInput(filename)
    }

    fun openFileByInputStream(filename: String, context: Context): FileInputStream {
        return context.openFileInput(filename)
    }

    fun setImageFromInternalFile(context: Context, filename: String, imageView: ImageView) {
        val file = File(context.filesDir, HomeFragment.SAVED_FILE_NAME)
        file.setImage(context, imageView)
        Toast.makeText(context, "Set new profile picture from saved image", Toast.LENGTH_LONG)
            .show()
    }

}

fun Uri.toBitmap() {

}

fun Uri.setImage(imageView: ImageView) {
    Picasso.get().load(this).error(R.drawable.ic_baseline_error_24).into(imageView)
}

fun File.setImage(context: Context, imageView: ImageView) {
    try {
        Picasso.get().load(this).error(R.drawable.ic_baseline_error_24).into(imageView)
    } catch (e: Exception) {
        Toast.makeText(context, "Could not load profile picture: ${e.message}", Toast.LENGTH_LONG)
            .show()
    }
}

/**
 * Extension to compress bitmap
 * adapted from
 * https://stackoverflow.com/a/64648577
 */
fun Bitmap.compress(cacheDir: File, f_name: String, quality: Int = 70): File {
    val f = File(cacheDir, "user$f_name.jpg")
    f.createNewFile()
    ByteArrayOutputStream().use { stream ->
        compress(Bitmap.CompressFormat.JPEG, quality, stream)
        val bArray = stream.toByteArray()
        FileOutputStream(f).use { os -> os.write(bArray) }
    }
    return f
}

