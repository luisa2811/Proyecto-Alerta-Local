package uniquindio.proyectoalertalocal.features.dashboard.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import uniquindio.proyectoalertalocal.domain.model.GeoPoint
import uniquindio.proyectoalertalocal.domain.model.UserRole
import uniquindio.proyectoalertalocal.features.home.HomeScreen
import uniquindio.proyectoalertalocal.features.location.LocationPickerScreen
import uniquindio.proyectoalertalocal.features.moderator.pending.ModeratorPendingScreen
import uniquindio.proyectoalertalocal.features.moderator.resolutions.ModeratorResolutionsScreen
import uniquindio.proyectoalertalocal.features.moderator.detail.ModeratorReportDetailScreen
import uniquindio.proyectoalertalocal.features.moderator.profile.ModeratorProfileScreen
import uniquindio.proyectoalertalocal.features.notifications.NotificationsScreen
import uniquindio.proyectoalertalocal.features.report.create.CreateReportScreen
import uniquindio.proyectoalertalocal.features.report.detail.ReportDetailScreen
import uniquindio.proyectoalertalocal.features.report.list.MyReportsScreen
import uniquindio.proyectoalertalocal.features.report.list.ReportListScreen
import uniquindio.proyectoalertalocal.features.user.profile.EditProfileScreen
import uniquindio.proyectoalertalocal.features.user.profile.EditResidenceScreen
import uniquindio.proyectoalertalocal.features.user.profile.ProfileDashboardScreen

private const val PICKED_LOCATION_LATITUDE = "picked_location_latitude"
private const val PICKED_LOCATION_LONGITUDE = "picked_location_longitude"

@Composable
fun DashboardNavigation(
    navController: NavHostController,
    innerPadding: PaddingValues,
    role: UserRole,
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
            ModeratorPendingScreen(
                onShowMessage = onShowMessage,
                onOpenDetail = { reportId ->
                    navController.navigate(DashboardRoutes.ModeratorReportDetail(reportId))
                },
                onOpenProfile = {
                    navController.navigate(DashboardRoutes.ModeratorProfile)
                }
            )
        }

        composable<DashboardRoutes.ModeratorResolutions> {
            ModeratorResolutionsScreen(
                onShowMessage = onShowMessage,
                onOpenDetail = { reportId ->
                    navController.navigate(DashboardRoutes.ModeratorReportDetail(reportId))
                },
                onOpenProfile = {
                    navController.navigate(DashboardRoutes.ModeratorProfile)
                }
            )
        }

        composable<DashboardRoutes.ModeratorProfile> {
            ModeratorProfileScreen(
                onBack = { navController.popBackStack() },
                onEditProfile = {
                    navController.navigate(DashboardRoutes.EditProfile)
                }
            )
        }

        composable<DashboardRoutes.ModeratorReportDetail> { backStackEntry ->
            val reportId =
                backStackEntry.toRoute<DashboardRoutes.ModeratorReportDetail>().reportId

            ModeratorReportDetailScreen(
                reportId = reportId,
                onBack = { navController.popBackStack() },
                onShowMessage = onShowMessage
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
                onShowMessage = onShowMessage,
                onOpenReportDetail = { reportId ->
                    navController.navigate(DashboardRoutes.ReportDetail(reportId))
                }
            )
        }

        composable<DashboardRoutes.EditResidence> { backStackEntry ->
            val savedStateHandle = backStackEntry.savedStateHandle
            val latitude by savedStateHandle
                .getStateFlow<Double?>(PICKED_LOCATION_LATITUDE, null)
                .collectAsStateWithLifecycle()
            val longitude by savedStateHandle
                .getStateFlow<Double?>(PICKED_LOCATION_LONGITUDE, null)
                .collectAsStateWithLifecycle()
            val selectedLocation = latitude?.let { selectedLatitude ->
                longitude?.let { selectedLongitude ->
                    GeoPoint(selectedLatitude, selectedLongitude)
                }
            }
            EditResidenceScreen(
                onBack = { navController.popBackStack() },
                onShowMessage = onShowMessage,
                selectedLocation = selectedLocation,
                onLocationPickedHandled = {
                    savedStateHandle.remove<Double>(PICKED_LOCATION_LATITUDE)
                    savedStateHandle.remove<Double>(PICKED_LOCATION_LONGITUDE)
                },
                onPickLocation = { point ->
                    navController.navigate(
                        DashboardRoutes.LocationPicker(
                            initialLatitude = point?.latitude,
                            initialLongitude = point?.longitude
                        )
                    )
                }
            )
        }

        composable<DashboardRoutes.Alerts> {
            ReportListScreen(
                onOpenPublication = { reportId ->
                    navController.navigate(DashboardRoutes.ReportDetail(reportId))
                },
                onOpenNotifications = {
                    navController.navigate(DashboardRoutes.Notifications)
                },
                onShowMessage = onShowMessage,
                onCreateReport = {
                    navController.navigate(DashboardRoutes.CreateReport)
                }
            )
        }

        composable<DashboardRoutes.CreateReport> { backStackEntry ->
            val savedStateHandle = backStackEntry.savedStateHandle
            val latitude by savedStateHandle
                .getStateFlow<Double?>(PICKED_LOCATION_LATITUDE, null)
                .collectAsStateWithLifecycle()
            val longitude by savedStateHandle
                .getStateFlow<Double?>(PICKED_LOCATION_LONGITUDE, null)
                .collectAsStateWithLifecycle()
            CreateReportScreen(
                onBack = { navController.popBackStack() },
                onShowMessage = onShowMessage,
                selectedLocation = latitude?.let { selectedLatitude ->
                    longitude?.let { selectedLongitude ->
                        GeoPoint(selectedLatitude, selectedLongitude)
                    }
                },
                onLocationPickedHandled = {
                    savedStateHandle.remove<Double>(PICKED_LOCATION_LATITUDE)
                    savedStateHandle.remove<Double>(PICKED_LOCATION_LONGITUDE)
                },
                onPickLocation = { point ->
                    navController.navigate(
                        DashboardRoutes.LocationPicker(
                            initialLatitude = point?.latitude,
                            initialLongitude = point?.longitude
                        )
                    )
                },
                onPublished = {
                    navController.navigate(DashboardRoutes.MyReports) {
                        popUpTo<DashboardRoutes.Home>()
                        launchSingleTop = true
                    }
                },
                onOpenReport = { reportId ->
                    navController.navigate(DashboardRoutes.ReportDetail(reportId))
                }
            )
        }

        composable<DashboardRoutes.EditReport> { backStackEntry ->
            val reportId = backStackEntry.toRoute<DashboardRoutes.EditReport>().reportId
            val savedStateHandle = backStackEntry.savedStateHandle
            val latitude by savedStateHandle
                .getStateFlow<Double?>(PICKED_LOCATION_LATITUDE, null)
                .collectAsStateWithLifecycle()
            val longitude by savedStateHandle
                .getStateFlow<Double?>(PICKED_LOCATION_LONGITUDE, null)
                .collectAsStateWithLifecycle()
            CreateReportScreen(
                reportId = reportId,
                onBack = { navController.popBackStack() },
                onShowMessage = onShowMessage,
                selectedLocation = latitude?.let { selectedLatitude ->
                    longitude?.let { selectedLongitude ->
                        GeoPoint(selectedLatitude, selectedLongitude)
                    }
                },
                onLocationPickedHandled = {
                    savedStateHandle.remove<Double>(PICKED_LOCATION_LATITUDE)
                    savedStateHandle.remove<Double>(PICKED_LOCATION_LONGITUDE)
                },
                onPickLocation = { point ->
                    navController.navigate(
                        DashboardRoutes.LocationPicker(
                            initialLatitude = point?.latitude,
                            initialLongitude = point?.longitude
                        )
                    )
                },
                onPublished = {
                    navController.navigate(DashboardRoutes.MyReports) {
                        popUpTo<DashboardRoutes.Home>()
                        launchSingleTop = true
                    }
                },
                onOpenReport = { id ->
                    navController.navigate(DashboardRoutes.ReportDetail(id))
                }
            )
        }

        composable<DashboardRoutes.MyReports> {
            MyReportsScreen(
                onBack = { navController.popBackStack() },
                onOpenReport = { reportId ->
                    navController.navigate(DashboardRoutes.ReportDetail(reportId))
                },
                onCreateReport = {
                    navController.navigate(DashboardRoutes.CreateReport)
                }
            )
        }

        composable<DashboardRoutes.Profile> {
            ProfileDashboardScreen(
                onBack = { navController.popBackStack() },
                onEditProfile = {
                    navController.navigate(DashboardRoutes.EditProfile)
                },
                onShowMessage = onShowMessage
            )
        }

        composable<DashboardRoutes.EditProfile> {
            EditProfileScreen(
                onBack = { navController.popBackStack() },
                onShowMessage = onShowMessage,
                onEditResidence = {
                    navController.navigate(DashboardRoutes.EditResidence)
                }
            )
        }

        composable<DashboardRoutes.Notifications> {
            NotificationsScreen(
                onBack = { navController.popBackStack() },
                onShowMessage = onShowMessage,
                onOpenReport = { reportId ->
                    navController.navigate(DashboardRoutes.ReportDetail(reportId))
                },
                onOpenProfile = {
                    navController.navigate(DashboardRoutes.Profile)
                }
            )
        }

        composable<DashboardRoutes.ReportDetail> { backStackEntry ->
            val reportId =
                backStackEntry.toRoute<DashboardRoutes.ReportDetail>().reportId

            ReportDetailScreen(
                reportId = reportId,
                onBack = { navController.popBackStack() },
                onShowMessage = onShowMessage,
                onEdit = { id ->
                    navController.navigate(DashboardRoutes.EditReport(id))
                },
                onDeleted = {
                    navController.navigate(DashboardRoutes.MyReports) {
                        popUpTo<DashboardRoutes.Home>()
                        launchSingleTop = true
                    }
                }
            )
        }

        composable<DashboardRoutes.LocationPicker> { backStackEntry ->
            val route = backStackEntry.toRoute<DashboardRoutes.LocationPicker>()
            val initialPoint = if (
                route.initialLatitude != null && route.initialLongitude != null
            ) {
                GeoPoint(route.initialLatitude, route.initialLongitude)
            } else {
                null
            }
            LocationPickerScreen(
                initialPoint = initialPoint,
                onBack = { navController.popBackStack() },
                onConfirm = { point ->
                    val previousEntry = checkNotNull(navController.previousBackStackEntry) {
                        "No hay una pantalla anterior para recibir la ubicación."
                    }
                    previousEntry.savedStateHandle[PICKED_LOCATION_LATITUDE] = point.latitude
                    previousEntry.savedStateHandle[PICKED_LOCATION_LONGITUDE] = point.longitude
                    navController.popBackStack()
                }
            )
        }
    }
}
