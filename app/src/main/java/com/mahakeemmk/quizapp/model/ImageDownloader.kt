package com.mahakeemmk.quizapp.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.net.HttpURLConnection
import java.net.URL

class ImageDownloader {
    fun download(link:String):Bitmap {
        val url = URL(link)
        val connection = url.openConnection() as HttpURLConnection
        val bitmap = try {
            val inputStream = connection.getInputStream()
            BitmapFactory.decodeStream(inputStream)
        } finally {
            connection.disconnect()
        }
        return bitmap
    }
}