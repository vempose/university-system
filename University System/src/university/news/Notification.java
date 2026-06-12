package university.news;

import java.io.Serializable;
import java.util.Date;

public class Notification implements Serializable {
    private String message;
    private Date timestamp;
    public Notification(String message) {
        this.message   = message;
        this.timestamp = new Date();
    }
    public String getMessage() { 
    	return message; 
    	}
    public Date   getTimestamp() { 
    	return timestamp; 
    	}

    @Override
    public String toString() {
        return String.format(
            "Notification { timestamp=%s, message='%s' }",timestamp.toString(), message);
    }
}
