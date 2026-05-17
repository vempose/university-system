package university.domain.research;

import java.util.Comparator;
import java.util.List;

/// Interface for anyone who does research.
///
/// Researchers can publish papers, join projects, and have an h-index.
public interface Researcher {
    /// Calculates the h-index based on current papers.
    int calculateHIndex();

    /// Prints papers sorted by the given comparator.
    void printPapers(Comparator<ResearchPaper> comparator);

    /// Returns papers sorted by the given comparator.
    List<ResearchPaper> getSortedPapers(Comparator<ResearchPaper> comparator);

    /// Returns all papers (read-only).
    List<ResearchPaper> getPapers();

    /// Adds a paper to this researcher's publication list.
    void publishPaper(ResearchPaper paper);

    /// Adds this researcher to a project.
    void joinProject(ResearchProject project);

    /// Returns all projects this researcher is part of.
    List<ResearchProject> getProjects();
}
