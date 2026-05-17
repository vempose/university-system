package university.comparator;

import university.domain.research.ResearchPaper;

/// Compares research papers by their publication date (oldest first).
public final class PaperByDateComparator implements PaperComparator {

    @Override
    /// Delegates to {@link java.time.LocalDate#compareTo} of the {@code publishDate}.
    public int compare(ResearchPaper a, ResearchPaper b) {
        return a.publishDate().compareTo(b.publishDate());
    }
}
