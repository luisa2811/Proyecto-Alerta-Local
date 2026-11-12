package uniquindio.proyectoalertalocal.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import uniquindio.proyectoalertalocal.core.util.AndroidResourceProvider
import uniquindio.proyectoalertalocal.core.util.ResourceProvider

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    @Binds
    @Singleton
    abstract fun bindResourceProvider(
        implementation: AndroidResourceProvider
    ): ResourceProvider
}
