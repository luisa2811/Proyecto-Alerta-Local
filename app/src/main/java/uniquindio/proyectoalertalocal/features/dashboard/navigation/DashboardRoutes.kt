package uniquindio.proyectoalertalocal.features.dashboard.navigation

object DashboardRoutes {
    const val INICIO = "dashboard_inicio"
    const val ALERTAS = "dashboard_alertas"
    const val CREAR = "dashboard_crear"
    const val MIS_REPORTES = "dashboard_mis_reportes"
    const val PERFIL = "dashboard_perfil"
    const val NOTIFICACIONES = "dashboard_notificaciones"
    const val DETALLE_PUBLICACION = "dashboard_detalle_publicacion/{publicationId}"

    fun detallePublicacion(publicationId: String): String =
        "dashboard_detalle_publicacion/$publicationId"
}
