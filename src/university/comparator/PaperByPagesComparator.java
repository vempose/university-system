package university.comparator;

import university.domain.research.ResearchPaper;

public final class PaperByPagesComparator implements PaperComparator {

    @Override
    public int compare(ResearchPaper a, ResearchPaper b) {
        return Integer.compare(a.getPageCount(), b.getPageCount());
    }
}
