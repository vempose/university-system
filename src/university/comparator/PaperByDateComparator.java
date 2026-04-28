package university.comparator;

import university.domain.research.ResearchPaper;

public final class PaperByDateComparator implements PaperComparator {

    @Override
    public int compare(ResearchPaper a, ResearchPaper b) {
        return a.getPublishDate().compareTo(b.getPublishDate());
    }
}
