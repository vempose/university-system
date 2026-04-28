package university.news;

import university.users.User;

import java.io.Serializable;
import java.util.Date;

public class NewsComment implements Serializable {
    private String text;
    private User author;
    private Date date;
    public NewsComment(String text, User author){
        this.text = text;
        this.author = author;
        this.date  = new Date();
    }
    public void editComment(String newText) {
        this.text = newText;
    }
    public String getText() { 
    	return text; 
    	}
    public User   getAuthor() { 
    	return author; 
    	}
    public Date   getDate() { return date; }

    @Override
    public String toString(){
        return String.format(
            "NewsComment { author='%s', date=%s, text='%s' }",
            author.getName(), date.toString(), text
        );
    }
}
