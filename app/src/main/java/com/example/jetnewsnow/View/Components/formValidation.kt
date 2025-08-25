package com.example.jetnewsnow.View.Components

import android.util.Patterns



fun validateEmail(email: String): String? {
    return when {
        email.isBlank() -> "Email can't be empty"
        !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Enter a valid email"
        else -> null
    }
}

fun validatePassword(password: String): String? {
    return when {
        password.isBlank() -> "Password can't be empty"
        password.length < 6 -> "Password must be at least 6 characters"
        else -> null
    }
}
