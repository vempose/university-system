package university.domain.communication;

import university.domain.user.Employee;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/// A basic message between two employees.
///
/// Tracks sender, receiver, text, timestamp, and read status.
public class Message implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String id;
    private final Employee sender;
    private final Employee receiver;
    private final String text;
    private final LocalDateTime sentDate;
    private boolean isRead;

    /// Creates a message. A UUID and timestamp are generated automatically;
    /// starts unread.
    public Message(Employee sender, Employee receiver, String text) {
        if (sender == null) throw new IllegalArgumentException(
                "Sender must not be null."
        );
        if (receiver == null) throw new IllegalArgumentException(
                "Receiver must not be null."
        );
        if (text == null) throw new IllegalArgumentException(
                "Message text must not be null."
        );

        this.id = UUID.randomUUID().toString();
        this.sender = sender;
        this.receiver = receiver;
        this.text = text;
        this.sentDate = LocalDateTime.now();
        this.isRead = false;
    }

    /// Marks this message as read.
    public void markRead() {
        this.isRead = true;
    }

    public String getId() {
        return id;
    }

    public Employee getSender() {
        return sender;
    }

    public Employee getReceiver() {
        return receiver;
    }

    public String getText() {
        return text;
    }

    public LocalDateTime getSentDate() {
        return sentDate;
    }

    public boolean isRead() {
        return isRead;
    }

    @Override
    public String toString() {
        return "Message{id='%s', sender=%s, receiver=%s, sentDate=%s, isRead=%b, text='%s'}".formatted(
                id,
                sender.getName(),
                receiver.getName(),
                sentDate,
                isRead,
                text
        );
    }
}
