package uniquindio.proyectoalertalocal.features.user.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uniquindio.proyectoalertalocal.core.util.RequestResult
import uniquindio.proyectoalertalocal.domain.model.Reputation
import uniquindio.proyectoalertalocal.domain.model.User
import uniquindio.proyectoalertalocal.domain.model.UserRole
import uniquindio.proyectoalertalocal.domain.model.UserStats
import uniquindio.proyectoalertalocal.domain.usecase.auth.LogoutUseCase
import uniquindio.proyectoalertalocal.domain.usecase.user.DeleteAccountUseCase
import uniquindio.proyectoalertalocal.domain.usecase.user.GetCurrentUserUseCase
import uniquindio.proyectoalertalocal.domain.usecase.user.GetUserReputationUseCase
import uniquindio.proyectoalertalocal.domain.usecase.user.GetUserStatsUseCase

@HiltViewModel
class ProfileDashboardViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getUserStatsUseCase: GetUserStatsUseCase,
    private val getUserReputationUseCase: GetUserReputationUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val deleteAccountUseCase: DeleteAccountUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileDashboardUiState())
    val uiState: StateFlow<ProfileDashboardUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                getCurrentUserUseCase(),
                getUserStatsUseCase(),
                getUserReputationUseCase()
            ) { user, stats, reputation ->
                buildState(user, stats, reputation)
            }
                .catch {
                    _uiState.update {
                        it.copy(result = RequestResult.Failure(UNEXPECTED_ERROR))
                    }
                }
                .collect { newState ->
                    _uiState.update { current ->
                        newState.copy(result = current.result)
                    }
                }
        }
    }

    fun logout() {
        viewModelScope.launch {
            logoutUseCase()
        }
    }

    fun deleteAccount() {
        _uiState.update { it.copy(result = RequestResult.Loading) }
        viewModelScope.launch {
            deleteAccountUseCase().onFailure { error ->
                _uiState.update {
                    it.copy(
                        result = RequestResult.Failure(
                            error.message ?: UNEXPECTED_ERROR
                        )
                    )
                }
            }
        }
    }

    fun onResultShown() {
        _uiState.update { it.copy(result = null) }
    }

    private fun buildState(
        user: User?,
        stats: UserStats,
        reputation: Reputation?
    ): ProfileDashboardUiState {
        if (user == null) return ProfileDashboardUiState()

        val isResident = user.role == UserRole.RESIDENT
        val visibleReputation = reputation.takeIf { isResident }

        return ProfileDashboardUiState(
            fullName = user.name,
            memberSince = user.memberSince?.let(::memberSinceLabel),
            levelName = visibleReputation?.level?.displayName,
            points = visibleReputation?.points ?: 0,
            progress = visibleReputation?.let(::progress) ?: 0f,
            levelMessage = visibleReputation?.let(::levelMessage),
            activeReports = stats.activeReports,
            resolvedReports = stats.resolvedReports,
            pendingReports = stats.pendingReports,
            earnedBadges = visibleReputation?.badges.orEmpty(),
            showBadges = isResident
        )
    }

    private fun memberSinceLabel(memberSince: Instant): String =
        "Miembro desde " + MEMBER_SINCE_FORMATTER.format(
            memberSince.atZone(ZoneId.systemDefault())
        )

    private fun levelMessage(reputation: Reputation): String {
        val nextLevel = reputation.nextLevel ?: return MAX_LEVEL_MESSAGE
        val pointsToNextLevel = reputation.pointsToNextLevel ?: 0
        return "Faltan $pointsToNextLevel puntos para ${nextLevel.displayName}"
    }

    private fun progress(reputation: Reputation): Float {
        val nextLevel = reputation.nextLevel ?: return 1f
        val levelRange = (nextLevel.minPoints - reputation.level.minPoints)
            .coerceAtLeast(1)
        return ((reputation.points - reputation.level.minPoints).toFloat() / levelRange)
            .coerceIn(0f, 1f)
    }

    companion object {
        const val UNEXPECTED_ERROR = "Ocurrió un error inesperado."
        const val MAX_LEVEL_MESSAGE = "Nivel máximo alcanzado"
        private val MEMBER_SINCE_FORMATTER = DateTimeFormatter.ofPattern(
            "MMMM yyyy",
            Locale.forLanguageTag("es-ES")
        )
    }
}
