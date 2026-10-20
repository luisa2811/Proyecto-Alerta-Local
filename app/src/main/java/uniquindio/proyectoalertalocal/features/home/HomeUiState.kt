package uniquindio.proyectoalertalocal.features.home

enum class VistaAlertas {
    MAPA,
    LISTA
}

data class HomeUiState(
    val categoriaSeleccionada: CategoriaAlerta? = null,
    val proximidadSeleccionada: String = "Cerca de mí",
    val vistaSeleccionada: VistaAlertas = VistaAlertas.MAPA,
    val alertas: List<AlertUiModel> = alertasDemostrativas(),
    val alertaSeleccionadaId: String? = "seguridad-1"
) {
    val alertasFiltradas: List<AlertUiModel>
        get() = categoriaSeleccionada?.let { categoria ->
            alertas.filter { it.categoria == categoria }
        } ?: alertas

    val alertaSeleccionada: AlertUiModel?
        get() = alertasFiltradas.firstOrNull { it.id == alertaSeleccionadaId }
            ?: alertasFiltradas.firstOrNull()
}

fun alertasDemostrativas(): List<AlertUiModel> = listOf(
    AlertUiModel(
        id = "seguridad-1",
        titulo = "Intento de robo en la Cra 15 con Calle 8",
        descripcion = "Reportado cerca de Cra 15 #8-42. 2 personas en moto...",
        categoria = CategoriaAlerta.SEGURIDAD,
        verificada = true,
        nombreReportante = "María Jiménez",
        inicialesReportante = "MJ",
        rolReportante = "Residente",
        ubicacion = "Cra 15 #8-42",
        distancia = "A 300 m",
        antiguedad = "hace 2 h",
        posicionX = 0.34f,
        posicionY = 0.20f,
        meGusta = 28,
        cantidadComentarios = 6
    ),
    AlertUiModel(
        id = "emergencia-1",
        titulo = "Persona requiere asistencia médica",
        descripcion = "Vecinos solicitan apoyo en la esquina del parque.",
        categoria = CategoriaAlerta.EMERGENCIAS,
        verificada = true,
        nombreReportante = "Carlos Rojas",
        inicialesReportante = "CR",
        rolReportante = "Residente",
        ubicacion = "Calle 9 con Cra 14",
        distancia = "A 450 m",
        antiguedad = "hace 15 min",
        posicionX = 0.51f,
        posicionY = 0.42f,
        meGusta = 18,
        cantidadComentarios = 4
    ),
    AlertUiModel(
        id = "infraestructura-1",
        titulo = "Alumbrado público fuera de servicio",
        descripcion = "La luminaria permanece apagada desde anoche.",
        categoria = CategoriaAlerta.INFRAESTRUCTURA,
        verificada = false,
        nombreReportante = "Ana Torres",
        inicialesReportante = "AT",
        rolReportante = "Residente",
        ubicacion = "Cra 16 con Calle 7",
        distancia = "A 600 m",
        antiguedad = "hace 3 h",
        posicionX = 0.77f,
        posicionY = 0.38f,
        meGusta = 14,
        cantidadComentarios = 2
    ),
    AlertUiModel(
        id = "seguridad-2",
        titulo = "Vehículo sospechoso en la cuadra",
        descripcion = "Automóvil estacionado por varias horas frente al parque.",
        categoria = CategoriaAlerta.SEGURIDAD,
        verificada = false,
        nombreReportante = "Julián Mora",
        inicialesReportante = "JM",
        rolReportante = "Residente",
        ubicacion = "Calle 10 #14-20",
        distancia = "A 750 m",
        antiguedad = "hace 40 min",
        posicionX = 0.25f,
        posicionY = 0.53f,
        meGusta = 8,
        cantidadComentarios = 1
    ),
    AlertUiModel(
        id = "infraestructura-2",
        titulo = "Hueco en la vía",
        descripcion = "Daño en la calzada que dificulta el paso de bicicletas.",
        categoria = CategoriaAlerta.INFRAESTRUCTURA,
        verificada = true,
        nombreReportante = "Laura Peña",
        inicialesReportante = "LP",
        rolReportante = "Residente",
        ubicacion = "Cra 14 #9-18",
        distancia = "A 900 m",
        antiguedad = "hace 5 h",
        posicionX = 0.67f,
        posicionY = 0.56f,
        meGusta = 11,
        cantidadComentarios = 3
    )
)
