package university.courses;

import university.enums.RegistrationStatus;

import java.io.Serializable;
import java.util.Date;

public class Enrollment implements Serializable {
    private Course course;
    private String semester;
    private int attempt;
    private RegistrationStatus status;
    private Date registrationDate;
    private Mark mark;
    public Enrollment(Course course, String semester) {
        this.course = course;
        this.semester = semester;
        this.attempt= 1;
        this.status = RegistrationStatus.PENDING;
        this.registrationDate =new Date();
    }
    public void approve() { 
    	this.status = RegistrationStatus.APPROVED;}
    public void reject() { 
    	this.status = RegistrationStatus.REJECTED;}
    public void drop() { 
    	this.status = RegistrationStatus.DROPPED;}

    public boolean isActive() {
        return status == RegistrationStatus.APPROVED;
    }

    public Course getCourse(){ 
    	return course; }
    public String getSemester() { 
    	return semester; }
    public int getAttempt() { 
    	return attempt; }
    public void setAttempt(int a) { 
    	this.attempt = a; }
    public RegistrationStatus getStatus() { 
    	return status; }
    public Date getRegistrationDate() { 
    	return registrationDate; }
    public Mark getMark() { 
    	return mark; }
    public void setMark(Mark mark){ 
    	this.mark = mark; }

    @Override
    public String toString() {
        return String.format(
            "Enrollment { course='%s', semester='%s', attempt=%d, status=%s, date=%s }",
            course.getTitle(), semester, attempt, status.getDisplayName(), registrationDate.toString()
        );
    }
}
