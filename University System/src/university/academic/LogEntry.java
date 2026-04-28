package university.academic;

import java.io.Serializable;
import java.util.Date;

public class LogEntry implements Serializable {
    private String action;
    private String authorId;
    private String details;
    private Date timestamp;
    public LogEntry(String action, String authorId, String details){
        this.action = action;
        this.authorId = authorId;
        this.details = details;
        this.timestamp = new Date();
    }
    public String getAction() { 
    	return action; }
    public String getAuthorId() { 
    	return authorId; }
    public String getDetails() { 
    	return details; }
    public Date   getTimestamp(){ 
    	return timestamp; }

    @Override
    public String toString(){
        return String.format(
            "LogEntry { timestamp='%s', authorId='%s', action='%s', details='%s'}",
            timestamp.toString(), authorId, action, details
        );
    }
}
