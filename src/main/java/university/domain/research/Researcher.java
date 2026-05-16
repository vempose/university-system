package university.domain.research;

import java.util.Comparator;
import java.util.List;

public interface Researcher {
    int calculateHIndex();

    void printPapers(Comparator<ResearchPaper> comparator);

    List<ResearchPaper> getPapers();

    void publishPaper(ResearchPaper paper);

    void joinProject(ResearchProject project);

    List<ResearchProject> getProjects();
}
