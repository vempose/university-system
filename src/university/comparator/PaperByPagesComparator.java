package university.comparator;

import university.domain.research.ResearchPaper;

public final class PaperByPagesComparator implements PaperComparator {

    @Override
    public int compare(ResearchPaper first, ResearchPaper second) {
        return first.getPages().compareTo(second.getPages());
    }
}
