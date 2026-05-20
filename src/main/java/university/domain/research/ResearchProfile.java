package university.domain.research;

import university.enums.CitationFormat;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/// Tracks a researcher's publications and computes their h-index.
///
/// Every `Employee` who is also a researcher has one of these.
public class ResearchProfile implements Researcher, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private final List<ResearchPaper> papers;
    private final List<ResearchProject> projects;
    private int hIndex;

    /// Creates an empty research profile.
    public ResearchProfile() {
        this.hIndex = 0;
        this.papers = new ArrayList<>();
        this.projects = new ArrayList<>();
    }

    /// Adds a paper to the researcher's list if not null.
    @Override
    public void publishPaper(ResearchPaper paper) {
        if (paper != null) papers.add(paper);
    }

    /// Joins a project if it's not null and not already joined.
    @Override
    public void joinProject(ResearchProject project) {
        if (project != null && !projects.contains(project)) {
            projects.add(project);
        }
    }

    /// Recalculates the h-index from all papers and returns it.
    ///
    /// H-index = the largest h such that h papers have at least h citations.
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

    /// Prints all papers in the given order using PLAIN_TEXT format.
    @Override
    public void printPapers(Comparator<ResearchPaper> comparator) {
        getSortedPapers(comparator)
                .forEach(p -> System.out.println(p.getCitation(CitationFormat.PLAIN_TEXT)));
    }

    /// Returns papers sorted by the given comparator.
    @Override
    public List<ResearchPaper> getSortedPapers(Comparator<ResearchPaper> comparator) {
        return papers.stream().sorted(comparator).toList();
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

    /// Returns a summary of the profile.
    @Override
    public String toString() {
        return "ResearchProfile{hIndex=%d, papers=%d, projects=%d}".formatted(
                hIndex,
                papers.size(),
                projects.size()
        );
    }
}
