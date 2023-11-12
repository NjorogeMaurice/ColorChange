package com.android.ux.colorchange

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import java.util.Locale
import java.util.Objects

class MainActivity : ComponentActivity(),TextToSpeech.OnInitListener {
    lateinit var speechRecognizer : SpeechRecognizer
    lateinit var tts : TextToSpeech
    private val REQUEST_CODE_SPEECH_INPUT=1
    lateinit var  blueScreenView : View
    lateinit var  redScreenView : View
    lateinit var  changeColorView : View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        blueScreenView = layoutInflater.inflate(R.layout.blue_screen, null)
        redScreenView = layoutInflater.inflate(R.layout.red_screen, null)

        changeColorView= findViewById<TextView>(R.id.changetextview)

        val startrecognitionbtn = findViewById<Button>(R.id.startRecognitionButton)

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)

        tts = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val results = tts.setLanguage(Locale.getDefault())
                if (results == TextToSpeech.LANG_MISSING_DATA || results == TextToSpeech.LANG_NOT_SUPPORTED){
                    Toast.makeText(this,"Language is not supported",Toast.LENGTH_LONG).show()
                }
            }
        }

        startrecognitionbtn.setOnClickListener {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
//            speechRecognizer.startListening(intent)
            try{
//                    onActivityResult(REQUEST_CODE_SPEECH_INPUT, intent)
                startActivityForResult(intent,REQUEST_CODE_SPEECH_INPUT)

            }catch (e:Exception){
                Toast.makeText(this,"This: "+e.message,Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onActivityResult(requestcode: Int, resultCode:Int, data: Intent?) {
        super.onActivityResult(requestcode,resultCode,data)

        if (requestcode == REQUEST_CODE_SPEECH_INPUT){

            if (resultCode == RESULT_OK && data != null){
                val res: ArrayList<String> = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS) as ArrayList<String>
                val output : String = Objects.requireNonNull(res)[0]

                if (output.contains("blue") || output.contains("Blue")  ){
                    showBlueScreen()
                }
                else if (output.contains("red") || output.contains(" Red") ){
                    showRedScreen()
                }
                else{
                    showNone()
                    Toast.makeText(this,"Invalid text",Toast.LENGTH_LONG).show()
                }

            }
            else{
                Toast.makeText(this,"Error",Toast.LENGTH_LONG).show()

            }
        }
        else{
            Toast.makeText(this,"Not speech",Toast.LENGTH_LONG).show()

        }

    }

    private fun processSpeech(text: String) {
        when (text.toLowerCase()) {
            "blue" -> {
                tts.speak("Here is the blue screen", TextToSpeech.QUEUE_FLUSH, null, null)
                showBlueScreen()
            }
            "red" -> {
                tts.speak("Here is the red screen", TextToSpeech.QUEUE_FLUSH, null, null)
                showRedScreen()
            }
        }
    }

    private fun showBlueScreen() {
        changeColorView.setBackgroundColor(Color.BLUE)
    }

    private fun showRedScreen() {
        changeColorView.setBackgroundColor(Color.RED)
    }
    private fun showNone() {
        changeColorView.setBackgroundColor(Color.WHITE)

    }

    override fun onInit(p0: Int) {
        TODO("Not yet implemented")
    }

}

