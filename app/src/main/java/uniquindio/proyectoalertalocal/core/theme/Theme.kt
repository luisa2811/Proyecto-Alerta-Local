package uniquindio.proyectoalertalocal.core.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = AppPrimary,
    onPrimary = AppWhite,
    secondary = AppAccent,
    onSecondary = AppWhite,
    tertiary = AppTextSecondary,
    background = AppTextPrimary,
    onBackground = AppWhite,
    surface = AppTextPrimary,
    onSurface = AppWhite,
    outline = AppBorder
)

private val LightColorScheme = lightColorScheme(
    primary = AppPrimary,
    onPrimary = AppWhite,
    secondary = AppAccent,
    onSecondary = AppWhite,
    tertiary = AppTextSecondary,
    background = AppBackground,
    onBackground = AppTextPrimary,
    surface = AppWhite,
    onSurface = AppTextPrimary,
    outline = AppBorder
)

@Composable
fun ProyectoAlertaLocalTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}