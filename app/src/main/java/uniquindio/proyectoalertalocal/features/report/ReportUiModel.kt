package uniquindio.proyectoalertalocal.features.report

import androidx.compose.ui.graphics.Color

enum class CategoriaPublicacion(val etiqueta: String, val color: Color) {
    SEGURIDAD("Seguridad", Color(0xFFE95050)),
    EMERGENCIAS("Emergencias", Color(0xFFE89A21)),
    INFRAESTRUCTURA("Infraestructura", Color(0xFF3084BB)),
    MASCOTAS("Mascotas", Color(0xFFD29213)),
    COMUNIDAD("Comunidad", Color(0xFF36A278))
}

data class PublicacionUiModel(
    val id: String,
    val titulo: String,
    val descripcion: String,
    val categoria: CategoriaPublicacion,
    val estado: String,
    val distancia: String,
    val antiguedad: String,
    val ubicacion: String,
    val nombreReportante: String,
    val inicialesReportante: String,
    val votosImportante: Int,
    val cantidadComentarios: Int
)

val publicacionesDemostrativas = listOf(
    PublicacionUiModel(
        id = "publicacion-seguridad-1",
        titulo = "Intento de robo en la Cra 15 con Calle 8",
        descripcion = "Anoche cerca de las 9:30 pm dos personas en moto intentaron forzar la puerta de una vivienda en esta cuadra. Un vecino los espantó con las luces del carro. Se recomienda reforzar la vigilancia y avisar a la policía del sector.",
        categoria = CategoriaPublicacion.SEGURIDAD,
        estado = "Verificado",
        distancia = "A 300 m",
        antiguedad = "hace 2 h",
        ubicacion = "Cra 15 #8-42, La Esperanza",
        nombreReportante = "María Jiménez",
        inicialesReportante = "MJ",
        votosImportante = 28,
        cantidadComentarios = 6
    ),
    PublicacionUiModel(
        id = "publicacion-infraestructura-1",
        titulo = "Poste de luz apagado hace 3 días",
        descripcion = "La luminaria de la esquina está apagada y la calle queda oscura durante la noche.",
        categoria = CategoriaPublicacion.INFRAESTRUCTURA,
        estado = "Pendiente",
        distancia = "A 850 m",
        antiguedad = "hace 5 h",
        ubicacion = "Calle 10 con Cra 14",
        nombreReportante = "Carlos Rojas",
        inicialesReportante = "CR",
        votosImportante = 14,
        cantidadComentarios = 2
    ),
    PublicacionUiModel(
        id = "publicacion-mascotas-1",
        titulo = "Perro perdido, raza mestiza, collar azul",
        descripcion = "Se busca perro pequeño color café visto por última vez cerca del parque.",
        categoria = CategoriaPublicacion.MASCOTAS,
        estado = "Resuelto",
        distancia = "A 1.2 km",
        antiguedad = "hace 1 día",
        ubicacion = "Parque La Esperanza",
        nombreReportante = "Ana Torres",
        inicialesReportante = "AT",
        votosImportante = 41,
        cantidadComentarios = 19
    ),
    PublicacionUiModel(
        id = "publicacion-comunidad-1",
        titulo = "Acumulación de basuras en el parque",
        descripcion = "Hay varias bolsas de basura junto a la zona de juegos infantiles.",
        categoria = CategoriaPublicacion.COMUNIDAD,
        estado = "Verificado",
        distancia = "A 400 m",
        antiguedad = "hace 2 días",
        ubicacion = "Parque del barrio",
        nombreReportante = "Julián Mora",
        inicialesReportante = "JM",
        votosImportante = 9,
        cantidadComentarios = 3
    )
)
