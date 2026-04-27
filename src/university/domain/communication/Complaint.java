package university.domain.communication;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import university.domain.user.Manager;
import university.domain.user.Student;
import university.domain.user.Teacher;
import university.enums.UrgencyLevel;

public class Complaint {

    private final String id;
    private final Teacher sender;
    private final List<Student> targetStudents;
    private final UrgencyLevel urgency;
    private final String text;
    private final LocalDate createdDate;
    private Manager receiver;

    public Complaint(
        Teacher sender,
        List<Student> targetStudents,
        UrgencyLevel urgency,
        String text,
        Manager receiver
    ) {
        if (sender == null) throw new NullPointerException(
            "sender must not be null"
        );
        if (targetStudents == null) throw new NullPointerException(
            "targetStudents must not be null"
        );
        if (urgency == null) throw new NullPointerException(
            "urgency must not be null"
        );
        if (text == null) throw new NullPointerException(
            "text must not be null"
        );
        if (receiver == null) throw new NullPointerException(
            "receiver must not be null"
        );
        if (targetStudents.isEmpty()) throw new IllegalArgumentException(
            "A complaint must target at least one student"
        );

        this.id = UUID.randomUUID().toString();
        this.sender = sender;
        this.targetStudents = List.copyOf(targetStudents);
        this.urgency = urgency;
        this.text = text;
        this.createdDate = LocalDate.now();
        this.receiver = receiver;
    }

    public String getId() {
        return id;
    }

    public Teacher getSender() {
        return sender;
    }

    public List<Student> getTargetStudents() {
        return targetStudents;
    }

    public UrgencyLevel getUrgency() {
        return urgency;
    }

    public String getText() {
        return text;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public Manager getReceiver() {
        return receiver;
    }

    public void setReceiver(Manager receiver) {
        if (receiver == null) throw new NullPointerException(
            "receiver must not be null"
        );
        this.receiver = receiver;
    }

    @Override
    public String toString() {
        return (
            "Complaint{" +
            "id='" +
            id +
            '\'' +
            ", sender='" +
            sender.getName() +
            '\'' +
            ", targets=" +
            targetStudents.size() +
            " student(s)" +
            ", urgency=" +
            urgency +
            ", receiver='" +
            receiver.getName() +
            '\'' +
            ", createdDate=" +
            createdDate +
            ", text='" +
            text +
            '\'' +
            '}'
        );
    }
}
