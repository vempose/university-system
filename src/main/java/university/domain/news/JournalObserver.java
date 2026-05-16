package university.domain.news;

import university.domain.research.ResearchPaper;

@FunctionalInterface
public interface JournalObserver {
    void onPaperPublished(ResearchPaper paper, UniversityJournal journal);
}
