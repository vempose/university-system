package university.domain.user;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import university.domain.academic.School;
import university.domain.communication.Message;
import university.enums.Language;

/// Staff member with a salary, school, and messaging.
///
/// Extends User with work-related stuff like salary and
/// the ability to send/receive messages to other employees.
public abstract class Employee extends User {

    @Serial
    private static final long serialVersionUID = 1L;

    private final List<Message> sentMessages = new ArrayList<>();
    List<Message> receivedMessages = new ArrayList<>();
    private double salary;
    private School school;

    /// Creates an employee with a salary.
    ///
    /// @param salary  annual salary (must be >= 0)
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

    /// Sends a text message to another employee.
    ///
    /// The message gets added to both the sender's and receiver's lists.
    public Message sendMessage(Employee receiver, String text) {
        Message message = new Message(this, receiver, text);
        sentMessages.add(message);
        receiver.receivedMessages.add(message);
        return message;
    }

    /// Sends an official message (or any pre-built Message) to another employee.
    ///
    /// The message gets added to both the sender's and receiver's lists.
    public void sendOfficialMessage(Message message) {
        sentMessages.add(message);
        message.getReceiver().receivedMessages.add(message);
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
