package uniquindio.proyectoalertalocal.features.dashboard.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import uniquindio.proyectoalertalocal.domain.model.UserRole
import uniquindio.proyectoalertalocal.features.home.HomeScreen
import uniquindio.proyectoalertalocal.features.moderator.ModeratorDashboardScreen
import uniquindio.proyectoalertalocal.features.moderator.detail.ModeratorReportDetailScreen
import uniquindio.proyectoalertalocal.features.notifications.NotificationsScreen
import uniquindio.proyectoalertalocal.features.report.create.CreateReportScreen
import uniquindio.proyectoalertalocal.features.report.detail.ReportDetailScreen
import uniquindio.proyectoalertalocal.features.report.list.MyReportsScreen
import uniquindio.proyectoalertalocal.features.report.list.ReportListScreen
import uniquindio.proyectoalertalocal.features.user.profile.EditProfileScreen
import uniquindio.proyectoalertalocal.features.user.profile.EditResidenceScreen
import uniquindio.proyectoalertalocal.features.user.profile.ProfileDashboardScreen

@Composable
fun DashboardNavigation(
    navController: NavHostController,
    innerPadding: PaddingValues,
    role: UserRole,
    onLogout: () -> Unit,
    onShowMessage: (String) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = when (role) {
            UserRole.RESIDENT -> DashboardRoutes.Home
            UserRole.MODERATOR -> DashboardRoutes.ModeratorHome
        },
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        composable<DashboardRoutes.ModeratorHome> {
            ModeratorDashboardScreen(
                onBack = onLogout,
                onShowMessage = onShowMessage,
                onOpenDetail = { reportId ->
                    navController.navigate(DashboardRoutes.ModeratorReportDetail(reportId))
                },
                onOpenProfile = {
                    navController.navigate(DashboardRoutes.Profile)
                }
            )
        }

        composable<DashboardRoutes.ModeratorReportDetail> { backStackEntry ->
            val reportId =
                backStackEntry.toRoute<DashboardRoutes.ModeratorReportDetail>().reportId

            ModeratorReportDetailScreen(
                reportId = reportId,
                onBack = { navController.popBackStack() }
            )
        }

        composable<DashboardRoutes.Home> {
            HomeScreen(
                onCreateReport = {
                    navController.navigate(DashboardRoutes.CreateReport)
                },
                onEditAddress = {
                    navController.navigate(DashboardRoutes.EditResidence)
                },
                onOpenNotifications = {
                    navController.navigate(DashboardRoutes.Notifications)
                },
                onShowMessage = onShowMessage
            )
        }

        composable<DashboardRoutes.EditResidence> {
            EditResidenceScreen(
                onBack = { navController.popBackStack() },
                onConfirm = { complement ->
                    onShowMessage("Dirección confirmada: $complement")
                    navController.popBackStack()
                }
            )
        }

        composable<DashboardRoutes.Alerts> {
            ReportListScreen(
                onOpenPublication = { publicationId ->
                    navController.navigate(DashboardRoutes.PublicationDetail(publicationId))
                },
                onOpenNotifications = {
                    navController.navigate(DashboardRoutes.Notifications)
                },
                onShowMessage = onShowMessage
            )
        }

        composable<DashboardRoutes.CreateReport> {
            CreateReportScreen(
                onBack = { navController.popBackStack() },
                onSubmit = {
                    onShowMessage("Formulario de demostración: el reporte no se publicó.")
                    navController.navigate(DashboardRoutes.Alerts) {
                        popUpTo<DashboardRoutes.Home>()
                        launchSingleTop = true
                    }
                },
                onShowMessage = onShowMessage
            )
        }

        composable<DashboardRoutes.MyReports> {
            MyReportsScreen(onBack = { navController.popBackStack() })
        }

        composable<DashboardRoutes.Profile> {
            ProfileDashboardScreen(
                onBack = { navController.popBackStack() },
                onEditProfile = {
                    navController.navigate(DashboardRoutes.EditProfile)
                }
            )
        }

        composable<DashboardRoutes.EditProfile> {
            EditProfileScreen(
                onBack = { navController.popBackStack() },
                onShowMessage = onShowMessage
            )
        }

        composable<DashboardRoutes.Notifications> {
            NotificationsScreen(onBack = { navController.popBackStack() })
        }

        composable<DashboardRoutes.PublicationDetail> { backStackEntry ->
            val publicationId =
                backStackEntry.toRoute<DashboardRoutes.PublicationDetail>().publicationId

            ReportDetailScreen(
                publicationId = publicationId,
                onBack = { navController.popBackStack() },
                onShowMessage = onShowMessage
            )
        }
    }
}
