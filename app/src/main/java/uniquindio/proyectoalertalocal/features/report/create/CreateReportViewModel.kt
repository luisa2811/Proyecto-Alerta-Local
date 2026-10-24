package uniquindio.proyectoalertalocal.features.report.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uniquindio.proyectoalertalocal.core.util.RequestResult
import uniquindio.proyectoalertalocal.domain.model.ReportCategory
import uniquindio.proyectoalertalocal.domain.model.ReportStatus
import uniquindio.proyectoalertalocal.domain.usecase.report.CreateReportUseCase
import uniquindio.proyectoalertalocal.domain.usecase.report.FindSimilarReportsUseCase
import uniquindio.proyectoalertalocal.domain.usecase.report.ObserveReportDetailUseCase
import uniquindio.proyectoalertalocal.domain.usecase.report.SupportReportUseCase
import uniquindio.proyectoalertalocal.domain.usecase.report.UpdateReportUseCase
import uniquindio.proyectoalertalocal.domain.usecase.user.GetCurrentUserUseCase

@HiltViewModel
class CreateReportViewModel @Inject constructor(
    private val createReportUseCase: CreateReportUseCase,
    private val findSimilarReportsUseCase: FindSimilarReportsUseCase,
    private val supportReportUseCase: SupportReportUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val updateReportUseCase: UpdateReportUseCase,
    private val observeReportDetailUseCase: ObserveReportDetailUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateReportUiState())
    val uiState: StateFlow<CreateReportUiState> = _uiState.asStateFlow()

    private var loadedEditReportId: String? = null

    init {
        getCurrentUserUseCase()
            .onEach { user ->
                _uiState.update { it.copy(currentUserId = user?.id) }
            }
            .launchIn(viewModelScope)
    }

    fun loadForEdit(reportId: String) {
        if (loadedEditReportId == reportId) return
        loadedEditReportId = reportId
        _uiState.update { it.copy(editingReportId = reportId) }
        viewModelScope.launch {
            val summary = observeReportDetailUseCase(reportId).first()
            if (summary == null) {
                _uiState.update {
                    it.copy(result = RequestResult.Failure("El reporte no existe."))
                }
                return@launch
            }
            val report = summary.report
            _uiState.update {
                it.copy(
                    category = report.category,
                    title = report.title,
                    description = report.description,
                    location = report.location,
                    originalStatus = report.status
                )
            }
            validate()
        }
    }

    fun selectCategory(category: ReportCategory) {
        _uiState.update { it.copy(category = category) }
        validate()
    }

    fun onTitleChange(title: String) {
        _uiState.update { it.copy(title = title) }
        validate()
    }

    fun onDescriptionChange(description: String) {
        _uiState.update { it.copy(description = description) }
        validate()
    }

    fun onLocationChange(location: String) {
        _uiState.update { it.copy(location = location) }
        validate()
    }

    private fun validate() {
        val state = _uiState.value
        val titleTrim = state.title.trim()
        val descriptionTrim = state.description.trim()
        val locationTrim = state.location.trim()
        val validTitle = titleTrim.length in 5..80
        val validDescription = descriptionTrim.length in 10..500
        val validLocation = locationTrim.length in 5..120
        val valid = state.category != null && validTitle && validDescription && validLocation && !state.isPublishing
        _uiState.update { it.copy(isFormValid = valid) }
    }

    fun publish() {
        val state = _uiState.value
        if (!state.isFormValid || state.category == null) return
        _uiState.update { it.copy(result = RequestResult.Loading, isPublishing = true) }
        viewModelScope.launch {
            findSimilarReportsUseCase(state.title, state.category)
                .onSuccess { similar ->
                    if (similar.isEmpty()) {
                        createReport()
                    } else {
                        _uiState.update { it.copy(similarReports = similar, result = null, isPublishing = false) }
                    }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(result = RequestResult.Failure(error.message ?: "Ocurrió un error inesperado."), isPublishing = false) }
                }
        }
    }

    fun saveChanges() {
        val state = _uiState.value
        val reportId = state.editingReportId ?: return
        val category = state.category ?: return
        if (!state.isFormValid) return
        _uiState.update { it.copy(result = RequestResult.Loading, isPublishing = true) }
        viewModelScope.launch {
            updateReportUseCase(reportId, state.title, state.description, category, state.location)
                .onSuccess {
                    val message = if (state.originalStatus == ReportStatus.VERIFIED) {
                        "Cambios guardados. El reporte volverá a revisión."
                    } else {
                        "Cambios guardados correctamente."
                    }
                    _uiState.update { it.copy(result = RequestResult.Success(message), isPublishing = false) }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(result = RequestResult.Failure(error.message ?: "Ocurrió un error inesperado."), isPublishing = false) }
                }
        }
    }

    fun publishAnyway() {
        val state = _uiState.value
        val similar = state.similarReports
        val chosen = similar.firstOrNull { it.authorId != state.currentUserId } ?: similar.firstOrNull()
        createReport(possibleDuplicateOf = chosen?.id)
    }

    fun cancelSimilar() {
        _uiState.update { it.copy(similarReports = emptyList()) }
        validate()
    }

    fun supportSimilar(reportId: String) {
        viewModelScope.launch {
            supportReportUseCase(reportId)
                .onSuccess {
                    _uiState.update {
                        it.copy(
                            result = RequestResult.Success("Te uniste a este reporte. No se creó uno nuevo."),
                            similarReports = emptyList(),
                            category = null,
                            title = "",
                            description = "",
                            location = "",
                            isFormValid = false,
                            isPublishing = false,
                            navigation = CreateReportNavigation.ToReportDetail(reportId)
                        )
                    }
                }
                .onFailure { error ->
                    val message = error.message ?: "Ocurrió un error inesperado."
                    if (message == SupportReportUseCase.ERROR_ALREADY_SUPPORTED) {
                        _uiState.update {
                            it.copy(
                                result = RequestResult.Failure(message),
                                similarReports = emptyList(),
                                category = null,
                                title = "",
                                description = "",
                                location = "",
                                isFormValid = false,
                                isPublishing = false,
                                navigation = CreateReportNavigation.ToReportDetail(reportId)
                            )
                        }
                    } else if (message == SupportReportUseCase.ERROR_NOT_ACTIVE) {
                        _uiState.update {
                            it.copy(result = RequestResult.Failure(message), similarReports = emptyList(), isPublishing = false)
                        }
                    } else {
                        _uiState.update { it.copy(result = RequestResult.Failure(message), isPublishing = false) }
                    }
                }
        }
    }

    fun openOwnReport(reportId: String) {
        _uiState.update { it.copy(similarReports = emptyList(), navigation = CreateReportNavigation.ToReportDetail(reportId)) }
    }

    private fun createReport(possibleDuplicateOf: String? = null) {
        val state = _uiState.value
        val category = state.category ?: return
        viewModelScope.launch {
            createReportUseCase(state.title, state.description, category, state.location, possibleDuplicateOf)
                .onSuccess {
                    _uiState.update {
                        it.copy(
                            result = RequestResult.Success("Reporte publicado. Un moderador lo revisará pronto."),
                            category = null,
                            title = "",
                            description = "",
                            location = "",
                            similarReports = emptyList(),
                            isFormValid = false,
                            isPublishing = false,
                            navigation = CreateReportNavigation.ToMyReports
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(result = RequestResult.Failure(error.message ?: "Ocurrió un error inesperado."), isPublishing = false) }
                }
        }
    }

    fun onResultShown() {
        _uiState.update { it.copy(result = null) }
    }

    fun onNavigationHandled() {
        _uiState.update { it.copy(navigation = null) }
    }

    fun titleError(): String? {
        val state = _uiState.value
        if (state.title.isEmpty()) return null
        return if (state.title.trim().length in 5..80) null else "El título debe tener entre 5 y 80 caracteres."
    }

    fun descriptionError(): String? {
        val state = _uiState.value
        if (state.description.isEmpty()) return null
        return if (state.description.trim().length in 10..500) null else "La descripción debe tener entre 10 y 500 caracteres."
    }

    fun locationError(): String? {
        val state = _uiState.value
        if (state.location.isEmpty()) return null
        return if (state.location.trim().length in 5..120) null else "La dirección debe tener entre 5 y 120 caracteres."
    }
}
