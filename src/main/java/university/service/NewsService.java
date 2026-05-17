package university.service;

import university.domain.news.News;
import university.domain.research.ResearchPaper;
import university.enums.CitationFormat;
import university.enums.NewsTopic;
import university.system.UniversitySystem;

import java.time.LocalDate;

/// Handles news in the system — publishing, pinning, comments, and filtering by topic.
///
/// Research news gets pinned automatically. Also works with ResearchService
/// to create news about top-cited researchers.
public class NewsService {

    private final UniversitySystem system;
    private final ResearchService researchService;

    /// Creates a NewsService.
    ///
    /// @param system           the main system instance
    /// @param researchService  service used for research-related news
    public NewsService(
            UniversitySystem system,
            ResearchService researchService
    ) {
        this.system = system;
        this.researchService = researchService;
    }

    /// Publishes a news article. Research news gets pinned right away.
    ///
    /// @param news the news to publish
    public void publishNews(News news) {
        if (news.getTopic() == NewsTopic.RESEARCH) {
            news.pin();
        }
        system.addNews(news);
    }

    /// Creates a news item when a new research paper is published.
    ///
    /// @param paper the published paper
    /// @return the generated news item
    public News announcePaperPublication(ResearchPaper paper) {
        News news = new News(
                "New Research Paper: " + paper.title(),
                "A new paper has been published: " +
                        paper.getCitation(CitationFormat.PLAIN_TEXT),
                NewsTopic.RESEARCH
        );
        publishNews(news);
        return news;
    }

    /// Announces the top-cited researcher of the current year.
    ///
    /// @return the news item
    public News announceTopCitedResearcher() {
        int year = LocalDate.now().getYear();
        News news = researchService.generateTopCitedResearcherNews(year);
        publishNews(news);
        return news;
    }

    /// Pins every news item with a RESEARCH topic.
    public void pinAllResearchNews() {
        system
                .getNewsList()
                .stream()
                .filter(n -> n.getTopic() == NewsTopic.RESEARCH)
                .forEach(News::pin);
    }

    /// Adds an official event news item.
    ///
    /// @param title   the headline
    /// @param content the body text
    public void addOfficialEventNews(String title, String content) {
        News news = new News(title, content, NewsTopic.EVENT);
        system.addNews(news);
    }
}
