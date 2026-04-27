package university.domain.communication;

import university.domain.user.Employee;

public class OfficialMessage extends Message {

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
        return (
            "OfficialMessage{" +
            "id='" +
            getId() +
            '\'' +
            ", eventType='" +
            eventType +
            '\'' +
            ", sender=" +
            getSender().getName() +
            ", receiver=" +
            getReceiver().getName() +
            ", text='" +
            getText() +
            '\'' +
            ", sentDate=" +
            getSentDate() +
            ", isRead=" +
            isRead() +
            '}'
        );
    }
}
