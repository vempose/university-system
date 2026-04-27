package university.service;

import java.time.LocalDate;
import java.util.Objects;
import university.domain.news.News;
import university.domain.research.ResearchPaper;
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
        this.system = Objects.requireNonNull(system, "system must not be null");
        this.researchService = Objects.requireNonNull(
            researchService,
            "researchService must not be null"
        );
    }

    public void publishNews(News news) {
        system.addNews(Objects.requireNonNull(news, "news must not be null"));
    }

    public News announcePaperPublication(ResearchPaper paper) {
        Objects.requireNonNull(paper, "paper must not be null");
        var news = new News(
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
        var topResearcher = researchService.getTopCitedResearcherOfYear(
            currentYear
        );
        var content = topResearcher
            .map(
                profile ->
                    "Top cited researcher this year has h-index: " +
                    profile.getHIndex()
            )
            .orElse("No research data available for this year.");
        var news = new News(
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
