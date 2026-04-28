package university.interfaces;
import university.research.ResearchPaper;
import university.research.ResearchProfile;

import java.util.Comparator;

public interface Researcher{
    int calculateHIndex();
    void printPapers(Comparator<ResearchPaper> comparator);
    ResearchProfile getResearchProfile();
}
