package university.news;
import university.enums.NewsTopic;
import university.interfaces.Researcher;
import university.research.ResearchPaper;
import university.users.User;

import java.util.ArrayList;
import java.util.List;

public class NewsService{
    private List<News> newsFeed;
    public NewsService(){
        this.newsFeed = new ArrayList<>();
    }
    public void onPaperPublished(ResearchPaper paper){
        String title = "New paper published: " +paper.getTitle();
        String content = "published in " + paper.getJournal() +". DOI:" + paper.getDoi();
        generateAutoNews(title, NewsTopic.RESEARCH, content);
    }
    public void onTopResearchersIdentified(List<Researcher> researchers) {
        StringBuilder sb = new StringBuilder("Top researchers: ");
        for (Researcher r : researchers) {
            if (r instanceof User u) sb.append(u.getName()).append(",");
        }
        generateAutoNews("top cited researchers", NewsTopic.RESEARCH, sb.toString());
    }

    private News generateAutoNews(String title, NewsTopic topic, String content) {
        News news = new News(title, content, topic);
        newsFeed.add(news);
        return news;
    }

    public void addNews(News news){
        newsFeed.add(news);
    }
    public List<News> getNewsFeed() {
        List<News> pinned = new ArrayList<>();
        List<News> rest = new ArrayList<>();
        for (News n : newsFeed) {
            if (n.isPinned()) pinned.add(n);
            else rest.add(n);
        }
        pinned.addAll(rest);
        return pinned;
    }

    @Override
    public String toString() {
        return "NewsService{totalNews=" + newsFeed.size() + "}";
    }
}
