package university.domain.user;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import university.domain.academic.*;
import university.domain.communication.EmployeeRequest;
import university.domain.news.News;
import university.domain.support.AcademicReport;
import university.enums.CourseCategory;
import university.enums.Language;
import university.enums.ManagerType;

public class Manager extends Employee {

    @Serial
    private static final long serialVersionUID = 1L;

    private final List<News> managedNews = new ArrayList<>();
    private final List<EmployeeRequest> viewedRequests = new ArrayList<>();
    private final List<AcademicReport> createdReports = new ArrayList<>();
    private ManagerType type;

    public Manager(
        String id,
        String name,
        String email,
        String password,
        Language language,
        double salary,
        ManagerType type
    ) {
        super(id, name, email, password, language, salary);
        this.type = type;
    }

    public void assignTeacherToCourse(
        Teacher teacher,
        Course course,
        Lesson lesson
    ) {
        lesson.setInstructor(teacher);
        teacher.addAssignedCourse(course);
    }

    public void approveRegistration(Enrollment enrollment) {
        enrollment.approve();
    }

    public CourseRequirement addCourseForRegistration(
        Course course,
        Major major,
        int yearOfStudy,
        CourseCategory category
    ) {
        return new CourseRequirement(course, major, yearOfStudy, category);
    }

    public AcademicReport createAcademicReport() {
        AcademicReport report = new AcademicReport();
        createdReports.add(report);
        return report;
    }

    public void manageNews(News news) {
        managedNews.add(news);
    }

    public List<Student> viewStudentsSorted(
        List<Student> students,
        Comparator<User> comparator
    ) {
        return students.stream().sorted(comparator).toList();
    }

    public List<Teacher> viewTeachersSorted(
        List<Teacher> teachers,
        Comparator<User> comparator
    ) {
        return teachers.stream().sorted(comparator).toList();
    }

    public List<EmployeeRequest> viewEmployeeRequests() {
        return List.copyOf(viewedRequests);
    }

    public void addEmployeeRequest(EmployeeRequest request) {
        viewedRequests.add(request);
    }

    public ManagerType getType() {
        return type;
    }

    public void setType(ManagerType type) {
        this.type = type;
    }

    public List<AcademicReport> getCreatedReports() {
        return List.copyOf(createdReports);
    }

    public List<News> getManagedNews() {
        return List.copyOf(managedNews);
    }

    @Override
    public String toString() {
        return "Manager{id='%s', name='%s'}".formatted(getId(), getName());
    }
}
