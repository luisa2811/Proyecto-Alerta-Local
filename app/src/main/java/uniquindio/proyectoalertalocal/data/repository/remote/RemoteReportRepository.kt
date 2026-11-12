package uniquindio.proyectoalertalocal.data.repository.remote

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import uniquindio.proyectoalertalocal.data.remote.report.ReportDto
import uniquindio.proyectoalertalocal.domain.model.Report
import uniquindio.proyectoalertalocal.domain.repository.ReportRepository

@Singleton
class RemoteReportRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) : ReportRepository {
    private val mutableReports = MutableStateFlow<List<Report>>(emptyList())
    private val collection = firestore.collection(COLLECTION_REPORTS)

    override val reports: StateFlow<List<Report>> = mutableReports.asStateFlow()

    init {
        collection.addSnapshotListener { snapshot, error ->
            if (error == null) {
                mutableReports.value = snapshot?.documents.orEmpty().mapNotNull { document ->
                    document.toObject(ReportDto::class.java)?.copy(id = document.id)?.toDomain()
                }
            }
        }
    }

    override suspend fun findById(id: String): Report? =
        collection.document(id).get().await().toReport()

    override suspend fun save(report: Report) {
        collection.document(report.id).set(ReportDto.fromDomain(report)).await()
    }

    override suspend fun update(report: Report) {
        collection.document(report.id).set(ReportDto.fromDomain(report)).await()
    }

    override suspend fun delete(id: String) {
        collection.document(id).delete().await()
    }

    override suspend fun setSupport(reportId: String, userId: String, supported: Boolean) {
        val operation = if (supported) {
            FieldValue.arrayUnion(userId)
        } else {
            FieldValue.arrayRemove(userId)
        }
        collection.document(reportId).update(FIELD_SUPPORTED_BY, operation).await()
    }

    private fun com.google.firebase.firestore.DocumentSnapshot.toReport(): Report? =
        toObject(ReportDto::class.java)?.copy(id = id)?.toDomain()

    private companion object {
        const val COLLECTION_REPORTS = "reports"
        const val FIELD_SUPPORTED_BY = "supportedBy"
    }
}
