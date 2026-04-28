package university.domain.research;

import university.enums.CitationFormat;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ResearchProfile implements Researcher, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private final List<ResearchPaper> papers;
    private final List<ResearchProject> projects;
    private int hIndex;

    public ResearchProfile() {
        this.hIndex = 0;
        this.papers = new ArrayList<>();
        this.projects = new ArrayList<>();
    }

    @Override
    public void publishPaper(ResearchPaper paper) {
        if (paper != null) papers.add(paper);
    }

    @Override
    public void joinProject(ResearchProject project) {
        if (project != null) projects.add(project);
    }

    @Override
    public int calculateHIndex() {
        List<Integer> sorted = papers
                .stream()
                .mapToInt(ResearchPaper::citations)
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

    @Override
    public List<ResearchProject> getProjects() {
        return Collections.unmodifiableList(projects);
    }

    public int getHIndex() {
        return hIndex;
    }

    @Override
    public String toString() {
        return "ResearchProfile{hIndex=%d, papers=%d, projects=%d}".formatted(
                hIndex,
                papers.size(),
                projects.size()
        );
    }
}
