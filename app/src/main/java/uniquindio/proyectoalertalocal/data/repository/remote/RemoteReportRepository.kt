package uniquindio.proyectoalertalocal.data.repository.remote

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ListenerRegistration
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
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : ReportRepository {
    private val mutableReports = MutableStateFlow<List<Report>>(emptyList())
    private val collection = firestore.collection(COLLECTION_REPORTS)
    private var reportListener: ListenerRegistration? = null

    override val reports: StateFlow<List<Report>> = mutableReports.asStateFlow()

    private val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        reportListener?.remove()
        reportListener = null

        if (firebaseAuth.currentUser == null) {
            mutableReports.value = emptyList()
        } else {
            var registration: ListenerRegistration? = null
            registration = collection.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    registration?.remove()
                    if (reportListener === registration) {
                        reportListener = null
                    }
                } else if (snapshot != null && reportListener === registration) {
                    mutableReports.value = snapshot.documents.mapNotNull { document ->
                        document.toObject(ReportDto::class.java)
                            ?.copy(id = document.id)
                            ?.toDomain()
                    }
                }
            }
            reportListener = registration
        }
    }

    init {
        auth.addAuthStateListener(authStateListener)
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
