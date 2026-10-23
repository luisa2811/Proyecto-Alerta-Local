package uniquindio.proyectoalertalocal.features.dashboard.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class DashboardRoutes {
    @Serializable
    data object ModeratorHome : DashboardRoutes()

    @Serializable
    data class ModeratorReportDetail(val reportId: String) : DashboardRoutes()

    @Serializable
    data object Home : DashboardRoutes()

    @Serializable
    data object EditResidence : DashboardRoutes()

    @Serializable
    data object Alerts : DashboardRoutes()

    @Serializable
    data object CreateReport : DashboardRoutes()

    @Serializable
    data object MyReports : DashboardRoutes()

    @Serializable
    data object Profile : DashboardRoutes()

    @Serializable
    data object EditProfile : DashboardRoutes()

    @Serializable
    data object Notifications : DashboardRoutes()

    @Serializable
    data class ReportDetail(val reportId: String) : DashboardRoutes()

    @Serializable
    data class EditReport(val reportId: String) : DashboardRoutes()
}
