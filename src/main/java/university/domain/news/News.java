package university.domain.news;

import university.enums.NewsTopic;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/// A news article posted in the system.
///
/// Can be pinned, commented on, and filtered by topic.
public class News implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String id;
    private final NewsTopic topic;
    private final LocalDateTime createdDate;
    private final List<NewsComment> comments = new ArrayList<>();
    private String title;
    private String content;
    private boolean pinned;

    /// Creates a news article with the given title, content and topic.
    ///
    /// Automatically generates an ID and timestamp.
    public News(String title, String content, NewsTopic topic) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.content = content;
        this.topic = topic;
        this.createdDate = LocalDateTime.now();
        this.pinned = false;
    }

    /// Pins this article so it stays at the top.
    public void pin() {
        this.pinned = true;
    }

    /// Adds a comment to this article.
    public void addComment(NewsComment comment) {
        comments.add(comment);
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    /// Changes the title of the article.
    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    /// Replaces the article content.
    public void setContent(String content) {
        this.content = content;
    }

    public NewsTopic getTopic() {
        return topic;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public boolean isPinned() {
        return pinned;
    }

    /// Returns the list of comments (read-only).
    public List<NewsComment> getComments() {
        return Collections.unmodifiableList(comments);
    }

    /// Two articles are equal if they have the same ID.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof News other)) return false;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    /// Returns a summary of the article.
    @Override
    public String toString() {
        return "News{id='%s', topic=%s, pinned=%b, title='%s', createdDate=%s, comments=%d}".formatted(
                id,
                topic,
                pinned,
                title,
                createdDate,
                comments.size()
        );
    }
}
