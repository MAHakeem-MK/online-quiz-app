package com.mahakeemmk.quizapp.controller

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.opengl.Visibility
import android.os.AsyncTask
import android.os.Bundle
import android.provider.CalendarContract
import android.provider.MediaStore
import android.provider.Settings
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity;
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.mahakeemmk.quizapp.R
import com.mahakeemmk.quizapp.model.*

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    var correctAnswerIndex: Int = -1
    var correctPlant:Plant? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        button1.setOnClickListener {
            showCorrectAnswer(0)

        }
        button2.setOnClickListener {
            showCorrectAnswer(1)
        }
        button3.setOnClickListener {
            showCorrectAnswer(2)
        }
        button4.setOnClickListener {
            showCorrectAnswer(3)
        }

    }

    override fun onResume() {
        super.onResume()

        val networkChecker = NetworkChecker(this)
        if (networkChecker.isConnected()) {
            LoadData().execute()
            floatingActionButton.setOnClickListener {
                showCorrectAnswerReset()
                LoadData().execute()
            }
        } else {
            val alertDialog = AlertDialog.Builder(this).create()
            alertDialog.setTitle("Network Error!")
            alertDialog.setMessage("Please connect to internet")
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,"OK",{
                    dialog, which ->
                startActivity(Intent(Settings.ACTION_SETTINGS))
            })
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,"Cancel",{
                    dialog, which ->
                Toast.makeText(this,"Internet is required!",Toast.LENGTH_LONG).show()
                finish()
            })
            alertDialog.show()
        }
    }

    fun progressVisibility(state : Boolean) {
        if (state) {
            progress_background.visibility = View.VISIBLE
            progressBar.visibility = View.VISIBLE
            window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        } else {
            progress_background.visibility = View.GONE
            progressBar.visibility = View.GONE
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }
    }

    fun showCorrectAnswer(guess:Int){
        when(correctAnswerIndex) {
            0 -> button1.setBackgroundResource(R.drawable.true_button)
            1 -> button2.setBackgroundResource(R.drawable.true_button)
            2 -> button3.setBackgroundResource(R.drawable.true_button)
            3 -> button4.setBackgroundResource(R.drawable.true_button)
        }

        if (guess != correctAnswerIndex) {
            when(guess) {
                0 -> button1.setBackgroundResource(R.drawable.false_button)
                1 -> button2.setBackgroundResource(R.drawable.false_button)
                2 -> button3.setBackgroundResource(R.drawable.false_button)
                3 -> button4.setBackgroundResource(R.drawable.false_button)
            }
        }
    }

    fun showCorrectAnswerReset() {
        button1.setBackgroundResource(R.drawable.curved_button)
        button2.setBackgroundResource(R.drawable.curved_button)
        button3.setBackgroundResource(R.drawable.curved_button)
        button4.setBackgroundResource(R.drawable.curved_button)
    }

    inner class LoadData:AsyncTask<String,Int,ArrayList<Plant>>() {

        override fun onPreExecute() {
            super.onPreExecute()
            progressVisibility(true)
        }

        override fun doInBackground(vararg params: String?): ArrayList<Plant> {
            val jsonDownloader = JsonDownloader()
            val LINK = "http://plantplaces.com/perl/mobile/flashcard.pl"
            val plantParser = PlantParser()
            return plantParser.parseJson(jsonDownloader.download(LINK))
        }

        override fun onPostExecute(result: ArrayList<Plant>) {
            super.onPostExecute(result)

            var noOfItems = result.size
            var randomPlant1Index = (Math.random() * noOfItems).toInt()
            var randomPlant2Index = (Math.random() * noOfItems).toInt()
            var randomPlant3Index = (Math.random() * noOfItems).toInt()
            var randomPlant4Index = (Math.random()* noOfItems).toInt()

            var allRandomPlants = ArrayList<Plant>()
            allRandomPlants.add(result.get(randomPlant1Index))
            allRandomPlants.add(result.get(randomPlant2Index))
            allRandomPlants.add(result.get(randomPlant3Index))
            allRandomPlants.add(result.get(randomPlant4Index))

            button1.text = allRandomPlants.get(0).toString()
            button2.text = allRandomPlants.get(1).toString()
            button3.text = allRandomPlants.get(2).toString()
            button4.text = allRandomPlants.get(3).toString()

            Log.d("options","1: ${allRandomPlants[0].species} 2: ${allRandomPlants[1].species} 3: ${allRandomPlants[2].species} 4: ${allRandomPlants[3].species}")

            correctAnswerIndex = (Math.random() * allRandomPlants.size).toInt()
            correctPlant = allRandomPlants.get(correctAnswerIndex)

            val plantDownloader = PlantDownloader()
            val plantName:String = correctPlant?.pinctureName ?: "https://mdbootstrap.com/img/Photos/Others/placeholder-avatar.jpg"
            plantDownloader.execute(plantName)
        }

    }

    inner class PlantDownloader:AsyncTask<String,Int,Bitmap?>() {
        override fun doInBackground(vararg params: String?): Bitmap? {
            val link = "http://plantplaces.com/photos/${params[0]}"
            return ImageDownloader().download(link)
        }

        override fun onPostExecute(result: Bitmap?) {
            super.onPostExecute(result)
            imageView.setImageBitmap(result)

            progressVisibility(false)
        }
    }

}
