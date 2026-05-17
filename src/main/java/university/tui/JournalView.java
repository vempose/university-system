package university.tui;

import university.tui.Messages;
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
            options.put(1, Messages.get("journal.view_all"));
            options.put(2, Messages.get("journal.subscribe"));
            options.put(3, Messages.get("journal.unsubscribe"));
            options.put(4, Messages.get("journal.view_papers"));
            options.put(5, Messages.get("journal.create"));

            int choice = ConsoleMenu.showMenu(Messages.get("journal.title"), options, true, false);
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
        ConsoleMenu.printSection(Messages.get("journal.title"));
        List<UniversityJournal> journals = system.getJournals();
        if (journals.isEmpty()) {
            ConsoleMenu.printInfo(Messages.get("journal.no_journals"));
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
        ConsoleMenu.printSection(Messages.get("journal.subscribe"));
        List<UniversityJournal> allJournals = system.getJournals();
        List<UniversityJournal> available = allJournals.stream()
                .filter(j -> j.getSubscriptions().stream()
                        .noneMatch(s -> s.getSubscriber().equals(user)))
                .toList();
        if (available.isEmpty()) {
            ConsoleMenu.printInfo(allJournals.isEmpty()
                    ? Messages.get("journal.no_journals")
                    : "You are subscribed to all journals.");
            ConsoleInput.waitForEnter();
            return;
        }
        for (int i = 0; i < available.size(); i++) {
            System.out.printf("  [%d]  %s%n", i + 1, available.get(i).getName());
        }
        int ji = ConsoleInput.readInt("\n  " + Messages.get("journal.select") + ": ", 1, available.size()) - 1;
        available.get(ji).subscribe(user);
        ConsoleMenu.printSuccess(Messages.get("journal.subscribed", available.get(ji).getName()));
        ConsoleInput.waitForEnter();
    }

    private void unsubscribe(UniversitySystem system, User user) {
        ConsoleMenu.printSection(Messages.get("journal.unsubscribe"));
        List<UniversityJournal> allJournals = system.getJournals();
        List<UniversityJournal> subscribed = allJournals.stream()
                .filter(j -> j.getSubscriptions().stream()
                        .anyMatch(s -> s.getSubscriber().equals(user)))
                .toList();
        if (subscribed.isEmpty()) {
            ConsoleMenu.printInfo("You are not subscribed to any journals.");
            ConsoleInput.waitForEnter();
            return;
        }
        for (int i = 0; i < subscribed.size(); i++) {
            System.out.printf("  [%d]  %s%n", i + 1, subscribed.get(i).getName());
        }
        int ji = ConsoleInput.readInt("\n  " + Messages.get("journal.select") + ": ", 1, subscribed.size()) - 1;
        subscribed.get(ji).unsubscribe(user);
        ConsoleMenu.printSuccess(Messages.get("journal.unsubscribed", subscribed.get(ji).getName()));
        ConsoleInput.waitForEnter();
    }

    private void viewJournalPapers(UniversitySystem system) {
        ConsoleMenu.printSection(Messages.get("journal.view_papers"));
        List<UniversityJournal> journals = system.getJournals();
        if (journals.isEmpty()) {
            ConsoleMenu.printInfo(Messages.get("journal.no_journals"));
            ConsoleInput.waitForEnter();
            return;
        }
        for (int i = 0; i < journals.size(); i++) {
            System.out.printf("  [%d]  %s%n", i + 1, journals.get(i).getName());
        }
        int ji = ConsoleInput.readInt("\n  " + Messages.get("journal.select") + ": ", 1, journals.size()) - 1;
        UniversityJournal journal = journals.get(ji);

        ConsoleMenu.printSection(Messages.get("journal.view_papers"));
        List<ResearchPaper> papers = journal.getPublishedPapers();
        if (papers.isEmpty()) {
            ConsoleMenu.printInfo(Messages.get("journal.no_papers"));
        } else {
            for (ResearchPaper p : papers) {
                System.out.println("  " + p.getCitation(university.enums.CitationFormat.PLAIN_TEXT));
            }
        }
        ConsoleInput.waitForEnter();
    }

    private void createJournal(UniversitySystem system) {
        ConsoleMenu.printSection(Messages.get("journal.create"));
        String name = ConsoleInput.readLine("  " + Messages.get("journal.name") + ": ");
        UniversityJournal journal = new UniversityJournal(name);
        system.addJournal(journal);
        ConsoleMenu.printSuccess(Messages.get("journal.created", name));
        ConsoleInput.waitForEnter();
    }
}
