package university.domain.news;

import university.domain.research.ResearchPaper;
import university.domain.user.User;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/// A journal that publishes research papers and notifies subscribers.
///
/// Users can subscribe to get notified when new papers come out.
public class UniversityJournal implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String name;
    private final List<JournalSubscription> subscriptions = new ArrayList<>();
    private final List<ResearchPaper> publishedPapers = new ArrayList<>();

    /// Creates a journal with the given name.
    ///
    /// @param name must not be null or blank
    public UniversityJournal(String name) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException(
                "Journal name must not be blank"
        );
        this.name = name;
    }

    /// Subscribes a user to this journal.
    public void subscribe(User user) {
        subscriptions.add(new JournalSubscription(user, this));
    }

    /// Removes a user's subscription.
    public void unsubscribe(User user) {
        subscriptions
                .stream()
                .filter(s -> s.getSubscriber().equals(user))
                .findFirst()
                .ifPresent(subscriptions::remove);
    }

    /// Publishes a paper and notifies all subscribers.
    public void publishPaper(ResearchPaper paper) {
        publishedPapers.add(paper);
        notifySubscribers(paper);
    }

    /// Sends a notification to every subscriber.
    ///
    /// If the subscriber implements {@link JournalObserver},
    /// their callback is invoked; otherwise a console message is printed.
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

    /// Two journals are equal if they have the same name.
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

    /// Returns a summary of the journal.
    @Override
    public String toString() {
        return "UniversityJournal{name='%s', subscribers=%d, publishedPapers=%d}".formatted(
                name,
                subscriptions.size(),
                publishedPapers.size()
        );
    }
}
