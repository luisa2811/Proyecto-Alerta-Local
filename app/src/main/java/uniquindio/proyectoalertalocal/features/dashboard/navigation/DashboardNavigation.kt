package uniquindio.proyectoalertalocal.features.dashboard.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import uniquindio.proyectoalertalocal.features.home.HomeScreen
import uniquindio.proyectoalertalocal.features.moderator.ModeratorDashboardScreen
import uniquindio.proyectoalertalocal.features.moderator.ModeratorReportDetailScreen
import uniquindio.proyectoalertalocal.features.notifications.NotificationsScreen
import uniquindio.proyectoalertalocal.features.profile.EditProfileScreen
import uniquindio.proyectoalertalocal.features.profile.ProfileDashboardScreen
import uniquindio.proyectoalertalocal.features.report.create.CreateReportScreen
import uniquindio.proyectoalertalocal.features.report.detail.ReportDetailScreen
import uniquindio.proyectoalertalocal.features.report.list.FeedScreen
import uniquindio.proyectoalertalocal.features.report.list.MisReportesScreen

@Composable
fun DashboardNavigation(
    controlador: NavHostController,
    espacioInterior: PaddingValues,
    esModerador: Boolean,
    alCerrarSesion: () -> Unit,
    alMostrarMensaje: (String) -> Unit
) {
    val modifier = Modifier
        .fillMaxSize()
        .padding(espacioInterior)

    if (esModerador) {
        NavHost(
            navController = controlador,
            startDestination = ModeratorDashboardRoute,
            modifier = modifier
        ) {
            dashboardDestinations(
                controlador = controlador,
                alCerrarSesion = alCerrarSesion,
                alMostrarMensaje = alMostrarMensaje
            )
        }
    } else {
        NavHost(
            navController = controlador,
            startDestination = DashboardHomeRoute,
            modifier = modifier
        ) {
            dashboardDestinations(
                controlador = controlador,
                alCerrarSesion = alCerrarSesion,
                alMostrarMensaje = alMostrarMensaje
            )
        }
    }
}

private fun NavGraphBuilder.dashboardDestinations(
    controlador: NavHostController,
    alCerrarSesion: () -> Unit,
    alMostrarMensaje: (String) -> Unit
) {
    composable<ModeratorDashboardRoute> {
        ModeratorDashboardScreen(
            onBack = alCerrarSesion,
            onShowMessage = alMostrarMensaje,
            onOpenDetail = { reportId ->
                controlador.navigate(ModeratorReportDetailRoute(reportId))
            },
            onOpenProfile = {
                controlador.navigate(ProfileRoute)
            }
        )
    }

    composable<ModeratorReportDetailRoute> { entrada ->
        val reportId = entrada.toRoute<ModeratorReportDetailRoute>().reportId

        ModeratorReportDetailScreen(
            reportId = reportId,
            onBack = { controlador.popBackStack() }
        )
    }

    composable<DashboardHomeRoute> {
        HomeScreen(
            onCreateReport = {
                controlador.navigate(CreateReportRoute)
            },
            onOpenNotifications = {
                controlador.navigate(NotificationsRoute)
            },
            onShowMessage = alMostrarMensaje
        )
    }

    composable<AlertsRoute> {
        FeedScreen(
            onOpenPublication = { id ->
                controlador.navigate(PublicationDetailRoute(id))
            },
            onOpenNotifications = {
                controlador.navigate(NotificationsRoute)
            },
            onShowMessage = alMostrarMensaje
        )
    }

    composable<CreateReportRoute> {
        CreateReportScreen(
            onBack = { controlador.popBackStack() },
            onSubmit = {
                alMostrarMensaje(
                    "Formulario de demostración: el reporte no se publicó."
                )
                controlador.navigate(AlertsRoute) {
                    popUpTo<DashboardHomeRoute>()
                    launchSingleTop = true
                }
            },
            onShowMessage = alMostrarMensaje
        )
    }

    composable<MyReportsRoute> {
        MisReportesScreen(
            onBack = { controlador.popBackStack() }
        )
    }

    composable<ProfileRoute> {
        ProfileDashboardScreen(
            onBack = { controlador.popBackStack() },
            onEditProfile = {
                controlador.navigate(EditProfileRoute)
            }
        )
    }

    composable<EditProfileRoute> {
        EditProfileScreen(
            onBack = { controlador.popBackStack() }
        )
    }

    composable<NotificationsRoute> {
        NotificationsScreen(
            onBack = { controlador.popBackStack() }
        )
    }

    composable<PublicationDetailRoute> { entrada ->
        val publicationId =
            entrada.toRoute<PublicationDetailRoute>().publicationId

        ReportDetailScreen(
            publicationId = publicationId,
            onBack = { controlador.popBackStack() },
            onShowMessage = alMostrarMensaje
        )
    }
}