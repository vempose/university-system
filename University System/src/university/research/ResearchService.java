package university.research;
import university.interfaces.Researcher;
import university.news.NewsService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ResearchService{
    private List<ResearchPaper> allPapers;
    private List<ResearchProject> allProjects;
    private NewsService newsService;
    public ResearchService(NewsService newsService){
        this.allPapers = new ArrayList<>();
        this.allProjects = new ArrayList<>();
        this.newsService = newsService;
    }

    public void publishPaper(ResearchPaper paper){
        allPapers.add(paper);
        if (newsService != null) {
            newsService.onPaperPublished(paper);
        }
    }
    public ResearchProject createProject(String topic) {
        ResearchProject project = new ResearchProject(topic);
        allProjects.add(project);
        return project;
    }
    public List<Researcher> getTopResearchers(int n) {
        List<Researcher> all = new ArrayList<>();
        for (ResearchPaper p : allPapers) {
            for (Researcher r : p.getAuthors()) {
                if (!all.contains(r)) all.add(r);
            }
        }
        all.sort((a, b) -> b.calculateHIndex() - a.calculateHIndex());
        return all.subList(0, Math.min(n, all.size()));
    }

    public void printAllPapers(Comparator<ResearchPaper> comparator) {
        List<ResearchPaper> sorted = new ArrayList<>(allPapers);
        sorted.sort(comparator);
        for (ResearchPaper p : sorted) {
            System.out.println(p);
        }
    }
    public List<ResearchPaper> getAllPapers() { 
    	return new ArrayList<>(allPapers); 
    	}
    public List<ResearchProject> getAllProjects() { 
    	return new ArrayList<>(allProjects); 
    	}

    @Override
    public String toString() {
        return "ResearchService{papers=" + allPapers.size() + ", projects=" + allProjects.size() + "}";
    }
}
