package uniquindio.proyectoalertalocal.domain.repository

import kotlinx.coroutines.flow.StateFlow
import uniquindio.proyectoalertalocal.domain.model.Report

interface ReportRepository {
    val reports: StateFlow<List<Report>>

    suspend fun findById(id: String): Report?

    suspend fun save(report: Report)

    suspend fun update(report: Report)

    suspend fun setSupport(reportId: String, userId: String, supported: Boolean)

    suspend fun delete(id: String)
}
