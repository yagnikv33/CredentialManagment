package com.example.credentialmanagment.utils

import java.util.regex.Pattern

object UserCredUstils {
    fun isValidEmail(email: String): Boolean {
        return email.contains("@") && email.contains(".")
    }

    fun isValidPassword(password: String): Boolean {
        val passwordPattern = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@#\$%^&+=!]).{8,}\$"
        return Pattern.compile(passwordPattern).matcher(password).matches()
    }
}