package uniquindio.proyectoalertalocal.core.util

private val EMAIL_PATTERN = Regex(
    "^[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+)*@" +
        "(?:[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?\\.)+[A-Za-z]{2,63}$"
)

fun String.isValidEmail(): Boolean = EMAIL_PATTERN.matches(trim())
