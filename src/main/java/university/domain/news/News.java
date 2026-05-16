package university.domain.news;

import university.enums.NewsTopic;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

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

    public News(String title, String content, NewsTopic topic) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.content = content;
        this.topic = topic;
        this.createdDate = LocalDateTime.now();
        this.pinned = false;
    }

    public void pin() {
        this.pinned = true;
    }

    public void addComment(NewsComment comment) {
        comments.add(comment);
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

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
                id,
                topic,
                pinned,
                title,
                createdDate,
                comments.size()
        );
    }
}
