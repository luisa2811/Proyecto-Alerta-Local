package uniquindio.proyectoalertalocal.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import uniquindio.proyectoalertalocal.data.repository.memory.FakeAiAssistantRepository
import uniquindio.proyectoalertalocal.data.repository.memory.FakeImageStorageRepository
import uniquindio.proyectoalertalocal.data.repository.local.SessionRepositoryImpl
import uniquindio.proyectoalertalocal.data.repository.remote.RemoteCommentRepository
import uniquindio.proyectoalertalocal.data.repository.remote.RemoteNotificationRepository
import uniquindio.proyectoalertalocal.data.repository.remote.RemoteReportRepository
import uniquindio.proyectoalertalocal.data.repository.remote.RemoteUserRepository
import uniquindio.proyectoalertalocal.domain.repository.CommentRepository
import uniquindio.proyectoalertalocal.domain.repository.AiAssistantRepository
import uniquindio.proyectoalertalocal.domain.repository.ImageStorageRepository
import uniquindio.proyectoalertalocal.domain.repository.NotificationRepository
import uniquindio.proyectoalertalocal.domain.repository.ReportRepository
import uniquindio.proyectoalertalocal.domain.repository.SessionRepository
import uniquindio.proyectoalertalocal.domain.repository.UserRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindImageStorageRepository(
        implementation: FakeImageStorageRepository
    ): ImageStorageRepository

    @Binds
    @Singleton
    abstract fun bindAiAssistantRepository(
        implementation: FakeAiAssistantRepository
    ): AiAssistantRepository

    @Binds
    @Singleton
    abstract fun bindReportRepository(
        implementation: RemoteReportRepository
    ): ReportRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        implementation: RemoteUserRepository
    ): UserRepository

    @Binds
    @Singleton
    abstract fun bindCommentRepository(
        implementation: RemoteCommentRepository
    ): CommentRepository

    @Binds
    @Singleton
    abstract fun bindNotificationRepository(
        implementation: RemoteNotificationRepository
    ): NotificationRepository

    @Binds
    @Singleton
    abstract fun bindSessionRepository(
        implementation: SessionRepositoryImpl
    ): SessionRepository
}
