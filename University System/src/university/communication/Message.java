package university.communication;

import university.users.Employee;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {
    private String text;
    private Employee sender;
    private Employee receiver;
    private Date timestamp;
    public Message(String text, Employee sender, Employee receiver){
        this.text = text;
        this.sender = sender;
        this.receiver = receiver;
        this.timestamp =new Date();
    }
    public String getText() { 
    	return text; }
    public Employee getSender() { 
    	return sender; }
    public Employee getReceiver() { 
    	return receiver; }
    public Date getTimestamp() { 
    	return timestamp; }

    @Override
    public String toString(){
        return "Message{from=" + sender.getName() + ", to=" + receiver.getName() + ", date=" + timestamp + ", text=" + text + "}";
    }
}
