package university.comparator;

import university.domain.research.ResearchPaper;

/// Compares research papers by their citation count (highest first).
public final class PaperByCitationsComparator implements PaperComparator {

    @Override
    /// Returns {@code b.citations - a.citations} so the paper with the most citations comes first.
    public int compare(ResearchPaper a, ResearchPaper b) {
        return Integer.compare(b.citations(), a.citations());
    }
}
