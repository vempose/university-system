package university.courses;

import university.users.Teacher;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Course implements Serializable{
    private String courseCode;
    private String title;
    private int credits;
    private List<Teacher> instructors;
    private List<Lesson> lessons;
    public Course(String courseCode, String title, int credits){
        this.courseCode = courseCode;
        this.title = title;
        this.credits = credits;
        this.instructors = new ArrayList<>();
        this.lessons = new ArrayList<>();
    }
    public void addInstructor(Teacher teacher){ 
    	instructors.add(teacher); }
    public void addLesson(Lesson lesson) { 
    	lessons.add(lesson); }
    public String getCourseCode(){ 
    	return courseCode; }
    public String getTitle(){ 
    	return title; }
    public int getCredits(){ 
    	return credits; }
    public List<Teacher> getInstructors(){ 
    	return new ArrayList<>(instructors);
    	}
    public List<Lesson> getLessons(){ 
    	return new ArrayList<>(lessons); }
    public void setTitle(String title){ 
    	this.title = title; }
    public void setCredits(int credits){ 
    	this.credits = credits; }

    @Override
    public String toString() {
        return "Course{code=" + courseCode + ", title=" + title + ", credits=" + credits + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Course c)) return false;
        return Objects.equals(courseCode, c.courseCode);
    }

    @Override
    public int hashCode() { 
    	return Objects.hashCode(courseCode); }
}
