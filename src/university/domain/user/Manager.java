package university.domain.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import university.comparator.UserComparator;
import university.domain.academic.Course;
import university.domain.academic.CourseRequirement;
import university.domain.academic.Enrollment;
import university.domain.academic.Lesson;
import university.domain.academic.Major;
import university.domain.communication.EmployeeRequest;
import university.domain.news.News;
import university.domain.support.AcademicReport;
import university.enums.CourseCategory;
import university.enums.Language;
import university.enums.ManagerType;

public class Manager extends Employee {

    private ManagerType type;
    private final List<News> managedNews = new ArrayList<>();
    private final List<EmployeeRequest> viewedRequests = new ArrayList<>();
    private final List<AcademicReport> createdReports = new ArrayList<>();

    public Manager(
        String id,
        String name,
        String email,
        String passwordHash,
        Language language,
        double salary,
        ManagerType type
    ) {
        super(id, name, email, passwordHash, language, salary);
        this.type = Objects.requireNonNull(type, "type must not be null");
    }

    public void assignTeacherToCourse(
        Teacher teacher,
        Course course,
        Lesson lesson
    ) {
        Objects.requireNonNull(teacher, "teacher must not be null");
        Objects.requireNonNull(course, "course must not be null");
        Objects.requireNonNull(lesson, "lesson must not be null");
        lesson.setInstructor(teacher);
    }

    public void approveRegistration(Enrollment enrollment) {
        Objects.requireNonNull(enrollment, "enrollment must not be null");
        enrollment.approve();
    }

    public CourseRequirement addCourseForRegistration(
        Course course,
        Major major,
        int yearOfStudy,
        CourseCategory category
    ) {
        Objects.requireNonNull(course, "course must not be null");
        Objects.requireNonNull(major, "major must not be null");
        Objects.requireNonNull(category, "category must not be null");
        return new CourseRequirement(course, major, yearOfStudy, category);
    }

    public AcademicReport createAcademicReport() {
        var report = new AcademicReport();
        createdReports.add(report);
        return report;
    }

    public void manageNews(News news) {
        managedNews.add(Objects.requireNonNull(news, "news must not be null"));
    }

    public List<Student> viewStudentsSorted(UserComparator comparator) {
        Objects.requireNonNull(comparator, "comparator must not be null");
        // Full implementation requires system-level access — delegated to service layer
        return List.of();
    }

    public List<Teacher> viewTeachersSorted(UserComparator comparator) {
        Objects.requireNonNull(comparator, "comparator must not be null");
        // Full implementation requires system-level access — delegated to service layer
        return List.of();
    }

    public List<EmployeeRequest> viewEmployeeRequests() {
        return List.copyOf(viewedRequests);
    }

    public void addEmployeeRequest(EmployeeRequest request) {
        viewedRequests.add(
            Objects.requireNonNull(request, "request must not be null")
        );
    }

    public ManagerType getType() {
        return type;
    }

    public void setType(ManagerType type) {
        this.type = Objects.requireNonNull(type, "type must not be null");
    }

    public List<AcademicReport> getCreatedReports() {
        return List.copyOf(createdReports);
    }

    public List<News> getManagedNews() {
        return List.copyOf(managedNews);
    }

        @Override
    public String toString() {
        return (
            "Manager{" +
            "id='" +
            getId() +
            '\'' +
            ", name='" +
            getName() +
            '\'' +
            ", type=" +
            type +
            ", reports=" +
            createdReports.size() +
            ", news=" +
            managedNews.size() +
            '}'
        );
    }
}
