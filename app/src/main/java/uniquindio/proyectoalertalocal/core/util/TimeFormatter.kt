package uniquindio.proyectoalertalocal.core.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import java.time.Duration
import java.time.Instant
import uniquindio.proyectoalertalocal.R

private const val MINUTES_PER_HOUR = 60
private const val MINUTES_PER_DAY = 1440

@Composable
fun Instant.toRelativeTime(now: Instant = Instant.now()): String {
    val minutes = Duration.between(this, now).toMinutes().coerceAtLeast(0)
    val days = minutes / MINUTES_PER_DAY
    return when {
        minutes < 1 -> stringResource(R.string.time_just_now)
        minutes < MINUTES_PER_HOUR -> pluralStringResource(
            R.plurals.time_minutes_ago,
            minutes.toInt(),
            minutes.toInt()
        )
        minutes < MINUTES_PER_DAY -> {
            val hours = (minutes / MINUTES_PER_HOUR).toInt()
            pluralStringResource(R.plurals.time_hours_ago, hours, hours)
        }
        else -> pluralStringResource(R.plurals.time_days_ago, days.toInt(), days.toInt())
    }
}
