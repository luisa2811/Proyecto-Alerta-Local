package uniquindio.proyectoalertalocal.domain.repository

import kotlinx.coroutines.flow.Flow
import uniquindio.proyectoalertalocal.domain.model.Comment

interface CommentRepository {
    fun observeByReport(reportId: String): Flow<List<Comment>>

    suspend fun save(comment: Comment)
}
