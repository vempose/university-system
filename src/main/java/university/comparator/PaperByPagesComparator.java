package university.comparator;

import university.domain.research.ResearchPaper;

/// Compares research papers by their page count (shortest first).
public final class PaperByPagesComparator implements PaperComparator {

    @Override
    /// Returns {@code a.pageCount - b.pageCount}.
    public int compare(ResearchPaper a, ResearchPaper b) {
        return Integer.compare(a.pageCount(), b.pageCount());
    }
}
