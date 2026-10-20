package uniquindio.proyectoalertalocal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import uniquindio.proyectoalertalocal.navigation.AppNavigation
import uniquindio.proyectoalertalocal.core.theme.ProyectoAlertaLocalTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProyectoAlertaLocalTheme {
                AppNavigation()
            }
        }
    }
}
