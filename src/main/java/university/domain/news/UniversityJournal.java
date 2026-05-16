package university.domain.news;

import university.domain.research.ResearchPaper;
import university.domain.user.User;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class UniversityJournal implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String name;
    private final List<JournalSubscription> subscriptions = new ArrayList<>();
    private final List<ResearchPaper> publishedPapers = new ArrayList<>();

    public UniversityJournal(String name) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException(
                "Journal name must not be blank"
        );
        this.name = name;
    }

    public void subscribe(User user) {
        subscriptions.add(new JournalSubscription(user, this));
    }

    public void unsubscribe(User user) {
        subscriptions
                .stream()
                .filter(s -> s.getSubscriber().equals(user))
                .findFirst()
                .ifPresent(subscriptions::remove);
    }

    public void publishPaper(ResearchPaper paper) {
        publishedPapers.add(paper);
        notifySubscribers(paper);
    }

    public void notifySubscribers(ResearchPaper paper) {
        for (JournalSubscription subscription : List.copyOf(subscriptions)) {
            User subscriber = subscription.getSubscriber();
            if (subscriber instanceof JournalObserver observer) {
                observer.onPaperPublished(paper, this);
            } else {
                System.out.printf(
                        "[Journal Notification] Dear %s, a new paper has been published in \"%s\": \"%s\".%n",
                        subscriber.getName(), name, paper.title()
                );
            }
        }
    }

    public String getName() {
        return name;
    }

    public List<JournalSubscription> getSubscriptions() {
        return Collections.unmodifiableList(subscriptions);
    }

    public List<ResearchPaper> getPublishedPapers() {
        return Collections.unmodifiableList(publishedPapers);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UniversityJournal other)) return false;
        return name.equals(other.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "UniversityJournal{name='%s', subscribers=%d, publishedPapers=%d}".formatted(
                name,
                subscriptions.size(),
                publishedPapers.size()
        );
    }
}
