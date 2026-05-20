package university.tui;

import university.domain.academic.*;
import university.domain.student.OrganizationMembership;
import university.domain.student.StudentOrganization;
import university.domain.user.*;
import university.enums.*;
import university.exception.CreditLimitExceededException;
import university.exception.RetakeLimitExceededException;
import university.service.NewsService;
import university.service.ResearchService;

import java.util.LinkedHashMap;
import java.util.List;

/// Student panel — register for courses, view marks/transcript,
/// rate teachers, browse courses, and manage organizations.
class StudentView {

    private final Session session;
    private final ResearchService researchService;
    private final NewsService newsService;
    private final ResearchView researchView;
    private final CourseView courseView;

    StudentView(Session session, ResearchService researchService, NewsService newsService) {
        this.session = session;
        this.researchService = researchService;
        this.newsService = newsService;
        this.researchView = new ResearchView(session, researchService, newsService);
        this.courseView = new CourseView(session);
    }

    /// Shows the student menu and handles user choices.
    void show() {
        Student student = (Student) session.getCurrentUser();

        while (true) {
            LinkedHashMap<Integer, String> options = new LinkedHashMap<>();
            options.put(1, Messages.get("student.browse_courses"));
            options.put(2, Messages.get("student.register"));
            options.put(3, Messages.get("student.view_marks"));
            options.put(4, Messages.get("student.view_transcript"));
            options.put(5, Messages.get("student.rate_teacher"));
            options.put(6, Messages.get("student.teacher_info"));
            options.put(7, Messages.get("student.organizations"));
            if (student.getResearchProfile() != null) {
                options.put(8, Messages.get("main.research"));
            }

            int choice = ConsoleMenu.showMenu(Messages.get("student.title"), options, true, false);
            switch (choice) {
                case 0 -> { return; }
                case 1 -> courseView.show();
                case 2 -> registerForCourse(student);
                case 3 -> viewMyCoursesAndMarks(student);
                case 4 -> viewTranscript(student);
                case 5 -> rateTeacher(student);
                case 6 -> viewTeacherInfo();
                case 7 -> studentOrganizations(student);
                case 8 -> {
                    if (student.getResearchProfile() != null) researchView.show();
                }
            }
        }
    }

    private void registerForCourse(Student student) {
        ConsoleMenu.printSection(Messages.get("student.register"));
        List<Course> allCourses = session.getSystem().getCourses();
        if (allCourses.isEmpty()) {
            ConsoleMenu.printInfo(Messages.get("student.no_courses"));
            ConsoleInput.waitForEnter();
            return;
        }

        for (int i = 0; i < allCourses.size(); i++) {
            Course c = allCourses.get(i);
            System.out.printf("  [%d]  %s - %s (%d %s)%n", i + 1,
                    c.getCourseCode(), c.getTitle(), c.getCredits(), Messages.get("student.credits_word"));
        }
        System.out.println("  " + Messages.get("student.credits_used",
                student.getTotalCredits(), Student.MAX_CREDITS));

        int ci = ConsoleInput.readInt("\n  " + Messages.get("student.select_course") + ": ", 1, allCourses.size()) - 1;
        Course course = allCourses.get(ci);

        try {
            Enrollment enrollment = student.registerForCourse(course);
            ConsoleMenu.printSuccess(Messages.get("student.reg_success",
                    course.getCourseCode(), enrollment.getStatus()));
            ConsoleMenu.printInfo(Messages.get("student.reg_pending"));
        } catch (CreditLimitExceededException | RetakeLimitExceededException e) {
            ConsoleMenu.printError(e.getMessage());
        }
        ConsoleInput.waitForEnter();
    }

    private void viewMyCoursesAndMarks(Student student) {
        ConsoleMenu.printSection(Messages.get("student.view_marks"));
        List<Enrollment> enrollments = student.getEnrollments();
        if (enrollments.isEmpty()) {
            ConsoleMenu.printInfo(Messages.get("student.no_enrollments"));
        } else {
            ConsoleMenu.printDivider();
            for (Enrollment e : enrollments) {
                System.out.printf(
                        "  %s | %-25s | %s: %-12s | %s: %s%n",
                        e.getCourse().getCourseCode(),
                        e.getCourse().getTitle(),
                        Messages.get("student.status_label"), e.getStatus(),
                        Messages.get("student.mark_label"),
                        e.getMark().map(m -> String.valueOf(m.getTotal())).orElse("N/A")
                );
            }
            ConsoleMenu.printDivider();
            System.out.println("  " + Messages.get("student.total_credits",
                    student.getTotalCredits(), Student.MAX_CREDITS, student.getGpa()));
        }
        ConsoleInput.waitForEnter();
    }

    private void viewTranscript(Student student) {
        ConsoleMenu.printSection(Messages.get("student.view_transcript"));
        System.out.println(student.getTranscript());
        ConsoleInput.waitForEnter();
    }

    private void rateTeacher(Student student) {
        ConsoleMenu.printSection(Messages.get("student.rate_teacher"));
        List<Teacher> teachers = session.getSystem().getAllTeachers();
        if (teachers.isEmpty()) {
            ConsoleMenu.printInfo(Messages.get("student.no_teachers"));
            ConsoleInput.waitForEnter();
            return;
        }
        Teacher teacher = ConsoleMenu.pickFromList(teachers,
                t -> t.getName() + " (" + t.getPosition() + ")",
                Messages.get("student.select_teacher"));

        int score = ConsoleInput.readInt("  " + Messages.get("student.rating_input") + ": ", 1, 5);
        String comment = ConsoleInput.readLineOrBlank("  " + Messages.get("student.comment_prompt") + ": ");

        student.rateTeacher(teacher, score, comment);
        ConsoleMenu.printSuccess(Messages.get("student.rating_submitted",
                String.format("%.2f", teacher.getAverageRating())));
        ConsoleInput.waitForEnter();
    }

    private void viewTeacherInfo() {
        ConsoleMenu.printSection(Messages.get("student.teacher_info"));
        List<Teacher> teachers = session.getSystem().getAllTeachers();
        if (teachers.isEmpty()) {
            ConsoleMenu.printInfo(Messages.get("student.no_teachers"));
            ConsoleInput.waitForEnter();
            return;
        }
        Teacher teacher = ConsoleMenu.pickFromList(teachers, Teacher::getName,
                Messages.get("student.select_teacher"));

        System.out.println();
        System.out.println("  " + Messages.get("student.name_label") + ": " + teacher.getName());
        System.out.println("  " + Messages.get("student.email_label") + ": " + teacher.getEmail());
        System.out.println("  " + Messages.get("student.position_label") + ": " + teacher.getPosition());
        System.out.printf("  " + Messages.get("student.rating_label") + ": %.2f / 5%n", teacher.getAverageRating());
        System.out.println("  " + Messages.get("student.courses_label") + ": "
                + teacher.viewCourses().stream().map(Course::getCourseCode).toList());
        ConsoleInput.waitForEnter();
    }

    private void studentOrganizations(Student student) {
        while (true) {
            LinkedHashMap<Integer, String> options = new LinkedHashMap<>();
            options.put(1, Messages.get("student.org_view"));
            options.put(2, Messages.get("student.org_create"));
            int choice = ConsoleMenu.showMenu(Messages.get("student.org_title"), options, true, false);
            switch (choice) {
                case 0 -> { return; }
                case 1 -> viewMyOrganizations(student);
                case 2 -> createOrganization(student);
            }
        }
    }

    private void viewMyOrganizations(Student student) {
        ConsoleMenu.printSection(Messages.get("student.org_view"));
        List<OrganizationMembership> memberships = student.getMemberships();
        if (memberships.isEmpty()) {
            ConsoleMenu.printInfo(Messages.get("student.org_no_memberships"));
        } else {
            for (OrganizationMembership m : memberships) {
                System.out.printf("  %s | Role: %s | %s%n",
                        m.getOrganization().getName(),
                        m.getRole(),
                        m.getOrganization().getDescription());
            }
        }
        ConsoleInput.waitForEnter();
    }

    private void createOrganization(Student student) {
        ConsoleMenu.printSection(Messages.get("student.org_create"));
        String name = ConsoleInput.readLine("  " + Messages.get("student.org_name") + ": ");
        String description = ConsoleInput.readLine("  " + Messages.get("student.org_desc") + ": ");

        StudentOrganization org = new StudentOrganization(name, description);
        OrganizationMembership membership = new OrganizationMembership(student, org, OrganizationRole.HEAD);
        org.addMembership(membership);
        student.addMembership(membership);

        ConsoleMenu.printSuccess(Messages.get("student.org_created", name));
        ConsoleInput.waitForEnter();
    }
}
