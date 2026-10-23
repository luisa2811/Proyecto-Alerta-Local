package uniquindio.proyectoalertalocal.data.repository.memory

import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import uniquindio.proyectoalertalocal.domain.model.Report
import uniquindio.proyectoalertalocal.domain.repository.ReportRepository

@Singleton
class ReportRepositoryImpl @Inject constructor() : ReportRepository {
    private val mutableReports = MutableStateFlow(InMemoryDemoData.reports)

    override val reports = mutableReports.asStateFlow()

    override suspend fun findById(id: String): Report? =
        mutableReports.value.firstOrNull { it.id == id }

    override suspend fun save(report: Report) {
        mutableReports.update { currentReports ->
            val existingIndex = currentReports.indexOfFirst { it.id == report.id }
            if (existingIndex < 0) {
                currentReports + report
            } else {
                currentReports.toMutableList().apply { set(existingIndex, report) }
            }
        }
    }

    override suspend fun update(report: Report) {
        mutableReports.update { currentReports ->
            currentReports.map { currentReport ->
                if (currentReport.id == report.id) report else currentReport
            }
        }
    }

    override suspend fun delete(id: String) {
        mutableReports.update { currentReports ->
            currentReports.filterNot { it.id == id }
        }
    }
}
