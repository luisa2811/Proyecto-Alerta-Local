package uniquindio.proyectoalertalocal.core.util

private val EMAIL_PATTERN = Regex(
    "^[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+)*@" +
        "(?:[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?\\.)+[A-Za-z]{2,63}$"
)

fun String.isValidEmail(): Boolean = EMAIL_PATTERN.matches(trim())

fun String.hasMinimumPasswordLength(): Boolean = length >= 8

fun String.hasUppercaseLetter(): Boolean = any(Char::isUpperCase)

fun String.hasNumber(): Boolean = any(Char::isDigit)

fun String.isStrongPassword(): Boolean =
    hasMinimumPasswordLength() &&
        hasUppercaseLetter() &&
        hasNumber()
