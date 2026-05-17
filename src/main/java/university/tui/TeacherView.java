package university.tui;

import university.domain.academic.*;
import university.domain.communication.Complaint;
import university.domain.student.TeacherRating;
import university.domain.user.*;
import university.enums.*;
import university.service.NewsService;
import university.service.ResearchService;

import java.util.LinkedHashMap;
import java.util.List;

/// Teacher panel — view courses, put marks, attendance,
/// send messages/complaints, view ratings, and research.
class TeacherView {

    private final Session session;
    private final ResearchService researchService;
    private final NewsService newsService;
    private final ResearchView researchView;
    private final MessageView messageView;

    TeacherView(Session session, ResearchService researchService, NewsService newsService) {
        this.session = session;
        this.researchService = researchService;
        this.newsService = newsService;
        this.researchView = new ResearchView(session, researchService);
        this.messageView = new MessageView(session);
    }

    /// Shows the teacher menu and handles user choices.
    void show() {
        Teacher teacher = (Teacher) session.getCurrentUser();

        while (true) {
            LinkedHashMap<Integer, String> options = new LinkedHashMap<>();
            options.put(1, Messages.get("teacher.view_courses"));
            options.put(2, Messages.get("teacher.put_mark"));
            options.put(3, Messages.get("teacher.view_students"));
            options.put(4, Messages.get("teacher.send_message"));
            options.put(5, Messages.get("teacher.send_complaint"));
            options.put(6, Messages.get("teacher.view_messages"));
            options.put(7, Messages.get("teacher.view_ratings"));
            if (teacher.getResearchProfile() != null) {
                options.put(8, Messages.get("main.research"));
            }

            int choice = ConsoleMenu.showMenu(Messages.get("teacher.title"), options, true, false);
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
        ConsoleMenu.printSection(Messages.get("teacher.view_courses"));
        List<Course> courses = teacher.viewCourses();
        if (courses.isEmpty()) {
            ConsoleMenu.printInfo(Messages.get("teacher.no_courses"));
        } else {
            for (Course c : courses) {
                System.out.println("  " + c);
            }
        }
        ConsoleInput.waitForEnter();
    }

    private void putMark(Teacher teacher) {
        ConsoleMenu.printSection(Messages.get("teacher.put_mark"));
        List<Course> courses = teacher.viewCourses();
        if (courses.isEmpty()) {
            ConsoleMenu.printInfo(Messages.get("teacher.no_courses"));
            ConsoleInput.waitForEnter();
            return;
        }
        Course course = ConsoleMenu.pickFromList(courses,
                c -> c.getCourseCode() + " - " + c.getTitle(),
                Messages.get("teacher.select_course"));

        List<Enrollment> enrollments = session.getSystem().getAllStudents().stream()
                .flatMap(s -> s.getEnrollments().stream())
                .filter(e -> e.getCourse().equals(course) && e.getStatus() == EnrollmentStatus.REGISTERED)
                .toList();

        if (enrollments.isEmpty()) {
            ConsoleMenu.printInfo(Messages.get("teacher.no_students"));
            ConsoleInput.waitForEnter();
            return;
        }

        Enrollment enrollment = ConsoleMenu.pickFromList(enrollments,
                e -> e.getStudent().getName() + " | "
                        + Messages.get("teacher.attempt_label") + " " + e.getAttemptNo() + " | "
                        + Messages.get("student.mark_label") + ": "
                        + e.getMark().map(m -> String.valueOf(m.getTotal())).orElse("N/A"),
                Messages.get("teacher.select_enrollment"));

        double first = ConsoleInput.readDouble("  " + Messages.get("teacher.first_att") + ": ", 0, 30);
        double second = ConsoleInput.readDouble("  " + Messages.get("teacher.second_att") + ": ", 0, 30);
        double finalExam = ConsoleInput.readDouble("  " + Messages.get("teacher.final_exam") + ": ", 0, 40);

        Mark mark = new Mark(first, second, finalExam);
        teacher.putMark(enrollment, mark);
        ConsoleMenu.printSuccess(Messages.get("teacher.mark_set",
                String.valueOf(mark.getTotal()),
                mark.isPassed() ? Messages.get("teacher.mark_passed") : Messages.get("teacher.mark_failed")));
        ConsoleInput.waitForEnter();
    }

    private void viewStudents(Teacher teacher) {
        ConsoleMenu.printSection(Messages.get("teacher.view_students"));
        List<Course> courses = teacher.viewCourses();
        if (courses.isEmpty()) {
            ConsoleMenu.printInfo(Messages.get("teacher.no_courses"));
            ConsoleInput.waitForEnter();
            return;
        }
        Course course = ConsoleMenu.pickFromList(courses,
                c -> c.getCourseCode() + " - " + c.getTitle(),
                Messages.get("teacher.select_course"));

        List<Student> students = session.getSystem().getAllStudents().stream()
                .filter(s -> s.getEnrollments().stream().anyMatch(e -> e.getCourse().equals(course)))
                .toList();

        if (students.isEmpty()) {
            ConsoleMenu.printInfo(Messages.get("teacher.no_students"));
        } else {
            for (Student s : students) {
                System.out.printf("  %s | %s | GPA: %.2f%n", s.getId(), s.getName(), s.getGpa());
            }
        }
        ConsoleInput.waitForEnter();
    }

    private void sendMessage(Teacher teacher) {
        ConsoleMenu.printSection(Messages.get("teacher.send_message"));
        List<Employee> employees = session.getSystem().getUsers().stream()
                .filter(u -> u instanceof Employee && !u.equals(teacher))
                .map(u -> (Employee) u)
                .toList();

        if (employees.isEmpty()) {
            ConsoleMenu.printInfo(Messages.get("teacher.no_employees"));
            ConsoleInput.waitForEnter();
            return;
        }
        Employee receiver = ConsoleMenu.pickFromList(employees,
                e -> e.getName() + " (" + e.getClass().getSimpleName() + ")",
                Messages.get("teacher.select_receiver"));
        String text = ConsoleInput.readLine("  " + Messages.get("message.text") + ": ");
        teacher.sendMessage(receiver, text);
        ConsoleMenu.printSuccess(Messages.get("teacher.message_sent", receiver.getName()));
        ConsoleInput.waitForEnter();
    }

    private void sendComplaint(Teacher teacher) {
        ConsoleMenu.printSection(Messages.get("teacher.send_complaint"));
        List<Manager> managers = session.getSystem().getUsers().stream()
                .filter(u -> u instanceof Manager)
                .map(u -> (Manager) u)
                .toList();

        if (managers.isEmpty()) {
            ConsoleMenu.printInfo(Messages.get("teacher.no_managers"));
            ConsoleInput.waitForEnter();
            return;
        }
        Manager receiver = ConsoleMenu.pickFromList(managers,
                m -> m.getName() + " (" + m.getType() + ")",
                Messages.get("teacher.select_receiver"));

        List<Student> students = session.getSystem().getAllStudents();
        if (students.isEmpty()) {
            ConsoleMenu.printInfo(Messages.get("teacher.no_targets"));
            ConsoleInput.waitForEnter();
            return;
        }
        System.out.println("  " + Messages.get("teacher.select_target") + ":");
        for (int i = 0; i < students.size(); i++) {
            System.out.printf("  [%d]  %s%n", i + 1, students.get(i).getName());
        }
        List<Student> targets = new java.util.ArrayList<>();
        while (true) {
            int si = ConsoleInput.readInt("  " + Messages.get("teacher.student_number") + ": ", 0, students.size());
            if (si == 0) break;
            Student s = students.get(si - 1);
            if (!targets.contains(s)) targets.add(s);
            if (targets.size() == students.size()) break;
        }
        if (targets.isEmpty()) {
            ConsoleMenu.printInfo(Messages.get("teacher.no_targets"));
            ConsoleInput.waitForEnter();
            return;
        }

        LinkedHashMap<Integer, String> urgOptions = new LinkedHashMap<>();
        urgOptions.put(1, Messages.get("teacher.urgency_low"));
        urgOptions.put(2, Messages.get("teacher.urgency_medium"));
        urgOptions.put(3, Messages.get("teacher.urgency_high"));
        int uc = ConsoleMenu.showMenu(Messages.get("teacher.select_urgency"), urgOptions, false, false);
        UrgencyLevel urgency = switch (uc) {
            case 2 -> UrgencyLevel.MEDIUM;
            case 3 -> UrgencyLevel.HIGH;
            default -> UrgencyLevel.LOW;
        };

        String text = ConsoleInput.readLine("  " + Messages.get("teacher.complaint_text") + ": ");
        Complaint complaint = teacher.sendComplaint(targets, urgency, text, receiver);
        ConsoleMenu.printSuccess(Messages.get("teacher.complaint_filed", complaint.getId()));
        ConsoleInput.waitForEnter();
    }

    private void viewMyRatings(Teacher teacher) {
        ConsoleMenu.printSection(Messages.get("teacher.view_ratings"));
        List<TeacherRating> ratings = teacher.getReceivedRatings();
        if (ratings.isEmpty()) {
            ConsoleMenu.printInfo(Messages.get("teacher.no_ratings"));
        } else {
            for (var r : ratings) {
                System.out.printf("  %s: %d/5 | %s: %s | %s%n",
                        Messages.get("teacher.score_label"), r.getScore(),
                        Messages.get("teacher.by_label"), r.getStudent().getName(),
                        r.getComment());
            }
            System.out.println("  " + Messages.get("teacher.avg_rating",
                    String.format("%.2f", teacher.getAverageRating())));
        }
        ConsoleInput.waitForEnter();
    }
}
