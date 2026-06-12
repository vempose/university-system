package university.system;

import university.academic.LogEntry;
import university.academic.TechSupportRequest;
import university.courses.Course;
import university.news.News;
import university.news.NewsService;
import university.news.UniversityJournal;
import university.research.ResearchProject;
import university.research.ResearchService;
import university.users.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UniversitySystem implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String SAVE_FILE = "university_data.ser";
    private static UniversitySystem instance;
    private List<User> users;
    private List<Course> courses;
    private UniversityJournal journal;
    private List<ResearchProject> researchProjects;
    private List<News> news;
    private List<TechSupportRequest> requests;
    private List<LogEntry> logs;
    private NewsService newsService;
    private ResearchService researchService;
    private UniversitySystem() {
        this.users = new ArrayList<>();
        this.courses = new ArrayList<>();
        this.researchProjects = new ArrayList<>();
        this.news = new ArrayList<>();
        this.requests = new ArrayList<>();
        this.logs = new ArrayList<>();
        this.newsService = new NewsService();
        this.researchService = new ResearchService(newsService);
        this.journal = new UniversityJournal("University Research Journal");
    }
    public static UniversitySystem getInstance() {
        if (instance == null) {
            synchronized (UniversitySystem.class) {
                if (instance == null) {
                    instance = new UniversitySystem();
                }
            }
        }
        return instance;
    }
    public User login(String id, String password) {
        for (User user : users) {
            if (user.getId().equals(id) && user.login(user.getEmail(), password)) {
                log(user.getId(), "LOGIN", "User logged in.");
                return user;
            }
        }
        return null;
    }
    public void log(String authorId, String action, String details) {
        logs.add(new LogEntry(action, authorId, details));
    }

    public void serialize() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SAVE_FILE))) {
            oos.writeObject(this);
        } catch (IOException e) {
            System.out.println("Save failed: " + e.getMessage());
        }
    }

    public static void deserialize() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SAVE_FILE))) {
            instance = (UniversitySystem) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Load failed: " + e.getMessage());
        }
    }

    public void addUser(User user) { 
    	users.add(user); }
    public void addCourse(Course course) { 
    	courses.add(course); }
    public void addNews(News n) { 
    	news.add(n); }
    public void addRequest(TechSupportRequest req) { 
    	requests.add(req); }
    public void addResearchProject(ResearchProject rp) { 
    	researchProjects.add(rp); }
    public List<User> getUsers() { 
    	return new ArrayList<>(users); }
    public List<Course> getCourses() { 
    	return new ArrayList<>(courses); }
    public List<ResearchProject> getResearchProjects() { 
    	return new ArrayList<>(researchProjects); }
    public List<News> getNews() { 
    	return new ArrayList<>(news); }
    public List<TechSupportRequest> getRequests() { 
    	return new ArrayList<>(requests); }
    public List<LogEntry> getLogs() { 
    	return new ArrayList<>(logs); }
    public NewsService getNewsService() { 
    	return newsService; }
    public ResearchService getResearchService() { 
    	return researchService; }
    public UniversityJournal getJournal() { 
    return journal; }

    @Override
    public String toString() {
        return "UniversitySystem{users=" + users.size() + ", courses=" + courses.size() + ", researchProjects=" + researchProjects.size() + ", news=" + news.size() + ", requests=" + requests.size() + ", logs=" + logs.size() + "}";
    }
}
