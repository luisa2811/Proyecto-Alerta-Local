package uniquindio.proyectoalertalocal.domain.usecase.comment

import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import uniquindio.proyectoalertalocal.domain.model.CommentSummary
import uniquindio.proyectoalertalocal.domain.repository.CommentRepository
import uniquindio.proyectoalertalocal.domain.repository.UserRepository

class ObserveCommentsUseCase @Inject constructor(
    private val commentRepository: CommentRepository,
    private val userRepository: UserRepository
) {
    operator fun invoke(reportId: String): Flow<List<CommentSummary>> =
        combine(
            commentRepository.observeByReport(reportId),
            userRepository.observeAll()
        ) { comments, users ->
            CommentSummary.build(comments.sortedBy { it.createdAt }, users)
        }
}
