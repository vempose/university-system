package university.users;

import university.communication.Message;

import java.util.ArrayList;
import java.util.List;

public abstract class Employee extends User {
    private double salary;
    private List<Message> inbox;
    protected Employee(String name, String email, String password, double salary) {
        super(name, email, password);
        this.salary = salary;
        this.inbox = new ArrayList<>();
    }
    public Message sendMessage(Employee receiver, String text) {
        Message msg = new Message(text, this, receiver);
        receiver.inbox.add(msg);
        return msg;
    }
    public List<Message> receiveMessages() {
        return new ArrayList<>(inbox);
    }
    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }

    @Override
    public String toString() {
        return "Employee{id=" + getId() + ", name=" + getName() + ", salary=" + salary + "}";
    }
}
