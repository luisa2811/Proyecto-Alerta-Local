package uniquindio.proyectoalertalocal.data.repository.memory

import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import uniquindio.proyectoalertalocal.domain.model.Comment
import uniquindio.proyectoalertalocal.domain.repository.CommentRepository

@Singleton
class CommentRepositoryImpl @Inject constructor() : CommentRepository {
    private val mutableComments = MutableStateFlow(InMemoryDemoData.comments)

    override fun observeByReport(reportId: String): Flow<List<Comment>> =
        mutableComments.map { comments -> comments.filter { it.reportId == reportId } }

    override suspend fun save(comment: Comment) {
        mutableComments.update { currentComments ->
            val existingIndex = currentComments.indexOfFirst { it.id == comment.id }
            if (existingIndex < 0) {
                currentComments + comment
            } else {
                currentComments.toMutableList().apply { set(existingIndex, comment) }
            }
        }
    }
}
