package university.tui;

import university.domain.academic.*;
import university.domain.student.OrganizationMembership;
import university.domain.student.StudentOrganization;
import university.domain.user.*;
import university.enums.*;
import university.exception.CreditLimitExceededException;
import university.exception.RetakeLimitExceededException;
import university.service.ResearchService;

import java.util.LinkedHashMap;
import java.util.List;

class StudentView {

    private final Session session;
    private final ResearchService researchService;
    private final ResearchView researchView;
    private final CourseView courseView;

    StudentView(Session session, ResearchService researchService) {
        this.session = session;
        this.researchService = researchService;
        this.researchView = new ResearchView(session, researchService);
        this.courseView = new CourseView(session);
    }

    void show() {
        Student student = (Student) session.getCurrentUser();

        while (true) {
            LinkedHashMap<Integer, String> options = new LinkedHashMap<>();
            options.put(1, "Browse Available Courses");
            options.put(2, "Register for Course");
            options.put(3, "View My Courses & Marks");
            options.put(4, "View My Transcript");
            options.put(5, "Rate a Teacher");
            options.put(6, "View Teacher Info");
            options.put(7, "Student Organizations");
            if (student.getResearchProfile() != null) {
                options.put(8, "Research Panel");
            }

            int choice = ConsoleMenu.showMenu("Student Panel", options, true, false);
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
        ConsoleMenu.printSection("Register for Course");
        List<Course> allCourses = session.getSystem().getCourses();
        if (allCourses.isEmpty()) {
            ConsoleMenu.printInfo("No courses available.");
            ConsoleInput.waitForEnter();
            return;
        }

        for (int i = 0; i < allCourses.size(); i++) {
            Course c = allCourses.get(i);
            System.out.printf("  [%d]  %s - %s (%d credits)%n", i + 1, c.getCourseCode(), c.getTitle(), c.getCredits());
        }
        System.out.printf("  Credits used: %d / %d%n", student.getTotalCredits(), Student.MAX_CREDITS);

        int ci = ConsoleInput.readInt("\n  Select course: ", 1, allCourses.size()) - 1;
        Course course = allCourses.get(ci);

        try {
            Enrollment enrollment = student.registerForCourse(course);
            ConsoleMenu.printSuccess("Registered for " + course.getCourseCode() + ". Status: " + enrollment.getStatus());
            ConsoleMenu.printInfo("Your registration is pending manager approval.");
        } catch (CreditLimitExceededException e) {
            ConsoleMenu.printError(e.getMessage());
        } catch (RetakeLimitExceededException e) {
            ConsoleMenu.printError(e.getMessage());
        }
        ConsoleInput.waitForEnter();
    }

    private void viewMyCoursesAndMarks(Student student) {
        ConsoleMenu.printSection("My Courses & Marks");
        List<Enrollment> enrollments = student.getEnrollments();
        if (enrollments.isEmpty()) {
            ConsoleMenu.printInfo("You are not enrolled in any courses.");
        } else {
            ConsoleMenu.printDivider();
            for (Enrollment e : enrollments) {
                System.out.printf(
                        "  %s | %-25s | Status: %-12s | Mark: %s%n",
                        e.getCourse().getCourseCode(),
                        e.getCourse().getTitle(),
                        e.getStatus(),
                        e.getMark().map(m -> String.valueOf(m.getTotal())).orElse("N/A")
                );
            }
            ConsoleMenu.printDivider();
            System.out.printf("  Total credits: %d / %d | GPA: %.2f%n", student.getTotalCredits(), Student.MAX_CREDITS, student.getGpa());
        }
        ConsoleInput.waitForEnter();
    }

    private void viewTranscript(Student student) {
        ConsoleMenu.printSection("Transcript");
        System.out.println(student.getTranscript());
        ConsoleInput.waitForEnter();
    }

    private void rateTeacher(Student student) {
        ConsoleMenu.printSection("Rate a Teacher");
        List<Teacher> teachers = session.getSystem().getAllTeachers();
        if (teachers.isEmpty()) {
            ConsoleMenu.printInfo("No teachers in the system.");
            ConsoleInput.waitForEnter();
            return;
        }
        for (int i = 0; i < teachers.size(); i++) {
            System.out.printf("  [%d]  %s (%s)%n", i + 1, teachers.get(i).getName(), teachers.get(i).getPosition());
        }
        int ti = ConsoleInput.readInt("\n  Select teacher: ", 1, teachers.size()) - 1;
        Teacher teacher = teachers.get(ti);

        int score = ConsoleInput.readInt("  Rating (1-5): ", 1, 5);
        String comment = ConsoleInput.readLineOrBlank("  Comment (optional): ");

        student.rateTeacher(teacher, score, comment);
        ConsoleMenu.printSuccess("Rating submitted. Teacher average: " + String.format("%.2f", teacher.getAverageRating()));
        ConsoleInput.waitForEnter();
    }

    private void viewTeacherInfo() {
        ConsoleMenu.printSection("Teacher Information");
        List<Teacher> teachers = session.getSystem().getAllTeachers();
        if (teachers.isEmpty()) {
            ConsoleMenu.printInfo("No teachers in the system.");
            ConsoleInput.waitForEnter();
            return;
        }
        for (int i = 0; i < teachers.size(); i++) {
            System.out.printf("  [%d]  %s%n", i + 1, teachers.get(i).getName());
        }
        int ti = ConsoleInput.readInt("\n  Select teacher: ", 1, teachers.size()) - 1;
        Teacher teacher = teachers.get(ti);

        System.out.println();
        System.out.println("  Name: " + teacher.getName());
        System.out.println("  Email: " + teacher.getEmail());
        System.out.println("  Position: " + teacher.getPosition());
        System.out.printf("  Average Rating: %.2f / 5%n", teacher.getAverageRating());
        System.out.println("  Courses: " + teacher.viewCourses().stream().map(Course::getCourseCode).toList());
        ConsoleInput.waitForEnter();
    }

    private void studentOrganizations(Student student) {
        while (true) {
            LinkedHashMap<Integer, String> options = new LinkedHashMap<>();
            options.put(1, "View My Organizations");
            options.put(2, "Create Organization");
            int choice = ConsoleMenu.showMenu("Student Organizations", options, true, false);
            switch (choice) {
                case 0 -> { return; }
                case 1 -> viewMyOrganizations(student);
                case 2 -> createOrganization(student);
            }
        }
    }

    private void viewMyOrganizations(Student student) {
        ConsoleMenu.printSection("My Organizations");
        List<OrganizationMembership> memberships = student.getMemberships();
        if (memberships.isEmpty()) {
            ConsoleMenu.printInfo("You are not a member of any organization.");
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
        ConsoleMenu.printSection("Create Organization");
        String name = ConsoleInput.readLine("  Organization name: ");
        String description = ConsoleInput.readLine("  Description: ");

        StudentOrganization org = new StudentOrganization(name, description);
        OrganizationMembership membership = new OrganizationMembership(student, org, OrganizationRole.HEAD);
        org.addMembership(membership);
        student.addMembership(membership);

        ConsoleMenu.printSuccess("Organization created: " + name + " (you are the HEAD)");
        ConsoleInput.waitForEnter();
    }
}
