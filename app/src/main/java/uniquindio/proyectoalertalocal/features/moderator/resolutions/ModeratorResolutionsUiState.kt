package uniquindio.proyectoalertalocal.features.moderator.resolutions

import uniquindio.proyectoalertalocal.core.util.RequestResult
import uniquindio.proyectoalertalocal.domain.model.ReportCategory
import uniquindio.proyectoalertalocal.domain.model.ResolutionBoard

enum class ResolutionTab {
    ACTIVE,
    TO_FINISH,
    RESOLVED
}

data class ModeratorResolutionsUiState(
    val board: ResolutionBoard? = null,
    val selectedTab: ResolutionTab = ResolutionTab.ACTIVE,
    val selectedCategory: ReportCategory? = null,
    val notes: Map<String, String> = emptyMap(),
    val reopeningReportId: String? = null,
    val result: RequestResult? = null,
    val isLoading: Boolean = true,
    val neighborhood: String = ""
)
