package uniquindio.proyectoalertalocal.core.util

import androidx.annotation.StringRes
import uniquindio.proyectoalertalocal.R
import uniquindio.proyectoalertalocal.domain.model.AppError

@StringRes
fun AppError.messageRes(): Int = when (this) {
    AppError.NO_ACTIVE_SESSION -> R.string.error_no_active_session
    AppError.IMAGE_UPLOAD_FAILED -> R.string.error_image_upload_failed
    AppError.EMAIL_ALREADY_EXISTS -> R.string.error_email_already_exists
    AppError.NETWORK -> R.string.error_network
    AppError.REPORT_NOT_FOUND -> R.string.error_report_not_found
    AppError.INVALID_CREDENTIALS -> R.string.error_invalid_credentials
    AppError.MODERATOR_ACCOUNT_REQUIRES_MODERATOR_ROLE ->
        R.string.error_moderator_account_requires_moderator_role
    AppError.MODERATOR_ACCESS_DENIED -> R.string.error_moderator_access_denied
    AppError.INVALID_MODERATOR_CODE -> R.string.error_invalid_moderator_code
    AppError.NOTIFICATION_NOT_FOUND -> R.string.error_notification_not_found
    AppError.ACCOUNT_NOT_FOUND -> R.string.error_account_not_found
    AppError.PASSWORD_RECOVERY_ACCOUNT_NOT_FOUND -> R.string.error_password_recovery_account_not_found
    AppError.PASSWORD_RESET_CODE_NOT_VERIFIED -> R.string.error_password_reset_code_not_verified
    AppError.PASSWORD_RESET_CODE_EXPIRED -> R.string.error_password_reset_code_expired
    AppError.INVALID_RECOVERY_CODE -> R.string.error_invalid_recovery_code
    AppError.REPORT_ALREADY_REVIEWED -> R.string.error_report_already_reviewed
    AppError.INVALID_REJECTION_REASON -> R.string.error_invalid_rejection_reason
    AppError.MODERATOR_REQUIRED -> R.string.error_moderator_required
    AppError.REPORT_NOT_RESOLVED -> R.string.error_report_not_resolved
    AppError.REPORT_NOT_OWNER -> R.string.error_report_not_owner
    AppError.REPORT_INVALID_STATUS -> R.string.error_report_invalid_status
    AppError.REPORT_NOT_ACTIVE -> R.string.error_report_not_active
    AppError.REPORT_OWN_SUPPORT -> R.string.error_report_own_support
    AppError.REPORT_ALREADY_SUPPORTED -> R.string.error_report_already_supported
    AppError.REPORT_ALREADY_RESOLVED -> R.string.error_report_already_resolved
    AppError.EMAIL_ALREADY_REGISTERED -> R.string.error_email_already_registered
    AppError.UNRECOGNIZED_SPEECH -> R.string.error_unrecognized_speech
}
