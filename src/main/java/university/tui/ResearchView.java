package university.tui;

import university.comparator.*;
import university.domain.research.*;
import university.domain.user.*;
import university.enums.CitationFormat;
import university.exception.NonResearcherJoinProjectException;
import university.service.ResearchService;
import university.system.UniversitySystem;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;

class ResearchView {

    private final Session session;
    private final ResearchService researchService;

    ResearchView(Session session, ResearchService researchService) {
        this.session = session;
        this.researchService = researchService;
    }

    void show() {
        User user = session.getCurrentUser();
        ResearchProfile profile = user.getResearchProfile();

        while (true) {
            LinkedHashMap<Integer, String> options = new LinkedHashMap<>();
            options.put(1, "Publish Research Paper");
            options.put(2, "View My Papers");
            options.put(3, "Print My Papers Sorted");
            options.put(4, "View My H-Index");
            options.put(5, "Join Research Project");
            options.put(6, "View My Projects");
            options.put(7, "View All Papers (University)");
            options.put(8, "Top Cited Researcher of Year");
            if (user instanceof Student s && s.getSchool() != null) {
                options.put(9, "Top Cited Researcher by School");
            }

            int choice = ConsoleMenu.showMenu("Research Panel", options, true, false);
            switch (choice) {
                case 0 -> { return; }
                case 1 -> publishPaper(profile);
                case 2 -> viewMyPapers(profile);
                case 3 -> printMyPapersSorted(profile);
                case 4 -> viewHIndex(profile);
                case 5 -> joinProject(profile);
                case 6 -> viewMyProjects(profile);
                case 7 -> viewAllPapers();
                case 8 -> topCitedOfYear();
                case 9 -> {
                    if (user instanceof Student s && s.getSchool() != null) {
                        topCitedBySchool(s.getSchool());
                    }
                }
            }
        }
    }

    private void publishPaper(ResearchProfile profile) {
        ConsoleMenu.printSection("Publish Research Paper");
        String title = ConsoleInput.readLine("  Title: ");
        String authors = ConsoleInput.readLine("  Authors: ");
        String journal = ConsoleInput.readLine("  Journal name: ");
        String pages = ConsoleInput.readLine("  Pages (e.g. 1-10): ");
        int pageCount = ConsoleInput.readInt("  Page count: ", 1, 1000);
        LocalDate date = ConsoleInput.readDate("  Publish date");
        String doi = ConsoleInput.readLine("  DOI: ");
        int citations = ConsoleInput.readInt("  Citations: ", 0, 1000000);

        ResearchPaper paper = new ResearchPaper(title, authors, journal, pages, pageCount, date, doi, citations);
        profile.publishPaper(paper);
        ConsoleMenu.printSuccess("Paper published: " + paper.getCitation(CitationFormat.PLAIN_TEXT));
        ConsoleInput.waitForEnter();
    }

    private void viewMyPapers(ResearchProfile profile) {
        ConsoleMenu.printSection("My Research Papers");
        List<ResearchPaper> papers = profile.getPapers();
        if (papers.isEmpty()) {
            ConsoleMenu.printInfo("No papers published yet.");
        } else {
            for (int i = 0; i < papers.size(); i++) {
                System.out.printf("  [%d]  %s%n", i + 1, papers.get(i).getCitation(CitationFormat.PLAIN_TEXT));
            }
        }
        ConsoleInput.waitForEnter();
    }

    private void printMyPapersSorted(ResearchProfile profile) {
        ConsoleMenu.printSection("Print My Papers Sorted");
        LinkedHashMap<Integer, String> sortOptions = new LinkedHashMap<>();
        sortOptions.put(1, "By Citations (most first)");
        sortOptions.put(2, "By Date (newest first)");
        sortOptions.put(3, "By Page Count (smallest first)");

        int sc = ConsoleMenu.showMenu("Sort By", sortOptions, true, false);
        if (sc == 0) return;

        switch (sc) {
            case 1 -> profile.printPapers(new PaperByCitationsComparator());
            case 2 -> profile.printPapers(new PaperByDateComparator());
            case 3 -> profile.printPapers(new PaperByPagesComparator());
        }
        ConsoleInput.waitForEnter();
    }

    private void viewHIndex(ResearchProfile profile) {
        ConsoleMenu.printSection("H-Index");
        int hIndex = profile.calculateHIndex();
        System.out.printf("  Your H-Index: %d%n", hIndex);
        System.out.printf("  Total Papers: %d%n", profile.getPapers().size());
        System.out.printf("  Total Projects: %d%n", profile.getProjects().size());
        ConsoleInput.waitForEnter();
    }

    private void joinProject(ResearchProfile profile) {
        ConsoleMenu.printSection("Join Research Project");
        String projectId = ConsoleInput.readLine("  Project ID: ");
        String topic = ConsoleInput.readLine("  Project topic: ");

        ResearchProject project = new ResearchProject(projectId, topic);
        try {
            project.addParticipant(profile);
            profile.joinProject(project);
            ConsoleMenu.printSuccess("Joined project: " + topic);
        } catch (NonResearcherJoinProjectException e) {
            ConsoleMenu.printError(e.getMessage());
        }
        ConsoleInput.waitForEnter();
    }

    private void viewMyProjects(ResearchProfile profile) {
        ConsoleMenu.printSection("My Research Projects");
        List<ResearchProject> projects = profile.getProjects();
        if (projects.isEmpty()) {
            ConsoleMenu.printInfo("No projects yet.");
        } else {
            for (ResearchProject p : projects) {
                System.out.printf("  [%s] %s | Participants: %d | Papers: %d%n",
                        p.getId(), p.getTopic(), p.getParticipants().size(), p.getPublishedPapers().size());
            }
        }
        ConsoleInput.waitForEnter();
    }

    private void viewAllPapers() {
        ConsoleMenu.printSection("All University Papers");
        LinkedHashMap<Integer, String> sortOptions = new LinkedHashMap<>();
        sortOptions.put(1, "By Citations (most first)");
        sortOptions.put(2, "By Date (newest first)");
        sortOptions.put(3, "By Page Count");

        int sc = ConsoleMenu.showMenu("Sort By", sortOptions, true, false);
        if (sc == 0) return;

        switch (sc) {
            case 1 -> researchService.printAllPapers(new PaperByCitationsComparator());
            case 2 -> researchService.printAllPapers(new PaperByDateComparator());
            case 3 -> researchService.printAllPapers(new PaperByPagesComparator());
        }
        ConsoleInput.waitForEnter();
    }

    private void topCitedOfYear() {
        ConsoleMenu.printSection("Top Cited Researcher");
        int year = ConsoleInput.readInt("  Year: ", 2000, 2100);
        researchService.printTopCitedResearcherOfYear(year);
        ConsoleInput.waitForEnter();
    }

    private void topCitedBySchool(university.domain.academic.School school) {
        ConsoleMenu.printSection("Top Cited Researcher in " + school.getName());
        researchService.printTopCitedResearcherBySchool(school);
        ConsoleInput.waitForEnter();
    }
}
