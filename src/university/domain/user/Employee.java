package university.domain.user;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import university.domain.academic.School;
import university.domain.communication.Message;
import university.enums.Language;

public abstract class Employee extends User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final List<Message> sentMessages = new ArrayList<>();
    List<Message> receivedMessages = new ArrayList<>();
    private double salary;
    private School school;

    protected Employee(
        String id,
        String name,
        String email,
        String password,
        Language language,
        double salary
    ) {
        super(id, name, email, password, language);
        this.salary = salary;
    }

    public Message sendMessage(Employee receiver, String text) {
        Message message = new Message(this, receiver, text);
        sentMessages.add(message);
        receiver.receivedMessages.add(message);
        return message;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        if (salary < 0) throw new IllegalArgumentException(
            "Salary must be >= 0, got: " + salary
        );
        this.salary = salary;
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public List<Message> getSentMessages() {
        return List.copyOf(sentMessages);
    }

    public List<Message> getReceivedMessages() {
        return List.copyOf(receivedMessages);
    }
}
