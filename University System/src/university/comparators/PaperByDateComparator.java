package university.comparators;

import university.research.ResearchPaper;
import java.util.Comparator;

public class PaperByDateComparator implements Comparator<ResearchPaper> {
    @Override
    public int compare(ResearchPaper p1, ResearchPaper p2) {
        return p2.getDate().compareTo(p1.getDate()); 
    }
}
