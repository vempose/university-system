package university.domain.news;

import university.domain.research.ResearchPaper;

/// Callback interface for users who subscribe to a journal.
///
/// When a journal publishes a new paper, all subscribers that implement
/// this interface get notified directly.
@FunctionalInterface
public interface JournalObserver {
    /// Called when a journal publishes a new paper.
    ///
    /// @param paper the paper that was just published
    /// @param journal the journal that published it
    void onPaperPublished(ResearchPaper paper, UniversityJournal journal);
}
