package university.comparator;

import university.domain.research.ResearchPaper;

public final class PaperByCitationsComparator implements PaperComparator {

    @Override
    public int compare(ResearchPaper first, ResearchPaper second) {
        // descending: most-cited first
        return Integer.compare(second.getCitations(), first.getCitations());
    }
}
