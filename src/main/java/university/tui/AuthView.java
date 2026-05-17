package university.tui;

import university.domain.user.User;
import university.system.UniversitySystem;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public final class AuthView {

    private final Session session;

    public AuthView(Session session) {
        this.session = session;
    }

    public void show() {
        ConsoleMenu.printHeader("UNIVERSITY MANAGEMENT SYSTEM");
        System.out.println("  Welcome! Please authenticate to continue.");
        System.out.println();

        boolean authenticated = false;
        while (!authenticated && !Thread.currentThread().isInterrupted()) {
            String email = ConsoleInput.readEmail("  Email : ");
            String password = ConsoleInput.readPassword("  Password: ");

            User user = session.getSystem().authenticate(email, password).orElse(null);

            if (user != null) {
                session.setCurrentUser(user);
                ConsoleMenu.printSuccess("Login successful. Welcome, " + user.getName() + "!");
                authenticated = true;
            } else {
                ConsoleMenu.printError("Invalid email or password. Please try again.");
                System.out.println();
            }
        }
    }
}
