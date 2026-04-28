package university.news;

import university.enums.NewsTopic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class News implements Serializable {
    private String title;
    private String content;
    private NewsTopic topic;
    private Date date;
    private boolean isPinned;
    private List<NewsComment> comments;
    public News(String title, String content, NewsTopic topic){
        this.title = title;
        this.content = content;
        this.topic = topic;
        this.date = new Date();
        this.isPinned = (topic == NewsTopic.RESEARCH);
        this.comments =new ArrayList<>();
    }
    public void pinNews(){ 
    	this.isPinned = true;}
    public void unpinNews() { 
    	this.isPinned = false;}
    public void addComment(NewsComment comment) { 
    	comments.add(comment); 
    	}
    public String getTitle() { 
    	return title; }
    public String getContent() { 
    	return content; }
    public NewsTopic getTopic() { 
    	return topic; }
    public Date getDate() { return date; }
    public boolean isPinned() { return isPinned; }
    public List<NewsComment> getComments() { 
    	return new ArrayList<>(comments); 
    	}

    @Override
    public String toString() {
        return "News{title=" + title + ", topic=" + topic + ", date=" + date + ", pinned=" + isPinned + "}";
    }
}
