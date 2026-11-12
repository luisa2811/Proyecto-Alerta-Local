package uniquindio.proyectoalertalocal.data.remote.user

import uniquindio.proyectoalertalocal.domain.model.Badge
import uniquindio.proyectoalertalocal.domain.model.User
import uniquindio.proyectoalertalocal.domain.model.UserLevel
import uniquindio.proyectoalertalocal.domain.model.UserRole
import java.time.Instant

data class UserDto(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val role: String = UserRole.RESIDENT.name,
    val phone: String? = null,
    val address: String? = null,
    val neighborhood: String? = null,
    val emailVerified: Boolean = false,
    val phoneVerified: Boolean = false,
    val displayNameInReports: Boolean = true,
    val memberSince: Long? = null,
    val complement: String? = null,
    val earnedBadges: List<String> = emptyList(),
    val notifiedLevel: String = UserLevel.NOVICE.name,
    val moderatorCode: String? = null,
    val assignedCommune: String? = null,
    val profilePictureUrl: String? = null,
    val residenceLatitude: Double? = null,
    val residenceLongitude: Double? = null
) {
    fun toDomain(): User = User(
        id = id,
        name = name,
        email = email,
        role = UserRole.valueOf(role),
        phone = phone,
        address = address,
        neighborhood = neighborhood,
        emailVerified = emailVerified,
        phoneVerified = phoneVerified,
        displayNameInReports = displayNameInReports,
        memberSince = memberSince?.let(Instant::ofEpochMilli),
        complement = complement,
        earnedBadges = earnedBadges.map(Badge::valueOf).toSet(),
        notifiedLevel = UserLevel.valueOf(notifiedLevel),
        moderatorCode = moderatorCode,
        assignedCommune = assignedCommune,
        profilePictureUrl = profilePictureUrl,
        residenceLatitude = residenceLatitude,
        residenceLongitude = residenceLongitude
    )

    companion object {
        fun fromDomain(user: User): UserDto = UserDto(
            id = user.id,
            name = user.name,
            email = user.email,
            role = user.role.name,
            phone = user.phone,
            address = user.address,
            neighborhood = user.neighborhood,
            emailVerified = user.emailVerified,
            phoneVerified = user.phoneVerified,
            displayNameInReports = user.displayNameInReports,
            memberSince = user.memberSince?.toEpochMilli(),
            complement = user.complement,
            earnedBadges = user.earnedBadges.map(Badge::name),
            notifiedLevel = user.notifiedLevel.name,
            moderatorCode = user.moderatorCode,
            assignedCommune = user.assignedCommune,
            profilePictureUrl = user.profilePictureUrl,
            residenceLatitude = user.residenceLatitude,
            residenceLongitude = user.residenceLongitude
        )
    }
}
