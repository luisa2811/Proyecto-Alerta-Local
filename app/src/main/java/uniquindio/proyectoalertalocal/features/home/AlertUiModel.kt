package uniquindio.proyectoalertalocal.features.home

import androidx.compose.ui.graphics.Color

enum class CategoriaAlerta(val etiqueta: String, val color: Color) {
    SEGURIDAD("Seguridad", Color(0xFFEF476F)),
    EMERGENCIAS("Emergencias", Color(0xFFFF9800)),
    INFRAESTRUCTURA("Infraestructura", Color(0xFF168BD2))
}

data class AlertUiModel(
    val id: String,
    val titulo: String,
    val descripcion: String,
    val categoria: CategoriaAlerta,
    val verificada: Boolean,
    val nombreReportante: String,
    val inicialesReportante: String,
    val rolReportante: String,
    val ubicacion: String,
    val distancia: String,
    val antiguedad: String,
    val posicionX: Float,
    val posicionY: Float,
    val meGusta: Int = 0,
    val cantidadComentarios: Int = 0
)
