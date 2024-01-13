package com.example.appchat.utils


fun isEmailValid(email: CharSequence): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun isTextValid(minLength: Int, text: String?): Boolean {
    if (text.isNullOrBlank() || text.length < minLength) {
        return false
    }
    return true
}

//fun isValidPassword(password: String?):PasswordStrength{
//    val hasUppercase = password!!.matches(Regex(".*[A-Z].*"))
//    val hasDigit = password.matches(Regex(".*\\d.*"))
//    val hasSpecialChar = password.matches(Regex(".*[!@#\\\$%^&*()-=_+\\\\[\\\\]{}|;':\\\",./<>?].*"))
//
//
//    return when {
//        password.length < 6 -> PasswordStrength.WEAK
//        password.length < 8 && (hasUppercase || hasDigit || hasSpecialChar) -> PasswordStrength.MEDIUM
//        password.length >= 8 && (hasUppercase && hasDigit && hasSpecialChar) -> PasswordStrength.STRONG
//        else -> PasswordStrength.WEAK
//    }
//}

