package university;

import university.domain.academic.*;
import university.domain.communication.EmployeeRequest;
import university.domain.communication.OfficialMessage;
import university.domain.news.News;
import university.domain.news.NewsComment;
import university.domain.news.UniversityJournal;
import university.domain.research.ResearchPaper;
import university.domain.research.ResearchProject;
import university.domain.student.OrganizationMembership;
import university.domain.student.StudentOrganization;
import university.domain.support.TechSupportRequest;
import university.domain.user.*;
import university.enums.*;
import university.exception.CreditLimitExceededException;
import university.exception.InvalidSupervisorException;
import university.exception.NonResearcherJoinProjectException;
import university.service.NewsService;
import university.service.ResearchService;
import university.system.UniversitySystem;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/// Fills the system with sample data for testing.
///
/// Creates users, courses, lessons, enrollments, research papers, journals,
/// news, tech support requests, student orgs, messages, complaints, etc.
/// Everything you need to see the app actually do something.
public final class MockData {

    /// Utility class — no instantiation.
    private MockData() {
    }

    /// Fills the given system with mock data.
    ///
    /// Calls all the private createX methods to build up a realistic state.
    ///
    /// @param system the system to populate
    public static void populate(UniversitySystem system) {
        ResearchService researchService = new ResearchService(system);
        NewsService newsService = new NewsService(system, researchService);

        createUsers(system);
        createCourses(system);
        createLessons(system);
        createEnrollmentsAndMarks(system);
        createResearchPapers(system, newsService);
        createResearchProjects(system);
        createJournals(system);
        createNews(system, newsService);
        createTechSupportRequests(system);
        createStudentOrganizations(system);
        createMessages(system);
        createComplaints(system);
        createAttendance(system);
        createEmployeeRequests(system);
    }

    private static void createUsers(UniversitySystem system) {
        system.addUser(new Admin(UUID.randomUUID().toString(), "Alice Admin",
                "alice@uni.edu", "admin123", Language.EN, 5000.0));

        system.addUser(new Manager(UUID.randomUUID().toString(), "Bob Manager",
                "bob@uni.edu", "manager123", Language.EN, 4500.0, ManagerType.OR));

        system.addUser(new Manager(UUID.randomUUID().toString(), "Carol DeptHead",
                "carol.d@uni.edu", "manager123", Language.EN, 4200.0, ManagerType.DEPARTMENT));

        system.addUser(new Manager(UUID.randomUUID().toString(), "Dave Dean",
                "dave.d@uni.edu", "manager123", Language.EN, 5500.0, ManagerType.DEAN));

        system.addUser(new Teacher(UUID.randomUUID().toString(), "Eva Lector",
                "eva@uni.edu", "teacher123", Language.EN, 3000.0, TeacherPosition.LECTOR));

        system.addUser(new Teacher(UUID.randomUUID().toString(), "Frank Senior",
                "frank@uni.edu", "teacher123", Language.EN, 3500.0, TeacherPosition.SENIOR_LECTOR));

        system.addUser(new Teacher(UUID.randomUUID().toString(), "Grace Tutor",
                "grace@uni.edu", "teacher123", Language.EN, 2500.0, TeacherPosition.TUTOR));

        Teacher henry = new Teacher(UUID.randomUUID().toString(), "Henry Prof",
                "henry@uni.edu", "teacher123", Language.EN, 5000.0, TeacherPosition.TUTOR);
        henry.setPosition(TeacherPosition.PROFESSOR);
        system.addUser(henry);

        Teacher irene = new Teacher(UUID.randomUUID().toString(), "Irene Prof",
                "irene@uni.edu", "teacher123", Language.EN, 4800.0, TeacherPosition.TUTOR);
        irene.setPosition(TeacherPosition.PROFESSOR);
        system.addUser(irene);

        system.addUser(new TechSupportSpecialist(UUID.randomUUID().toString(),
                "Nick Tech", "nick@uni.edu", "tech123", Language.EN, 2800.0));

        system.addUser(new TechSupportSpecialist(UUID.randomUUID().toString(),
                "Olga Support", "olga@uni.edu", "tech123", Language.EN, 2600.0));

        School seds = new School("School of Engineering and Digital Sciences");
        Major csMajor = new Major("Computer Science", seds);
        Major eeMajor = new Major("Electrical Engineering", seds);
        seds.addMajor(csMajor);
        seds.addMajor(eeMajor);

        School ssh = new School("School of Sciences and Humanities");
        Major mathMajor = new Major("Mathematics", ssh);
        Major physMajor = new Major("Physics", ssh);
        ssh.addMajor(mathMajor);
        ssh.addMajor(physMajor);

        Student jack = new Student(UUID.randomUUID().toString(), "Jack Bachelor",
                "jack@uni.edu", "student123", Language.EN, DegreeType.BACHELOR, csMajor);
        jack.setSchool(seds);
        system.addUser(jack);

        GraduateStudent kate = new GraduateStudent(UUID.randomUUID().toString(),
                "Kate Master", "kate@uni.edu", "student123", Language.EN,
                DegreeType.MASTER, csMajor);
        kate.setSchool(seds);
        system.addUser(kate);

        GraduateStudent leo = new GraduateStudent(UUID.randomUUID().toString(),
                "Leo PhD", "leo@uni.edu", "student123", Language.EN,
                DegreeType.PHD, eeMajor);
        leo.setSchool(seds);
        system.addUser(leo);

        Student mia = new Student(UUID.randomUUID().toString(), "Mia Bachelor",
                "mia@uni.edu", "student123", Language.EN, DegreeType.BACHELOR, mathMajor);
        mia.setSchool(ssh);
        system.addUser(mia);
    }

    private static void createCourses(UniversitySystem system) {
        system.addCourse(new Course("CS101", "Introduction to Programming", 5));
        system.addCourse(new Course("CS201", "Data Structures and Algorithms", 4));
        system.addCourse(new Course("CS301", "Operating Systems", 5));
        system.addCourse(new Course("CS401", "Machine Learning", 4));
        system.addCourse(new Course("EE101", "Circuit Analysis", 4));
        system.addCourse(new Course("EE201", "Digital Logic Design", 3));
        system.addCourse(new Course("MATH201", "Linear Algebra", 3));
        system.addCourse(new Course("MATH301", "Probability and Statistics", 3));
        system.addCourse(new Course("PHYS101", "Classical Mechanics", 4));
        system.addCourse(new Course("PHYS201", "Electromagnetism", 4));
        system.addCourse(new Course("BUS101", "Principles of Management", 3));
        system.addCourse(new Course("BUS201", "Financial Accounting", 3));
    }

    private static void createLessons(UniversitySystem system) {
        List<Course> courses = system.getCourses();
        Teacher eva = findTeacher(system, "Eva Lector");
        Teacher frank = findTeacher(system, "Frank Senior");
        Teacher grace = findTeacher(system, "Grace Tutor");
        Teacher henry = findTeacher(system, "Henry Prof");
        Teacher irene = findTeacher(system, "Irene Prof");

        Course cs101 = findCourse(courses, "CS101");
        Course cs201 = findCourse(courses, "CS201");
        Course cs301 = findCourse(courses, "CS301");
        Course cs401 = findCourse(courses, "CS401");
        Course ee101 = findCourse(courses, "EE101");
        Course ee201 = findCourse(courses, "EE201");
        Course math201 = findCourse(courses, "MATH201");
        Course math301 = findCourse(courses, "MATH301");
        Course phys101 = findCourse(courses, "PHYS101");
        Course phys201 = findCourse(courses, "PHYS201");
        Course bus101 = findCourse(courses, "BUS101");
        Course bus201 = findCourse(courses, "BUS201");

        cs101.addLesson(new Lesson("L-CS101-01", LessonType.LECTURE, "Room 301",
                LocalDateTime.of(2025, 1, 15, 9, 0), eva));
        cs101.addLesson(new Lesson("P-CS101-01", LessonType.PRACTICE, "Lab 101",
                LocalDateTime.of(2025, 1, 16, 11, 0), grace));
        eva.addAssignedCourse(cs101);
        grace.addAssignedCourse(cs101);

        cs201.addLesson(new Lesson("L-CS201-01", LessonType.LECTURE, "Room 302",
                LocalDateTime.of(2025, 1, 15, 10, 0), frank));
        cs201.addLesson(new Lesson("P-CS201-01", LessonType.PRACTICE, "Lab 102",
                LocalDateTime.of(2025, 1, 17, 9, 0), eva));
        frank.addAssignedCourse(cs201);
        eva.addAssignedCourse(cs201);

        cs301.addLesson(new Lesson("L-CS301-01", LessonType.LECTURE, "Room 401",
                LocalDateTime.of(2025, 1, 20, 13, 0), henry));
        cs301.addLesson(new Lesson("P-CS301-01", LessonType.PRACTICE, "Lab 201",
                LocalDateTime.of(2025, 1, 21, 15, 0), grace));
        henry.addAssignedCourse(cs301);
        grace.addAssignedCourse(cs301);

        cs401.addLesson(new Lesson("L-CS401-01", LessonType.LECTURE, "Room 402",
                LocalDateTime.of(2025, 1, 18, 11, 0), henry));
        cs401.addLesson(new Lesson("P-CS401-01", LessonType.PRACTICE, "Lab 202",
                LocalDateTime.of(2025, 1, 19, 14, 0), frank));
        henry.addAssignedCourse(cs401);
        frank.addAssignedCourse(cs401);

        ee101.addLesson(new Lesson("L-EE101-01", LessonType.LECTURE, "Room 501",
                LocalDateTime.of(2025, 1, 15, 9, 0), irene));
        ee101.addLesson(new Lesson("P-EE101-01", LessonType.PRACTICE, "Lab 301",
                LocalDateTime.of(2025, 1, 17, 11, 0), frank));
        irene.addAssignedCourse(ee101);
        frank.addAssignedCourse(ee101);

        ee201.addLesson(new Lesson("L-EE201-01", LessonType.LECTURE, "Room 502",
                LocalDateTime.of(2025, 1, 22, 10, 0), irene));
        ee201.addLesson(new Lesson("P-EE201-01", LessonType.PRACTICE, "Lab 302",
                LocalDateTime.of(2025, 1, 23, 13, 0), eva));
        irene.addAssignedCourse(ee201);
        eva.addAssignedCourse(ee201);

        math201.addLesson(new Lesson("L-MATH201-01", LessonType.LECTURE, "Room 601",
                LocalDateTime.of(2025, 1, 15, 13, 0), frank));
        math201.addLesson(new Lesson("P-MATH201-01", LessonType.PRACTICE, "Room 602",
                LocalDateTime.of(2025, 1, 18, 9, 0), grace));
        frank.addAssignedCourse(math201);
        grace.addAssignedCourse(math201);

        math301.addLesson(new Lesson("L-MATH301-01", LessonType.LECTURE, "Room 603",
                LocalDateTime.of(2025, 1, 20, 9, 0), frank));
        frank.addAssignedCourse(math301);

        phys101.addLesson(new Lesson("L-PHYS101-01", LessonType.LECTURE, "Room 701",
                LocalDateTime.of(2025, 1, 16, 10, 0), henry));
        phys101.addLesson(new Lesson("P-PHYS101-01", LessonType.PRACTICE, "Lab 401",
                LocalDateTime.of(2025, 1, 19, 9, 0), irene));
        henry.addAssignedCourse(phys101);
        irene.addAssignedCourse(phys101);

        phys201.addLesson(new Lesson("L-PHYS201-01", LessonType.LECTURE, "Room 702",
                LocalDateTime.of(2025, 1, 24, 11, 0), irene));
        irene.addAssignedCourse(phys201);

        bus101.addLesson(new Lesson("L-BUS101-01", LessonType.LECTURE, "Room 801",
                LocalDateTime.of(2025, 1, 16, 14, 0), eva));
        eva.addAssignedCourse(bus101);

        bus201.addLesson(new Lesson("L-BUS201-01", LessonType.LECTURE, "Room 802",
                LocalDateTime.of(2025, 1, 25, 9, 0), grace));
        grace.addAssignedCourse(bus201);
    }

    private static void createEnrollmentsAndMarks(UniversitySystem system) {
        List<Course> courses = system.getCourses();
        Course cs101 = findCourse(courses, "CS101");
        Course cs201 = findCourse(courses, "CS201");
        Course cs301 = findCourse(courses, "CS301");
        Course cs401 = findCourse(courses, "CS401");
        Course ee101 = findCourse(courses, "EE101");
        Course math201 = findCourse(courses, "MATH201");
        Course math301 = findCourse(courses, "MATH301");
        Course phys101 = findCourse(courses, "PHYS101");
        Course bus101 = findCourse(courses, "BUS101");

        Teacher eva = findTeacher(system, "Eva Lector");
        Teacher henry = findTeacher(system, "Henry Prof");
        Teacher irene = findTeacher(system, "Irene Prof");
        Manager bob = findManager(system, "Bob Manager");
        Manager carol = findManager(system, "Carol DeptHead");

        Student jack = findStudent(system, "Jack Bachelor");
        GraduateStudent kate = findGradStudent(system, "Kate Master");
        GraduateStudent leo = findGradStudent(system, "Leo PhD");
        Student mia = findStudent(system, "Mia Bachelor");

        jack.setGpa(3.5);
        kate.setGpa(3.9);
        leo.setGpa(3.7);
        mia.setGpa(3.2);

        registerAndApprove(jack, cs101, bob, eva, new Mark(27, 25, 35));
        registerAndApprove(jack, cs201, carol, henry, new Mark(20, 22, 30));
        registerAndApprove(jack, math201, carol, irene, new Mark(25, 28, 36));

        registerAndApprove(kate, cs301, bob, henry, new Mark(28, 29, 38));
        registerAndApprove(kate, cs401, carol, irene, new Mark(26, 27, 35));
        registerAndApprove(kate, math301, bob, eva, new Mark(30, 28, 39));

        registerAndApprove(leo, ee101, carol, irene, new Mark(22, 24, 32));
        registerAndApprove(leo, cs301, bob, henry, new Mark(24, 26, 34));
        registerAndApprove(leo, phys101, carol, irene, new Mark(20, 22, 30));

        registerAndApprove(mia, math201, bob, irene, new Mark(18, 20, 28));
        registerAndApprove(mia, bus101, carol, eva, new Mark(15, 18, 25));
        registerAndApprove(mia, phys101, bob, henry, new Mark(14, 16, 22));

        jack.rateTeacher(eva, 5, "Excellent teaching style, very clear explanations.");
        jack.rateTeacher(henry, 4, "Good lectures, sometimes moves too fast.");
        kate.rateTeacher(henry, 5, "Brilliant professor, inspiring lectures.");
        kate.rateTeacher(irene, 4, "Solid teaching, great lab sessions.");
        leo.rateTeacher(irene, 5, "Best physics teacher I've had.");
        mia.rateTeacher(irene, 3, "Lectures are okay, grading is tough.");
        mia.rateTeacher(eva, 4, "Helpful and patient with questions.");
    }

    private static void registerAndApprove(Student student, Course course,
                                           Manager manager, Teacher teacher, Mark mark) {
        try {
            Enrollment enrollment = student.registerForCourse(course);
            manager.approveRegistration(enrollment);
            enrollment.register();
            teacher.putMark(enrollment, mark);
        } catch (CreditLimitExceededException e) {
            System.err.println("MockData: " + e.getMessage());
        }
    }

    private static void createResearchPapers(UniversitySystem system, NewsService newsService) {
        Teacher henry = findTeacher(system, "Henry Prof");
        Teacher irene = findTeacher(system, "Irene Prof");
        GraduateStudent kate = findGradStudent(system, "Kate Master");
        GraduateStudent leo = findGradStudent(system, "Leo PhD");

        ResearchPaper paper1 = new ResearchPaper(
                "Deep Neural Networks for Image Recognition",
                "Henry Prof, Jane Coauthor", "IEEE Transactions on Pattern Analysis",
                "1-15", 15, LocalDate.of(2023, 3, 15), "10.1109/TPAMI.2023.001", 45);
        henry.getResearchProfile().publishPaper(paper1);
        newsService.announcePaperPublication(paper1);

        ResearchPaper paper2 = new ResearchPaper(
                "Efficient Graph Algorithms for Large-Scale Networks", "Henry Prof",
                "ACM Computing Surveys", "20-38", 19,
                LocalDate.of(2023, 6, 20), "10.1145/CS.2023.002", 30);
        henry.getResearchProfile().publishPaper(paper2);

        ResearchPaper paper3 = new ResearchPaper(
                "Quantum Computing Approaches to Optimization",
                "Henry Prof, Kate Master", "Nature Computational Science",
                "45-60", 16, LocalDate.of(2024, 1, 10), "10.1038/NCS.2024.003", 12);
        henry.getResearchProfile().publishPaper(paper3);
        kate.getResearchProfile().publishPaper(paper3);
        kate.addDiplomaPaper(paper3);

        ResearchPaper paper4 = new ResearchPaper(
                "Stochastic Processes in Financial Modeling", "Irene Prof",
                "Journal of Applied Probability", "100-120", 21,
                LocalDate.of(2023, 9, 5), "10.1017/JAP.2023.004", 25);
        irene.getResearchProfile().publishPaper(paper4);
        newsService.announcePaperPublication(paper4);

        ResearchPaper paper5 = new ResearchPaper(
                "Nonlinear Dynamics in Plasma Physics", "Irene Prof, Leo PhD",
                "Physical Review Letters", "015001-1-015001-8", 8,
                LocalDate.of(2024, 2, 28), "10.1103/PRL.2024.005", 18);
        irene.getResearchProfile().publishPaper(paper4);
        irene.getResearchProfile().publishPaper(paper5);
        leo.getResearchProfile().publishPaper(paper5);

        ResearchPaper extraPaper = new ResearchPaper(
                "Advanced Statistical Methods in Experimental Physics", "Irene Prof",
                "Nuclear Instruments and Methods", "330-345", 16,
                LocalDate.of(2024, 3, 10), "10.1016/NIMA.2024.008", 20);
        irene.getResearchProfile().publishPaper(extraPaper);
        newsService.announcePaperPublication(extraPaper);

        leo.addDiplomaPaper(paper5);
        newsService.announcePaperPublication(paper5);

        ResearchPaper paper6 = new ResearchPaper(
                "Machine Learning Applications in Education", "Kate Master",
                "Journal of Educational Technology", "1-10", 10,
                LocalDate.of(2024, 4, 12), "10.1000/JET.2024.006", 8);
        kate.getResearchProfile().publishPaper(paper6);
        kate.addDiplomaPaper(paper6);

        ResearchPaper paper7 = new ResearchPaper(
                "Topological Data Analysis for Big Data", "Leo PhD, Henry Prof",
                "SIAM Journal on Computing", "55-70", 16,
                LocalDate.of(2024, 5, 30), "10.1137/SICOMP.2024.007", 6);
        leo.getResearchProfile().publishPaper(paper7);
        henry.getResearchProfile().publishPaper(paper7);
        leo.addDiplomaPaper(paper7);

        try {
            kate.setSupervisor(henry.getResearchProfile());
            leo.setSupervisor(irene.getResearchProfile());
        } catch (InvalidSupervisorException e) {
            System.err.println("MockData: " + e.getMessage());
        }

        henry.getResearchProfile().calculateHIndex();
        irene.getResearchProfile().calculateHIndex();
        kate.getResearchProfile().calculateHIndex();
        leo.getResearchProfile().calculateHIndex();
    }

    private static void createResearchProjects(UniversitySystem system) {
        Teacher henry = findTeacher(system, "Henry Prof");
        Teacher irene = findTeacher(system, "Irene Prof");
        GraduateStudent leo = findGradStudent(system, "Leo PhD");

        ResearchProject project1 = new ResearchProject("RP-2024-01",
                "AI-Driven Healthcare Diagnostics");
        try {
            project1.addParticipant(henry.getResearchProfile());
            project1.addParticipant(irene.getResearchProfile());
            henry.getResearchProfile().joinProject(project1);
            irene.getResearchProfile().joinProject(project1);
        } catch (NonResearcherJoinProjectException e) {
            System.err.println("MockData: " + e.getMessage());
        }

        ResearchProject project2 = new ResearchProject("RP-2024-02",
                "Quantum Computing for Materials Science");
        try {
            project2.addParticipant(irene.getResearchProfile());
            project2.addParticipant(leo.getResearchProfile());
            irene.getResearchProfile().joinProject(project2);
            leo.getResearchProfile().joinProject(project2);
        } catch (NonResearcherJoinProjectException e) {
            System.err.println("MockData: " + e.getMessage());
        }
    }

    private static void createJournals(UniversitySystem system) {
        UniversityJournal journal1 = new UniversityJournal(
                "IEEE Transactions on Computer Science");
        UniversityJournal journal2 = new UniversityJournal(
                "Journal of Applied Mathematics");
        UniversityJournal journal3 = new UniversityJournal(
                "Physical Review Research");

        Teacher henry = findTeacher(system, "Henry Prof");
        Teacher irene = findTeacher(system, "Irene Prof");
        Student jack = findStudent(system, "Jack Bachelor");
        GraduateStudent kate = findGradStudent(system, "Kate Master");
        GraduateStudent leo = findGradStudent(system, "Leo PhD");
        Teacher eva = findTeacher(system, "Eva Lector");

        journal1.subscribe(henry);
        journal1.subscribe(kate);
        journal1.subscribe(jack);
        journal2.subscribe(irene);
        journal2.subscribe(leo);
        journal2.subscribe(kate);
        journal3.subscribe(henry);
        journal3.subscribe(irene);
        journal3.subscribe(leo);
        journal3.subscribe(eva);

        henry.getResearchProfile().getPapers().stream().findFirst()
                .ifPresent(journal1::publishPaper);
        irene.getResearchProfile().getPapers().stream().findFirst()
                .ifPresent(journal2::publishPaper);

        system.addJournal(journal1);
        system.addJournal(journal2);
        system.addJournal(journal3);
    }

    private static void createNews(UniversitySystem system, NewsService newsService) {
        newsService.addOfficialEventNews(
                "Spring 2025 Semester Begins",
                "Welcome to the Spring 2025 semester! Classes start January 15, 2025.");

        newsService.addOfficialEventNews(
                "New Lab Equipment Installed",
                "The School of Engineering has installed new oscilloscopes in Lab 301.");

        newsService.publishNews(new News(
                "Guest Lecture: Quantum Computing Frontiers",
                "Dr. Richard Feynman III will deliver a guest lecture on February 10, 2025.",
                NewsTopic.ACADEMIC));

        newsService.publishNews(new News(
                "Student Research Symposium 2025",
                "Submit your papers by March 15 for the annual Student Research Symposium.",
                NewsTopic.RESEARCH));

        newsService.announceTopCitedResearcher();

        List<News> newsList = system.getNewsList();
        if (!newsList.isEmpty()) {
            newsList.getFirst().addComment(new NewsComment(
                    "Excited for the new semester!",
                    findStudent(system, "Jack Bachelor")));
            newsList.getFirst().addComment(new NewsComment(
                    "Looking forward to teaching this semester.",
                    findTeacher(system, "Eva Lector")));
        }
    }

    private static void createTechSupportRequests(UniversitySystem system) {
        TechSupportSpecialist nick = findTechSupport(system, "Nick Tech");
        TechSupportSpecialist olga = findTechSupport(system, "Olga Support");
        Student jack = findStudent(system, "Jack Bachelor");
        Teacher eva = findTeacher(system, "Eva Lector");
        Student mia = findStudent(system, "Mia Bachelor");

        TechSupportRequest req1 = new TechSupportRequest(jack,
                "Projector in Room 301 is not working.");
        nick.assignRequest(req1);
        nick.acceptRequest(req1);
        nick.completeRequest(req1);

        TechSupportRequest req2 = new TechSupportRequest(eva,
                "Printer in the faculty lounge is jammed.");
        olga.assignRequest(req2);
        olga.acceptRequest(req2);

        TechSupportRequest req3 = new TechSupportRequest(jack,
                "Cannot access the student portal since yesterday.");
        nick.assignRequest(req3);

        TechSupportRequest req4 = new TechSupportRequest(mia,
                "WiFi is unstable in the library.");
        olga.assignRequest(req4);
        olga.rejectRequest(req4);
    }

    private static void createStudentOrganizations(UniversitySystem system) {
        Student jack = findStudent(system, "Jack Bachelor");
        GraduateStudent kate = findGradStudent(system, "Kate Master");
        Student mia = findStudent(system, "Mia Bachelor");
        GraduateStudent leo = findGradStudent(system, "Leo PhD");

        StudentOrganization codingClub = new StudentOrganization(
                "CS Programming Club",
                "A club for competitive programming and hackathons.");
        codingClub.addMembership(new OrganizationMembership(
                jack, codingClub, OrganizationRole.HEAD));
        codingClub.addMembership(new OrganizationMembership(
                kate, codingClub, OrganizationRole.MEMBER));
        jack.addMembership(new OrganizationMembership(
                jack, codingClub, OrganizationRole.HEAD));
        kate.addMembership(new OrganizationMembership(
                kate, codingClub, OrganizationRole.MEMBER));

        StudentOrganization roboticsClub = new StudentOrganization(
                "Robotics Club",
                "Building robots for inter-university competitions.");
        roboticsClub.addMembership(new OrganizationMembership(
                leo, roboticsClub, OrganizationRole.HEAD));
        roboticsClub.addMembership(new OrganizationMembership(
                mia, roboticsClub, OrganizationRole.MEMBER));
        leo.addMembership(new OrganizationMembership(
                leo, roboticsClub, OrganizationRole.HEAD));
        mia.addMembership(new OrganizationMembership(
                mia, roboticsClub, OrganizationRole.MEMBER));

        StudentOrganization mathSociety = new StudentOrganization(
                "Mathematics Society",
                "Exploring advanced topics in pure and applied mathematics.");
        mathSociety.addMembership(new OrganizationMembership(
                mia, mathSociety, OrganizationRole.HEAD));
        mathSociety.addMembership(new OrganizationMembership(
                kate, mathSociety, OrganizationRole.MEMBER));
        mia.addMembership(new OrganizationMembership(
                mia, mathSociety, OrganizationRole.HEAD));
        kate.addMembership(new OrganizationMembership(
                kate, mathSociety, OrganizationRole.MEMBER));
    }

    private static void createMessages(UniversitySystem system) {
        Teacher eva = findTeacher(system, "Eva Lector");
        Manager bob = findManager(system, "Bob Manager");

        eva.sendMessage(bob,
                "Hi Bob, I need an additional projector for Room 301 next Tuesday.");
        bob.sendMessage(eva,
                "Sure Eva, I'll arrange it. Please submit a formal request as well.");

        OfficialMessage officialMsg = new OfficialMessage(bob, eva,
                "Exam room booking confirmed for CS101 final exam on January 30, 2025, Room 301.",
                "Exam Room Booking");
    }

    private static void createComplaints(UniversitySystem system) {
        Teacher irene = findTeacher(system, "Irene Prof");
        Manager bob = findManager(system, "Bob Manager");
        Student mia = findStudent(system, "Mia Bachelor");

        irene.sendComplaint(List.of(mia), UrgencyLevel.MEDIUM,
                "Student consistently submits assignments late and disrupts lab sessions.",
                bob);
    }

    private static void createAttendance(UniversitySystem system) {
        Teacher eva = findTeacher(system, "Eva Lector");
        Teacher henry = findTeacher(system, "Henry Prof");
        Student jack = findStudent(system, "Jack Bachelor");
        GraduateStudent kate = findGradStudent(system, "Kate Master");

        List<Course> courses = system.getCourses();
        Course cs101 = findCourse(courses, "CS101");
        Course cs301 = findCourse(courses, "CS301");

        eva.markAttendance(cs101.getLessons().getFirst(), jack, AttendanceStatus.PRESENT);
        eva.markAttendance(cs101.getLessons().getFirst(), kate, AttendanceStatus.PRESENT);
        henry.markAttendance(cs301.getLessons().getFirst(), kate, AttendanceStatus.PRESENT);
    }

    private static void createEmployeeRequests(UniversitySystem system) {
        Manager bob = findManager(system, "Bob Manager");
        Teacher eva = findTeacher(system, "Eva Lector");

        EmployeeRequest req1 = new EmployeeRequest(eva,
                "Requesting approval to attend IEEE Conference in April 2025.");
        req1.view();
        req1.sign(bob);
        req1.done();
        bob.addEmployeeRequest(req1);

        EmployeeRequest req2 = new EmployeeRequest(eva,
                "Proposal for new Computer Vision elective course for Fall 2025.");
        req2.view();
        req2.sign(bob);
        bob.addEmployeeRequest(req2);
    }

    private static Teacher findTeacher(UniversitySystem system, String name) {
        return system.getAllTeachers().stream()
                .filter(t -> t.getName().equals(name))
                .findFirst().orElseThrow();
    }

    private static Student findStudent(UniversitySystem system, String name) {
        return system.getAllStudents().stream()
                .filter(s -> s.getName().equals(name) && !(s instanceof GraduateStudent))
                .findFirst().orElseThrow();
    }

    private static GraduateStudent findGradStudent(UniversitySystem system, String name) {
        return system.getAllStudents().stream()
                .filter(s -> s instanceof GraduateStudent && s.getName().equals(name))
                .map(s -> (GraduateStudent) s)
                .findFirst().orElseThrow();
    }

    private static Manager findManager(UniversitySystem system, String name) {
        return system.getUsers().stream()
                .filter(u -> u instanceof Manager && u.getName().equals(name))
                .map(u -> (Manager) u)
                .findFirst().orElseThrow();
    }

    private static TechSupportSpecialist findTechSupport(UniversitySystem system, String name) {
        return system.getUsers().stream()
                .filter(u -> u instanceof TechSupportSpecialist && u.getName().equals(name))
                .map(u -> (TechSupportSpecialist) u)
                .findFirst().orElseThrow();
    }

    private static Course findCourse(List<Course> courses, String code) {
        return courses.stream()
                .filter(c -> c.getCourseCode().equals(code))
                .findFirst().orElseThrow();
    }
}
