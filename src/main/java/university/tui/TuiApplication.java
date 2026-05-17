package university.tui;

import university.MockData;
import university.domain.user.*;
import university.service.NewsService;
import university.service.ResearchService;
import university.system.UniversitySystem;

import java.util.LinkedHashMap;

public final class TuiApplication {

    private final Session session;
    private final ResearchService researchService;
    private final NewsService newsService;
    private final AuthView authView;
    private final AdminView adminView;
    private final TeacherView teacherView;
    private final StudentView studentView;
    private final ManagerView managerView;
    private final TechSupportView techSupportView;
    private final ResearchView researchView;
    private final NewsView newsView;
    private final JournalView journalView;
    private final MessageView messageView;
    private final CourseView courseView;
    private int languageMenuChoice;

    public TuiApplication() {
        UniversitySystem system = UniversitySystem.getInstance();
        try {
            system.load();
        } catch (Exception e) {
            system = UniversitySystem.getInstance();
        }

        if (system.getUsers().isEmpty()) {
            MockData.populate(system);
            try {
                system.save();
            } catch (Exception e) {
            }
        }

        this.session = new Session(system);
        this.researchService = new ResearchService(system);
        this.newsService = new NewsService(system, researchService);
        this.authView = new AuthView(session);
        this.adminView = new AdminView(session);
        this.teacherView = new TeacherView(session, researchService, newsService);
        this.studentView = new StudentView(session, researchService);
        this.managerView = new ManagerView(session, newsService, researchService);
        this.techSupportView = new TechSupportView(session);
        this.researchView = new ResearchView(session, researchService);
        this.newsView = new NewsView(session);
        this.journalView = new JournalView(session);
        this.messageView = new MessageView(session);
        this.courseView = new CourseView(session);
    }

    public void run() {
        while (true) {
            authView.show();

            if (!session.isAuthenticated()) {
                break;
            }

            boolean running = true;
            while (running) {
                User user = session.getCurrentUser();
                int choice = showMainMenu(user);

                switch (choice) {
                    case 0 -> {
                        session.getSystem().save();
                        running = false;
                    }
                    case 9 -> {
                        session.getSystem().save();
                        session.logout();
                        System.out.println("\n  " + Messages.get("goodbye") + "\n");
                        return;
                    }
                    default -> {
                        if (choice == languageMenuChoice) {
                            showLanguageMenu(user);
                        } else {
                            dispatchRoleMenu(user, choice);
                        }
                    }
                }
            }
        }
    }

    private void showLanguageMenu(User user) {
        ConsoleMenu.printSection(Messages.get("lang.title"));
        System.out.printf("  %s: %s%n%n",
                Messages.get("lang.current"), user.getLanguage());

        LinkedHashMap<Integer, String> langOptions = new LinkedHashMap<>();
        langOptions.put(1, "English (EN)");
        langOptions.put(2, "Қазақша (KZ)");
        langOptions.put(3, "Русский (RU)");

        int lc = ConsoleMenu.showMenu(Messages.get("lang.title"), langOptions, true, false);
        if (lc == 0) return;

        university.enums.Language newLang = switch (lc) {
            case 2 -> university.enums.Language.KZ;
            case 3 -> university.enums.Language.RU;
            default -> university.enums.Language.EN;
        };

        user.changeLanguage(newLang);
        Messages.setLanguage(newLang);
        ConsoleMenu.printSuccess(Messages.get("lang.changed") + ": " + newLang);
        ConsoleInput.waitForEnter();
    }

    private int showMainMenu(User user) {
        LinkedHashMap<Integer, String> options = new LinkedHashMap<>();
        int index = 1;

        options.put(index++, Messages.get("main.news"));
        options.put(index++, Messages.get("main.journals"));
        options.put(index++, Messages.get("main.courses"));

        if (user instanceof Admin) {
            options.put(index++, Messages.get("main.admin"));
        }
        if (user instanceof Teacher) {
            options.put(index++, Messages.get("main.teacher"));
        }
        if (user instanceof Student) {
            options.put(index++, Messages.get("main.student"));
        }
        if (user instanceof Manager) {
            options.put(index++, Messages.get("main.manager"));
        }
        if (user instanceof TechSupportSpecialist) {
            options.put(index++, Messages.get("main.techsupport"));
        }
        if (user.getResearchProfile() != null) {
            options.put(index++, Messages.get("main.research"));
        }
        if (user instanceof Employee) {
            options.put(index++, Messages.get("main.messages"));
        }
        languageMenuChoice = index;
        options.put(index++, Messages.get("main.language"));

        return ConsoleMenu.showMenu(
                "Main Menu [" + user.getClass().getSimpleName() + ": " + user.getName() + "]",
                options,
                false,
                true
        );
    }

    private void dispatchRoleMenu(User user, int choice) {
        int offset = 4;
        int manageIndex = offset;

        if (choice == 1) {
            newsView.show();
        } else if (choice == 2) {
            journalView.show();
        } else if (choice == 3) {
            courseView.show();
        } else if (choice == manageIndex && user instanceof Admin) {
            adminView.show();
        } else if (choice == (manageIndex + (user instanceof Admin ? 1 : 0)) && user instanceof Teacher) {
            teacherView.show();
        } else if (choice == (manageIndex + (user instanceof Admin ? 1 : 0) + (user instanceof Teacher ? 1 : 0)) && user instanceof Student) {
            studentView.show();
        } else if (choice == (manageIndex + (user instanceof Admin ? 1 : 0) + (user instanceof Teacher ? 1 : 0) + (user instanceof Student ? 1 : 0)) && user instanceof Manager) {
            managerView.show();
        } else if (choice == (manageIndex + (user instanceof Admin ? 1 : 0) + (user instanceof Teacher ? 1 : 0) + (user instanceof Student ? 1 : 0) + (user instanceof Manager ? 1 : 0)) && user instanceof TechSupportSpecialist) {
            techSupportView.show();
        } else if (choice == (manageIndex + (user instanceof Admin ? 1 : 0) + (user instanceof Teacher ? 1 : 0) + (user instanceof Student ? 1 : 0) + (user instanceof Manager ? 1 : 0) + (user instanceof TechSupportSpecialist ? 1 : 0)) && user.getResearchProfile() != null) {
            researchView.show();
        } else if (user instanceof Employee && isMessageChoice(user, choice)) {
            messageView.show((Employee) user);
        }
    }

    private boolean isMessageChoice(User user, int choice) {
        int messageIndex = 4 +
                (user instanceof Admin ? 1 : 0) +
                (user instanceof Teacher ? 1 : 0) +
                (user instanceof Student ? 1 : 0) +
                (user instanceof Manager ? 1 : 0) +
                (user instanceof TechSupportSpecialist ? 1 : 0) +
                (user.getResearchProfile() != null ? 1 : 0);
        return choice == messageIndex;
    }

    public static void main(String[] args) {
        new TuiApplication().run();
    }
}
