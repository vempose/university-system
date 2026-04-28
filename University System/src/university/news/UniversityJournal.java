package university.news;

import university.research.ResearchPaper;
import university.users.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UniversityJournal implements Serializable {
    private String journalName;
    private List<User> subscribers;
    private List<JournalSubscription> subscriptions;
    private List<ResearchPaper> publishedPapers;
    public UniversityJournal(String journalName){
        this.journalName = journalName;
        this.subscribers = new ArrayList<>();
        this.subscriptions = new ArrayList<>();
        this.publishedPapers = new ArrayList<>();
    }

    public void subscribe(User user) {
        if (!subscribers.contains(user)) {
            subscribers.add(user);
            subscriptions.add(new JournalSubscription(user, this));
        }
    }
    public void unsubscribe(User user) {
        subscribers.remove(user);
        subscriptions.removeIf(s ->s.getUser().equals(user));
    }
    public void publishIssue(ResearchPaper paper) {
        publishedPapers.add(paper);
        for (User user : subscribers) {
            System.out.println("Notification to " + user.getName() + ": new paper -" + paper.getTitle());
        }
    }

    public String getJournalName() { 
    	return journalName; }
    public List<User> getSubscribers() { 
    	return new ArrayList<>(subscribers); }
    public List<ResearchPaper> getPublishedPapers() { 
    	return new ArrayList<>(publishedPapers); }

    @Override
    public String toString() {
        return "UniversityJournal{name=" + journalName + ",subscribers=" + subscribers.size()
                + ",papers=" + publishedPapers.size() + "}";
    }
}
