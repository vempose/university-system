package university.domain.news;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import university.domain.user.User;

public class JournalSubscription implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final User subscriber;
    private final UniversityJournal journal;
    private final LocalDate subscribeDate;

    public JournalSubscription(User subscriber, UniversityJournal journal) {
        if (subscriber == null) throw new IllegalArgumentException(
            "Subscriber must not be null."
        );
        if (journal == null) throw new IllegalArgumentException(
            "Journal must not be null."
        );

        this.subscriber = subscriber;
        this.journal = journal;
        this.subscribeDate = LocalDate.now();
    }

    public User getSubscriber() {
        return subscriber;
    }

    public UniversityJournal getJournal() {
        return journal;
    }

    public LocalDate getSubscribeDate() {
        return subscribeDate;
    }

    @Override
    public String toString() {
        return "JournalSubscription{subscriber=%s, journal='%s', subscribeDate=%s}".formatted(
            subscriber.getName(),
            journal.getName(),
            subscribeDate
        );
    }
}
