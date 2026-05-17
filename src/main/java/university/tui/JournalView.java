package university.tui;

import university.domain.news.UniversityJournal;
import university.domain.research.ResearchPaper;
import university.domain.user.User;
import university.system.UniversitySystem;

import java.util.LinkedHashMap;
import java.util.List;

class JournalView {

    private final Session session;

    JournalView(Session session) {
        this.session = session;
    }

    void show() {
        UniversitySystem system = session.getSystem();
        User user = session.getCurrentUser();

        while (true) {
            LinkedHashMap<Integer, String> options = new LinkedHashMap<>();
            options.put(1, "View All Journals");
            options.put(2, "Subscribe to Journal");
            options.put(3, "Unsubscribe from Journal");
            options.put(4, "View Journal Papers");
            options.put(5, "Create New Journal");

            int choice = ConsoleMenu.showMenu("University Journals", options, true, false);
            switch (choice) {
                case 0 -> { return; }
                case 1 -> viewAllJournals(system);
                case 2 -> subscribe(system, user);
                case 3 -> unsubscribe(system, user);
                case 4 -> viewJournalPapers(system);
                case 5 -> createJournal(system);
            }
        }
    }

    private void viewAllJournals(UniversitySystem system) {
        ConsoleMenu.printSection("All Journals");
        List<UniversityJournal> journals = system.getJournals();
        if (journals.isEmpty()) {
            ConsoleMenu.printInfo("No journals available.");
        } else {
            for (UniversityJournal j : journals) {
                System.out.printf(
                        "  %s | Subscribers: %d | Papers: %d%n",
                        j.getName(),
                        j.getSubscriptions().size(),
                        j.getPublishedPapers().size()
                );
            }
        }
        ConsoleInput.waitForEnter();
    }

    private void subscribe(UniversitySystem system, User user) {
        ConsoleMenu.printSection("Subscribe to Journal");
        List<UniversityJournal> journals = system.getJournals();
        if (journals.isEmpty()) {
            ConsoleMenu.printInfo("No journals available.");
            ConsoleInput.waitForEnter();
            return;
        }
        for (int i = 0; i < journals.size(); i++) {
            System.out.printf("  [%d]  %s%n", i + 1, journals.get(i).getName());
        }
        int ji = ConsoleInput.readInt("\n  Select journal: ", 1, journals.size()) - 1;
        journals.get(ji).subscribe(user);
        ConsoleMenu.printSuccess("Subscribed to " + journals.get(ji).getName());
        ConsoleInput.waitForEnter();
    }

    private void unsubscribe(UniversitySystem system, User user) {
        ConsoleMenu.printSection("Unsubscribe from Journal");
        List<UniversityJournal> journals = system.getJournals();
        if (journals.isEmpty()) {
            ConsoleMenu.printInfo("No journals available.");
            ConsoleInput.waitForEnter();
            return;
        }
        for (int i = 0; i < journals.size(); i++) {
            System.out.printf("  [%d]  %s%n", i + 1, journals.get(i).getName());
        }
        int ji = ConsoleInput.readInt("\n  Select journal: ", 1, journals.size()) - 1;
        journals.get(ji).unsubscribe(user);
        ConsoleMenu.printSuccess("Unsubscribed from " + journals.get(ji).getName());
        ConsoleInput.waitForEnter();
    }

    private void viewJournalPapers(UniversitySystem system) {
        ConsoleMenu.printSection("View Journal Papers");
        List<UniversityJournal> journals = system.getJournals();
        if (journals.isEmpty()) {
            ConsoleMenu.printInfo("No journals available.");
            ConsoleInput.waitForEnter();
            return;
        }
        for (int i = 0; i < journals.size(); i++) {
            System.out.printf("  [%d]  %s%n", i + 1, journals.get(i).getName());
        }
        int ji = ConsoleInput.readInt("\n  Select journal: ", 1, journals.size()) - 1;
        UniversityJournal journal = journals.get(ji);

        ConsoleMenu.printSection(journal.getName() + " - Published Papers");
        List<ResearchPaper> papers = journal.getPublishedPapers();
        if (papers.isEmpty()) {
            ConsoleMenu.printInfo("No papers published in this journal yet.");
        } else {
            for (ResearchPaper p : papers) {
                System.out.println("  " + p.getCitation(university.enums.CitationFormat.PLAIN_TEXT));
            }
        }
        ConsoleInput.waitForEnter();
    }

    private void createJournal(UniversitySystem system) {
        ConsoleMenu.printSection("Create New Journal");
        String name = ConsoleInput.readLine("  Journal name: ");
        UniversityJournal journal = new UniversityJournal(name);
        system.addJournal(journal);
        ConsoleMenu.printSuccess("Journal created: " + name);
        ConsoleInput.waitForEnter();
    }
}
