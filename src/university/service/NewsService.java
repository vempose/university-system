package university.service;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;
import university.domain.news.News;
import university.domain.research.ResearchPaper;
import university.domain.research.ResearchProfile;
import university.enums.CitationFormat;
import university.enums.NewsTopic;
import university.system.UniversitySystem;

public class NewsService {

    private final UniversitySystem system;
    private final ResearchService researchService;

    public NewsService(
        UniversitySystem system,
        ResearchService researchService
    ) {
        this.system = system;
        this.researchService = researchService;
    }

    public void publishNews(News news) {
        system.addNews(news);
    }

    public News announcePaperPublication(ResearchPaper paper) {
        News news = new News(
            "New Research Paper Published: " + paper.getTitle(),
            "A new paper has been published: " +
                paper.getCitation(CitationFormat.PLAIN_TEXT),
            NewsTopic.RESEARCH
        );
        publishNews(news);
        return news;
    }

    public News announceTopCitedResearcher() {
        int currentYear = LocalDate.now().getYear();
        Optional<ResearchProfile> topResearcher = researchService.getTopCitedResearcherOfYear(
            currentYear
        );
        String content;
        if (topResearcher.isPresent()) {
            content = "Top cited researcher this year has h-index: " +
                topResearcher.get().getHIndex();
        } else {
            content = "No research data available for this year.";
        }
        News news = new News(
            "Top Cited Researcher of " + currentYear,
            content,
            NewsTopic.RESEARCH
        );
        publishNews(news);
        return news;
    }

    public void pinResearchNews() {
        system
            .getNewsList()
            .stream()
            .filter(n -> n.getTopic() == NewsTopic.RESEARCH)
            .forEach(News::pin);
    }
}
