package university.academic;

import university.enums.UrgencyLevel;
import university.users.Student;
import university.users.Teacher;

import java.io.Serializable;
import java.util.Date;

public class Complaint implements Serializable {
    private String text;
    private Date date;
    private UrgencyLevel urgency;
    private Teacher sender;
    private Student target;
    public Complaint(String text, UrgencyLevel urgency, Teacher sender, Student target){
        this.text = text;
        this.urgency = urgency;
        this.sender = sender;
        this.target = target;
        this.date = new Date();
    }

    public String getText(){ 
    	return text; }
    public Date getDate() { 
    	return date; }
    public UrgencyLevel getUrgency() { 
    	return urgency; }
    public Teacher getSender() {
    	return sender; }
    public Student getTarget() { 
    	return target; }

    @Override
    public String toString() {
        return "Complaint{from=" + sender.getName() + ", about=" + target.getName() + ", urgency=" + urgency + ", date=" + date + ", text=" + text + "}";
    }
}
