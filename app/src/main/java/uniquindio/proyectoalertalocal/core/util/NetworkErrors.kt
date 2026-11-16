package uniquindio.proyectoalertalocal.core.util

import android.util.Log
import uniquindio.proyectoalertalocal.BuildConfig
import uniquindio.proyectoalertalocal.domain.model.AppError
import uniquindio.proyectoalertalocal.domain.model.AppException
import java.util.concurrent.CancellationException

fun Throwable.toRequestFailure(resources: ResourceProvider): RequestResult.Failure {
    if (this is CancellationException) throw this
    val error = (this as? AppException)?.error ?: AppError.NETWORK.also {
        if (BuildConfig.DEBUG) {
            Log.e("AlertaLocal", "Error no controlado", this)
        }
    }
    return RequestResult.Failure(resources.getString(error.messageRes()))
}
