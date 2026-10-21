package uniquindio.proyectoalertalocal.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import uniquindio.proyectoalertalocal.domain.model.UserRole
import uniquindio.proyectoalertalocal.features.dashboard.MainScreen
import uniquindio.proyectoalertalocal.features.home.PreLoginHomeScreen
import uniquindio.proyectoalertalocal.features.login.LoginScreen
import uniquindio.proyectoalertalocal.features.login.PasswordRecoveryScreen
import uniquindio.proyectoalertalocal.features.onboarding.OnboardingScreen
import uniquindio.proyectoalertalocal.features.register.RegisterScreen

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    sessionViewModel: SessionViewModel = hiltViewModel()
) {
    val sessionState by sessionViewModel.sessionState.collectAsStateWithLifecycle()
    var returningFromLogout by remember { mutableStateOf(false) }

    Surface(modifier = Modifier.fillMaxSize()) {
        when (val state = sessionState) {
            SessionState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            SessionState.NotAuthenticated -> {
                AuthNavigation(
                    navController = navController,
                    startAtLogin = returningFromLogout,
                    onLoginSuccess = { role ->
                        returningFromLogout = false
                        sessionViewModel.startDemoSession(role)
                    }
                )
            }

            is SessionState.Authenticated -> {
                MainScreen(
                    role = state.session.role,
                    onLogout = {
                        returningFromLogout = true
                        sessionViewModel.logout()
                    }
                )
            }
        }
    }
}

@Composable
private fun AuthNavigation(
    navController: NavHostController,
    startAtLogin: Boolean,
    onLoginSuccess: (UserRole) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = if (startAtLogin) AuthRoutes.Login else AuthRoutes.Onboarding
    ) {
        composable<AuthRoutes.Onboarding> {
            OnboardingScreen(
                onContinue = {
                    navController.navigate(AuthRoutes.Home) {
                        popUpTo<AuthRoutes.Onboarding> { inclusive = true }
                    }
                }
            )
        }

        composable<AuthRoutes.Home> {
            PreLoginHomeScreen(
                onLogin = { navController.navigate(AuthRoutes.Login) },
                onRegister = { navController.navigate(AuthRoutes.Register) }
            )
        }

        composable<AuthRoutes.Login> {
            LoginScreen(
                onLoginSuccess = { roleName ->
                    onLoginSuccess(
                        if (roleName == "Moderador") UserRole.MODERATOR else UserRole.RESIDENT
                    )
                },
                onForgotPasswordClick = { email ->
                    navController.navigate(AuthRoutes.PasswordRecovery(email = email))
                },
                onRegisterClick = {
                    navController.navigate(AuthRoutes.Register)
                }
            )
        }

        composable<AuthRoutes.Register> {
            RegisterScreen(onLoginClick = { navController.popBackStack() })
        }

        composable<AuthRoutes.PasswordRecovery> { backStackEntry ->
            val route = backStackEntry.toRoute<AuthRoutes.PasswordRecovery>()
            PasswordRecoveryScreen(
                initialEmail = route.email,
                onBackToLogin = { navController.popBackStack() }
            )
        }
    }
}
