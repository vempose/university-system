package university.tui;

import university.tui.Messages;
import university.domain.news.UniversityJournal;
import university.domain.research.ResearchPaper;
import university.domain.user.User;
import university.enums.CitationFormat;
import university.system.UniversitySystem;

import java.util.LinkedHashMap;
import java.util.List;

/// Journal menu — subscribe/unsubscribe to journals, view papers,
/// and create new journals.
class JournalView {

    private final Session session;

    JournalView(Session session) {
        this.session = session;
    }

    /// Shows the journal menu and handles user choices.
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
                        "  %s | %s: %d | %s: %d%n",
                        j.getName(),
                        Messages.get("journal.subscribers_label"), j.getSubscriptions().size(),
                        Messages.get("journal.papers_label"), j.getPublishedPapers().size()
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
                    : Messages.get("journal.subscribed_all"));
            ConsoleInput.waitForEnter();
            return;
        }
        UniversityJournal selected = ConsoleMenu.pickFromList(available,
                UniversityJournal::getName, Messages.get("journal.select"));
        selected.subscribe(user);
        ConsoleMenu.printSuccess(Messages.get("journal.subscribed", selected.getName()));
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
            ConsoleMenu.printInfo(Messages.get("journal.not_subscribed"));
            ConsoleInput.waitForEnter();
            return;
        }
        UniversityJournal selected = ConsoleMenu.pickFromList(subscribed,
                UniversityJournal::getName, Messages.get("journal.select"));
        selected.unsubscribe(user);
        ConsoleMenu.printSuccess(Messages.get("journal.unsubscribed", selected.getName()));
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
        UniversityJournal journal = ConsoleMenu.pickFromList(journals,
                UniversityJournal::getName, Messages.get("journal.select"));

        ConsoleMenu.printSection(Messages.get("journal.view_papers"));
        List<ResearchPaper> papers = journal.getPublishedPapers();
        if (papers.isEmpty()) {
            ConsoleMenu.printInfo(Messages.get("journal.no_papers"));
        } else {
            for (ResearchPaper p : papers) {
                System.out.println("  " + p.getCitation(CitationFormat.PLAIN_TEXT));
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
