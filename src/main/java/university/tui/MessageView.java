package university.tui;

import university.tui.Messages;
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
            options.put(1, Messages.get("message.received"));
            options.put(2, Messages.get("message.sent"));
            options.put(3, Messages.get("message.send"));

            int choice = ConsoleMenu.showMenu(Messages.get("message.title"), options, true, false);
            switch (choice) {
                case 0 -> { return; }
                case 1 -> viewReceivedMessages(employee);
                case 2 -> viewSentMessages(employee);
                case 3 -> sendMessage(employee);
            }
        }
    }

    private void viewReceivedMessages(Employee employee) {
        ConsoleMenu.printSection(Messages.get("message.received"));
        List<Message> messages = employee.getReceivedMessages();
        printMessages(messages, Messages.get("message.no_received"));
    }

    private void viewSentMessages(Employee employee) {
        ConsoleMenu.printSection(Messages.get("message.sent"));
        List<Message> messages = employee.getSentMessages();
        printMessages(messages, Messages.get("message.no_sent"));
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
        ConsoleMenu.printSection(Messages.get("message.send"));
        List<Employee> employees = session.getSystem().getUsers().stream()
                .filter(u -> u instanceof Employee && !u.equals(sender))
                .map(u -> (Employee) u)
                .toList();

        if (employees.isEmpty()) {
            ConsoleMenu.printInfo(Messages.get("message.no_employees"));
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
        int ei = ConsoleInput.readInt("\n  " + Messages.get("message.select_receiver") + ": ", 1, employees.size()) - 1;
        Employee receiver = employees.get(ei);
        String text = ConsoleInput.readLine("  " + Messages.get("message.text") + ": ");
        sender.sendMessage(receiver, text);
        ConsoleMenu.printSuccess(Messages.get("message.sent_to", receiver.getName()));
        ConsoleInput.waitForEnter();
    }
}
