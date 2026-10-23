package uniquindio.proyectoalertalocal.domain.model

enum class Badge(val displayName: String, val requirement: String) {
    FIRST_REPORT(
        "Primer reporte",
        "Crea tu primer reporte."
    ),
    TRUSTED_REPORTER(
        "Reportero confiable",
        "Logra 10 reportes verificados."
    ),
    NEIGHBORHOOD_VOICE(
        "Voz del barrio",
        "Ten el reporte con más votos \"Es importante\" del mes, con al menos 5 votos."
    ),
    ACTIVE_NEIGHBOR(
        "Vecino participativo",
        "Escribe 10 comentarios."
    )
}
