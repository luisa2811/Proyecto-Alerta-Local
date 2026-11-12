package uniquindio.proyectoalertalocal.domain.model

class AppException(val error: AppError, message: String) : Exception(message)
