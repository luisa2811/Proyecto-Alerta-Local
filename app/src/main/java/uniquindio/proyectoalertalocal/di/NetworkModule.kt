package uniquindio.proyectoalertalocal.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Named
import jakarta.inject.Singleton
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uniquindio.proyectoalertalocal.BuildConfig
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {
            builder.addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
        }

        return builder.build()
    }

    @Provides
    @Singleton
    @Named("cloudinary")
    fun provideCloudinaryRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(CLOUDINARY_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    @Named("openrouter")
    fun provideOpenRouterRetrofit(client: OkHttpClient): Retrofit {
        val openRouterClient = client.newBuilder()
            .addInterceptor(
                Interceptor { chain ->
                    val authenticatedRequest = chain.request()
                        .newBuilder()
                        .header(
                            AUTHORIZATION_HEADER,
                            "Bearer ${BuildConfig.OPENROUTER_API_KEY}"
                        )
                        .build()
                    chain.proceed(authenticatedRequest)
                }
            )
            .build()

        return Retrofit.Builder()
            .baseUrl(OPENROUTER_BASE_URL)
            .client(openRouterClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private const val TIMEOUT_SECONDS = 20L
    private const val CLOUDINARY_BASE_URL = "https://api.cloudinary.com/"
    private const val OPENROUTER_BASE_URL = "https://openrouter.ai/api/v1/"
    private const val AUTHORIZATION_HEADER = "Authorization"
}
