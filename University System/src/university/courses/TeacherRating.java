package university.courses;
import university.users.Teacher;

import java.io.Serializable;
import java.util.Date;

public class TeacherRating implements Serializable{
    private Teacher teacher;
    private double score;
    private String comment;
    private Date date;
    public TeacherRating(Teacher teacher, double score, String comment){
        this.teacher = teacher;
        this.score = Math.max(0, Math.min(5, score));
        this.comment = comment;
        this.date =new Date();
    }
    public Teacher getTeacher() { 
    	return teacher; }
    public double  getScore() { 
    	return score; }
    public String  getComment() { 
    	return comment; }
    public Date    getDate() { 
    	return date; }

    @Override
    public String toString() {
        return String.format(
            "TeacherRating { teacher='%s', score=%.1f/5.0, comment='%s', date=%s}", teacher.getName(), score, comment, date.toString()
        );
    }
}
