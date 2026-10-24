package uniquindio.proyectoalertalocal.domain.usecase.report

import jakarta.inject.Inject
import java.time.Instant
import java.util.UUID
import uniquindio.proyectoalertalocal.domain.model.Report
import uniquindio.proyectoalertalocal.domain.model.ReportCategory
import uniquindio.proyectoalertalocal.domain.model.ReportStatus
import uniquindio.proyectoalertalocal.domain.repository.ReportRepository
import uniquindio.proyectoalertalocal.domain.repository.SessionRepository
import uniquindio.proyectoalertalocal.domain.repository.UserRepository
import uniquindio.proyectoalertalocal.domain.usecase.user.CheckNewAchievementsUseCase

class CreateReportUseCase @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val userRepository: UserRepository,
    private val reportRepository: ReportRepository,
    private val checkNewAchievementsUseCase: CheckNewAchievementsUseCase
) {
    companion object {
        const val ERROR_NO_SESSION = "No existe una sesión activa."
    }

    suspend operator fun invoke(
        title: String,
        description: String,
        category: ReportCategory,
        location: String,
        possibleDuplicateOf: String? = null
    ): Result<Report> {
        val session = sessionRepository.session.value
            ?: return Result.failure(Exception(ERROR_NO_SESSION))
        val user = userRepository.findById(session.userId)
        val report = Report(
            id = UUID.randomUUID().toString(),
            title = title.trim(),
            description = description.trim(),
            category = category,
            status = ReportStatus.PENDING,
            authorId = session.userId,
            location = location.trim(),
            neighborhood = user?.neighborhood.orEmpty(),
            createdAt = Instant.now(),
            distanceMeters = 0,
            possibleDuplicateOf = possibleDuplicateOf
        )
        reportRepository.save(report)
        checkNewAchievementsUseCase(session.userId)
        return Result.success(report)
    }
}
