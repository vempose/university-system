package university.courses;

import university.enums.CourseCategory;

import java.io.Serializable;

public class CourseRequirement implements Serializable {
    private int yearOfStudy;
    private CourseCategory category;
    private Course course;
    public CourseRequirement(Course course, int yearOfStudy, CourseCategory category){
        this.course = course;
        this.yearOfStudy = yearOfStudy;
        this.category = category;
    }
    public int getYearOfStudy() { 
    	return yearOfStudy; }
    public CourseCategory getCategory() { 
    	return category; }
    public Course getCourse() { 
    	return course; }

    public void setYearOfStudy(int y){ 
    	this.yearOfStudy = y; }
    public void setCategory(CourseCategory c){ 
    	this.category = c; }

    @Override
    public String toString() {
        return String.format(
            "CourseRequirement { course='%s', year=%d, category=%s}",
            course.getTitle(), yearOfStudy, category.getDisplayName()
        );
    }
}
