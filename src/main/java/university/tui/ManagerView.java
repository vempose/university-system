package university.tui;

import university.comparator.*;
import university.domain.academic.*;
import university.domain.communication.EmployeeRequest;
import university.domain.news.News;
import university.domain.support.AcademicReport;
import university.domain.user.*;
import university.enums.*;
import university.service.NewsService;
import university.service.ResearchService;
import university.system.UniversitySystem;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;

/// Manager panel — approve registrations, assign teachers,
/// add courses, view sorted students/teachers, manage news, stats.
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

    /// Shows the manager menu and handles user choices.
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
            options.put(7, Messages.get("manager.employee_requests"));
            options.put(8, Messages.get("manager.reports"));
            options.put(9, Messages.get("manager.view_messages"));
            options.put(10, Messages.get("manager.statistics"));
            options.put(11, Messages.get("manager.browse_courses"));

            int choice = ConsoleMenu.showMenu(Messages.get("manager.title"), options, true, false);
            switch (choice) {
                case 0 -> { return; }
                case 1 -> approveRegistrations(manager);
                case 2 -> assignTeacherToCourse(manager);
                case 3 -> addCourseForRegistration(manager);
                case 4 -> viewStudentsSorted(manager, system);
                case 5 -> viewTeachersSorted(manager, system);
                case 6 -> manageNews(manager);
                case 7 -> manageEmployeeRequests(manager);
                case 8 -> manageReports(manager);
                case 9 -> messageView.show(manager);
                case 10 -> viewStatistics(system);
                case 11 -> courseView.show();
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
                    "  [%d]  %s | %s - %s | %s %d%n",
                    i + 1,
                    e.getStudent().getName(),
                    e.getCourse().getCourseCode(),
                    e.getCourse().getTitle(),
                    Messages.get("manager.attempt"),
                    e.getAttemptNo()
            );
        }

        int ei = ConsoleInput.readInt("\n  " + Messages.get("manager.cancel_hint") + ": ", 0, pending.size());
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

        Course course = ConsoleMenu.pickFromList(courses,
                c -> c.getCourseCode() + " - " + c.getTitle(),
                Messages.get("manager.select_course_for"));

        Teacher teacher = ConsoleMenu.pickFromList(teachers,
                t -> t.getName() + " (" + t.getPosition() + ")",
                Messages.get("manager.select_teacher_for"));

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

        List<School> schools = new java.util.ArrayList<>();
        for (User u : session.getSystem().getUsers()) {
            if (u instanceof Student s && s.getSchool() != null && !schools.contains(s.getSchool()))
                schools.add(s.getSchool());
            if (u instanceof Employee e && e.getSchool() != null && !schools.contains(e.getSchool()))
                schools.add(e.getSchool());
        }

        if (!schools.isEmpty()) {
            School school = ConsoleMenu.pickFromList(schools, School::getName,
                    Messages.get("admin.select_school"));

            Major major = ConsoleMenu.pickFromList(school.getMajors(), Major::getName,
                    Messages.get("manager.select_major"));

            int year = ConsoleInput.readInt("  " + Messages.get("manager.select_year") + ": ", 1, 6);

            LinkedHashMap<Integer, String> catOptions = new LinkedHashMap<>();
            catOptions.put(1, "MAJOR");
            catOptions.put(2, "MINOR");
            catOptions.put(3, "FREE_ELECTIVE");
            int cc = ConsoleMenu.showMenu(Messages.get("manager.select_category"), catOptions, false, false);
            CourseCategory category = switch (cc) {
                case 2 -> CourseCategory.MINOR;
                case 3 -> CourseCategory.FREE_ELECTIVE;
                default -> CourseCategory.MAJOR;
            };

            manager.addCourseForRegistration(course, major, year, category);
            ConsoleMenu.printSuccess(Messages.get("manager.course_requirement_added",
                    major.getName(), String.valueOf(year), category.name()));
        } else {
            ConsoleMenu.printInfo(Messages.get("manager.course_added", code, title));
        }
        ConsoleInput.waitForEnter();
    }

    private void viewStudentsSorted(Manager manager, UniversitySystem system) {
        ConsoleMenu.printSection(Messages.get("manager.view_students"));
        LinkedHashMap<Integer, String> sortOptions = new LinkedHashMap<>();
        sortOptions.put(1, Messages.get("manager.sort_gpa_high"));
        sortOptions.put(2, Messages.get("manager.sort_gpa_low"));
        sortOptions.put(3, Messages.get("manager.sort_name_az"));
        sortOptions.put(4, Messages.get("manager.sort_name_za"));
        sortOptions.put(5, Messages.get("manager.sort_credits_most"));
        sortOptions.put(6, Messages.get("manager.sort_credits_least"));

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
            System.out.printf("  %-20s | GPA: %.2f | %s: %d | %s%n",
                    s.getName(), s.getGpa(), Messages.get("manager.credits_word"), s.getTotalCredits(), s.getDegreeType());
        }
        ConsoleMenu.printDivider();
        ConsoleInput.waitForEnter();
    }

    private void viewTeachersSorted(Manager manager, UniversitySystem system) {
        ConsoleMenu.printSection(Messages.get("manager.view_teachers"));
        LinkedHashMap<Integer, String> sortOptions = new LinkedHashMap<>();
        sortOptions.put(1, Messages.get("manager.sort_name_az"));
        sortOptions.put(2, Messages.get("manager.sort_name_za"));
        sortOptions.put(3, Messages.get("manager.sort_rating_high"));
        sortOptions.put(4, Messages.get("manager.sort_rating_low"));

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
        int choice = ConsoleMenu.showMenu(Messages.get("manager.manage_news"), options, true, false);

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
            List<News> allNews = session.getSystem().getNewsList().stream()
                    .sorted(Comparator.comparing(News::isPinned).reversed())
                    .toList();
            ConsoleMenu.printSection(Messages.get("manager.view_all_news"));
            if (allNews.isEmpty()) {
                ConsoleMenu.printInfo(Messages.get("news.no_news"));
            } else {
                for (News n : allNews) {
                    System.out.printf("  %s%s | %s%n",
                            n.isPinned() ? Messages.get("news.pinned") + " " : "",
                            n.getTopic(), n.getTitle());
                    System.out.println("    " + n.getContent());
                    System.out.println("    " + Messages.get("news.comments_header", String.valueOf(n.getComments().size())));
                    System.out.println();
                }
            }
        }
        ConsoleInput.waitForEnter();
    }

    private void manageEmployeeRequests(Manager manager) {
        ConsoleMenu.printSection(Messages.get("manager.employee_requests"));
        List<Manager> allMgrs = session.getSystem().getUsers().stream()
                .filter(u -> u instanceof Manager)
                .map(u -> (Manager) u)
                .toList();
        List<EmployeeRequest> requests = allMgrs.stream()
                .flatMap(m -> m.viewEmployeeRequests().stream())
                .toList();

        if (requests.isEmpty()) {
            ConsoleMenu.printInfo(Messages.get("manager.no_requests"));
            ConsoleInput.waitForEnter();
            return;
        }

        for (int i = 0; i < requests.size(); i++) {
            EmployeeRequest r = requests.get(i);
            String signed = r.getSignedBy() != null ? " | Signed by: " + r.getSignedBy().getName() : "";
            System.out.printf("  [%d]  %s | %s | %s%s%n", i + 1,
                    r.getSender().getName(), r.getStatus(), r.getDescription(), signed);
        }

        int ri = ConsoleInput.readInt("\n  " + Messages.get("menu.choose") + ": ", 0, requests.size());
        if (ri == 0) return;
        EmployeeRequest selected = requests.get(ri - 1);

        LinkedHashMap<Integer, String> actOptions = new LinkedHashMap<>();
        actOptions.put(1, Messages.get("manager.sign_request"));
        actOptions.put(2, Messages.get("techsupport.accept"));
        actOptions.put(3, Messages.get("techsupport.reject"));
        int ac = ConsoleMenu.showMenu(Messages.get("menu.choose"), actOptions, true, false);
        if (ac == 1) {
            try {
                selected.view();
                selected.sign(manager);
                ConsoleMenu.printSuccess(Messages.get("manager.request_signed"));
            } catch (IllegalStateException ex) {
                ConsoleMenu.printError(ex.getMessage());
            }
        } else if (ac == 2) {
            try {
                selected.view();
                selected.accept();
                ConsoleMenu.printSuccess(Messages.get("manager.request_accepted"));
            } catch (IllegalStateException ex) {
                ConsoleMenu.printError(ex.getMessage());
            }
        } else if (ac == 3) {
            try {
                selected.view();
                selected.reject();
                ConsoleMenu.printSuccess(Messages.get("manager.request_rejected"));
            } catch (IllegalStateException ex) {
                ConsoleMenu.printError(ex.getMessage());
            }
        }
        ConsoleInput.waitForEnter();
    }

    private void manageReports(Manager manager) {
        ConsoleMenu.printSection(Messages.get("manager.reports"));
        LinkedHashMap<Integer, String> options = new LinkedHashMap<>();
        options.put(1, Messages.get("manager.create_report"));
        options.put(2, Messages.get("manager.view_reports"));
        int choice = ConsoleMenu.showMenu(Messages.get("manager.reports"), options, true, false);

        if (choice == 1) {
            AcademicReport report = manager.createAcademicReport();
            List<Student> allStudents = session.getSystem().getAllStudents();
            for (Student s : allStudents) {
                for (Enrollment e : s.getEnrollments()) {
                    e.getMark().ifPresent(m ->
                            report.addEntry("%s | %s | %s: %.0f".formatted(
                                    s.getName(), e.getCourse().getCourseCode(),
                                    Messages.get("student.mark_label"), m.getTotal())));
                }
            }
            System.out.println(report.generateMarksReport());
            ConsoleMenu.printSuccess(Messages.get("manager.report_created", report.getId()));
        } else if (choice == 2) {
            List<AcademicReport> reports = manager.getCreatedReports();
            if (reports.isEmpty()) {
                ConsoleMenu.printInfo(Messages.get("manager.no_reports"));
            } else {
                for (AcademicReport r : reports) {
                    System.out.println(r);
                    System.out.println(r.generateStatistics());
                    ConsoleMenu.printDivider();
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
