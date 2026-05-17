package university.tui;

import university.comparator.*;
import university.domain.research.*;
import university.domain.user.*;
import university.enums.CitationFormat;
import university.exception.NonResearcherJoinProjectException;
import university.service.ResearchService;

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
            options.put(1, Messages.get("research.publish"));
            options.put(2, Messages.get("research.view_papers"));
            options.put(3, Messages.get("research.print_sorted"));
            options.put(4, Messages.get("research.view_hindex"));
            options.put(5, Messages.get("research.join_project"));
            options.put(6, Messages.get("research.view_projects"));
            options.put(7, Messages.get("research.view_all"));
            options.put(8, Messages.get("research.top_year"));
            if (user instanceof Student s && s.getSchool() != null) {
                options.put(9, Messages.get("research.top_school"));
            }

            int choice = ConsoleMenu.showMenu(Messages.get("research.title"), options, true, false);
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
        ConsoleMenu.printSection(Messages.get("research.publish"));
        String title = ConsoleInput.readLine("  " + Messages.get("research.paper_title") + ": ");
        String authors = ConsoleInput.readLine("  " + Messages.get("research.authors") + ": ");
        String journal = ConsoleInput.readLine("  " + Messages.get("research.journal") + ": ");
        String pages = ConsoleInput.readLine("  " + Messages.get("research.pages") + ": ");
        int pageCount = ConsoleInput.readInt("  " + Messages.get("research.page_count") + ": ", 1, 1000);
        LocalDate date = ConsoleInput.readDate("  " + Messages.get("research.publish_date"));
        String doi = ConsoleInput.readLine("  " + Messages.get("research.doi") + ": ");
        int citations = ConsoleInput.readInt("  " + Messages.get("research.citations") + ": ", 0, 1000000);

        ResearchPaper paper = new ResearchPaper(title, authors, journal, pages, pageCount, date, doi, citations);
        profile.publishPaper(paper);
        ConsoleMenu.printSuccess(Messages.get("research.paper_published",
                paper.getCitation(CitationFormat.PLAIN_TEXT)));
        ConsoleInput.waitForEnter();
    }

    private void viewMyPapers(ResearchProfile profile) {
        ConsoleMenu.printSection(Messages.get("research.view_papers"));
        List<ResearchPaper> papers = profile.getPapers();
        if (papers.isEmpty()) {
            ConsoleMenu.printInfo(Messages.get("research.no_papers"));
        } else {
            for (int i = 0; i < papers.size(); i++) {
                System.out.printf("  [%d]  %s%n", i + 1,
                        papers.get(i).getCitation(CitationFormat.PLAIN_TEXT));
            }
        }
        ConsoleInput.waitForEnter();
    }

    private void printMyPapersSorted(ResearchProfile profile) {
        ConsoleMenu.printSection(Messages.get("research.print_sorted"));
        LinkedHashMap<Integer, String> sortOptions = new LinkedHashMap<>();
        sortOptions.put(1, Messages.get("research.sort_citations"));
        sortOptions.put(2, Messages.get("research.sort_citations_asc"));
        sortOptions.put(3, Messages.get("research.sort_date"));
        sortOptions.put(4, Messages.get("research.sort_date_asc"));
        sortOptions.put(5, Messages.get("research.sort_pages"));
        sortOptions.put(6, Messages.get("research.sort_pages_desc"));

        int sc = ConsoleMenu.showMenu(Messages.get("manager.sort_by"), sortOptions, true, false);
        if (sc == 0) return;

        switch (sc) {
            case 1 -> profile.printPapers(new PaperByCitationsComparator());
            case 2 -> profile.printPapers(new PaperByCitationsComparator().reversed());
            case 3 -> profile.printPapers(new PaperByDateComparator());
            case 4 -> profile.printPapers(new PaperByDateComparator().reversed());
            case 5 -> profile.printPapers(new PaperByPagesComparator());
            case 6 -> profile.printPapers(new PaperByPagesComparator().reversed());
        }
        ConsoleInput.waitForEnter();
    }

    private void viewHIndex(ResearchProfile profile) {
        ConsoleMenu.printSection(Messages.get("research.view_hindex"));
        int hIndex = profile.calculateHIndex();
        System.out.println("  " + Messages.get("research.hindex_result", hIndex));
        System.out.println("  " + Messages.get("research.total_papers", profile.getPapers().size()));
        System.out.println("  " + Messages.get("research.total_projects", profile.getProjects().size()));
        ConsoleInput.waitForEnter();
    }

    private void joinProject(ResearchProfile profile) {
        ConsoleMenu.printSection(Messages.get("research.join_project"));
        String projectId = ConsoleInput.readLine("  " + Messages.get("research.project_id") + ": ");
        String topic = ConsoleInput.readLine("  " + Messages.get("research.project_topic") + ": ");

        ResearchProject project = new ResearchProject(projectId, topic);
        try {
            project.addParticipant(profile);
            profile.joinProject(project);
            ConsoleMenu.printSuccess(Messages.get("research.joined_project", topic));
        } catch (NonResearcherJoinProjectException e) {
            ConsoleMenu.printError(e.getMessage());
        }
        ConsoleInput.waitForEnter();
    }

    private void viewMyProjects(ResearchProfile profile) {
        ConsoleMenu.printSection(Messages.get("research.view_projects"));
        List<ResearchProject> projects = profile.getProjects();
        if (projects.isEmpty()) {
            ConsoleMenu.printInfo(Messages.get("research.no_projects"));
        } else {
            for (ResearchProject p : projects) {
                System.out.printf("  [%s] %s | Participants: %d | Papers: %d%n",
                        p.getId(), p.getTopic(), p.getParticipants().size(), p.getPublishedPapers().size());
            }
        }
        ConsoleInput.waitForEnter();
    }

    private void viewAllPapers() {
        ConsoleMenu.printSection(Messages.get("research.view_all"));
        LinkedHashMap<Integer, String> sortOptions = new LinkedHashMap<>();
        sortOptions.put(1, Messages.get("research.sort_citations"));
        sortOptions.put(2, Messages.get("research.sort_citations_asc"));
        sortOptions.put(3, Messages.get("research.sort_date"));
        sortOptions.put(4, Messages.get("research.sort_date_asc"));
        sortOptions.put(5, Messages.get("research.sort_pages"));
        sortOptions.put(6, Messages.get("research.sort_pages_desc"));

        int sc = ConsoleMenu.showMenu(Messages.get("manager.sort_by"), sortOptions, true, false);
        if (sc == 0) return;

        switch (sc) {
            case 1 -> researchService.printAllPapers(new PaperByCitationsComparator());
            case 2 -> researchService.printAllPapers(new PaperByCitationsComparator().reversed());
            case 3 -> researchService.printAllPapers(new PaperByDateComparator());
            case 4 -> researchService.printAllPapers(new PaperByDateComparator().reversed());
            case 5 -> researchService.printAllPapers(new PaperByPagesComparator());
            case 6 -> researchService.printAllPapers(new PaperByPagesComparator().reversed());
        }
        ConsoleInput.waitForEnter();
    }

    private void topCitedOfYear() {
        ConsoleMenu.printSection(Messages.get("research.top_year"));
        int year = ConsoleInput.readInt("  Year: ", 2000, 2100);
        researchService.printTopCitedResearcherOfYear(year);
        ConsoleInput.waitForEnter();
    }

    private void topCitedBySchool(university.domain.academic.School school) {
        ConsoleMenu.printSection(Messages.get("research.top_school"));
        researchService.printTopCitedResearcherBySchool(school);
        ConsoleInput.waitForEnter();
    }
}
