package university.tui;

import university.domain.academic.*;
import university.domain.communication.Complaint;
import university.domain.user.*;
import university.enums.*;
import university.service.NewsService;
import university.service.ResearchService;

import java.util.LinkedHashMap;
import java.util.List;

class TeacherView {

    private final Session session;
    private final ResearchService researchService;
    private final NewsService newsService;
    private final ResearchView researchView;
    private final MessageView messageView;
    private final CourseView courseView;

    TeacherView(Session session, ResearchService researchService, NewsService newsService) {
        this.session = session;
        this.researchService = researchService;
        this.newsService = newsService;
        this.researchView = new ResearchView(session, researchService);
        this.messageView = new MessageView(session);
        this.courseView = new CourseView(session);
    }

    void show() {
        Teacher teacher = (Teacher) session.getCurrentUser();

        while (true) {
            LinkedHashMap<Integer, String> options = new LinkedHashMap<>();
            options.put(1, "View My Courses");
            options.put(2, "Put Mark for Student");
            options.put(3, "View Students in Course");
            options.put(4, "Send Message to Employee");
            options.put(5, "Send Complaint");
            options.put(6, "View My Messages");
            options.put(7, "View My Ratings");
            if (teacher.getResearchProfile() != null) {
                options.put(8, "Research Panel");
            }

            int choice = ConsoleMenu.showMenu("Teacher Panel", options, true, false);
            switch (choice) {
                case 0 -> { return; }
                case 1 -> viewMyCourses(teacher);
                case 2 -> putMark(teacher);
                case 3 -> viewStudents(teacher);
                case 4 -> sendMessage(teacher);
                case 5 -> sendComplaint(teacher);
                case 6 -> messageView.show(teacher);
                case 7 -> viewMyRatings(teacher);
                case 8 -> {
                    if (teacher.getResearchProfile() != null) researchView.show();
                }
            }
        }
    }

    private void viewMyCourses(Teacher teacher) {
        ConsoleMenu.printSection("My Courses");
        List<Course> courses = teacher.viewCourses();
        if (courses.isEmpty()) {
            ConsoleMenu.printInfo("You are not assigned to any courses.");
        } else {
            for (Course c : courses) {
                System.out.println("  " + c);
            }
        }
        ConsoleInput.waitForEnter();
    }

    private void putMark(Teacher teacher) {
        ConsoleMenu.printSection("Put Mark");
        List<Course> courses = teacher.viewCourses();
        if (courses.isEmpty()) {
            ConsoleMenu.printInfo("You are not assigned to any courses.");
            ConsoleInput.waitForEnter();
            return;
        }
        for (int i = 0; i < courses.size(); i++) {
            System.out.printf("  [%d]  %s - %s%n", i + 1, courses.get(i).getCourseCode(), courses.get(i).getTitle());
        }
        int ci = ConsoleInput.readInt("\n  Select course: ", 1, courses.size()) - 1;
        Course course = courses.get(ci);

        List<Enrollment> enrollments = session.getSystem().getAllStudents().stream()
                .flatMap(s -> s.getEnrollments().stream())
                .filter(e -> e.getCourse().equals(course) && e.getStatus() == EnrollmentStatus.REGISTERED)
                .toList();

        if (enrollments.isEmpty()) {
            ConsoleMenu.printInfo("No registered students for this course.");
            ConsoleInput.waitForEnter();
            return;
        }

        for (int i = 0; i < enrollments.size(); i++) {
            Enrollment e = enrollments.get(i);
            System.out.printf(
                    "  [%d]  %s | Attempt %d | Current mark: %s%n",
                    i + 1,
                    e.getStudent().getName(),
                    e.getAttemptNo(),
                    e.getMark().map(m -> String.valueOf(m.getTotal())).orElse("N/A")
            );
        }
        int ei = ConsoleInput.readInt("\n  Select enrollment: ", 1, enrollments.size()) - 1;
        Enrollment enrollment = enrollments.get(ei);

        double first = ConsoleInput.readDouble("  First attestation (0-30): ", 0, 30);
        double second = ConsoleInput.readDouble("  Second attestation (0-30): ", 0, 30);
        double finalExam = ConsoleInput.readDouble("  Final exam (0-40): ", 0, 40);

        Mark mark = new Mark(first, second, finalExam);
        teacher.putMark(enrollment, mark);
        ConsoleMenu.printSuccess("Mark set. Total: " + mark.getTotal() + " (" + (mark.isPassed() ? "PASSED" : "FAILED") + ")");
        ConsoleInput.waitForEnter();
    }

    private void viewStudents(Teacher teacher) {
        ConsoleMenu.printSection("View Students in Course");
        List<Course> courses = teacher.viewCourses();
        if (courses.isEmpty()) {
            ConsoleMenu.printInfo("You are not assigned to any courses.");
            ConsoleInput.waitForEnter();
            return;
        }
        for (int i = 0; i < courses.size(); i++) {
            System.out.printf("  [%d]  %s - %s%n", i + 1, courses.get(i).getCourseCode(), courses.get(i).getTitle());
        }
        int ci = ConsoleInput.readInt("\n  Select course: ", 1, courses.size()) - 1;
        Course course = courses.get(ci);

        List<Student> students = session.getSystem().getAllStudents().stream()
                .filter(s -> s.getEnrollments().stream().anyMatch(e -> e.getCourse().equals(course)))
                .toList();

        if (students.isEmpty()) {
            ConsoleMenu.printInfo("No students enrolled in this course.");
        } else {
            for (Student s : students) {
                System.out.printf("  %s | %s | GPA: %.2f%n", s.getId(), s.getName(), s.getGpa());
            }
        }
        ConsoleInput.waitForEnter();
    }

    private void sendMessage(Teacher teacher) {
        ConsoleMenu.printSection("Send Message");
        List<Employee> employees = session.getSystem().getUsers().stream()
                .filter(u -> u instanceof Employee && !u.equals(teacher))
                .map(u -> (Employee) u)
                .toList();

        if (employees.isEmpty()) {
            ConsoleMenu.printInfo("No other employees in the system.");
            ConsoleInput.waitForEnter();
            return;
        }
        for (int i = 0; i < employees.size(); i++) {
            System.out.printf("  [%d]  %s (%s)%n", i + 1, employees.get(i).getName(), employees.get(i).getClass().getSimpleName());
        }
        int ei = ConsoleInput.readInt("\n  Select receiver: ", 1, employees.size()) - 1;
        Employee receiver = employees.get(ei);
        String text = ConsoleInput.readLine("  Message: ");
        teacher.sendMessage(receiver, text);
        ConsoleMenu.printSuccess("Message sent to " + receiver.getName());
        ConsoleInput.waitForEnter();
    }

    private void sendComplaint(Teacher teacher) {
        ConsoleMenu.printSection("Send Complaint");
        List<Manager> managers = session.getSystem().getUsers().stream()
                .filter(u -> u instanceof Manager)
                .map(u -> (Manager) u)
                .toList();

        if (managers.isEmpty()) {
            ConsoleMenu.printInfo("No managers in the system.");
            ConsoleInput.waitForEnter();
            return;
        }
        for (int i = 0; i < managers.size(); i++) {
            System.out.printf("  [%d]  %s (%s)%n", i + 1, managers.get(i).getName(), managers.get(i).getType());
        }
        int mi = ConsoleInput.readInt("\n  Select receiver: ", 1, managers.size()) - 1;
        Manager receiver = managers.get(mi);

        List<Student> students = session.getSystem().getAllStudents();
        if (students.isEmpty()) {
            ConsoleMenu.printInfo("No students in the system.");
            ConsoleInput.waitForEnter();
            return;
        }
        System.out.println("  Select target students (enter 0 when done):");
        for (int i = 0; i < students.size(); i++) {
            System.out.printf("  [%d]  %s%n", i + 1, students.get(i).getName());
        }
        List<Student> targets = new java.util.ArrayList<>();
        while (true) {
            int si = ConsoleInput.readInt("  Student #: ", 0, students.size());
            if (si == 0) break;
            Student s = students.get(si - 1);
            if (!targets.contains(s)) targets.add(s);
            if (targets.size() == students.size()) break;
        }
        if (targets.isEmpty()) {
            ConsoleMenu.printInfo("No students selected.");
            ConsoleInput.waitForEnter();
            return;
        }

        LinkedHashMap<Integer, String> urgOptions = new LinkedHashMap<>();
        urgOptions.put(1, "LOW");
        urgOptions.put(2, "MEDIUM");
        urgOptions.put(3, "HIGH");
        int uc = ConsoleMenu.showMenu("Select Urgency", urgOptions, false, false);
        UrgencyLevel urgency = switch (uc) {
            case 2 -> UrgencyLevel.MEDIUM;
            case 3 -> UrgencyLevel.HIGH;
            default -> UrgencyLevel.LOW;
        };

        String text = ConsoleInput.readLine("  Complaint text: ");
        Complaint complaint = teacher.sendComplaint(targets, urgency, text, receiver);
        ConsoleMenu.printSuccess("Complaint filed: " + complaint.getId());
        ConsoleInput.waitForEnter();
    }

    private void viewMyRatings(Teacher teacher) {
        ConsoleMenu.printSection("My Ratings");
        List<university.domain.student.TeacherRating> ratings = teacher.getReceivedRatings();
        if (ratings.isEmpty()) {
            ConsoleMenu.printInfo("No ratings yet.");
        } else {
            for (var r : ratings) {
                System.out.printf("  Score: %d/5 | By: %s | %s%n", r.getScore(), r.getStudent().getName(), r.getComment());
            }
            System.out.printf("%n  Average rating: %.2f / 5%n", teacher.getAverageRating());
        }
        ConsoleInput.waitForEnter();
    }
}
