package university.tui;

import university.comparator.*;
import university.domain.academic.*;
import university.domain.news.News;
import university.domain.user.*;
import university.enums.*;
import university.service.NewsService;
import university.service.ResearchService;
import university.system.UniversitySystem;

import university.tui.Messages;

import java.time.LocalDateTime;
import java.util.Comparator;
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
            options.put(1, Messages.get("manager.approve"));
            options.put(2, Messages.get("manager.assign"));
            options.put(3, Messages.get("manager.add_course"));
            options.put(4, Messages.get("manager.view_students"));
            options.put(5, Messages.get("manager.view_teachers"));
            options.put(6, Messages.get("manager.manage_news"));
            options.put(7, Messages.get("manager.view_messages"));
            options.put(8, Messages.get("manager.statistics"));
            options.put(9, Messages.get("manager.browse_courses"));

            int choice = ConsoleMenu.showMenu(Messages.get("manager.title"), options, true, false);
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
        ConsoleMenu.printSection(Messages.get("manager.approve"));
        List<Student> students = session.getSystem().getAllStudents();
        List<Enrollment> pending = students.stream()
                .flatMap(s -> s.getEnrollments().stream())
                .filter(e -> e.getStatus() == EnrollmentStatus.PENDING)
                .toList();

        if (pending.isEmpty()) {
            ConsoleMenu.printInfo(Messages.get("manager.no_pending"));
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
        if (ConsoleMenu.confirm(Messages.get("manager.approve_confirm", selected.getStudent().getName(), selected.getCourse().getCourseCode()))) {
            manager.approveRegistration(selected);
            selected.register();
            ConsoleMenu.printSuccess(Messages.get("manager.reg_approved"));
        }
        ConsoleInput.waitForEnter();
    }

    private void assignTeacherToCourse(Manager manager) {
        ConsoleMenu.printSection(Messages.get("manager.assign"));
        List<Teacher> teachers = session.getSystem().getAllTeachers();
        List<Course> courses = session.getSystem().getCourses();

        if (teachers.isEmpty() || courses.isEmpty()) {
            ConsoleMenu.printInfo(Messages.get("manager.no_teachers_courses"));
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

        String lessonId = ConsoleInput.readLine("  " + Messages.get("manager.lesson_id") + ": ");
        LinkedHashMap<Integer, String> ltOptions = new LinkedHashMap<>();
        ltOptions.put(1, "LECTURE");
        ltOptions.put(2, "PRACTICE");
        int ltc = ConsoleMenu.showMenu(Messages.get("manager.lesson_type"), ltOptions, false, false);
        LessonType lessonType = ltc == 2 ? LessonType.PRACTICE : LessonType.LECTURE;

        String room = ConsoleInput.readLine("  " + Messages.get("manager.room") + ": ");
        Lesson lesson = new Lesson(lessonId, lessonType, room, LocalDateTime.now(), teacher);
        course.addLesson(lesson);
        manager.assignTeacherToCourse(teacher, course, lesson);
        ConsoleMenu.printSuccess(Messages.get("manager.teacher_assigned"));
        ConsoleInput.waitForEnter();
    }

    private void addCourseForRegistration(Manager manager) {
        ConsoleMenu.printSection(Messages.get("manager.add_course"));
        String code = ConsoleInput.readLine("  " + Messages.get("manager.course_code") + ": ");
        String title = ConsoleInput.readLine("  " + Messages.get("manager.course_title") + ": ");
        int credits = ConsoleInput.readInt("  " + Messages.get("manager.credits") + ": ", 1, 30);

        Course course = new Course(code, title, credits);
        session.getSystem().addCourse(course);

        ConsoleMenu.printSuccess(Messages.get("manager.course_added", code, title));
        ConsoleInput.waitForEnter();
    }

    private void viewStudentsSorted(Manager manager, UniversitySystem system) {
        ConsoleMenu.printSection(Messages.get("manager.view_students"));
        LinkedHashMap<Integer, String> sortOptions = new LinkedHashMap<>();
        sortOptions.put(1, "By GPA (highest first)");
        sortOptions.put(2, "By GPA (lowest first)");
        sortOptions.put(3, "By Name (A-Z)");
        sortOptions.put(4, "By Name (Z-A)");
        sortOptions.put(5, "By Credits (most first)");
        sortOptions.put(6, "By Credits (least first)");

        int sc = ConsoleMenu.showMenu(Messages.get("manager.sort_by"), sortOptions, true, false);
        if (sc == 0) return;

        List<Student> sorted = switch (sc) {
            case 1 -> manager.viewStudentsSorted(system.getAllStudents(), new StudentByGpaComparator());
            case 2 -> manager.viewStudentsSorted(system.getAllStudents(), new StudentByGpaComparator().reversed());
            case 3 -> manager.viewStudentsSorted(system.getAllStudents(), new UserByNameComparator());
            case 4 -> manager.viewStudentsSorted(system.getAllStudents(), new UserByNameComparator().reversed());
            case 5 -> manager.viewStudentsSorted(system.getAllStudents(),
                    Comparator.<User, Integer>comparing(u -> ((Student) u).getTotalCredits(), Comparator.reverseOrder()));
            case 6 -> manager.viewStudentsSorted(system.getAllStudents(),
                    Comparator.comparingInt(u -> ((Student) u).getTotalCredits()));
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
        ConsoleMenu.printSection(Messages.get("manager.view_teachers"));
        LinkedHashMap<Integer, String> sortOptions = new LinkedHashMap<>();
        sortOptions.put(1, "By Name (A-Z)");
        sortOptions.put(2, "By Name (Z-A)");
        sortOptions.put(3, "By Rating (highest first)");
        sortOptions.put(4, "By Rating (lowest first)");

        int sc = ConsoleMenu.showMenu(Messages.get("manager.sort_by"), sortOptions, true, false);
        if (sc == 0) return;

        List<Teacher> sorted = switch (sc) {
            case 1 -> manager.viewTeachersSorted(system.getAllTeachers(), new UserByNameComparator());
            case 2 -> manager.viewTeachersSorted(system.getAllTeachers(), new UserByNameComparator().reversed());
            case 3 -> manager.viewTeachersSorted(system.getAllTeachers(), new TeacherByRatingComparator());
            case 4 -> manager.viewTeachersSorted(system.getAllTeachers(), new TeacherByRatingComparator().reversed());
            default -> system.getAllTeachers();
        };

        ConsoleMenu.printDivider();
        for (Teacher t : sorted) {
            System.out.printf("  %-20s | Position: %-15s | Rating: %.2f%n",
                    t.getName(), t.getPosition(), t.getAverageRating());
        }
        ConsoleMenu.printDivider();
        ConsoleInput.waitForEnter();
    }

    private void manageNews(Manager manager) {
        ConsoleMenu.printSection(Messages.get("manager.manage_news"));
        LinkedHashMap<Integer, String> options = new LinkedHashMap<>();
        options.put(1, Messages.get("manager.create_news"));
        options.put(2, Messages.get("manager.view_all_news"));
        int choice = ConsoleMenu.showMenu("News Management", options, true, false);

        if (choice == 1) {
            String title = ConsoleInput.readLine("  " + Messages.get("manager.news_title") + ": ");
            String content = ConsoleInput.readLine("  " + Messages.get("manager.news_content") + ": ");
            LinkedHashMap<Integer, String> topicOptions = new LinkedHashMap<>();
            topicOptions.put(1, "RESEARCH");
            topicOptions.put(2, "ACADEMIC");
            topicOptions.put(3, "EVENT");
            topicOptions.put(4, "GENERAL");
            int tc = ConsoleMenu.showMenu(Messages.get("manager.select_topic"), topicOptions, false, false);
            NewsTopic topic = switch (tc) {
                case 2 -> NewsTopic.ACADEMIC;
                case 3 -> NewsTopic.EVENT;
                case 4 -> NewsTopic.GENERAL;
                default -> NewsTopic.RESEARCH;
            };
            News news = new News(title, content, topic);
            newsService.publishNews(news);
            ConsoleMenu.printSuccess(Messages.get("manager.news_published"));
        } else if (choice == 2) {
            List<News> allNews = session.getSystem().getNewsList();
            ConsoleMenu.printSection(Messages.get("manager.view_all_news"));
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
        ConsoleMenu.printSection(Messages.get("manager.stat_title"));
        int studentCount = system.getAllStudents().size();
        int teacherCount = system.getAllTeachers().size();
        int courseCount = system.getCourses().size();
        int newsCount = system.getNewsList().size();
        int journalCount = system.getJournals().size();

        System.out.printf("  %s: %d%n", Messages.get("manager.stat_users"), system.getUsers().size());
        System.out.printf("  %s: %d%n", Messages.get("manager.stat_students"), studentCount);
        System.out.printf("  %s: %d%n", Messages.get("manager.stat_teachers"), teacherCount);
        System.out.printf("  %s: %d%n", Messages.get("manager.stat_courses"), courseCount);
        System.out.printf("  %s: %d%n", Messages.get("manager.stat_news"), newsCount);
        System.out.printf("  %s: %d%n", Messages.get("manager.stat_journals"), journalCount);

        if (!system.getAllStudents().isEmpty()) {
            double avgGpa = system.getAllStudents().stream()
                    .mapToDouble(Student::getGpa)
                    .average()
                    .orElse(0);
            System.out.printf("%n  %s: %.2f%n", Messages.get("manager.stat_avg_gpa"), avgGpa);
        }
        ConsoleInput.waitForEnter();
    }
}
