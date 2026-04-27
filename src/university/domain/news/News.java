package university.domain.news;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import university.enums.NewsTopic;

public class News {

    private final String id;
    private String title;
    private String content;
    private final NewsTopic topic;
    private final LocalDateTime createdDate;
    private boolean pinned;
    private final List<NewsComment> comments = new ArrayList<>();

    public News(String title, String content, NewsTopic topic) {
        this.id = UUID.randomUUID().toString();
        this.title = Objects.requireNonNull(title, "title must not be null");
        this.content = Objects.requireNonNull(
            content,
            "content must not be null"
        );
        this.topic = Objects.requireNonNull(topic, "topic must not be null");
        this.createdDate = LocalDateTime.now();
        this.pinned = false;
    }

    public void pin() {
        this.pinned = true;
    }

    public void addComment(NewsComment comment) {
        Objects.requireNonNull(comment, "comment must not be null");
        comments.add(comment);
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
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

    public void setTitle(String title) {
        this.title = Objects.requireNonNull(title, "title must not be null");
    }

    public void setContent(String content) {
        this.content = Objects.requireNonNull(
            content,
            "content must not be null"
        );
    }

    public List<NewsComment> getComments() {
        return Collections.unmodifiableList(comments);
    }

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

    @Override
    public String toString() {
        return "News{id='%s', topic=%s, pinned=%b, title='%s', createdDate=%s, comments=%d}".formatted(
                id, topic, pinned, title, createdDate, comments.size()
        );
    }
}
