package university.tui;

import university.comparator.*;
import university.domain.academic.*;
import university.domain.news.News;
import university.domain.user.*;
import university.enums.*;
import university.service.NewsService;
import university.service.ResearchService;
import university.system.UniversitySystem;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;

class ManagerView {

    private final Session session;
    private final NewsService newsService;
    private final ResearchService researchService;
    private final MessageView messageView;
    private final CourseView courseView;

    ManagerView(Session session, NewsService newsService, ResearchService researchService) {
        this.session = session;
        this.newsService = newsService;
        this.researchService = researchService;
        this.messageView = new MessageView(session);
        this.courseView = new CourseView(session);
    }

    void show() {
        Manager manager = (Manager) session.getCurrentUser();
        UniversitySystem system = session.getSystem();

        while (true) {
            LinkedHashMap<Integer, String> options = new LinkedHashMap<>();
            options.put(1, "Approve Student Registrations");
            options.put(2, "Assign Teacher to Course");
            options.put(3, "Add Course for Registration");
            options.put(4, "View Students Sorted");
            options.put(5, "View Teachers Sorted");
            options.put(6, "Manage News");
            options.put(7, "View My Messages");
            options.put(8, "View Statistics");
            options.put(9, "Browse Courses");

            int choice = ConsoleMenu.showMenu("Manager Panel", options, true, false);
            switch (choice) {
                case 0 -> { return; }
                case 1 -> approveRegistrations(manager);
                case 2 -> assignTeacherToCourse(manager);
                case 3 -> addCourseForRegistration(manager);
                case 4 -> viewStudentsSorted(manager, system);
                case 5 -> viewTeachersSorted(manager, system);
                case 6 -> manageNews(manager);
                case 7 -> messageView.show(manager);
                case 8 -> viewStatistics(system);
                case 9 -> courseView.show();
            }
        }
    }

    private void approveRegistrations(Manager manager) {
        ConsoleMenu.printSection("Approve Student Registrations");
        List<Student> students = session.getSystem().getAllStudents();
        List<Enrollment> pending = students.stream()
                .flatMap(s -> s.getEnrollments().stream())
                .filter(e -> e.getStatus() == EnrollmentStatus.PENDING)
                .toList();

        if (pending.isEmpty()) {
            ConsoleMenu.printInfo("No pending registrations.");
            ConsoleInput.waitForEnter();
            return;
        }

        for (int i = 0; i < pending.size(); i++) {
            Enrollment e = pending.get(i);
            System.out.printf(
                    "  [%d]  %s | %s - %s | Attempt %d%n",
                    i + 1,
                    e.getStudent().getName(),
                    e.getCourse().getCourseCode(),
                    e.getCourse().getTitle(),
                    e.getAttemptNo()
            );
        }

        int ei = ConsoleInput.readInt("\n  Select enrollment to approve (0 to cancel): ", 0, pending.size());
        if (ei == 0) return;

        Enrollment selected = pending.get(ei - 1);
        if (ConsoleMenu.confirm("Approve " + selected.getStudent().getName() + " for " + selected.getCourse().getCourseCode() + "?")) {
            manager.approveRegistration(selected);
            ConsoleMenu.printSuccess("Registration approved.");
        }
        ConsoleInput.waitForEnter();
    }

    private void assignTeacherToCourse(Manager manager) {
        ConsoleMenu.printSection("Assign Teacher to Course");
        List<Teacher> teachers = session.getSystem().getAllTeachers();
        List<Course> courses = session.getSystem().getCourses();

        if (teachers.isEmpty() || courses.isEmpty()) {
            ConsoleMenu.printInfo("Need at least one teacher and one course.");
            ConsoleInput.waitForEnter();
            return;
        }

        for (int i = 0; i < courses.size(); i++) {
            System.out.printf("  [%d]  %s - %s%n", i + 1, courses.get(i).getCourseCode(), courses.get(i).getTitle());
        }
        int ci = ConsoleInput.readInt("\n  Select course: ", 1, courses.size()) - 1;
        Course course = courses.get(ci);

        for (int i = 0; i < teachers.size(); i++) {
            System.out.printf("  [%d]  %s (%s)%n", i + 1, teachers.get(i).getName(), teachers.get(i).getPosition());
        }
        int ti = ConsoleInput.readInt("\n  Select teacher: ", 1, teachers.size()) - 1;
        Teacher teacher = teachers.get(ti);

        String lessonId = ConsoleInput.readLine("  Lesson ID (e.g. L001): ");
        LinkedHashMap<Integer, String> ltOptions = new LinkedHashMap<>();
        ltOptions.put(1, "LECTURE");
        ltOptions.put(2, "PRACTICE");
        int ltc = ConsoleMenu.showMenu("Lesson Type", ltOptions, false, false);
        LessonType lessonType = ltc == 2 ? LessonType.PRACTICE : LessonType.LECTURE;

        String room = ConsoleInput.readLine("  Room: ");
        Lesson lesson = new Lesson(lessonId, lessonType, room, LocalDateTime.now(), teacher);
        course.addLesson(lesson);
        manager.assignTeacherToCourse(teacher, course, lesson);
        ConsoleMenu.printSuccess("Teacher assigned to course.");
        ConsoleInput.waitForEnter();
    }

    private void addCourseForRegistration(Manager manager) {
        ConsoleMenu.printSection("Add Course for Registration");
        String code = ConsoleInput.readLine("  Course code: ");
        String title = ConsoleInput.readLine("  Course title: ");
        int credits = ConsoleInput.readInt("  Credits: ", 1, 30);

        Course course = new Course(code, title, credits);
        session.getSystem().addCourse(course);

        ConsoleMenu.printSuccess("Course added: " + code + " - " + title);
        ConsoleInput.waitForEnter();
    }

    private void viewStudentsSorted(Manager manager, UniversitySystem system) {
        ConsoleMenu.printSection("View Students Sorted");
        LinkedHashMap<Integer, String> sortOptions = new LinkedHashMap<>();
        sortOptions.put(1, "By GPA (highest first)");
        sortOptions.put(2, "By Name (A-Z)");

        int sc = ConsoleMenu.showMenu("Sort By", sortOptions, true, false);
        if (sc == 0) return;

        List<Student> sorted = switch (sc) {
            case 1 -> manager.viewStudentsSorted(system.getAllStudents(), new StudentByGpaComparator());
            case 2 -> manager.viewStudentsSorted(system.getAllStudents(), new UserByNameComparator());
            default -> system.getAllStudents();
        };

        ConsoleMenu.printDivider();
        for (Student s : sorted) {
            System.out.printf("  %-20s | GPA: %.2f | Credits: %d | %s%n",
                    s.getName(), s.getGpa(), s.getTotalCredits(), s.getDegreeType());
        }
        ConsoleMenu.printDivider();
        ConsoleInput.waitForEnter();
    }

    private void viewTeachersSorted(Manager manager, UniversitySystem system) {
        ConsoleMenu.printSection("View Teachers Sorted");
        LinkedHashMap<Integer, String> sortOptions = new LinkedHashMap<>();
        sortOptions.put(1, "By Name (A-Z)");

        int sc = ConsoleMenu.showMenu("Sort By", sortOptions, true, false);
        if (sc == 0) return;

        List<Teacher> sorted = manager.viewTeachersSorted(system.getAllTeachers(), new UserByNameComparator());

        ConsoleMenu.printDivider();
        for (Teacher t : sorted) {
            System.out.printf("  %-20s | Position: %-15s | Rating: %.2f%n",
                    t.getName(), t.getPosition(), t.getAverageRating());
        }
        ConsoleMenu.printDivider();
        ConsoleInput.waitForEnter();
    }

    private void manageNews(Manager manager) {
        ConsoleMenu.printSection("Manage News");
        LinkedHashMap<Integer, String> options = new LinkedHashMap<>();
        options.put(1, "Create News Post");
        options.put(2, "View All News");
        int choice = ConsoleMenu.showMenu("News Management", options, true, false);

        if (choice == 1) {
            String title = ConsoleInput.readLine("  Title: ");
            String content = ConsoleInput.readLine("  Content: ");
            LinkedHashMap<Integer, String> topicOptions = new LinkedHashMap<>();
            topicOptions.put(1, "RESEARCH");
            topicOptions.put(2, "ACADEMIC");
            topicOptions.put(3, "EVENT");
            topicOptions.put(4, "GENERAL");
            int tc = ConsoleMenu.showMenu("Select Topic", topicOptions, false, false);
            NewsTopic topic = switch (tc) {
                case 2 -> NewsTopic.ACADEMIC;
                case 3 -> NewsTopic.EVENT;
                case 4 -> NewsTopic.GENERAL;
                default -> NewsTopic.RESEARCH;
            };
            News news = new News(title, content, topic);
            newsService.publishNews(news);
            ConsoleMenu.printSuccess("News published.");
        } else if (choice == 2) {
            List<News> allNews = session.getSystem().getNewsList();
            ConsoleMenu.printSection("All News");
            if (allNews.isEmpty()) {
                ConsoleMenu.printInfo("No news available.");
            } else {
                for (News n : allNews) {
                    System.out.printf("  [%s%s] %s%n", n.isPinned() ? "PINNED " : "", n.getTopic(), n.getTitle());
                    System.out.println("    " + n.getContent());
                    System.out.println("    Comments: " + n.getComments().size());
                    System.out.println();
                }
            }
        }
        ConsoleInput.waitForEnter();
    }

    private void viewStatistics(UniversitySystem system) {
        ConsoleMenu.printSection("System Statistics");
        int studentCount = system.getAllStudents().size();
        int teacherCount = system.getAllTeachers().size();
        int courseCount = system.getCourses().size();
        int newsCount = system.getNewsList().size();
        int journalCount = system.getJournals().size();

        System.out.printf("  Total Users: %d%n", system.getUsers().size());
        System.out.printf("  Students: %d%n", studentCount);
        System.out.printf("  Teachers: %d%n", teacherCount);
        System.out.printf("  Courses: %d%n", courseCount);
        System.out.printf("  News Posts: %d%n", newsCount);
        System.out.printf("  Journals: %d%n", journalCount);

        if (!system.getAllStudents().isEmpty()) {
            double avgGpa = system.getAllStudents().stream()
                    .mapToDouble(Student::getGpa)
                    .average()
                    .orElse(0);
            System.out.printf("%n  Average Student GPA: %.2f%n", avgGpa);
        }
        ConsoleInput.waitForEnter();
    }
}
