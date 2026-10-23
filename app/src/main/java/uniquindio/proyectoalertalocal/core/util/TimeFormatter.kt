package uniquindio.proyectoalertalocal.core.util

import java.time.Duration
import java.time.Instant

private const val MINUTES_PER_HOUR = 60
private const val MINUTES_PER_DAY = 1440

fun Instant.toRelativeTime(now: Instant = Instant.now()): String {
    val minutes = Duration.between(this, now).toMinutes().coerceAtLeast(0)
    val days = minutes / MINUTES_PER_DAY
    return when {
        minutes < 1 -> "hace un momento"
        minutes < MINUTES_PER_HOUR -> "hace $minutes min"
        minutes < MINUTES_PER_DAY -> "hace ${minutes / MINUTES_PER_HOUR} h"
        days == 1L -> "hace 1 día"
        else -> "hace $days días"
    }
}
