package com.sohail.errorhandling.domain

class UserDataValidator {
    fun validatePassword(password: String): Result<Unit, PasswordError> {
        if (password.length < 9) return Result.Error(PasswordError.TOO_SHORT)

        val hasUpperCaseChar = password.any { it.isUpperCase() }
        if (!hasUpperCaseChar) return Result.Error(PasswordError.NO_UPPERCASE)

        val hasDigit = password.any { it.isDigit() }
        if (!hasDigit) return Result.Error(PasswordError.NO_DIGIT)

        val hasSpecialChar = password.any { !it.isLetterOrDigit() }
        if (!hasSpecialChar) return Result.Error(PasswordError.NO_SPECIAL_CHAR)

        val hasLowerCaseChar = password.any { it.isLowerCase() }
        if (!hasLowerCaseChar) return Result.Error(PasswordError.NO_LOWERCASE)

        return Result.Success(Unit)

    }

    enum class PasswordError : Error {
        TOO_SHORT,
        NO_UPPERCASE,
        NO_LOWERCASE,
        NO_DIGIT,
        NO_SPECIAL_CHAR
    }
}