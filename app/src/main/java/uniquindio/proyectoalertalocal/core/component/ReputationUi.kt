package uniquindio.proyectoalertalocal.core.component

import androidx.annotation.StringRes
import uniquindio.proyectoalertalocal.R
import uniquindio.proyectoalertalocal.domain.model.Badge
import uniquindio.proyectoalertalocal.domain.model.UserLevel

@StringRes
fun Badge.titleRes(): Int = when (this) {
    Badge.FIRST_REPORT -> R.string.badge_first_report_title
    Badge.TRUSTED_REPORTER -> R.string.badge_trusted_reporter_title
    Badge.NEIGHBORHOOD_VOICE -> R.string.badge_neighborhood_voice_title
    Badge.ACTIVE_NEIGHBOR -> R.string.badge_active_neighbor_title
}

@StringRes
fun Badge.requirementRes(): Int = when (this) {
    Badge.FIRST_REPORT -> R.string.badge_first_report_requirement
    Badge.TRUSTED_REPORTER -> R.string.badge_trusted_reporter_requirement
    Badge.NEIGHBORHOOD_VOICE -> R.string.badge_neighborhood_voice_requirement
    Badge.ACTIVE_NEIGHBOR -> R.string.badge_active_neighbor_requirement
}

@StringRes
fun UserLevel.titleRes(): Int = when (this) {
    UserLevel.NOVICE -> R.string.level_novice
    UserLevel.COLLABORATOR -> R.string.level_collaborator
    UserLevel.GUARDIAN -> R.string.level_guardian
    UserLevel.COMMUNITY_HERO -> R.string.level_community_hero
}
