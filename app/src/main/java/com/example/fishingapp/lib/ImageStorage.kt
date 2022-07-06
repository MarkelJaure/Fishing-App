package com.example.fishingapp.lib

import android.content.ContentValues
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.util.Log
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ImageStorage {

    fun storeImageOnLocal(image: Bitmap, packageName: String, prefix: String): File? {
        val pictureFile: File = getNameAndPathForImage(prefix,packageName)!!
        if (pictureFile == null) {
            Log.d(
                "Imagen",
                "Error creating media file, check storage permissions: "
            ) // e.getMessage());
            return null
        }
        try {
            val fos = FileOutputStream(pictureFile)
            image.compress(Bitmap.CompressFormat.PNG, 90, fos)
            fos.close()

        } catch (e: FileNotFoundException) {
            Log.d(ContentValues.TAG, "File not found: ")
        } catch (e: IOException) {
            Log.d(ContentValues.TAG, "Error accessing file: " + e.message)
        }
        return pictureFile
    }

    fun getNameAndPathForImage(prefix:String,packageName: String): File? {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        val mediaStorageDir: File = File(
            Environment.getExternalStorageDirectory()
                .toString() + "/Android/data/"
                    + packageName
                    + "/Files"
        )

        Log.w("Imagen path", Environment.getExternalStorageDirectory()
            .toString() + "/Android/data/")

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null
            }
        }
        // Create a media file name
        val timeStamp = SimpleDateFormat("ddMMyyyy_HHmmssSS").format(Date())
        val mediaFile: File
        val mImageName = prefix + "_$timeStamp.png"
        mediaFile = File(mediaStorageDir.path + File.separator + mImageName)

        Log.w("Imagen path completo", mediaStorageDir.path + File.separator + mImageName)
        return mediaFile
    }

    fun uploadImageToFirebase(image: File) {
        var file = Uri.fromFile(image)
        val riversRef = Firebase.storage.reference.child("${file.lastPathSegment}")
        var uploadTask = riversRef.putFile(file)

        uploadTask.addOnFailureListener {
            Log.w("storage firebase", "fallo")
        }.addOnSuccessListener { taskSnapshot ->
            Log.w("storage firebase", "exito")
        }
    }
}