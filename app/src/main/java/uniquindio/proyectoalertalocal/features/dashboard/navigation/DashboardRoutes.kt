package uniquindio.proyectoalertalocal.features.dashboard.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface DashboardRoute

@Serializable
data object ModeratorDashboardRoute : DashboardRoute

@Serializable
data class ModeratorReportDetailRoute(val reportId: String) : DashboardRoute

@Serializable
data object DashboardHomeRoute : DashboardRoute

@Serializable
data object AlertsRoute : DashboardRoute

@Serializable
data object CreateReportRoute : DashboardRoute

@Serializable
data object MyReportsRoute : DashboardRoute

@Serializable
data object ProfileRoute : DashboardRoute

@Serializable
data object EditProfileRoute : DashboardRoute

@Serializable
data object NotificationsRoute : DashboardRoute

@Serializable
data class PublicationDetailRoute(val publicationId: String) : DashboardRoute
