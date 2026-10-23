package uniquindio.proyectoalertalocal.domain.model

data class CommentSummary(
    val comment: Comment,
    val authorName: String
) {
    companion object {
        fun build(comments: List<Comment>, users: List<User>): List<CommentSummary> {
            val usersById = users.associateBy { it.id }
            return comments.map { comment ->
                CommentSummary(
                    comment = comment,
                    authorName = AuthorNames.displayName(usersById[comment.authorId])
                )
            }
        }
    }
}
