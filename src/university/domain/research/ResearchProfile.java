package university.domain.research;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import university.enums.CitationFormat;

public class ResearchProfile implements Researcher {

    private int hIndex;
    private final List<ResearchPaper> papers;
    private final List<ResearchProject> projects;

    public ResearchProfile() {
        this.hIndex = 0;
        this.papers = new ArrayList<>();
        this.projects = new ArrayList<>();
    }

    public void publishPaper(ResearchPaper paper) {
        if (paper != null) papers.add(paper);
    }

    public void joinProject(ResearchProject project) {
        if (project != null) projects.add(project);
    }

    @Override
    public int calculateHIndex() {
        var sorted = papers
            .stream()
            .mapToInt(ResearchPaper::getCitations)
            .boxed()
            .sorted(Comparator.reverseOrder())
            .toList();

        int h = 0;
        for (int i = 0; i < sorted.size(); i++) {
            if (sorted.get(i) >= i + 1) h = i + 1;
            else break;
        }
        hIndex = h;
        return hIndex;
    }

    @Override
    public void printPapers(Comparator<ResearchPaper> comparator) {
        papers
            .stream()
            .sorted(comparator)
            .map(p -> p.getCitation(CitationFormat.PLAIN_TEXT))
            .forEach(System.out::println);
    }

    @Override
    public List<ResearchPaper> getPapers() {
        return Collections.unmodifiableList(papers);
    }

    public int getHIndex() {
        return hIndex;
    }

    public List<ResearchProject> getProjects() {
        return Collections.unmodifiableList(projects);
    }

        @Override
    public String toString() {
        return "ResearchProfile{hIndex=%d, papers=%d, projects=%d}".formatted(hIndex, papers.size(), projects.size());
    }
}
