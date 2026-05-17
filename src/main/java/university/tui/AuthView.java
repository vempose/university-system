package university.tui;

import university.domain.user.User;
import university.enums.Language;

/// Login screen — asks for email + password and authenticates
/// the user against the system.
class AuthView {

    private final Session session;

    /// Wraps the shared session for auth.
    public AuthView(Session session) {
        this.session = session;
    }

    /// Shows the login prompt and loops until credentials are correct.
    public void show() {
        ConsoleMenu.printHeader(Messages.get("app.title"));
        System.out.println("  " + Messages.get("auth.welcome"));
        System.out.println();

        boolean authenticated = false;
        while (!authenticated && !Thread.currentThread().isInterrupted()) {
            String email = ConsoleInput.readEmail("  " + Messages.get("auth.email") + " : ");
            String password = ConsoleInput.readPassword("  " + Messages.get("auth.password") + ": ");

            User user = session.getSystem().authenticate(email, password).orElse(null);

            if (user != null) {
                Messages.setLanguage(user.getLanguage());
                session.setCurrentUser(user);
                ConsoleMenu.printSuccess(Messages.get("auth.success", user.getName()));
                authenticated = true;
            } else {
                ConsoleMenu.printError(Messages.get("auth.fail"));
                System.out.println();
            }
        }
    }
}
