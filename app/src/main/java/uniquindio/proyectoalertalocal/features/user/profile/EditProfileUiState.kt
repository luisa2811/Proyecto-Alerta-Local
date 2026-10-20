package uniquindio.proyectoalertalocal.features.user.profile

data class EditProfileUiState(
    val initials: String = "MJ",
    val fullName: String = "María Jiménez",
    val email: String = "maria.jimenez@vecinos.org",
    val emailVerified: Boolean = true,
    val phone: String = "🇨🇴  +57  312 456 7890",
    val phoneVerified: Boolean = true,
    val address: String = "Cra 15 #8-42, Chapinero Central · Apto 302",
    val neighborhood: String = "Sector Los Rosales (Cuadrante 4B)",
    val displayNameInReports: Boolean = true
)
