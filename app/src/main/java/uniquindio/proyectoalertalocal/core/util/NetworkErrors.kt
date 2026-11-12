package uniquindio.proyectoalertalocal.core.util

import uniquindio.proyectoalertalocal.domain.model.AppError
import uniquindio.proyectoalertalocal.domain.model.AppException
import java.util.concurrent.CancellationException

const val NETWORK_ERROR_MESSAGE =
    "No fue posible conectar. Revisa tu conexión e intenta de nuevo."

fun Throwable.asAppException(): AppException {
    if (this is CancellationException) throw this
    return this as? AppException ?: AppException(AppError.NETWORK, NETWORK_ERROR_MESSAGE)
}

fun Throwable.toRequestFailure(): RequestResult.Failure {
    val exception = asAppException()
    return RequestResult.Failure(exception.message ?: NETWORK_ERROR_MESSAGE)
}
