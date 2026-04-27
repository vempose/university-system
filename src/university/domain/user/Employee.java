package university.domain.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import university.domain.communication.Message;
import university.enums.Language;

public abstract class Employee extends User {

    private double salary;
    private final List<Message> sentMessages = new ArrayList<>();
    private final List<Message> receivedMessages = new ArrayList<>();

    protected Employee(
        String id,
        String name,
        String email,
        String passwordHash,
        Language language,
        double salary
    ) {
        super(id, name, email, passwordHash, language);
        if (salary < 0) {
            throw new IllegalArgumentException(
                "Salary must be non-negative, got: " + salary
            );
        }
        this.salary = salary;
    }

    public Message sendMessage(Employee receiver, String text) {
        Objects.requireNonNull(receiver, "receiver must not be null");
        Objects.requireNonNull(text, "text must not be null");

        var message = new Message(this, receiver, text);
        sentMessages.add(message);
        receiver.receivedMessages.add(message); // direct field access — same package
        return message;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        if (salary < 0) {
            throw new IllegalArgumentException(
                "Salary must be non-negative, got: " + salary
            );
        }
        this.salary = salary;
    }

    public List<Message> getSentMessages() {
        return List.copyOf(sentMessages);
    }

    public List<Message> getReceivedMessages() {
        return List.copyOf(receivedMessages);
    }
}
