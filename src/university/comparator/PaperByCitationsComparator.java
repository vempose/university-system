package university.comparator;

import university.domain.research.ResearchPaper;

public final class PaperByCitationsComparator implements PaperComparator {

    @Override
    public int compare(ResearchPaper a, ResearchPaper b) {
        return Integer.compare(b.getCitations(), a.getCitations());
    }
}
