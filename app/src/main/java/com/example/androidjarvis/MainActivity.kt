package com.example.androidjarvis

        import android.content.Intent
        import android.os.Bundle
        import android.os.Handler
        import android.os.Looper
        import android.speech.RecognizerIntent
        import android.speech.SpeechRecognizer
        import android.widget.Button
        import android.widget.EditText
        import android.widget.ImageView
        import android.widget.TextView
        import androidx.activity.ComponentActivity
        import androidx.activity.enableEdgeToEdge
        import com.example.androidjarvis.utils.CommandParser
        import java.util.Locale

        class MainActivity : ComponentActivity() {

            private lateinit var speechHandler: SpeechHandler
            private lateinit var responseTextView: TextView
            private lateinit var commandEditTextView: EditText
            private lateinit var commandParser: CommandParser

            override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                enableEdgeToEdge()
                setContentView(R.layout.activity_main)

                // Initialize SpeechHandler
                speechHandler = SpeechHandler(this)

                // Initialize CommandParser
                commandParser = CommandParser(speechHandler)

                // Bind views using findViewById
                commandEditTextView = findViewById(R.id.commandEditTextViewId)
                responseTextView = findViewById(R.id.showResponseTextViewId)
                val executeButton = findViewById<Button>(R.id.executeButtonId)
                val micImageView = findViewById<ImageView>(R.id.micImageViewId)

                // Set click listener for the execute button
                executeButton.setOnClickListener {
                    val command = commandEditTextView.text.toString().trim()
                    if (command.isNotEmpty()) {
                        // Parse the command using the CommandParser
                        val response = commandParser.parseCommand(command)
                        responseTextView.text = response
                    } else {
                        responseTextView.text = "Please enter a command."
                    }
                }

                // SpeechRecognizer setup
                val speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
                val speechIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                    putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                    putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
                }
                val handler = Handler(Looper.getMainLooper())

                // Set click listener for the microphone image
                micImageView.setOnClickListener {
                    // Start listening using the SpeechHandler
                    speechHandler.startListening(speechRecognizer, speechIntent, responseTextView, handler)
                }
            }

            override fun onDestroy() {
                super.onDestroy()
                // Release resources
                speechHandler.shutdown()
            }
        }