package university.academic;

import university.enums.RequestStatus;

import java.io.Serializable;
import java.util.Date;

public class TechSupportRequest implements Serializable{
    private String description;
    private Date timestamp;
    private RequestStatus status;
    private String responseText;
    public TechSupportRequest(String description){
        this.description = description;
        this.timestamp = new Date();
        this.status = RequestStatus.NEW;
        this.responseText = "";
    }
    public void updateStatus(RequestStatus newStatus){
        this.status = newStatus;
    }
    public void setResponse(String text){
        this.responseText = text;
    }
    public String getDescription() { 
    	return description; }
    public Date getTimestamp() { 
    	return timestamp; }
    public RequestStatus getStatus() { 
    	return status; }
    public String getResponseText() { 
    	return responseText; }

    @Override
    public String toString(){
        return "TechSupportRequest{description=" + description + ", status=" + status + ", timestamp=" + timestamp + "}";
    }
}
