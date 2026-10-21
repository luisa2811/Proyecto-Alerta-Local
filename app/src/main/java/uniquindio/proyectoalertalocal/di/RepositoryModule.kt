package uniquindio.proyectoalertalocal.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import uniquindio.proyectoalertalocal.data.repository.memory.CommentRepositoryImpl
import uniquindio.proyectoalertalocal.data.repository.memory.NotificationRepositoryImpl
import uniquindio.proyectoalertalocal.data.repository.memory.ReportRepositoryImpl
import uniquindio.proyectoalertalocal.data.repository.memory.SessionRepositoryImpl
import uniquindio.proyectoalertalocal.data.repository.memory.UserRepositoryImpl
import uniquindio.proyectoalertalocal.domain.repository.CommentRepository
import uniquindio.proyectoalertalocal.domain.repository.NotificationRepository
import uniquindio.proyectoalertalocal.domain.repository.ReportRepository
import uniquindio.proyectoalertalocal.domain.repository.SessionRepository
import uniquindio.proyectoalertalocal.domain.repository.UserRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindReportRepository(
        implementation: ReportRepositoryImpl
    ): ReportRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        implementation: UserRepositoryImpl
    ): UserRepository

    @Binds
    @Singleton
    abstract fun bindCommentRepository(
        implementation: CommentRepositoryImpl
    ): CommentRepository

    @Binds
    @Singleton
    abstract fun bindNotificationRepository(
        implementation: NotificationRepositoryImpl
    ): NotificationRepository

    @Binds
    @Singleton
    abstract fun bindSessionRepository(
        implementation: SessionRepositoryImpl
    ): SessionRepository
}
