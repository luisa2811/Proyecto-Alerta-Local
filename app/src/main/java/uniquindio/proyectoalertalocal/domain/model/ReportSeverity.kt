package uniquindio.proyectoalertalocal.domain.model

enum class ReportSeverity(val minVotes: Int) {
    LOW(0),
    MEDIUM(5),
    HIGH(15),
    CRITICAL(30);

    companion object {
        fun fromVotes(votes: Int): ReportSeverity =
            entries.lastOrNull { votes >= it.minVotes } ?: LOW
    }
}
