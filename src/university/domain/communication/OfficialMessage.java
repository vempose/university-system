package university.domain.communication;

import java.io.Serial;
import java.io.Serializable;
import university.domain.user.Employee;

public class OfficialMessage extends Message implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String eventType;

    public OfficialMessage(
        Employee sender,
        Employee receiver,
        String text,
        String eventType
    ) {
        super(sender, receiver, text);
        if (eventType == null || eventType.isBlank()) {
            throw new IllegalArgumentException(
                "eventType must not be null or blank"
            );
        }
        this.eventType = eventType;
    }

    public String getEventType() {
        return eventType;
    }

    @Override
    public String toString() {
        return "OfficialMessage{id='%s', eventType='%s', sender=%s, receiver=%s, text='%s', sentDate=%s, isRead=%b}".formatted(
            getId(),
            eventType,
            getSender().getName(),
            getReceiver().getName(),
            getText(),
            getSentDate(),
            isRead()
        );
    }
}
