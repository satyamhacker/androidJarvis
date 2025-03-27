package com.example.androidjarvis

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.speech.RecognitionListener
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.widget.TextView
import java.util.Locale

class SpeechHandler(private val context: Context) : TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = null
    private var speechRecognizer: SpeechRecognizer? = null
    var isTtsReady = false

    init {
        // Initialize TextToSpeech
        tts = TextToSpeech(context, this)

        // Initialize SpeechRecognizer
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            // Check if the desired language is available
            val result = tts?.setLanguage(Locale.US)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                isTtsReady = false
                println("Language is not supported or missing data.")
            } else {
                isTtsReady = true
                println("TextToSpeech is ready.")
            }
        } else {
            isTtsReady = false
            println("TextToSpeech initialization failed with status: $status")
        }
    }

    fun speak(text: String) {
        if (isTtsReady && tts != null) {
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
        } else {
            println("TextToSpeech is not ready yet.")
        }
    }

    fun startListening(
        speechRecognizer: SpeechRecognizer,
        speechIntent: Intent,
        responseTextView: TextView,
        handler: Handler
    ) {
        speechRecognizer?.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                responseTextView.text = "Listening..."
            }

            override fun onBeginningOfSpeech() {}

            override fun onRmsChanged(rmsdB: Float) {}

            override fun onBufferReceived(buffer: ByteArray?) {}

            override fun onEndOfSpeech() {}

            override fun onError(error: Int) {
                responseTextView.text = "Error: $error"
            }

            override fun onResults(results: Bundle?) {
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (!matches.isNullOrEmpty()) {
                    responseTextView.text = matches[0]
                }
            }

            override fun onPartialResults(partialResults: Bundle?) {}

            override fun onEvent(eventType: Int, params: Bundle?) {}
        })

        speechRecognizer?.startListening(speechIntent)

        handler.postDelayed({
            speechRecognizer?.stopListening()
        }, 3000)
    }

    fun shutdown() {
        // Shutdown TextToSpeech
        tts?.stop()
        tts?.shutdown()
        tts = null
        isTtsReady = false

        // Shutdown SpeechRecognizer
        speechRecognizer?.destroy()
        speechRecognizer = null
    }
}