package university.news;

import university.users.User;

import java.io.Serializable;
import java.util.Date;

public class JournalSubscription implements Serializable{
    private User user;
    private UniversityJournal journal;
    private Date subscriptionDate;
    private boolean isActive;
    public JournalSubscription(User user, UniversityJournal journal){
        this.user = user;
        this.journal = journal;
        this.subscriptionDate = new Date();
        this.isActive =true;
    }
    public User getUser() { 
    	return user; }
    public UniversityJournal getJournal() { return journal; }
    public Date getSubscriptionDate() { 
    	return subscriptionDate; }
    public boolean isActive() { 
    	return isActive; }
    public void deactivate() { 
    	this.isActive = false; 
    	}

    @Override
    public String toString() {
        return String.format(
            "JournalSubscription { user='%s', journal='%s', active=%b, since=%s }",
            user.getName(), journal.getJournalName(), isActive, subscriptionDate.toString()
        );
    }
}
