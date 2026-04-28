package university.users;

import university.academic.LogEntry;

import java.util.ArrayList;
import java.util.List;

public class Admin extends Employee{
    public Admin(String name, String email, String password, double salary) {
        super(name, email, password, salary);
    }
    public void addUser(User user) {
        System.out.println("User added: " + user.getName());
    }
    public void removeUser(User user) {
        System.out.println("User removed: " + user.getName());
    }
    public void updateUser(User user) {
        System.out.println("User updated: " + user.getName());
    }
    public List<LogEntry> viewLogs() {
        return new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Admin{id=" + getId() + ", name=" + getName() + ", salary=" + getSalary() + "}";
    }
}
