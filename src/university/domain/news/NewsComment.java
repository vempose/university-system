package university.domain.news;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;
import university.domain.user.User;

public class NewsComment implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String id;
    private final String text;
    private final LocalDateTime createdDate;
    private final User author;

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
