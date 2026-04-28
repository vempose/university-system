package university.users;

import university.academic.Complaint;
import university.communication.Message;
import university.courses.Course;
import university.courses.Enrollment;
import university.courses.Mark;
import university.enums.TeacherPosition;
import university.enums.UrgencyLevel;

import java.util.ArrayList;
import java.util.List;

public class Teacher extends Employee{
    private TeacherPosition position;
    private List<Course> courses;
    public Teacher(String name, String email, String password, double salary, TeacherPosition position){
        super(name, email, password, salary);
        this.position = position;
        this.courses = new ArrayList<>();
    }

    public List<Course> viewCourses() {
        return new ArrayList<>(courses);
    }
    public void addCourse(Course course) {
        courses.add(course);
    }
    public void manageCourse(Course course) {
        System.out.println("Managing course: " + course.getTitle());
    }
    public void putMark(Enrollment enrollment, Mark mark) {
        enrollment.setMark(mark);
    }
    public List<Student> viewStudents(Course course) {
        return new ArrayList<>();
    }
    public void sendComplaint(Student target, UrgencyLevel urgency, String text) {
        new Complaint(text, urgency, this, target);
    }
    public List<Message> viewMessages() {
        return receiveMessages();
    }
    public TeacherPosition getPosition() { 
    	return position; 
    	}
    public void setPosition(TeacherPosition position) { this.position = position; }

    @Override
    public String toString() {
        return "Teacher{id=" + getId() + ", name=" + getName() + ", position=" + position + ", salary=" + getSalary() + ", courses=" + courses.size() + "}";
    }
}
