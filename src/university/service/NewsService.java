package university.service;

import java.time.LocalDate;
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
        this.system = system;
        this.researchService = researchService;
    }

    public void publishNews(News news) {
        if (news.getTopic() == NewsTopic.RESEARCH) {
            news.pin();
        }
        system.addNews(news);
    }

    public News announcePaperPublication(ResearchPaper paper) {
        News news = new News(
            "New Research Paper: " + paper.getTitle(),
            "A new paper has been published: " +
                paper.getCitation(CitationFormat.PLAIN_TEXT),
            NewsTopic.RESEARCH
        );
        publishNews(news);
        return news;
    }

    public News announceTopCitedResearcher() {
        int year = LocalDate.now().getYear();
        News news = researchService.generateTopCitedResearcherNews(year);
        publishNews(news);
        return news;
    }

    public void pinAllResearchNews() {
        system
            .getNewsList()
            .stream()
            .filter(n -> n.getTopic() == NewsTopic.RESEARCH)
            .forEach(News::pin);
    }

    public void addOfficialEventNews(String title, String content) {
        News news = new News(title, content, NewsTopic.EVENT);
        system.addNews(news);
    }
}
