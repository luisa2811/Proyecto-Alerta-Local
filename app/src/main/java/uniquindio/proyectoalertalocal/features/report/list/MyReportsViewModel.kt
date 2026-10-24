package uniquindio.proyectoalertalocal.features.report.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uniquindio.proyectoalertalocal.domain.usecase.report.ObserveMyReportsUseCase

@HiltViewModel
class MyReportsViewModel @Inject constructor(
    private val observeMyReportsUseCase: ObserveMyReportsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MyReportsUiState())
    val uiState: StateFlow<MyReportsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            observeMyReportsUseCase()
                .catch {
                    _uiState.update { it.copy(isLoading = false) }
                }
                .collect { reports ->
                    _uiState.update { it.copy(reports = reports, isLoading = false) }
                }
        }
    }

    fun selectTab(tab: MyReportsTab) {
        _uiState.update { it.copy(selectedTab = tab) }
    }
}
