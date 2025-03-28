package com.example.androidjarvis.utils

import com.example.androidjarvis.SpeechHandler

class CommandParser(private val speechHandler: SpeechHandler) {

    fun parseCommand(input: String): String {
        // Convert input to lowercase to make it case-insensitive
        val command = input.trim().lowercase()

        return when (command) {
            "hello" -> {
                speechHandler.speak("Hello! Main Jarvis hoon")
                "Hello! Main Jarvis hoon"
            }
            else -> {
                speechHandler.speak("Mujhe samajh nahi aaya, kya bolo?")
                "Mujhe samajh nahi aaya, kya bolo?"
            }
        }
    }
}