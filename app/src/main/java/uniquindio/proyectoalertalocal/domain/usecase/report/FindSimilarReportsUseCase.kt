package uniquindio.proyectoalertalocal.domain.usecase.report

import jakarta.inject.Inject
import java.text.Normalizer
import java.time.Duration
import java.time.Instant
import uniquindio.proyectoalertalocal.domain.model.Report
import uniquindio.proyectoalertalocal.domain.model.ReportCategory
import uniquindio.proyectoalertalocal.domain.model.ReportStatus
import uniquindio.proyectoalertalocal.domain.repository.ReportRepository
import uniquindio.proyectoalertalocal.domain.repository.SessionRepository
import uniquindio.proyectoalertalocal.domain.repository.UserRepository

class FindSimilarReportsUseCase @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val userRepository: UserRepository,
    private val reportRepository: ReportRepository
) {
    companion object {
        const val ERROR_NO_SESSION = "No existe una sesión activa."
        private val STOP_WORDS = setOf(
            "el", "la", "los", "las", "en", "de", "del", "con", "por", "para", "una", "uno"
        )
    }

    suspend operator fun invoke(title: String, category: ReportCategory): Result<List<Report>> {
        val session = sessionRepository.session.value
            ?: return Result.failure(Exception(ERROR_NO_SESSION))
        val userId = session.userId
        val user = userRepository.findById(userId)
        val neighborhood = user?.neighborhood.orEmpty()
        val now = Instant.now()
        val inputTokens = significantTokens(title)

        val candidates = reportRepository.reports.value.filter { report ->
            if (report.possibleDuplicateOf != null) return@filter false
            if (report.category != category) return@filter false
            val sameNeighborhood = neighborhood.isNotEmpty() && report.neighborhood == neighborhood
            val nearby = report.distanceMeters <= 500
            if (!sameNeighborhood && !nearby) return@filter false
            if (report.status != ReportStatus.PENDING && report.status != ReportStatus.VERIFIED) return@filter false
            if (Duration.between(report.createdAt, now) > Duration.ofHours(48)) return@filter false
            significantTokens(report.title).intersect(inputTokens).size >= 2
        }

        val sorted = candidates.sortedWith(
            compareByDescending<Report> { significantTokens(it.title).intersect(inputTokens).size }
                .thenByDescending<Report> { if (it.authorId == userId) 0 else 1 }
                .thenByDescending { it.createdAt }
        ).take(3)
        return Result.success(sorted)
    }

    private fun significantTokens(text: String): Set<String> {
        val normalized = normalize(text)
        return normalized
            .split(Regex("[^a-z0-9]+"))
            .filter { it.length >= 3 && it !in STOP_WORDS }
            .toSet()
    }

    private fun normalize(text: String): String {
        val withoutDiacritics = Normalizer.normalize(text, Normalizer.Form.NFD)
            .replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
        return withoutDiacritics.lowercase()
    }
}
