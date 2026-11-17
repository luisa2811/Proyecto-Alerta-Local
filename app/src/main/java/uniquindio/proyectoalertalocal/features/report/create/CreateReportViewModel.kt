package uniquindio.proyectoalertalocal.features.report.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import uniquindio.proyectoalertalocal.core.util.RequestResult
import uniquindio.proyectoalertalocal.core.util.ResourceProvider
import uniquindio.proyectoalertalocal.core.util.toRequestFailure
import uniquindio.proyectoalertalocal.R
import uniquindio.proyectoalertalocal.domain.model.GeoPoint
import uniquindio.proyectoalertalocal.domain.model.ReportCategory
import uniquindio.proyectoalertalocal.domain.model.ReportDraft
import uniquindio.proyectoalertalocal.domain.model.ReportStatus
import uniquindio.proyectoalertalocal.domain.usecase.report.CreateReportUseCase
import uniquindio.proyectoalertalocal.domain.usecase.report.DraftReportFromVoiceUseCase
import uniquindio.proyectoalertalocal.domain.usecase.report.FindSimilarReportsUseCase
import uniquindio.proyectoalertalocal.domain.usecase.report.GeocodeAddressUseCase
import uniquindio.proyectoalertalocal.domain.usecase.report.ReverseGeocodeAddressUseCase
import uniquindio.proyectoalertalocal.domain.usecase.report.ObserveReportDetailUseCase
import uniquindio.proyectoalertalocal.domain.usecase.report.SupportReportUseCase
import uniquindio.proyectoalertalocal.domain.usecase.report.UploadReportImagesUseCase
import uniquindio.proyectoalertalocal.domain.usecase.report.UpdateReportUseCase
import uniquindio.proyectoalertalocal.domain.usecase.user.GetCurrentUserUseCase

@HiltViewModel
class CreateReportViewModel @Inject constructor(
    private val createReportUseCase: CreateReportUseCase,
    private val findSimilarReportsUseCase: FindSimilarReportsUseCase,
    private val supportReportUseCase: SupportReportUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val updateReportUseCase: UpdateReportUseCase,
    private val observeReportDetailUseCase: ObserveReportDetailUseCase,
    private val uploadReportImagesUseCase: UploadReportImagesUseCase,
    private val draftReportFromVoiceUseCase: DraftReportFromVoiceUseCase,
    private val geocodeAddressUseCase: GeocodeAddressUseCase,
    private val reverseGeocodeAddressUseCase: ReverseGeocodeAddressUseCase,
    private val resources: ResourceProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateReportUiState())
    val uiState: StateFlow<CreateReportUiState> = _uiState.asStateFlow()

    private var loadedEditReportId: String? = null
    private var geocodingJob: Job? = null

    init {
        getCurrentUserUseCase()
            .onEach { user ->
                _uiState.update { it.copy(currentUserId = user?.id) }
            }
            .catch { error ->
                _uiState.update { it.copy(result = error.toRequestFailure(resources)) }
            }
            .launchIn(viewModelScope)
    }

    fun loadForEdit(reportId: String) {
        if (loadedEditReportId == reportId) return
        loadedEditReportId = reportId
        _uiState.update { it.copy(editingReportId = reportId, result = RequestResult.Loading) }
        viewModelScope.launch {
            runCatching { observeReportDetailUseCase(reportId).first() }
                .fold(
                    onSuccess = { summary ->
                        if (summary == null) {
                            _uiState.update {
                                it.copy(result = RequestResult.Failure(resources.getString(R.string.reports_missing)))
                            }
                            return@fold
                        }
                        val report = summary.report
                        _uiState.update {
                            it.copy(
                                category = report.category,
                                title = report.title,
                                description = report.description,
                                location = report.location,
                                latitude = report.latitude,
                                longitude = report.longitude,
                                retainedImageUrls = report.imageUrls,
                                localImageUris = emptyList(),
                                originalStatus = report.status,
                                result = null
                            )
                        }
                        validate()
                    },
                    onFailure = { error ->
                        _uiState.update { it.copy(result = error.toRequestFailure(resources)) }
                    }
                )
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
        geocodingJob?.cancel()
        val currentState = _uiState.value
        val proximity = if (currentState.latitude != null && currentState.longitude != null) {
            GeoPoint(currentState.latitude, currentState.longitude)
        } else {
            DEFAULT_GEOCODING_PROXIMITY
        }
        _uiState.update {
            it.copy(location = location, latitude = null, longitude = null, result = null)
        }
        validate()
        val query = location.trim()
        if (query.length < MIN_GEOCODING_QUERY_LENGTH) return

        geocodingJob = viewModelScope.launch {
            delay(GEOCODING_DEBOUNCE_MS)
            geocodeAddressUseCase(query, proximity).fold(
                onSuccess = { point ->
                    _uiState.update { state ->
                        if (state.location.trim() == query) {
                            state.copy(latitude = point?.latitude, longitude = point?.longitude)
                        } else {
                            state
                        }
                    }
                    validate()
                },
                onFailure = { error ->
                    if (_uiState.value.location.trim() == query) {
                        _uiState.update { it.copy(result = error.toRequestFailure(resources)) }
                    }
                }
            )
        }
    }

    fun onLocationPicked(latitude: Double, longitude: Double) {
        geocodingJob?.cancel()
        val point = GeoPoint(latitude, longitude)
        _uiState.update {
            it.copy(latitude = latitude, longitude = longitude, result = null)
        }
        validate()
        geocodingJob = viewModelScope.launch {
            reverseGeocodeAddressUseCase(point).fold(
                onSuccess = { address ->
                    _uiState.update { state ->
                        if (state.latitude == latitude && state.longitude == longitude) {
                            state.copy(location = address?.takeIf(String::isNotBlank) ?: state.location)
                        } else {
                            state
                        }
                    }
                    validate()
                },
                onFailure = { error ->
                    _uiState.update { it.copy(result = error.toRequestFailure(resources)) }
                }
            )
        }
    }

    fun generateDraftFromVoice(transcript: String, languageTag: String) {
        if (transcript.isBlank()) return
        _uiState.update {
            it.copy(
                isGeneratingDraft = true,
                draftResult = RequestResult.Loading,
                draftNotice = null
            )
        }
        viewModelScope.launch {
            runCatching {
                draftReportFromVoiceUseCase(transcript, languageTag).getOrThrow()
            }.fold(
                onSuccess = { draft ->
                    applyDraft(draft)
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isGeneratingDraft = false,
                            draftResult = error.toRequestFailure(resources)
                        )
                    }
                }
            )
        }
    }

    fun onDraftResultShown() {
        _uiState.update { it.copy(draftResult = null) }
    }

    private fun applyDraft(draft: ReportDraft) {
        _uiState.update { state ->
            state.copy(
                title = draft.title,
                description = draft.description,
                category = draft.category ?: state.category,
                location = draft.addressHint?.takeIf(String::isNotBlank) ?: state.location,
                isGeneratingDraft = false,
                draftResult = null,
                draftNotice = resources.getString(R.string.reports_draft_review)
            )
        }
        validate()
        val addressHint = draft.addressHint
        val state = _uiState.value
        if (!addressHint.isNullOrBlank() && state.latitude == null && state.longitude == null) {
            viewModelScope.launch {
                val user = getCurrentUserUseCase()
                    .catch { error ->
                        _uiState.update { it.copy(result = error.toRequestFailure(resources)) }
                        emit(null)
                    }
                    .first()
                val userLatitude = user?.residenceLatitude
                val userLongitude = user?.residenceLongitude
                val proximity = if (userLatitude != null && userLongitude != null) {
                    GeoPoint(latitude = userLatitude, longitude = userLongitude)
                } else {
                    DEFAULT_GEOCODING_PROXIMITY
                }
                if (_uiState.value.latitude != null || _uiState.value.longitude != null) {
                    return@launch
                }
                val point = geocodeAddressUseCase(addressHint, proximity).getOrNull()
                    ?: return@launch
                _uiState.update { currentState ->
                    if (currentState.latitude != null || currentState.longitude != null) {
                        currentState
                    } else {
                        currentState.copy(
                            latitude = point.latitude,
                            longitude = point.longitude,
                            draftNotice = resources.getString(R.string.reports_draft_location)
                        )
                    }
                }
                validate()
            }
        }
    }

    fun addImage(localUri: String) {
        _uiState.update { state ->
            if (state.retainedImageUrls.size + state.localImageUris.size >= MAX_IMAGES) {
                state
            } else {
                state.copy(localImageUris = state.localImageUris + localUri)
            }
        }
        validate()
    }

    fun removeImage(imageUri: String) {
        _uiState.update { state ->
            if (imageUri in state.localImageUris) {
                state.copy(localImageUris = state.localImageUris - imageUri)
            } else {
                state.copy(retainedImageUrls = state.retainedImageUrls - imageUri)
            }
        }
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
        val hasRequiredPhoto = state.retainedImageUrls.isNotEmpty() || state.localImageUris.isNotEmpty()
        val withinPhotoLimit = state.retainedImageUrls.size + state.localImageUris.size <= MAX_IMAGES
        val valid = state.category != null && validTitle && validDescription && validLocation &&
            state.latitude != null && state.longitude != null && !state.isPublishing
            && hasRequiredPhoto && withinPhotoLimit
        _uiState.update { it.copy(isFormValid = valid) }
    }

    fun publish() {
        val state = _uiState.value
        if (!state.isFormValid || state.category == null) return
        val newLocation = GeoPoint(
            latitude = state.latitude ?: return,
            longitude = state.longitude ?: return
        )
        _uiState.update { it.copy(result = RequestResult.Loading, isPublishing = true) }
        viewModelScope.launch {
            runCatching {
                findSimilarReportsUseCase(state.title, state.category, newLocation).getOrThrow()
            }
                .fold(
                    onSuccess = { similar ->
                        if (similar.isEmpty()) {
                            createReport()
                        } else {
                            _uiState.update {
                                it.copy(similarReports = similar, result = null, isPublishing = false)
                            }
                        }
                    },
                    onFailure = { error ->
                        _uiState.update {
                            it.copy(result = error.toRequestFailure(resources), isPublishing = false)
                        }
                    }
                )
        }
    }

    fun saveChanges() {
        val state = _uiState.value
        val reportId = state.editingReportId ?: return
        val category = state.category ?: return
        if (!state.isFormValid) return
        val latitude = state.latitude ?: return
        val longitude = state.longitude ?: return
        _uiState.update {
            it.copy(
                result = RequestResult.Loading,
                isPublishing = true,
                loadingMessage = resources.getString(R.string.reports_loading_save)
            )
        }
        viewModelScope.launch {
            runCatching {
                val uploadedImageUrls = if (state.localImageUris.isNotEmpty()) {
                    _uiState.update { it.copy(loadingMessage = resources.getString(R.string.reports_loading_upload)) }
                    uploadReportImagesUseCase(state.localImageUris).getOrThrow()
                } else {
                    emptyList()
                }
                updateReportUseCase(
                    reportId = reportId,
                    title = state.title,
                    description = state.description,
                    category = category,
                    location = state.location,
                    latitude = latitude,
                    longitude = longitude,
                    imageUrls = state.retainedImageUrls + uploadedImageUrls
                ).getOrThrow()
            }.fold(
                onSuccess = {
                    val message = if (state.originalStatus == ReportStatus.VERIFIED) {
                        resources.getString(R.string.reports_update_pending)
                    } else {
                        resources.getString(R.string.reports_update_success)
                    }
                    _uiState.update {
                        it.copy(
                            result = RequestResult.Success(message),
                            isPublishing = false,
                            loadingMessage = ""
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(result = error.toRequestFailure(resources), isPublishing = false, loadingMessage = "")
                    }
                }
            )
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
        _uiState.update { it.copy(result = RequestResult.Loading, isPublishing = true) }
        viewModelScope.launch {
            runCatching { supportReportUseCase(reportId).getOrThrow() }
                .fold(
                    onSuccess = {
                    _uiState.update {
                        it.copy(
                            result = RequestResult.Success(resources.getString(R.string.reports_support_success)),
                            similarReports = emptyList(),
                            category = null,
                            title = "",
                            description = "",
                            location = "",
                            latitude = null,
                            longitude = null,
                            retainedImageUrls = emptyList(),
                            localImageUris = emptyList(),
                            isFormValid = false,
                            isPublishing = false,
                            loadingMessage = "",
                            navigation = CreateReportNavigation.ToReportDetail(reportId)
                        )
                    }
                    },
                    onFailure = { error ->
                        val message = error.toRequestFailure(resources).errorMessage
                        if (message == SupportReportUseCase.ERROR_ALREADY_SUPPORTED) {
                            _uiState.update {
                                it.copy(
                                    result = RequestResult.Failure(message),
                                    similarReports = emptyList(),
                                    category = null,
                                    title = "",
                                    description = "",
                                    location = "",
                                    retainedImageUrls = emptyList(),
                                    localImageUris = emptyList(),
                                    isFormValid = false,
                                    isPublishing = false,
                                    loadingMessage = "",
                                    navigation = CreateReportNavigation.ToReportDetail(reportId)
                                )
                            }
                        } else if (message == SupportReportUseCase.ERROR_NOT_ACTIVE) {
                            _uiState.update {
                                it.copy(result = RequestResult.Failure(message), similarReports = emptyList(), isPublishing = false)
                            }
                        } else {
                            _uiState.update {
                                it.copy(result = RequestResult.Failure(message), isPublishing = false)
                            }
                        }
                    }
                )
        }
    }

    fun openOwnReport(reportId: String) {
        _uiState.update { it.copy(similarReports = emptyList(), navigation = CreateReportNavigation.ToReportDetail(reportId)) }
    }

    private fun createReport(possibleDuplicateOf: String? = null) {
        val state = _uiState.value
        val category = state.category ?: return
        val latitude = state.latitude ?: return
        val longitude = state.longitude ?: return
        _uiState.update {
            it.copy(
                result = RequestResult.Loading,
                isPublishing = true,
                loadingMessage = resources.getString(R.string.reports_loading_publish)
            )
        }
        viewModelScope.launch {
            runCatching {
                val uploadedImageUrls = if (state.localImageUris.isNotEmpty()) {
                    _uiState.update { it.copy(loadingMessage = resources.getString(R.string.reports_loading_upload)) }
                    uploadReportImagesUseCase(state.localImageUris).getOrThrow()
                } else {
                    emptyList()
                }
                _uiState.update { it.copy(loadingMessage = resources.getString(R.string.reports_loading_publish)) }
                createReportUseCase(
                    title = state.title,
                    description = state.description,
                    category = category,
                    location = state.location,
                    latitude = latitude,
                    longitude = longitude,
                    possibleDuplicateOf = possibleDuplicateOf,
                    imageUrls = state.retainedImageUrls + uploadedImageUrls
                ).getOrThrow()
            }.fold(
                onSuccess = {
                    _uiState.update {
                        it.copy(
                            result = RequestResult.Success(resources.getString(R.string.reports_publish_success)),
                            category = null,
                            title = "",
                            description = "",
                            location = "",
                            latitude = null,
                            longitude = null,
                            retainedImageUrls = emptyList(),
                            localImageUris = emptyList(),
                            similarReports = emptyList(),
                            isFormValid = false,
                            isPublishing = false,
                            loadingMessage = "",
                            navigation = CreateReportNavigation.ToMyReports
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(result = error.toRequestFailure(resources), isPublishing = false, loadingMessage = "")
                    }
                }
            )
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
        return if (state.title.trim().length in 5..80) null else resources.getString(R.string.reports_title_validation)
    }

    fun descriptionError(): String? {
        val state = _uiState.value
        if (state.description.isEmpty()) return null
        return if (state.description.trim().length in 10..500) null else resources.getString(R.string.reports_description_validation)
    }

    fun locationError(): String? {
        val state = _uiState.value
        if (state.location.isEmpty()) return null
        return if (state.location.trim().length in 5..120) null else resources.getString(R.string.reports_address_validation)
    }

    companion object {
        const val MAX_IMAGES = 3
        private const val MIN_GEOCODING_QUERY_LENGTH = 5
        private const val GEOCODING_DEBOUNCE_MS = 600L
        private val DEFAULT_GEOCODING_PROXIMITY = GeoPoint(
            latitude = 4.5339,
            longitude = -75.6811
        )
    }
}
