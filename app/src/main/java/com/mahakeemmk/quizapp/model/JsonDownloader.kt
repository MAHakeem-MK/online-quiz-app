package com.mahakeemmk.quizapp.model

import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class JsonDownloader {

    fun download(link : String) : String {

        val stringBuilder = StringBuilder()

        val urlConnection = URL(link).openConnection() as HttpURLConnection

        try {
            var bufferedReader = BufferedReader(InputStreamReader(BufferedInputStream(urlConnection.inputStream)))
            var inputLine = bufferedReader.readLine()
            while (inputLine != null) {
                stringBuilder.append(inputLine)
                inputLine = bufferedReader.readLine()
            }
        } finally {
            urlConnection.disconnect()
        }

        return stringBuilder.toString()
    }
}