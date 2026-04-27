package university.comparator;

import university.domain.research.ResearchPaper;

public class PaperByDateComparator implements PaperComparator {

    @Override
    public int compare(ResearchPaper first, ResearchPaper second) {
        return first.getPublishDate().compareTo(second.getPublishDate());
    }
}
