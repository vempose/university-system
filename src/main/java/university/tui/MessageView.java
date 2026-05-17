package university.tui;

import university.domain.communication.Message;
import university.domain.user.Employee;
import university.domain.user.User;
import university.system.UniversitySystem;

import java.util.LinkedHashMap;
import java.util.List;

class MessageView {

    private final Session session;

    MessageView(Session session) {
        this.session = session;
    }

    void show(Employee employee) {
        UniversitySystem system = session.getSystem();

        while (true) {
            LinkedHashMap<Integer, String> options = new LinkedHashMap<>();
            options.put(1, "View Received Messages");
            options.put(2, "View Sent Messages");
            options.put(3, "Send Message to Employee");

            int choice = ConsoleMenu.showMenu("Messages", options, true, false);
            switch (choice) {
                case 0 -> { return; }
                case 1 -> viewReceivedMessages(employee);
                case 2 -> viewSentMessages(employee);
                case 3 -> sendMessage(employee);
            }
        }
    }

    private void viewReceivedMessages(Employee employee) {
        ConsoleMenu.printSection("Received Messages");
        List<Message> messages = employee.getReceivedMessages();
        printMessages(messages, "No received messages.");
    }

    private void viewSentMessages(Employee employee) {
        ConsoleMenu.printSection("Sent Messages");
        List<Message> messages = employee.getSentMessages();
        printMessages(messages, "No sent messages.");
    }

    private void printMessages(List<Message> messages, String emptyMessage) {
        if (messages.isEmpty()) {
            ConsoleMenu.printInfo(emptyMessage);
        } else {
            for (Message m : messages) {
                System.out.printf(
                        "  From: %-20s | To: %-20s | %s%n",
                        m.getSender().getName(),
                        m.getReceiver().getName(),
                        m.getSentDate()
                );
                System.out.println("    " + m.getText() + (m.isRead() ? "" : " [UNREAD]"));
                System.out.println();
                m.markRead();
            }
        }
        ConsoleInput.waitForEnter();
    }

    private void sendMessage(Employee sender) {
        ConsoleMenu.printSection("Send Message");
        List<Employee> employees = session.getSystem().getUsers().stream()
                .filter(u -> u instanceof Employee && !u.equals(sender))
                .map(u -> (Employee) u)
                .toList();

        if (employees.isEmpty()) {
            ConsoleMenu.printInfo("No other employees in the system.");
            ConsoleInput.waitForEnter();
            return;
        }
        for (int i = 0; i < employees.size(); i++) {
            System.out.printf("  [%d]  %s (%s)%n",
                    i + 1,
                    employees.get(i).getName(),
                    employees.get(i).getClass().getSimpleName()
            );
        }
        int ei = ConsoleInput.readInt("\n  Select receiver: ", 1, employees.size()) - 1;
        Employee receiver = employees.get(ei);
        String text = ConsoleInput.readLine("  Message: ");
        sender.sendMessage(receiver, text);
        ConsoleMenu.printSuccess("Message sent to " + receiver.getName());
        ConsoleInput.waitForEnter();
    }
}
