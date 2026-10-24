package uniquindio.proyectoalertalocal.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import uniquindio.proyectoalertalocal.features.dashboard.MainScreen
import uniquindio.proyectoalertalocal.features.home.PreLoginHomeScreen
import uniquindio.proyectoalertalocal.features.login.LoginScreen
import uniquindio.proyectoalertalocal.features.login.PasswordRecoveryScreen
import uniquindio.proyectoalertalocal.features.onboarding.OnboardingScreen
import uniquindio.proyectoalertalocal.features.register.RegisterScreen

@Composable
fun AppNavigation(
    sessionViewModel: SessionViewModel = hiltViewModel()
) {
    val sessionState by sessionViewModel.sessionState.collectAsStateWithLifecycle()
    var hadSession by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(sessionState) {
        if (sessionState is SessionState.Authenticated) {
            hadSession = true
        }
    }

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
                    startAtLogin = hadSession
                )
            }

            is SessionState.Authenticated -> {
                MainScreen(
                    role = state.session.role,
                    onLogout = {
                        sessionViewModel.logout()
                    }
                )
            }
        }
    }
}

@Composable
private fun AuthNavigation(
    startAtLogin: Boolean
) {
    val navController = rememberNavController()
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
                onForgotPasswordClick = { email ->
                    navController.navigate(AuthRoutes.PasswordRecovery(email = email))
                },
                onRegisterClick = {
                    navController.navigate(AuthRoutes.Register)
                }
            )
        }

        composable<AuthRoutes.Register> {
            RegisterScreen(
                onLoginClick = { navController.popBackStack() },
                onRegisterSuccess = {
                    navController.navigate(AuthRoutes.Login) {
                        popUpTo<AuthRoutes.Register> { inclusive = true }
                    }
                }
            )
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
