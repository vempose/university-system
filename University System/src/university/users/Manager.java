package university.users;

import university.academic.AcademicReport;
import university.academic.Complaint;
import university.academic.EmployeeRequest;
import university.academic.Major;
import university.courses.Course;
import university.courses.CourseRequirement;
import university.courses.Enrollment;
import university.enums.CourseCategory;
import university.enums.ManagerType;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Manager extends Employee{
    public ManagerType type;
    public Manager(String name, String email, String password, double salary, ManagerType type){
        super(name, email, password, salary);
        this.type = type;
    }
    public void assignTeacherToCourse(Teacher teacher, Course course) {
        course.addInstructor(teacher);
        teacher.addCourse(course);
    }
    public CourseRequirement addCourseForRegistration(Course course, Major major, int yearOfStudy, CourseCategory category) {
        return new CourseRequirement(course, yearOfStudy, category);
    }
    public void approveRegistration(Enrollment enrollment) {
        enrollment.approve();
    }
    public void rejectRegistration(Enrollment enrollment, String reason) {
        enrollment.reject();
    }

    public List<Enrollment> viewPendingEnrollments() {
        return new ArrayList<>();
    }
    public AcademicReport createAcademicReport() {
        return new AcademicReport("Report by " + getName());
    }
    public void manageNews() {
    }
    public List<Student> viewStudentsSorted(Comparator<Student> comparator) {
        return new ArrayList<>();
    }
    public List<Teacher> viewTeachersSorted(Comparator<Teacher> comparator) {
        return new ArrayList<>();
    }

    public void signRequest(EmployeeRequest request) {
        request.sign();
    }
    public List<EmployeeRequest> viewEmployeeRequests() {
        return new ArrayList<>();
    }
    public List<Complaint> viewComplaints(){
        return new ArrayList<>();
    }

    @Override
    public String toString(){
        return "Manager{id=" + getId() + ", name=" + getName() + ", type=" + type + ", salary=" + getSalary() + "}";
    }
}
