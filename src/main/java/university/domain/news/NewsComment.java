package university.domain.news;

import university.domain.user.User;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/// A comment left by a user on a news article.
///
/// Has an author, text content, and timestamp.
public class NewsComment implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String id;
    private final String text;
    private final LocalDateTime createdDate;
    private final User author;

    /// Creates a new comment.
    ///
    /// @param text the comment body (must not be blank)
    /// @param author the user who wrote it (must not be null)
    public NewsComment(String text, User author) {
        if (text == null || text.isBlank()) throw new IllegalArgumentException(
                "Comment text must not be null or blank."
        );
        if (author == null) throw new IllegalArgumentException(
                "Comment author must not be null."
        );

        this.id = UUID.randomUUID().toString();
        this.text = text;
        this.author = author;
        this.createdDate = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public User getAuthor() {
        return author;
    }

    /// Returns a summary of the comment.
    @Override
    public String toString() {
        return "NewsComment{id='%s', author='%s', createdDate=%s, text='%s'}".formatted(
                id,
                author.getName(),
                createdDate,
                text
        );
    }
}
